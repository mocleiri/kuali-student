#!/bin/bash

# This script waits for approximately 5 minutes to get a response from a Tomcat instance
# If no response is generated within that time, the script errors out

URL=$1

if [ "$URL" = "" ]
then
  echo URL is empty
  echo ------------------------------------------------------------------------------------------
  echo Usage: wait-for-tomcat-startup.sh http://dev.ks.kuali.org
  echo ------------------------------------------------------------------------------------------
  exit 1
fi

counter=50
echo --------- Determining Tomcat status - `date`
until [ "`curl --silent --connect-timeout 3 --max-time 3 -I $URL | grep 'Coyote'`" != "" ];
do
  echo --- $counter --- Waiting for Tomcat to start - `date`
  sleep 3
  counter=`expr $counter - 1`
  if [ "$counter" = 0 ]
  then
    echo --------- ERROR: Timed out waiting for Tomcat to start - `date`
    exit 1
  fi
done

echo --------- Tomcat is ready! - `date`
