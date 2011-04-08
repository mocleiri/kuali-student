# /bin/sh
#
# Description: This will configure KS on the first run of this AMI
# Author: Kyle Campos / Rsmart
#

KS_CONFIG_PATH=$KS_HOME/kuali/main/dev
KS_CONFIG_FILE=$KS_CONFIG_PATH/ks-embedded-config.xml
PUBLIC_HOSTNAME=`wget -q -O - http://instance-data.ec2.internal/latest/meta-data/public-hostname`
PORT='9080'

ec2_configure_ks()
{
	# Setup ks-embedded-config.xml
	perl -p -i -e 's/__HOSTNAME__/${PUBLIC_HOSTNAME}/g' $KS_CONFIG_FILE
	perl -p -i -e 's/__PORT__/${PORT}/g' $KS_CONFIG_FILE
	
}

echo "Configuring your KS configuration file: ${KS_CONFIG_FILE} ..."
ec2_configure_ks


echo "Done."