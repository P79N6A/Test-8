CONF_DIR=../resources/account
LIB_DIR=../lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/account/log -DLOG.NAME=ContractSynchroniseExpdate com.tydic.beijing.billing.account.ContractSynchroniseExpdateMain  > ../log/ContractSynchroniseExpdateMain.log 2>&1 &
