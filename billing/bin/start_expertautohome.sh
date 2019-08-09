LIB_DIR=$HOME/interface/lib/
startTime=`date -d "-2 hour" "+%Y%m%d%H0000"`
endTime=`date -d "-1 hour" "+%Y%m%d%H5959"`
echo "startTime="$startTime
echo "endTime="$endTime
LIB_JARS=`ls $LIB_DIR|grep .jar|grep -v saf-1.0.9.jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/interface/resources/interface
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/interface/log -DLOG.NAME=refund com.tydic.beijing.billing.interfacex.ExpertCDRofAutoHomeMain $startTime $endTime >expert.log &
