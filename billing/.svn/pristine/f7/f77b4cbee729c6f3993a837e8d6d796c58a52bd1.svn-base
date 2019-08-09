LIB_DIR=$HOME/interface/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|grep -v saf-1.0.9.jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/interface/resources/interface
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/interface/log -DLOG.NAME=queryremainresource com.tydic.beijing.billing.interfacex.QueryRemainResourceMain > /dev/null &
