#!/bin/bash
#Checking ...
echo "Now start exec pysMonthCdr:"$(date +"%Y-%m-%d %H:%M:%S")
sqlplus -s $1  <<EOF!
set serveroutput on
set linesize 1000
exec pysMonthCdr_ctc(to_char(add_months(sysdate,-1),'yyyymm'));
----------exec pysMonthCdr_ctc(201504);

exit;
EOF!
echo "Over Exec pysMonthCdr done:"$(date +"%Y-%m-%d %H:%M:%S")
exit
