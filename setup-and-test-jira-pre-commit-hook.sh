#!/bin/bash
#
# For debugging add a -x option above
#
# Author: Kuali Student Team
#
# Purpose:
#
# To test the require-jira-pre-commit-hook.sh on a dummy repository to make
# sure that the pre commit restrictions work as intended.
#

SVN_CMD=/usr/bin/svn
SVN_ADMIN_CMD=/usr/bin/svnadmin

TEST_REPO="test-repo"
BASE_DIR=$(pwd)
FULL_PATH="$BASE_DIR/$TEST_REPO"
TEST_CM_WC=test-contrib-cm
TEST_NON_CM_WC=test-enrollment

# Copied from the pre-commit hook.
# remember to keep these in sync with the hook's values.
#
# /enrollment/$module/{tags,branches,trunk} are protected
ENROLLMENT_MODULES="aggregate ks-ap ks-api ks-core ks-lum ks-enroll
ks-deployments"

# /contrib/CM/$module/{tags,branches,trunk} are protected
CM_MODULES="aggregate ks-api ks-core ks-lum ks-deployments"


show_log() {

    ENABLED=$1

    if test -n "$ENABLED"
    then

        echo "Press any key to show log"
        read KEY
        $SVN_CMD log --verbose file://$FULL_PATH 

    fi
}

test_commit () {
    
    USER=$1
    MESSAGE=$2
 
    if test -z "$USER"
    then
        echo "Missing User"
        exit 1
    fi

    if test -z "$MESSAGE"
    then
        echo "Missing Message"
        exit 1
    fi

    echo "data data data" >> README
    $SVN_CMD commit --username $USER -m "$MESSAGE" >/dev/null
    COMMITTED=$?

    echo "$COMMITTED"
}

test_create_branch_or_tag () {
    
    USER=$1
    MESSAGE=$2
    # the paths need to be the full repo url's
    SRC_PATH=$3
    DST_PATH=$4
 
    if test -z "$USER"
    then
        echo "Missing User"
        exit 1
    fi

    if test -z "$MESSAGE"
    then
        echo "Missing Message"
        exit 1
    fi

    $SVN_CMD copy --username $USER -m "$MESSAGE" $3 $4 >/dev/null
    COMMITTED=$?



    echo "$COMMITTED"
}

# do not run this in a sub shell like $()
test_delete_trunk_data_file () {
    
    PREFIX=$1
    
    echo "svn rm $PREFIX/trunk/DATA"
    svn rm $PREFIX/trunk/DATA

    R=$(test_commit "jcaddel" "delete $TARGET")

    # we expect this to succeed

    if test 1 -eq $R
    then
        # commit failed (unexpected)
        return 1
    fi

}

# do not run this in a sub shell like $()
test_delete_directory () {
    
    TARGET=$1
    
    svn rm $TARGET

    R=$(test_commit "jcaddel" "delete $TARGET")

    # we expect this to fail

    if test 1 -eq $R
    then
        # commit successfully denied
        svn revert -R . 2>&1 >/dev/null
    else
        # commit succeeded (incorrect)
        return 1
    fi

}

# do not run this in a sub shell like $()
test_delete_remote_directory () {
    
    TARGET=$1
    
    $SVN_CMD rm file://$FULL_PATH/$TARGET --username "jcaddel" -m "delete
remote directory $TARGET" >/dev/null

    R=$?

    # we expect this to fail

    if test 0 -eq $R
    then
        # commit succeeded (incorrect)
        return 1
    fi

}

# Test all of the project structure paths one at a time to make sure all of
# them can't be deleted.
# return 1 if the directory was deleted
test_prevent_delete_project_structure () {

    MODULES=$1

    # delete all of the modules in one shot


    echo "delete all of the modules in one shot"
    
    for module in $(echo "$MODULES" | tr ' ' '\n')
    do
        svn rm $module
    done

    R=$(test_commit "jcaddel" "delete $TARGET")

    # we expect this to fail

    if test 1 -eq $R
    then
        # commit successfully denied
        svn revert -R . 2>&1 >/dev/null
    else
        # commit succeeded (incorrect)
        echo "failed to prevent structural deletions"
        return 1
    fi

    for module in $(echo "$MODULES" | tr ' ' '\n')
    do
        test_delete_directory "$module"
        
        R=$?
        
        if test 1 -eq $R
        then
            # deleted the directory
            return 1
            break
        fi


        test_delete_directory "$module/tags"

        R=$?
        
        if test 1 -eq $R
        then
            # deleted the directory
            return 1
            break
        fi
        
        test_delete_directory "$module/branches"

        R=$?
        
        if test 1 -eq $R
        then
            # deleted the directory
            return 1
            break
        fi
        
        test_delete_directory "$module/trunk"

        R=$?
        
        if test 1 -eq $R
        then
            # deleted the directory
            return 1
            break
        fi

         

        test_delete_trunk_data_file "$module"

         R=$?
        
        if test 1 -eq $R
        then
            # failed to delete the file
            return 1
            break
        fi

    done

}

