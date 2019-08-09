echo '启动SKU赠送 1'
nohup sh startTradeSku_M.sh 1 >/dev/null &
sleep 1

echo '启动B值兑换 1'
nohup sh startTradeExchange_M.sh 1 >/dev/null &
sleep 1

echo '启动自动兑换查询 1'
nohup sh startQueryAutoExchange_M.sh 1 >/dev/null &
sleep 1

echo '启动京东pin关联 1'
nohup sh startJdpinRelation_M.sh 1 >/dev/null &
sleep 1

echo '启动自动兑换设置 1'
nohup sh startSetAutoExchange_M.sh 1 >/dev/null &
sleep 1

echo '启动余额查询 10'
nohup sh startQueryBAcctRemainBalance_M.sh 10 >/dev/null &
sleep 1

echo '启动收支记录查询 5'
nohup sh start_QueryBAcctAccessLog_M.sh 5 >/dev/null &
sleep 1

echo '启动台账查询 5'
nohup sh startQueryTrade_M.sh 5 >/dev/null &
sleep 1