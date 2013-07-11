#!/bin/sh
#
# check the status of each of the 5 git repositories
#
# this will let you see uncommitted changes that have to be dealt with
# before things like rebasing can be done.
cd ks-api
echo "status ks-api"
git status
cd ../ks-core
echo "status ks-core"
git status
cd ../ks-deployments
echo "status ks-deployments"
git status
cd ../ks-parent
echo "status ks-parent"
git status
cd ../ks-enroll
echo "status ks-enroll"
git status
cd ../ks-lum
echo "status ks-lum"
git status

