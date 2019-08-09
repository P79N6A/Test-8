CONF_DIR=../resources/memcache
LIB_DIR=../lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m $JAVA_JMX_OPTS -classpath $MAIN_JAR:$LIB_JARS:$CONF_DIR com.tydic.beijing.billing.memcache.MemcachedUpdate > log.txt 2>&1 &

