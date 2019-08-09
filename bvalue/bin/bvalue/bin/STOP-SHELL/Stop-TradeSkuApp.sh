#/bin/bash

#SKU 赠送
ps -ef | grep TradeSkuApp| grep -v 'grep' | awk -F ' ' '{print $2}' | xargs kill -9
