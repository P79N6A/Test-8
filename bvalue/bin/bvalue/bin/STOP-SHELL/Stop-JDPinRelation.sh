#/bin/bash

#京东帐号关联
ps -ef | grep JDPinRelationMain| grep -v 'grep' | awk -F ' ' '{print $2}' | xargs kill -9
