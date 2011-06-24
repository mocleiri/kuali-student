#!/bin/bash

counter=50
echo --------- Determining Tomcat status - `date`
until [ "`curl --silent --connect-timeout 3 --max-time 3 -I http://localhost | grep 'Coyote'`" != "" ];
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
