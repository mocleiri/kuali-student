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

SHOW_LOG=$1

rm -rf $TEST_REPO

rm -rf $TEST_CM_WC
rm -rf $TEST_NON_CM_WC

$SVN_ADMIN_CMD create $TEST_REPO

$SVN_ADMIN_CMD load ./$TEST_REPO < test-repo.dump

ln -s $BASE_DIR/require-jira-pre-commit-hook.sh $TEST_REPO/hooks/pre-commit

# checkout a working copy on the contrib/CM branch
$SVN_CMD co file://$FULL_PATH/contrib/CM $TEST_CM_WC

cd $TEST_CM_WC

show_log $SHOW_LOG

R=$(test_commit "mike" "no jira")


if test "1" == "$R"
then
    echo "commit without a jira failed as expected"
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
    echo "commit without a jira but as jcaddel succeeded"
fi

show_log $SHOW_LOG

R=$(test_commit "mike" "KSENROLL-12345")


if test "1" == "$R"
then
    echo "error: commit with a jira failed!"
    exit 1
else
    echo "commit with a jira succeeded"
fi

show_log $SHOW_LOG

# checkout a working copy off the contrib/CM branch
cd $BASE_DIR
svn co file://$FULL_PATH/enrollment/ks-api/trunk $TEST_NON_CM_WC
cd $TEST_NON_CM_WC

R=$(test_commit "mike" "no jira")


if test "0" == "$R"
then
    echo "commit without a jira succeeded as expected"
else
    echo "error: commit failed unexpectantly"
    exit 1
fi

show_log $SHOW_LOG

R=$(test_commit "jcaddel" "no jira")


if test "0" == "$R"
then
    echo "commit without a jira succeeded as expected"
else
    echo "error: commit failed unexpectantly"
    exit 1
fi


show_log $SHOW_LOG

R=$(test_commit "mike" "KSENROLL-12345")


if test "0" == "$R"
then
    echo "commit without a jira succeeded as expected"
else
    echo "error: commit failed unexpectantly"
    exit 1
fi


show_log $SHOW_LOG

