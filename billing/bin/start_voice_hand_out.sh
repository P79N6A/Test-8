LIB_DIR=$HOME/interface/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=$HOME/interface/resources/interface
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/data/log/interface -DLOG.NAME=voicehandout com.tydic.beijing.billing.interfacex.VoiceHandOutMain > /dev/null &
