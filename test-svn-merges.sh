#!/bin/bash 
#
# For debugging add a -x option above
#
# Author: Kuali Student Team
#
# Purpose:
#
# To test history loss and repair techniques that can then be applied to the
# main ks repository
#

SVN_CMD=/usr/bin/svn
SVN_ADMIN_CMD=/usr/bin/svnadmin

TEST_REPO="test-merges-repo"
BASE_DIR=$(pwd)
FULL_PATH="$BASE_DIR/$TEST_REPO"
TEST_CM_WC=test-contrib-cm
TEST_NON_CM_WC=test-enrollment

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

# Test all of the project structure paths one at a time to make sure all of
# them can't be deleted.
test_prevent_delete_project_structure () {


    for module in $(echo "aggregate ks-api ks-core ks-lum ks-enroll ks-deployments" | tr ' ' '\n')

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

rm -rf $TEST_REPO

rm -rf $TEST_CM_WC
rm -rf $TEST_NON_CM_WC

rm -rf merge-enrollment
rm -rf ks-deployments-branches

$SVN_ADMIN_CMD create $TEST_REPO

$SVN_ADMIN_CMD load ./$TEST_REPO < test-repo.dump


# copy trunk into branches

$SVN_CMD copy -m"create branch A" file://$FULL_PATH/enrollment/ks-deployments/trunk file://$FULL_PATH/enrollment/ks-deployments/branches/A 

$SVN_CMD copy -m"create branch B" file://$FULL_PATH/enrollment/ks-deployments/trunk file://$FULL_PATH/enrollment/ks-deployments/branches/B 


# 
$SVN_CMD rm -m"delete ks-deployments/branches" file://$FULL_PATH/enrollment/ks-deployments/branches 2>&1
R=$?

if test 0 -eq $R
then
    echo "deleted"
fi


# revert
$SVN_CMD co file://$FULL_PATH/enrollment merge-enrollment

cd merge-enrollment

REVERT_REV=6

$SVN_CMD merge  -c -$REVERT_REV file://$FULL_PATH/enrollment

$SVN_CMD commit -m"revert change"
R=$?

assert 0 "$R" "reverted $REVERT_REV" "failed to revert $REVERT_REV"

# now delete and replace without history
$SVN_CMD rm -m"delete ks-deployments/branches" file://$FULL_PATH/enrollment/ks-deployments/branches 2>&1
R=$?

if test 0 -eq $R
then
    echo "deleted"
fi


$SVN_CMD update

mkdir -p ks-deployments/branches/A

printf "manual restore\nmanual restore\nmanual restore" > ks-deployments/branches/A/DATA

$SVN_CMD add ks-deployments/branches

$SVN_CMD commit -m"manual revert"


printf "another change\nanother change" > ks-deployments/branches/A/DATA

$SVN_CMD commit -m"content change"

# at this point history is lost.
# now the goal is to reconnect it.

# create a sandbox branch at r5

$SVN_CMD mkdir  -m"create sandbox" file://$FULL_PATH/sandbox 2>&1

$SVN_CMD copy -m"create sandbox branch at r5" \
file://$FULL_PATH/enrollment/ks-deployments/branches@5  \
file://$FULL_PATH/sandbox/ks-deployments-branches
R=$?

if test 0 -eq $R
then
    echo "sandbox created"
fi

# check out the sandbox
cd ..

$SVN_CMD co file://$FULL_PATH/sandbox/ks-deployments-branches

cd ks-deployments-branches


svn merge file://$FULL_PATH/enrollment/ks-deployments/branches

echo "svn:mergeinfo"
svn propget svn:mergeinfo .

# checkout a working copy on the contrib/CM branch
#$SVN_CMD co file://$FULL_PATH/contrib/CM $TEST_CM_WC

#cd $TEST_CM_WC

#show_log $SHOW_LOG


