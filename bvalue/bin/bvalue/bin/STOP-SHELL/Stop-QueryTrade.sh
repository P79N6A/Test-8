#/bin/bash

#台账查询
ps -ef | grep QueryTradeMain| grep -v 'grep' | awk -F ' ' '{print $2}' | xargs kill -9
