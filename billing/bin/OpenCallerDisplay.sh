LIBPATH="./lib"
CP=.
for filename in ${LIBPATH}/*.jar
do
 CP=${CP}":"${filename}
done
echo $CP
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m -cp $CP:billing-0.0.1-SNAPSHOT.jar:resources/rating  com.tydic.beijing.billing.rating.OpenCallerDisplayMain>opencallerdisplay.log &
