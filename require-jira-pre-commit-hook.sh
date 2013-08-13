#!/bin/bash
#
# For debugging add a -x option above
#
# Author: Kuali Student Team
#
# Purpose:
#     To ensure that commits into the CM community contribution branch are 
# all linked to KS jira's.  
# We parse out any KS[A-Z]+[-]?[0-9]+ format jira numbers and then use the
# REST API on jira.kuali.org to verify that they exist.
#
# We also lock down the base directories in the /contrib and /enrollment roots
# to:
#
# prevent deleting the module/{tags,branches,trunk} paths (aggregate
# is counted as a module)
#

# source the configuration variables
SVNLOOK_CMD=/usr/bin/svnlook
SVN_CMD=/usr/bin/svn
WGET_CMD=/usr/bin/wget

# do not remove this but toggle 0 (off), 1 (on)
ENABLE_DEBUG_MESSAGES=0

# on success look at this file to find out what happenned
DEBUG_LOGFILE=/tmp/debug.log

# commits with jira's that match this pattern need to be valid.
# also the commit needs at least one match like this.
JIRA_EXPRESSION="KS[A-Z]+[-]?[0-9]+"

# match multiple users like: (user1|user2|user3)
ALLOWED_USER_EXPRESSION="(jcaddel)"

# /enrollment/$module/{tags,branches,trunk} are protected
ENROLLMENT_MODULES="aggregate ks-ap ks-api ks-core ks-lum ks-enroll
ks-deployments"

# /contrib/CM/$module/{tags,branches,trunk} are protected
CM_MODULES="aggregate ks-api ks-core ks-lum ks-deployments"


debug () {

        if test 1 -eq $ENABLE_DEBUG_MESSAGES
        then
                MSG=$1
                echo "$MSG" >&2
                echo "$MSG" >> $DEBUG_LOGFILE
        fi
}

# Test if the JIRA is valid.
# return's 0 if it is valid and 8 if it is not valid.
is_valid_jira () {

    JIRA=$1
    $WGET_CMD -O /dev/null "https://jira.kuali.org/rest/api/2/issue/$JIRA?fields=summary" 2> /dev/null > /dev/null
    RET_VAL=$?
    
    return $RET_VAL

}

# extract jira's from the given line in the comment message using $JIRA_EXPRESSION
# multiple jira's per line turn into mutliple JIRA loops.

parse_commit_message_line () {

	LINE=$1

	# skip blank lines
	#
	if test -z "$LINE"
	then
		return 0
	fi

	echo "$LINE" | grep -Po "((?:$JIRA_EXPRESSION))" | while read JIRA
    do
        
        debug "JIRA=$JIRA"

        if test -n "$JIRA"
        then

        	is_valid_jira "$JIRA"
                
            IS_JIRA_VALID=$?

            debug "IS_JIRA_VALID=$IS_JIRA_VALID"

            if test $IS_JIRA_VALID -eq 8
            then
                debug "'$JIRA' is not known to jira.kuali.org"
                return 1
            fi
        fi
    done


}

# return 1 if any path on the inbound commit is protected.
# return 0 where nothing is found to be protected.
is_path_protected () {

    PP_FILE=/tmp/protected-$RANDOM.dat

    # clean up in case the file already exists
    rm -rf $PP_FILE
   
    # instead of hard coding the modules that should be protected we read from
    # the current repository structure we want to protect:
    # enrollment/$module
    # contrib/$module (excluding CM)
    # contrib/CM/$module
 
    # read existing enrollment modules
    ENROLLMENT_PATH="enrollment"

    echo "$ENROLLMENT_PATH" >> $PP_FILE
    echo "$ENROLLMENT_PATH/" >> $PP_FILE

    $SVN_CMD ls file://$REPOS/$ENROLLMENT_PATH | grep "/" | sed 's/\/$//' | while read module 
    do
        # the value used in the comparison may have a trailing /
        # so we just provision both varients into the test file to be sure
        # we will match it properly.
        echo "$ENROLLMENT_PATH/$module" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/tags" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/tags/" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/branches" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/branches/" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/trunk" >> $PP_FILE
        echo "$ENROLLMENT_PATH/$module/trunk/" >> $PP_FILE

    done

    # read existing contrib (not CM) modules
    CONTRIB_PATH="contrib"

    echo "$CONTRIB_PATH" >> $PP_FILE
    echo "$CONTRIB_PATH/" >> $PP_FILE

    # exclude CM for now
    $SVN_CMD ls file://$REPOS/$CONTRIB_PATH | grep "/" | grep -v "CM" | sed 's/\/$//' | while read module 
    do
        # the value used in the comparison may have a trailing /
        # so we just provision both varients into the test file to be sure
        # we will match it properly.
        echo "$CONTRIB_PATH/$module" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/tags" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/tags/" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/branches" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/branches/" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/trunk" >> $PP_FILE
        echo "$CONTRIB_PATH/$module/trunk/" >> $PP_FILE

    done

    # read existing contrib/CM modules
    CONTRIB_CM_PATH="contrib/CM"

    echo "$CONTRIB_CM_PATH" >> $PP_FILE
    echo "$CONTRIB_CM_PATH/" >> $PP_FILE

    # exclude CM for now
    $SVN_CMD ls file://$REPOS/$CONTRIB_CM_PATH | grep "/" | sed 's/\/$//' | while read module 
    do
        # the value used in the comparison may have a trailing /
        # so we just provision both varients into the test file to be sure
        # we will match it properly.
        echo "$CONTRIB_CM_PATH/$module" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/tags" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/tags/" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/branches" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/branches/" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/trunk" >> $PP_FILE
        echo "$CONTRIB_CM_PATH/$module/trunk/" >> $PP_FILE

    done

    $SVNLOOK_CMD changed $SVNLOOK_OPTS | while read LINE
    do

        MODE=$(echo $LINE | awk '{print $1}')
        TARGET=$(echo $LINE | awk '{print $2}') 

        debug "MODE=$MODE, TARGET=$TARGET"

        if test "$MODE" == "D"
        then 
            debug "DELETE = $TARGET"
            # we want to prevent deletes on the repo structure paths

            egrep "^$TARGET$" $PP_FILE >/dev/null
            R=$?
           
            debug "R=$R"
 
            if test 0 -eq $R
            then
                # trying to delete a protected path so fail
                debug "fail delete on a proected path $TARGET"
                echo "1"
                break
            fi 

        fi
    done


    if test 0 -eq $ENABLE_DEBUG_MESSAGES
    then
        # if not in debug mode delete the file
        rm $PP_FILE
    else
        debug "PROTECTED_FILE = $PP_FILE"
    fi

}

