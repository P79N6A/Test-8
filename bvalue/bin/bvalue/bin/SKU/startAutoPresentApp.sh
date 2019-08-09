#!/bin/sh

ID=$1
LIB_DIR=../lib
LOG_NAME=TradeSkuApp_$ID
LOG_DIR=$BVALUE_LOG_PATH

if [ -z "$BVALUE_LOG_PATH" ] ;then
	LOG_DIR=./
fi

CURR_DATE=`date`
FLAG=`ps -ef | grep AutoPresentApp | grep -v 'grep'  | wc -l`
if [ $FLAG != 0 ]; then
	echo "${CURR_DATE} :AutoPresentApp,ID=${ID} has started, rerun after kill \!\!" >>TradeSkuApp_StartUp.log
	exit
fi

CP=`ls $LIB_DIR | grep '.jar$' | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":" | head -c -1`

java -server -Xms512m -Xmx512m -cp ../etc:./bvalue-0.0.1.jar:$CP -Djava.security.egd=file:///dev/urandom -DBVALUE.LOG.DIR=$LOG_DIR -DBVALUE.LOG.NAME=$LOG_NAME com.tydic.beijing.bvalue.AutoPresentApp
