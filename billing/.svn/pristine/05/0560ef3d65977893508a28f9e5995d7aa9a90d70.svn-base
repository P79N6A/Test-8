LIBPATH="./lib"


CP=.
for fileName in ${LIBPATH}/*.jar 
do
        CP=${CP}":"${fileName}
done
echo $CP



java -server -Xms1g -Xmx1g -XX:PermSize=128m -cp $CP:billing-0.0.1-SNAPSHOT.jar:resources/rating com.tydic.beijing.billing.interfacex.ExpertRLCdrMain 


