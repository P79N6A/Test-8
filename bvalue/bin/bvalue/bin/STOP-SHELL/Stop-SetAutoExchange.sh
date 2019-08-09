#/bin/bash

#自动兑换设置
ps -ef | grep SetAutoExchangeApp| grep -v 'grep' | awk -F ' ' '{print $2}' | xargs kill -9
