#!/bin/bash
#
# Author: Kuali Student Team
#
# Purpose:
#     To ensure that commits into the CM community contribution branch are 
# all linked to KS jira's.  We check for the format, not that the referenced
#  jira actually exists.
#

SVNLOOK=/usr/bin/svnlook
JIRA_EXPRESSION="KS[A-Z]+[-][0-9]+"
# match multiple like: (user1|user2|user3)
ALLOWED_USER_EXPRESSION="(jcaddel)"
REPOS="$1"
TXN="$2"

# echo "REPOS=$REPOS" >&2

# test if the user is on the whitelist

# return 0 if any of the paths changed in the commit start with contrib/CM.
$($SVNLOOK dirs-changed -t "$TXN" "$REPOS" | egrep "^contrib/CM" >/dev/null)

IS_CM_CONTRIB=$?

if test 0 -eq $IS_CM_CONTRIB
then
    # contains a CM contrib path so check for the jira in the commit message.

    $($SVNLOOK author -t "$TXN" "$REPOS" | egrep $ALLOWED_USER_EXPRESSION >/dev/null)

    IS_WHITELISTED_USER=$?

    if test 0 -eq $IS_WHITELISTED_USER
    then
        exit 0
    fi

    # else process as normal.


    # echo "CM_CONTRIB RELATED Check log message" >&2

    # apply the jira regex to the commit message
    $($SVNLOOK log -t "$TXN" "$REPOS" | egrep $JIRA_EXPRESSION > /dev/null)

    CONTAINS_JIRA=$?

    if test 0 -eq $CONTAINS_JIRA
    then
        exit 0
    else
        echo "The CM Contribution Branch requires JIRA numbers to be referenced in the commit message" >&2
        exit 1
    fi
else
    # not a CM contrib path

    # echo "SKIP JIRA Check" >&2
    exit 0
fi

