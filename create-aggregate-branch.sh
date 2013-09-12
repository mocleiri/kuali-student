#!/bin/bash
#
# NAME: create-aggregate-branch.sh
#
# PURPOSE: To help creating aggregates
# 

usage () {
	MSG=$1

	if test -n "$MSG"
	then
		echo "ERROR: $MSG"
	fi

	echo "USAGE: <ks-api branch> <aggregate name> <modules> <in_branches:0 or 1>  <source branch> <commit message> <source revision> [<source prefix> <target prefix: like sandbox> ]"
	echo "<ks-api branch>: either trunk or branches/some_branch"
	echo "<aggregate name>: in branches mode this is the name of the branch for each module.  i.e. ks-enroll/brances/aggregate_name"
	echo "<modules>:non-api based modules to apply"
	echo "<in_branches>:1 if $module/branches/aggregate_name is the target module location. 0 if $module is the target location. use 0 for sandbox based aggregates."
	echo "<source branch>: trunk or branches/source-branch"
	echo "<commit message>: commit message to use"
	echo "<source revision>: 0 will find out the current revision. >0 will use the indicated revision."
	echo "<source_prefix>: optional. enrollment or sandbox or contrib/CM"
	echo "<target_prefix>: optional. enrollment or sandbox or contrib/CM"
	exit 1

}

SVNMUCC_CMD=svnmucc

REPOSITORY="https://svn.kuali.org/repos/student"

API_SOURCE=$1

if test -z "$API_SOURCE"
then
	usage "Missing Api Source"
fi

AGGREGATE_NAME=$2

if test -z "$AGGREGATE_NAME"
then
	usage "Missing Aggregate Name"
fi

MODULES=$3

if test -z "$MODULES"
then
	usage "Missing modules"
fi

IN_BRANCH=$4

if test -z "$IN_BRANCH"
then
	usage "Missing In branch mode"
fi

SOURCE_BRANCH=$5

if test -z "$SOURCE_BRANCH"
then
	usage "Missing Source Branch"
fi

COMMIT_MESSAGE=$6

if test -z "$COMMIT_MESSAGE"
then
	usage "Missing Commit Message"
fi

SOURCE_REVISION=$7

if test -z "$SOURCE_REVISION"
then
	usage "Missing Source Revision"
fi

SOURCE_PREFIX=$8

SOURCE_PATH=""

if test -z "$SOURCE_PREFIX"
then
	SOURCE_PATH="$REPOSITORY/enrollment"
else
	SOURCE_PATH="$REPOSITORY/$SOURCE_PREFIX"
fi

TARGET_PREFIX=$9

TARGET_PATH=""

if test -z "$TARGET_PREFIX"
then
	TARGET_PATH="$REPOSITORY/enrollment"
else
	TARGET_PATH="$REPOSITORY/$TARGET_PREFIX"
fi

SOURCE_REV=""

if test 0 -eq "$SOURCE_REVISION"
then
	SOURCE_REV=$(svn info https://svn.kuali.org/repos/student | grep Revision: | cut -d: -f 2 | sed 's/^\ //')
else
	SOURCE_REV=$SOURCE_REVISION
fi

# echo $SOURCE_REV
# echo "TARGET_PATH=$TARGET_PATH"

CMD_FILE=/tmp/$RANDOM.dat

#echo "CMD_FILE=$CMD_FILE"

printf "$SVNMUCC_CMD " > $CMD_FILE

printf "cp $SOURCE_REV $SOURCE_PATH/ks-api/$API_SOURCE " >> $CMD_FILE

if test $IN_BRANCH == "1"
then
	printf "    $TARGET_PATH/ks-api/branches/$AGGREGATE_NAME " >> $CMD_FILE

else
	printf "    $TARGET_PATH/$AGGREGATE_NAME/ks-api  " >> $CMD_FILE
fi

for M in $MODULES
do
	# printf "module = $M"
	printf "cp $SOURCE_REV $SOURCE_PATH/$M/$SOURCE_BRANCH " >> $CMD_FILE

	if test $IN_BRANCH == "1"
	then
		printf "    $TARGET_PATH/$M/branches/$AGGREGATE_NAME " >> $CMD_FILE

	else
		printf "    $TARGET_PATH/$AGGREGATE_NAME/$M " >> $CMD_FILE
	fi

done

printf " -m \"$COMMIT_MESSAGE\"" >> $CMD_FILE

cat $CMD_FILE

printf "\nApply Change (y/n)"

read command

if test $command == "y"
then
	bash -c $CMD_FILE
	echo "applied"
fi
