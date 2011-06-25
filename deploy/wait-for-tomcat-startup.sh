#!/bin/bash

#
# This script waits for approximately 5 minutes to get a response from a Tomcat instance
# If no response is generated within that time, the script errors out
#

URL=$1
COUNT=$2

if [ "$URL" = "" ]
then
  echo URL is empty
  echo ------------------------------------------------------------------------------------------
  echo Usage: wait-for-tomcat-startup.sh http://dev.ks.kuali.org
  echo ------------------------------------------------------------------------------------------
  exit 1
fi

if [ "$COUNT" = "" ]
then
  COUNT=50
fi

echo ---------- Determining Tomcat status for $URL - `date`
until [ "`curl --silent --connect-timeout 3 --max-time 3 -I $URL | grep 'Coyote'`" != "" ];
do
  echo --- $COUNT --- Waiting for response from $URL     - `date`
  sleep 3
  COUNT=`expr $COUNT - 1`
  if [ "$COUNT" = 0 ]
  then
    echo --------- ERROR: Timed out waiting for Tomcat to start - `date`
    exit 1
  fi
done

echo ---------- Tomcat is ready!                                  - `date`

