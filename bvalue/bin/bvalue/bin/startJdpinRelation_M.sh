#!/bin/sh

START_COUNTS=$1
SLEEP_INTERVAL=15

if [ -z "$START_COUNTS" ] ;then
	echo "usage: sh $0 start_counts"
	exit
fi

b=''
t=`echo ${SLEEP_INTERVAL} / 100.0 | bc -l`

for id in $(seq $START_COUNTS)
do
	curr_date=`date +'%Y%m%d-%H-%M-%S'`
	nohup sh ./startJdpinRelation.sh $curr_date >/dev/null &
	sleep 0
	b=''
	for ((i=0;$i<=100;i+=2))
	do
			printf "Starting StartJdpinRelationApp, ID=[%s]:[%-50s]%d%%\r" $id $b $i
			sleep $t
			b=#$b
	done
	echo
done
