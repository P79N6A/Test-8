#/bin/bash

#收支记录查询
ps -ef | grep QueryBAccessLogMain| grep -v 'grep' | awk -F ' ' '{print $2}' | xargs kill -9
