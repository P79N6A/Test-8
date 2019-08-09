#!/bin/bash
#Checking ...
echo "Now start exec pysTruncateCdr:"$(date +"%Y-%m-%d %H:%M:%S")
ConnStr="newbilling/newbilling@172.168.1.246/bssgx"
sqlplus -s $ConnStr  <<EOF!
set serveroutput on
set linesize 1000
delete from pys_idx_month_ctc where RPT_DATE=to_char(add_months(sysdate,-1),'yyyymm');
--delete from pys_idx_month_ctc ;

commit;
exit;
EOF!
echo "Exec pysTruncateCdr done:"$(date +"%Y-%m-%d %H:%M:%S")
exit