assert () {

    OK_RESULT=$1
    ACTUAL_RESULT=$2
    OK_MSG=$3
    FAIL_MSG=$4

    
    if test "$OK_RESULT" == "$ACTUAL_RESULT"
    then
        echo "ok: $OK_MSG"
    else
        echo "error: $FAIL_MSG"
        show_log $SHOW_LOG
        exit 1
    fi
    
    show_log $SHOW_LOG

}

SHOW_LOG=$1

# clear the debug logging for each test run
rm -rf /tmp/debug.log

rm -rf $TEST_REPO

rm -rf $TEST_CM_WC
rm -rf $TEST_NON_CM_WC

$SVN_ADMIN_CMD create $TEST_REPO

ln -s $BASE_DIR/require-jira-pre-commit-hook.sh $FULL_PATH/hooks/pre-commit
        
$SVN_ADMIN_CMD load ./$TEST_REPO < test-repo.dump

# make sure contrib is protected
test_delete_remote_directory "contrib"

R=$?

assert "0" "$R" "failed to remove structural branches" "succeeded in removing structural branches!"


# make sure enrollment is protected
test_delete_remote_directory "enrollment"

R=$?

assert "0" "$R" "failed to remove structural branches" "succeeded in removing structural branches!"



# checkout a working copy on the contrib/CM branch
$SVN_CMD co file://$FULL_PATH/contrib/CM $TEST_CM_WC

cd $TEST_CM_WC

show_log $SHOW_LOG

# test that the root project structure directories are protected

test_prevent_delete_project_structure "$CM_MODULES"

R=$?

assert "0" "$R" "failed to remove structural branches" "succeeded in removing structural branches!"

show_log $SHOW_LOG

MULTI_LINE_COMMIT_MSG=$(printf "KSENROLL-123-KSCM-456\nKSFAKE345\nOTHER-123")

R=$(test_commit "mike" "$MULTI_LINE_COMMIT_MSG")

#KSFAKE345 should fail the test

assert "1" "$R" "commit with a fake jira failed" "commit with a fake jira succeeded"

show_log $SHOW_LOG

MULTI_LINE_COMMIT_MSG=$(printf "Some comment\nKSENROLL-1234\n")

R=$(test_commit "mike" "$MULTI_LINE_COMMIT_MSG")

assert "0" "$R" "commit with a valid jira on line 2 of the commit message succeeded" "commit with a valid jira failed"

show_log $SHOW_LOG


R=$(test_commit "mike" "no jira")

assert "1" "$R" "commit without a jira failed as expected" " commit without a jira succeeded"

show_log $SHOW_LOG

R=$(test_commit "jcaddel" "no jira")


assert "0" "$R" "commit without a jira but as jcaddel succeeded" "commit without a jira but as jcaddel failed!"

show_log $SHOW_LOG

R=$(test_commit "mike" "KSENROLL-1234")


assert "0" "$R" "commit with a valid jira succeeded" "commit with a valid jira failed!"

show_log $SHOW_LOG


# checkout a working copy off the contrib/CM branch
cd $BASE_DIR
svn co file://$FULL_PATH/enrollment $TEST_NON_CM_WC
cd $TEST_NON_CM_WC

# test that the root project structure directories are protected

test_prevent_delete_project_structure $ENROLLMENT_MODULES 

R=$?

assert "0" "$R" "failed to remove structural branches" "succeeded in removing structural branches!"

show_log $SHOW_LOG


R=$(test_commit "mike" "no jira")

assert "0" "$R" "commit without a jira succeeded as expected" " commit without a jira failed"

show_log $SHOW_LOG

R=$(test_commit "jcaddel" "no jira")

assert "0" "$R" "commit without a jira succeeded as expected" "commit without a jira failed"

show_log $SHOW_LOG

R=$(test_commit "mike" "KSENROLL-12345")

assert "0" "$R" "commit with an invalid jira succeeded as expected" "commit with an invalid jira failed"

show_log $SHOW_LOG

