LIBPATH="./lib"
CP=.
for filename in ${LIBPATH}/*.jar
 do
   CP=${CP}":"${filename}
 done
 echo $CP
nohup java -server -Xms512m -Xmx512m -XX:PermSize=128m -cp $CP:billing-0.0.1-SNAPSHOT.jar:resources/rating com.tydic.beijing.billing.rating.LiangHaoSmsMain &
