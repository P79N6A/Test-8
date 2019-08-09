ins=`jps -m|awk '/SmsDeal -h$/'|wc -l`
if [ $ins -gt 0 ]
then
	echo " SmsDeal -h exists !"
	exit 1
fi
LIB_DIR=$HOME/smsSend/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/smsSend/resources
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/smsSend/log -DLOG.NAME=smsLogService com.tydic.beijing.billing.sms.SmsDeal       -h >/dev/null & 
