CONF_DIR=../resources/memcache
LIB_DIR=../lib
LIB_JARS=`ls $LIB_DIR|grep .jar|grep -v saf-1.0.9.jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/interface/log -DLOG.NAME=memcachedupdate com.tydic.beijing.billing.memcache.MemcachedUpdate > /dev/null &

