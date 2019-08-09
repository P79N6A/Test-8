#!/bin/sh

curr_date=`date +'%Y%m%d-%H-%M-%S'`
nohup sh startAutoPresentApp.sh $curr_date>/dev/null &
