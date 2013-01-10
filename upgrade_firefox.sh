#!/bin/bash

TARGET=/usr/local

export installroot=$TARGET/src
export workpath=$TARGET

cd $workpath
rm -rf firefox
unlink /usr/local/bin/firefox
wget -r --no-parent --reject "index.html*" -nH --cut-dirs=7 https://ftp.mozilla.org/pub/mozilla.org/firefox/releases/14.0.1/linux-x86_64/en-US/
tar xvf firefox-14.0.1.tar.bz2
cd bin
ln -s ../firefox/firefox
ldconfig

