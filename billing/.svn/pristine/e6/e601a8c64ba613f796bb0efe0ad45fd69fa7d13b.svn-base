ins=`jps -m|awk '/SmsDeal -s$/'|wc -l`
if [ $ins -gt 0 ]; then
	echo "SmsDeal -s exists ..."
	exit 1
fi
LIB_DIR=$HOME/SmsSend/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/SmsSend/resources
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/SmsSend/log -DLOG.NAME=hlpSmsService com.tydic.beijing.billing.sms.SmsDeal -s >/dev/null & 
