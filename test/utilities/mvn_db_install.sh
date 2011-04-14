#!/bin/sh

HOST=$1
SCHEMA=$2
DBA_PASSWORD=$3
SID=$4

if [ -z $HOST ]
then
	echo "Must set HOST"
	exit
fi

if [ -z $SCHEMA ]
then
	echo "Must set SCHEMA"
	exit
fi

if [ -z $DBA_PASSWORD ]
then
	DBA_PASSWORD='ksSysPass'
fi

if [ -z $SID ]
then
	SID='KSDB'
fi

if [ $HOST = 'dbserv-1' ]
	then
		FULL_HOST='dbserv-1.ks.kuali.net'
elif [ $HOST = 'dbserv-2' ]
	then
		FULL_HOST='ec2-184-72-169-126.compute-1.amazonaws.com'
else
	FULL_HOST=$HOST
fi

echo "Running: mvn clean install -Pks-db,oracle -Dks.impex.url=jdbc:oracle:thin:@${FULL_HOST}:1521:${SID} -Dks.impex.dba.password=${DBA_PASSWORD} -Dks.impex.username=${SCHEMA} -Dks.impex.password=${SCHEMA}"
`mvn clean install -Pks-db,oracle -Dks.impex.url=jdbc:oracle:thin:@${FULL_HOST}:1521:${SID} -Dks.impex.dba.password=${DBA_PASSWORD} -Dks.impex.username=${SCHEMA} -Dks.impex.password=${SCHEMA}`