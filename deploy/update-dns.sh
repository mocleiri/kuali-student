#!/bin/sh

DMEUSER=kuali
DMEPASS=<dnsmadeeasy-password>
DMEID=5497004
IP_PUBLIC=`curl http://169.254.169.254/latest/meta-data/public-ipv4`
DNS_PUBLIC=ci.ks.kuali.org
echo $IP_PUBLIC > /etc/dnsmadeeasy/ip-public
echo $DNS_PUBLIC > /etc/hostname

if wget -q -O /proc/self/fd/1 http://www.dnsmadeeasy.com/servlet/updateip?username=$DMEUSER\&password=$DMEPASS\&id=$DMEID\&ip=$IP_PUBLIC | grep success > /dev/null; then
  logger -t DNS-Made-Easy -s "DNS Record Updated Successfully"
else
  logger -t DNS-Made-Easy -s "Problem updating DNS record."
fi