REPOS="$1"
TXN="$2"

SVNLOOK_OPTS="-t $TXN $REPOS"

if test -z "$REPOS"
then
        echo "ERROR: missing REPO" >&2
        exit 1
fi

if test -z "$TXN"
then 
        # if no transaction is given just exclude it from the opts
        # which will cause us to look at the most recent commit.
        # used in testing to commit and then evaluate if that commit should have worked.
        SVNLOOK_OPTS="$REPOS"
fi

debug "STARTING with REPOS=$REPOS" 

# check to make sure no protected directory is being altered.
IS_PROTECTED=$(is_path_protected)

debug "IS_PROTECTED=$IS_PROTECTED"

if test "1" == "$IS_PROTECTED"
then
    echo "You attempted to modify a protected location.  See https://wiki.kuali.org/display/STUDENTDOC/4.8+Protected+Source+Code+Locations" >&2
    exit 1
fi

# return 0 if any of the paths changed in the commit start with contrib/CM.
$($SVNLOOK_CMD dirs-changed $SVNLOOK_OPTS | egrep "^contrib/CM" >/dev/null)

IS_CM_CONTRIB=$?

debug "IS_CM_CONTRIB=$IS_CM_CONTRIB"

if test 0 -eq $IS_CM_CONTRIB
then
    # contains a CM contrib path so check for the jira in the commit message.

    # First check if the user is whitelisted and not subject to the valid JIRA constraint.
    $($SVNLOOK_CMD author $SVNLOOK_OPTS | egrep $ALLOWED_USER_EXPRESSION >/dev/null)

    IS_WHITELISTED_USER=$?

    debug "WHITELISTED_USER=$IS_WHITELISTED_USER"

    if test 0 -eq $IS_WHITELISTED_USER
    then
        # allow the commit
        exit 0 
    fi

    # else apply the jira checks

    # Check if there are any candidate matches in the commit message
    # Looking at all lines in the message, this catches the no jira in the message case
    $($SVNLOOK_CMD log $SVNLOOK_OPTS | egrep "$JIRA_EXPRESSION" $COMMIT_MSG_FILE >/dev/null)
    CONTAINS_JIRAS=$?

    debug "CONTAINS_JIRAS=$CONTAINS_JIRAS"
    
    # 0 on a match, 1 no match, 2 error (from the grep man page)
    if test "$CONTAINS_JIRAS"  != "0"
    then
        echo "The Curriculum Management Contribution branch requires valid JIRA's to be referenced in the commit message" >&2
        exit 1
    fi

    debug "CM_CONTRIB RELATED Check log message" 

    # At this point we know there are candidate matches to consider

    COMMIT_MSG=$($SVNLOOK_CMD log $SVNLOOK_OPTS) 
    
    debug "echo '$COMMIT_MSG'"
    
    for LINE in $COMMIT_MSG
    do
        debug "LINE=$LINE"

        # Evaluate each line in turn.  Its possible that multiple jira's are
        # specified on a single line and this will check all of them.
        #
        # This method fails fast so the output doesn't contain the full list 
        # of all failed jira's only the first one encountered.
        #
        # return 0 for all ok or empty line, 8 if the extracted jira doesn't exist
        parse_commit_message_line "$LINE"
   
        R=$?

        debug "R=$R"

        if test "$R" != "0"
        then
            echo "The Curriculum Management Contribution branch requires valid JIRA's to be referenced in the commit message" >&2
            exit 1
        fi
 
    done

else
    # not a CM contrib path
    debug "not a CM contrib path"
    exit 0
fi

