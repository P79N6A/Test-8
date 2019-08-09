#!/bin/bash
LIBPATH=$HOME/interface/lib/
for jar in `ls $LIBPATH/*.jar`
do
	CLASSPATH="$CLASSPATH:$jar"
done

# jvm优化参数
OPTS='-server -showversion -XX:+AggressiveOpts -XX:+UseLargePages -XX:MaxTenuringThreshold=15 -XX:MaxGCPauseMillis=50 -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0'

# 使用G1策略
${OPTS}='${OPTS} -XX:+UseG1GC'

nohup java ${OPTS} -cp $HOME/interface/resources/outerf:$CLASSPATH -DLOG.DIR=$HOME/interface/log -DLOG.NAME=outerf com.tydic.beijing.billing.outerf.applications.OuterfProviderApplication > /dev/null &

