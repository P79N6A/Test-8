CONF_DIR=../resources/memcache
LIB_DIR=../lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS com.tydic.beijing.billing.memcache.GetMemcacheInfo $1 >GetMemcacheInfo.log &
