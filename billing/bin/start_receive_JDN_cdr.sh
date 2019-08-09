LIB_DIR=$HOME/interface/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|grep -v saf-1.0.9.jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/interface/resources/interface
nohup java -server -Xms512m -Xmx512m -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/interface/log -DLOG.NAME=receive_jdn_cdr com.tydic.beijing.billing.interfacex.ReceiveCDRofJDNMain > /dev/null &

