#!/bin/bash
#
# compare-revs.sh
#
# Note on windows needs to be run within the git mingw window vs the standard
# mingw window.
# 

usage() {
	
	echo "USAGE: <mode: SHOW | BY_JIRA>"
	echo "SHOW: <changes in branch> <since branch> [gitk]"
	echo "gitk: trigger showing of commits in gitk"
	echo "BY_JIRA: <changes in branch> <since branch> [show_headers]"
	exit 1
}
checkParam () {

	PARAM=$1
	
	if test -z $PARAM
	then
		usage
	fi
}

computeRev () {

	SHA1=$1

	REV=$(git log $SHA1 -n 1 | grep git-svn-id | awk '{print $2}' | cut -d@ -f 2)

	echo $REV
}

MODE=$1

checkParam $MODE

if test "$MODE" == "SHOW"
then
	
	CHANGES_IN=$2
	SINCE=$3

	COMMAND="git log "

	if test -n $4
	then
		COMMAND=gitk
	fi

	checkParam $CHANGES_IN
	checkParam $SINCE

	echo "$COMMAND $SINCE..$CHANGES_IN"
	echo "Press any key to continue"
	read F
	$COMMAND $SINCE..$CHANGES_IN

elif test "$MODE" == "BY_JIRA"
then
	CHANGES_IN=$2
	SINCE=$3
	SHOW_HEADERS=$4
	

	checkParam $CHANGES_IN
	checkParam $SINCE

	JIRAS=$(git log $SINCE..$CHANGES_IN --pretty --format="%H:%an:%s") 

	if test ${#SHOW_HEADERS} -gt 0
	then
		echo "#SHA1|JIRA|REV"
	fi

	printf "%s" "$JIRAS" | while IFS= read -r LINE
	do
		D=$(echo "$LINE" | cut -d' ' -f 1)

		SHA1=$(echo "$D" | cut -d: -f 1)
		AUTHOR=$(echo "$D" | cut -d: -f 2)
		JIRA=$(echo "$D" | cut -d: -f 3)

		REV=$(computeRev $SHA1)

		
		echo "$SHA1|$JIRA|$AUTHOR|$REV"
	done
	

	
else
	echo "Invalid Mode: $MODE"
	usage
fi
