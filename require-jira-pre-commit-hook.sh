#!/bin/bash
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
# prevent moving or deleting the module/{tags,branches,trunk} paths (aggregate
# is counted as a module)
#
# prevent changes to an existing */tags/$tag path.
#
# prevent deleting an existing branch but allow it to be moved.
#

# source the configuration variables
SVNLOOK_CMD=/usr/bin/svnlook
WGET_CMD=/usr/bin/wget

ENABLE_DEBUG_MESSAGES=1

JIRA_EXPRESSION="KS[A-Z]+[-]?[0-9]+"
# match multiple users like: (user1|user2|user3)
ALLOWED_USER_EXPRESSION="(jcaddel)"

debug () {

        if test 1 -eq $ENABLE_DEBUG_MESSAGES
        then
                MSG=$1
                echo "$MSG" >&2
        fi
}

# Test if the JIRA is valid.
# return's 0 if it does and 8 if it doesn't.
is_valid_jira () {

    JIRA=$1
    $WGET_CMD "https://jira.kuali.org/rest/api/2/issue/$JIRA?fields=summary" 2> /dev/null > /dev/null
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

echo "REPOS=$REPOS" >&2



# return 0 if any of the paths changed in the commit start with contrib/CM.
$($SVNLOOK_CMD dirs-changed $SVNLOOK_OPTS | egrep "^contrib/CM" >/dev/null)

IS_CM_CONTRIB=$?

debug "IS_CM_CONTRIB=$IS_CM_CONTRIB"

if test 0 -eq $IS_CM_CONTRIB
then
    # contains a CM contrib path so check for the jira in the commit message.


    # Check if the user is whitelisted and not subject to the valid JIRA constraint.
    $($SVNLOOK_CMD author $SVNLOOK_OPTS | egrep $ALLOWED_USER_EXPRESSION >/dev/null)

    IS_WHITELISTED_USER=$?

    debug "WHITELISTED_USER=$IS_WHITELISTED_USER"

    if test 0 -eq $IS_WHITELISTED_USER
    then
        # allow the commit
        exit 0 
    fi

    # else apply the jira checks

    # First check if there are any candidate matches in the commit message
    # Looking at all lines in the message, this catches the no jira in the message case
    $($SVNLOOK_CMD log $SVNLOOK_OPTS | egrep "$JIRA_EXPRESSION" $COMMIT_MSG_FILE >/dev/null)
    CONTAINS_JIRAS=$?

    debug "CONTAINS_JIRAS=$CONTAINS_JIRAS"
    
    # 0 on a match, 1 no match, 2 error (from the grep man page)
    if test "$CONTAINS_JIRAS"  != "0"
    then
        echo "The Curriculum Management Contribution branch requires valid JIRA's
         to be referenced in the commit message" >&2
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

    exit 0;
    
else
    # not a CM contrib path
        
    echo "SKIP JIRA Check" >&2
    exit 0
fi

