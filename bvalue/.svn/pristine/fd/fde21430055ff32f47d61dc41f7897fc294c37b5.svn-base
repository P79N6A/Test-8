#!/bin/sh

ID=$1
LIB_DIR=../lib
LOG_NAME=importshoppingfile_$ID
LOG_DIR=$BVALUE_LOG_PATH

if [ -z "$BVALUE_LOG_PATH" ] ;then
	LOG_DIR=./
fi


CP=`ls $LIB_DIR | grep '.jar$' | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":" | head -c -1`

java -server -Xms1g -Xmx1g -cp ../etc:./billing-0.0.1-SNAPSHOT.jar:$CP -Djava.security.egd=file:///dev/urandom -DBVALUE.LOG.DIR=$LOG_DIR -DBVALUE.LOG.NAME=$LOG_NAME com.tydic.beijing.bvalue.ImportShoppingFileMain
