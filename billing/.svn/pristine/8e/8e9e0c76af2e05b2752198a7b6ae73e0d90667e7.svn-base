FLAG=`jps | grep ExpertCdrForCallTransferPartnerMain | grep -v 'grep' | wc -l`
if [ $FLAG != 0 ]; then
        echo "ExpertCdrForCallTransferPartnerMain has started, rerun after kill !!!"
        exit
fi
LIB_DIR=$HOME/interface/lib/
LIB_JARS=`ls $LIB_DIR|grep .jar|grep -v saf-1.0.9.jar|grep -v saf-1.0.9.jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CONF_DIR=/home/billing_dev/interface/resources/interface
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -classpath $CONF_DIR:$LIB_JARS -Djava.security.egd=file:///dev/urandom -DLOG.DIR=$HOME/interface/log -DLOG.NAME=expertcalltransfercdr com.tydic.beijing.billing.interfacex.ExpertCdrForCallTransferPartnerMain  > $HOME/interface/log/autohome.log &
