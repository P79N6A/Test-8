#/bin/bash
echo '停京东PIN关联'
sh Stop-JDPinRelation.sh
sleep 1

echo '停自动兑换设置查询'
sh Stop-QueryAutoExchange.sh
sleep 1

echo '停收支记录查询'
sh Stop-QueryBAccessLog.sh
sleep 1

echo '停余额查询'
sh Stop-QueryBAcctRemainBalance.sh
sleep 1

echo '停自动兑换设置'
sh Stop-SetAutoExchange.sh
sleep 1

echo 'B值兑换'
sh Stop-TradeExchange.sh
sleep 1

echo 'SKU 赠送接口'
sh Stop-TradeSkuApp.sh
sleep 1

echo '台账查询接口'
sh Stop-QueryTrade.sh
sleep 1

