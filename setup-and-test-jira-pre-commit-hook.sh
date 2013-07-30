#!/bin/bash -x
#
# setup the test repostitory and working copy directories
#
# one will be on the contrib/CM path and the other somewhere else.
SVN_CMD=/usr/bin/svn
SVN_ADMIN_CMD=/usr/bin/svnadmin

TEST_REPO="test-repo"
BASE_DIR=$(pwd)
FULL_PATH="$BASE_DIR/$TEST_REPO"
TEST_CM_WC=test-contrib-cm
TEST_NON_CM_WC=test-ks-api-trunk

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

rm -rf $TEST_REPO

rm -rf $TEST_CM_WC
rm -rf $TEST_NON_CM_WC

$SVN_ADMIN_CMD create $TEST_REPO

ln -s $BASE_DIR/require-jira-pre-commit-hook.sh $FULL_PATH/hooks/pre-commit
        
$SVN_ADMIN_CMD load ./$TEST_REPO < test-repo.dump

# checkout a working copy on the contrib/CM branch
$SVN_CMD co file://$FULL_PATH/contrib/CM $TEST_CM_WC

cd $TEST_CM_WC

show_log $SHOW_LOG

# test that the root project structure directories are protected

svn rm aggregate

R=$(test_commit "jcaddel" "delete aggregate")

# we expect this to fail
assert "1" "$R" "failed to delete aggregate" "deleted the aggregate!"

show_log $SHOW_LOG

svn revert -R .

# test that branches can be created, updated and deleted but that the root aggregate/branches directory can't be removed.
R=$(test_create_branch_or_tag "jcaddel" "branch for version 1" "file://$FULL_PATH/contrib/CM/aggregate/trunk" "file://$FULL_PATH/contrib/CM/aggregate/branches/1.x")

# we expect to be able to create the branch
assert "0" "$R" "version 1 branch created" "failed to create version 1 branch"

show_log $SHOW_LOG

# bring in the change from the repo based create branch or tag.
svn update

echo "version 1 branch" > aggregate/branches/1.x/DATA

R=$(test_commit "jcaddel" "update version 1 branch")

# we expect to be able to update the branch
assert "0" "$R" "version 1 branch updated" "failed to update version 1 branch"

show_log $SHOW_LOG

svn update 
# we expect that deleting the branch will fail
svn rm aggregate/branches/1.x

R=$(test_commit "jcaddel" "remove version 1 branch")

assert "1" "$R" "failed to delete version 1 branch" "deleted version 1 branch" 

show_log $SHOW_LOG
exit 2


# clean up failed changes
svn revert -R .

# we expect to be able to move the branch
svn mkdir aggregate/branches/archived

svn mv aggregate/branches/1.x aggregate/branches/archived/1.x

R=$(test_commit "jcaddel" "move version 1 branch")

assert "0" "$R" "moved version 1 branch" "failed to move version 1 branch"

show_log $SHOW_LOG

# we expect to not be able to delete the aggregate/branches

# test that updates to existing tags are blocked.

show_log $SHOW_LOG

MULTI_LINE_COMMIT_MSG=$(printf "KSENROLL-123-KSCM-456\nKSFAKE345\nOTHER-123")

R=$(test_commit "mike" "$MULTI_LINE_COMMIT_MSG")

#KSFAKE345 should fail the test
if test "0" == "$R"
then
    echo "error: commit with a fake jira succeeded!"
    show_log $SHOW_LOG
    exit 1
else
    echo "ok: commit with a fake jira failed."
fi

show_log $SHOW_LOG

MULTI_LINE_COMMIT_MSG=$(printf "Some comment\nKSENROLL-1234\n")

R=$(test_commit "mike" "$MULTI_LINE_COMMIT_MSG")

if test "0" == "$R"
then
    echo "ok: commit with a valid jira on line 2 of the commit message succeeded!"
else
    echo "error: commit with a valid jira failed."
    show_log $SHOW_LOG
    exit 1
fi


show_log $SHOW_LOG


R=$(test_commit "mike" "no jira")


if test "1" == "$R"
then
    echo "ok: commit without a jira failed as expected"
else
    echo "error: commit without a jira succeeded!"
    exit 1
fi

show_log $SHOW_LOG

R=$(test_commit "jcaddel" "no jira")


if test "1" == "$R"
then
    echo "error: commit without a jira but as jcaddel failed!"
    exit 1
else
    echo "ok: commit without a jira but as jcaddel succeeded"
fi

show_log $SHOW_LOG

R=$(test_commit "mike" "KSENROLL-1234")


if test "1" == "$R"
then
    echo "error: commit with a jira failed!"
    exit 1
else
    echo "ok: commit with a jira succeeded"
fi

show_log $SHOW_LOG


# checkout a working copy off the contrib/CM branch
cd $BASE_DIR
svn co file://$FULL_PATH/enrollment/ks-api/trunk $TEST_NON_CM_WC
cd $TEST_NON_CM_WC

R=$(test_commit "mike" "no jira")

echo "R=$R"

if test "0" == "$R"
then
    echo "ok: commit without a jira succeeded as expected"
else
    echo "error: commit failed unexpectantly"
    exit 1
fi

show_log $SHOW_LOG

R=$(test_commit "jcaddel" "no jira")


if test "0" == "$R"
then
    echo "ok: commit without a jira succeeded as expected"
else
    echo "error: commit failed unexpectantly"
    exit 1
fi


show_log $SHOW_LOG

R=$(test_commit "mike" "KSENROLL-12345")


if test "0" == "$R"
then
    echo "ok: commit without a jira succeeded as expected"
else
    echo "error: commit failed unexpectantly"
    exit 1
fi


show_log $SHOW_LOG

