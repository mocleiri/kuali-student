#!/bin/bash 
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
    USE_HOOK=$3
 
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

USE_HOOK=$1
SHOW_LOG=$2

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

