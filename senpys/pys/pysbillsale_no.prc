create or replace procedure pysbillsale_no(szDate varchar2,start_date varchar2,stop_date varchar2 ---六位具体到月份
                                        ) is
  v_sql        varchar2(2000);

begin

--国内短信
v_sql:='insert into PYS_BIL_CDR select ''sms'',''inner_sms'',0,''0'',nvl(trim(a.roamingtype),''0''),c.top_flag,count(1) ,0,'||szDate||'
from cdr_cdr300_'||to_char(to_date(szDate,'yyyymm'),'mm')||'  a,info_user b ,code_local_net c where a.servid=b.user_id and b.local_net=c.local_net
and a.call_type in(''10'') and a.payflag=0 and not b.tele_type=''CTC'' 
and not a.servid in ((select user_id from info_user where create_date in (select create_date from INFO_USER where create_date > to_date('||start_date||',''yyyymmdd'') and create_date < to_date('||stop_date||',''yyyymmdd''))  ))
group by a.roamingtype,c.top_flag  ';

dbms_output.put_line(v_sql);
execute immediate v_sql;


--国内彩信
v_sql:='insert into PYS_BIL_CDR select ''sms'',''inner_color'',0,''0'',nvl(trim(a.roamingtype),''0''),c.top_flag,count(1) ,0,'||szDate||'
from cdr_cdr300_'||to_char(to_date(szDate,'yyyymm'),'mm')||'  a,info_user b ,code_local_net c where a.servid=b.user_id and b.local_net=c.local_net
and a.call_type in(''30'') and a.payflag=0 and not b.tele_type=''CTC'' 
and not a.servid in ((select user_id from info_user where create_date in (select create_date from INFO_USER where create_date > to_date('||start_date||',''yyyymmdd'') and create_date < to_date('||stop_date||',''yyyymmdd''))  ))
group by a.roamingtype,c.top_flag  ';

dbms_output.put_line(v_sql);
execute immediate v_sql;

--国内数据

v_sql:='insert into PYS_BIL_CDR select ''data'',''inner_flow'',0,''0'',nvl(trim(a.roamingtype),''0''),a.calling_number_home_area,sum(round(to_number(a.total_flow)/1024,2)),0,'||szDate||'
from cdr_cdr200_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b ,code_local_net c where a.servid=b.user_id and b.local_net=c.local_net
and not a.roamingtype in(''5'',''4'') and a.payflag=0 and not b.tele_type=''CTC'' 
and not a.servid in ((select user_id from info_user where create_date in (select create_date from INFO_USER where create_date > to_date('||start_date||',''yyyymmdd'') and create_date < to_date('||stop_date||',''yyyymmdd''))  ))
group by a.roamingtype,a.calling_number_home_area';
dbms_output.put_line(v_sql);
execute immediate v_sql;



  commit;
end pysbillsale_no;
/
