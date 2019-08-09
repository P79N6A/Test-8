#!/bin/bash
#PATH=$PATH:$HOME/bin:.
#export PATH
export LANG=en_US.UTF-8

#------ConnStr="BILLING/tVxjLxAYDw@rb1"
ConnStr="newbilling/newbilling@172.168.1.246/bssgx"
#------cd /home/billing/pys

echo 'Starting pysMonthCdr_CTC'
./pysMonthCdr_ctc.sh $ConnStr

date
echo 'The pys mission is Over!'
