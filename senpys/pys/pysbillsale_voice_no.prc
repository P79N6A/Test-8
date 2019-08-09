create or replace procedure pysbillsale_voice_no(szDate varchar2,start_date varchar2,stop_date varchar2 ---六位具体到月份
                                        ) is
  v_sql        varchar2(2000);

begin
--and not a.call_type in(''1'',''2'',''3'') and not a.longdistancetype in(''3'')
--国内语音
v_sql:='insert into PYS_BIL_CDR select ''voice'',''voice_inner'',0,nvl (trim(a.longdistancetype),''0''),nvl(trim(a.roamingtype),''0''),decode(a.payflag,1,a.CALLEDPARTYLOCATIONCITY,a.callingpartylocationcity), sum(ceil(a.callduriation/60)*60) ,0,'||szDate||'
  from cdr_cdr100_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a , info_user b where a.servid=b.user_id
 and not  a.roamingtype in(''5'',''4'') and not a.calledparty in(''119'',''110'',''122'',''120'') and a.orignialcalledparty not like ''17911%'' and a.orignialcalledparty not like ''10193%'' and not a.callduriation < 3
 and not a.call_type in(''1'',''2'',''3'') and not a.longdistancetype in(''5'') and not b.tele_type=''CTC'' 
 and not a.servid in ((select user_id from info_user where create_date in (select create_date from INFO_USER where create_date > to_date('||start_date||',''yyyymmdd'') and create_date < to_date('||stop_date||',''yyyymmdd''))  ))
 group by a.longdistancetype,a.roamingtype,decode(a.payflag,1,a.CALLEDPARTYLOCATIONCITY,a.callingpartylocationcity) ';
dbms_output.put_line(v_sql);
execute immediate v_sql;

  commit;
end pysbillsale_voice_no;
/
