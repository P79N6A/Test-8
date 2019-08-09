LIB_DIR=$HOME/interface/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/interface/resources/interface
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/interface/log -DLOG.NAME=sendmsgservice_2 com.tydic.beijing.billing.interfacex.SendMessageServiceMain 2> /dev/null &
