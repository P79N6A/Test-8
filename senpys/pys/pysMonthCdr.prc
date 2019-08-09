create or replace procedure pysMonthCdr(szDate in varchar2 ---六位具体到月份
                                        ) is
  v_sql        varchar2(20000);
  v_arpu        number(20,2);
  v_usnelw      number;
  v_arpu_sumaus number(20,2);
  v_arpu_user1  number;
  v_arpu_user2  number;
  --v_monthday number;
  v_AMOUNT_sum  number;
  v_AMOUNT_bc  number;
  v_AMOUNT_s  number;
  v_AMOUNT_new_mon  number;
  v_AMOUNT_new_mon1  number;
  v_wy  number;
  allus number;
  v_aly  number;
  v_wys  number;
   v_AMOUNT_new_mons  varchar2(10);
  v_ZK varchar2(10);

begin
 --- v_monthday := to_char(last_day(to_date(szDate, 'yyyymm')),'yyyymmdd');

  ------1）计费时长秒/条数/MB  bill_mth_revecash_count_times

       pysbillsale_yes(szDate,start_date => '20141201' ,stop_date => '20150115'); --国内业务分时段
       pysbillsale_no(szDate,start_date => '20141201' ,stop_date => '20150115');  --国内业务分时段 其它

       pysbillsale_voice_yes(szDate,start_date => '20141201' ,stop_date => '20150115');
       pysbillsale_voice_no(szDate,start_date => '20141201' ,stop_date => '20150115');


------------------------------------------------------------------------------------------------------------------------------------------------------------------

---标准其他费用
v_sql:='insert into pys_idx_month select a.free_flag,'||szDate||',ceil(nvl(sum(a.bil_value),0)),
CAST(sum(a.bil_value)*CAST((select bjfl from pys_PAYABLE where zyw_code = a.busi_code) AS decimal(10,2)) AS decimal(10,2)) 
,''*'',a.area_code,a.SALE_FLAG,a.busi_code 
         from pys_bil_cdr a where a.free_flag=0 and not a.busi_code in (''inner_flow'',''voice_inner'',''inter_voice'',''10193_voice'',''ip_voice'',''inter_roaming_data'',''inter_roaming_voice'') and a.insert_date='||szDate||'   group by a.area_code,a.busi_code,a.SALE_FLAG,a.free_flag';
         

   execute immediate v_sql;



----国际漫游出访
--v_sql:='insert into pys_idx_month select a.free_flag,'||szDate||',ceil(nvl(sum(a.bil_value/1024),0)),
--CAST((sum(a.bil_value)/1024)*CAST((select bjfl from pys_PAYABLE where zyw_code = a.busi_code) AS decimal(10,2)) AS decimal(10,2))
--,''*'',a.area_code,a.SALE_FLAG,a.busi_code
--         from pys_bil_cdr a where a.free_flag=0 and a.busi_code in (''inter_roaming_voice'') and a.insert_date='||szDate||'   group by a.area_code,a.busi_code,a.SALE_FLAG,a.free_flag';

v_sql:='insert into pys_idx_month select ''0'','||szDate||',ceil(nvl(sum(ceil(a.callduriation /60)*60),0)),
ceil(nvl(CAST(sum(ceil(a.callduriation /60)*60)*1 AS decimal(10,2)),0))
,''*'',''0'',0,''inter_roaming_voice'' from cdr_cdr100_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a , info_user b where a.servid=b.user_id  and  a.roamingtype IN (4,5)';



   execute immediate v_sql;

----国际漫游数据
v_sql:='insert into pys_idx_month select a.free_flag,'||szDate||',ceil(nvl(sum(a.bil_value/1024),0)),
CAST((sum(a.bil_value)/1024)*CAST((select bjfl from pys_PAYABLE where zyw_code = a.busi_code) AS decimal(10,2)) AS decimal(10,2))
,''*'',a.area_code,a.SALE_FLAG,a.busi_code
         from pys_bil_cdr a where a.free_flag=0 and a.busi_code in (''inter_roaming_data'') and a.insert_date='||szDate||'   group by a.area_code,a.busi_code,a.SALE_FLAG,a.free_flag';

   execute immediate v_sql;


----国际长途
v_sql:='insert into pys_idx_month select a.free_flag,'||szDate||',ceil(nvl(sum(a.bil_value),0)),
CAST((sum(a.bil_value)/6)*CAST((select bjfl from pys_PAYABLE where zyw_code = a.busi_code) AS decimal(10,2)) AS decimal(10,2))
,''*'',a.area_code,a.SALE_FLAG,a.busi_code
         from pys_bil_cdr a where a.free_flag=0 and a.busi_code in (''inter_voice'') and a.insert_date='||szDate||'   group by a.area_code,a.busi_code,a.SALE_FLAG,a.free_flag';

   execute immediate v_sql;




----ip_voice 
v_sql:='insert into pys_idx_month select ''0'','||szDate||',ceil(nvl(sum(ceil(a.callduriation/60)*60),0)),
ceil(nvl(CAST(sum(ceil(a.callduriation/60)*60)*1 AS decimal(10,2)),0))
,''*'',''0'',0,''ip_voice'' from cdr_cdr100_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a , info_user b where a.servid=b.user_id  and not  a.roamingtype in(''5'',''4'') and a.orignialcalledparty not like ''10193%'' and a.orignialcalledparty  like ''17911%'' and not a.callduriation < 3 ';


   execute immediate v_sql;
   

 

   
----10193_voice

v_sql:='insert into pys_idx_month select ''0'','||szDate||',ceil(nvl(sum(ceil(a.callduriation/60)*60),0)),
ceil(nvl(CAST(sum(ceil(a.callduriation/60)*60)*1 AS decimal(10,2)),0))
,''*'',''0'',0,''10193_voice'' from cdr_cdr100_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a , info_user b where a.servid=b.user_id  and not  a.roamingtype in(''5'',''4'') and a.orignialcalledparty not like ''17911%'' and a.orignialcalledparty  like ''10193%'' and not a.callduriation < 3 ';


   execute immediate v_sql;





----国内数据

v_sql:='insert into pys_idx_month select a.free_flag,'||szDate||',ceil(nvl(sum(a.bil_value/1024),0)),
CAST(sum(a.bil_value/1024)*CAST((select bjfl from pys_PAYABLE where zyw_code = ''inner_flow'') AS decimal(10,2)) AS decimal(10,2))
,''*'',a.area_code,a.SALE_FLAG,a.busi_code
        from pys_bil_cdr a where a.free_flag=0 and a.busi_code=''inner_flow'' and a.insert_date='||szDate||'  group by a.area_code,a.busi_code,a.SALE_FLAG,a.free_flag';

   execute immediate v_sql;
----国内语音

v_sql:='insert into pys_idx_month select a.free_flag,'||szDate||',ceil(nvl(sum(a.bil_value),0)),
CAST((sum(a.bil_value)/60)*CAST((select bjfl from pys_PAYABLE where zyw_code = ''voice_inner'') AS decimal(10,2)) AS decimal(10,2))
,''*'',a.area_code,a.SALE_FLAG,a.busi_code
         from pys_bil_cdr a where a.free_flag=0 and a.busi_code=''voice_inner'' and a.insert_date='||szDate||'  group by a.area_code,a.busi_code,a.SALE_FLAG,a.free_flag';


   execute immediate v_sql;


      --来电显示业务费用计算
  --  v_sql:='insert into pys_idx_month select  ''0'','||szDate||',CAST( count(distinct user_id) AS decimal(10))-1000  , CAST(round(((count(distinct user_id)-1000)*5), 2) AS decimal(10,2)),''*'',''0'',0,''inner_disp''
  --  from CRM_USER.LIFE_USER_STATUS  where exp_date >= to_date('|| szDate||'01,''yyyymmdd'')  and eff_date < to_date('|| szDate ||'30,''yyyymmdd'') and user_status in(''201'',''101'',''501'') ';

 v_sql:='insert into pys_idx_month select ''0'','||szDate||',CAST( count(distinct user_id) AS decimal(10))  , CAST(round(((count(distinct user_id))*5), 2) AS decimal(10,2)),''*'',''0'',0,''inner_disp'' from 
 (
-- 用户服务生命周期表（取本月服务有效的数据）
SELECT user_id
  FROM CRM_USER.LIFE_SERVICE_CYCLE
 WHERE SERVICE_ID = ''S0004''
   AND EFF_FLAG = ''0''
   AND TO_CHAR(EFF_DATE, ''YYYYMM'') <= '|| szDate||'
   AND TO_CHAR(EXP_DATE, ''YYYYMM'') >= '|| szDate||'     
   
 INTERSECT -- 交集
 -- 用户状态生命周期（取本月状态为 501 201 101）
    SELECT user_id
     FROM CRM_USER.LIFE_USER_STATUS
    WHERE TO_CHAR(EFF_DATE, ''YYYYMM'') <= '|| szDate||'
      AND TO_CHAR(EXP_DATE, ''YYYYMM'') >= '|| szDate||'
      AND USER_STATUS  IN(''501'',''201'',''101'') 
  MINUS  -- 差集
  -- 用户停来显
SELECT user_id
  FROM CRM_USER.INFO_TAKE_LSFEE T
 WHERE TO_CHAR(START_DATE, ''YYYYMM'') < '|| szDate||'
   AND TO_CHAR(END_DATE, ''YYYYMM'') >= '|| szDate||'
   AND STOP_FLAG =''1''  
  
  )';


--dbms_output.put_line(v_sql);
   execute immediate v_sql;

  --呼叫转移 累计量

    v_sql:=' insert into pys_idx_month select ''0'','||szDate||
        ',ceil(nvl(sum(case
             when trim(a.forwardingflag) in (''0'', ''3'') and
                  a.callingpartylocationcity = a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 60
             when trim(a.forwardingflag) in (''0'', ''3'') and
                  a.callingpartylocationcity <> a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 60 + ceil(a.callduriation / 6) * 6
             when trim(a.forwardingflag) in (''1'', ''2'') and
                  trim(a.roamingtype) = ''1'' and
                  a.callingpartylocationcity = a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 60
             when trim(a.forwardingflag) in (''1'', ''2'') and
                  trim(a.roamingtype) = ''1'' and
                  a.callingpartylocationcity <> a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 60 + ceil(a.callduriation / 6) * 6
             when trim(a.forwardingflag) in (''1'', ''2'') and
                  trim(a.roamingtype) <> ''1'' then
              ceil(a.callduriation / 60) * 60
             else
              null
           end),0)) ,
  sum(case
             when trim(a.forwardingflag) in (''0'', ''3'') and
                  a.callingpartylocationcity = a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 0.1
             when trim(a.forwardingflag) in (''0'', ''3'') and
                  a.callingpartylocationcity <> a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 0.1 +
              ceil(a.callduriation / 6) * 0.07
             when trim(a.forwardingflag) in (''1'', ''2'') and
                  trim(a.roamingtype) = ''1'' and
                  a.callingpartylocationcity = a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 0.1
             when trim(a.forwardingflag) in (''1'', ''2'') and
                  trim(a.roamingtype) = ''1'' and
                  a.callingpartylocationcity <> a.calledpartylocationcity then
              ceil(a.callduriation / 60) * 0.1 +
              ceil(a.callduriation / 6) * 0.07
             when trim(a.forwardingflag) in (''1'', ''2'') and
                  trim(a.roamingtype) <> ''1'' then
              ceil(a.callduriation / 60)
             else
              null
           end) 
                ,''*'',decode(a.payflag,1,a.CALLEDPARTYLOCATIONCITY,a.callingpartylocationcity),0,''forward_voice_jb''
 from cdr_cdr100_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a , info_user b where a.servid=b.user_id
 and a.call_type in (''2'')
group by a.longdistancetype,decode(a.payflag,1,a.CALLEDPARTYLOCATIONCITY,a.callingpartylocationcity) ';


   execute immediate v_sql;



  
----------------------------------------------------------------------------------------------------------------------------------------------------------

   ------type_amount =0，基础打折资费  累计费用
--2)国际基本项费用,打折及附加规则-----------------------------------------------------------------------------------------------------------------------------------------
     --基本打折资费
     v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * b.zk) AS decimal(10,2)),''0'',a.SALE_FLAG,b.zk ,'|| szDate ||'
        from pys_idx_month a,PYS_PAYABLE b  where a.busi_code=b.zyw_code and idx_code=''0''or idx_code=''1'' and b.yw_type =''国际业务'' and rpt_date='|| szDate ||' group by a.SALE_FLAG,b.zyw,b.zk   ';

   execute immediate v_sql;



  ---select * from INFO_PAYABLES t where t.type_amount=0 基本费用计算
  



--------------type_amount =1附加打折资费  累计费用-----------------------------------------------------------------------------------------------------------------------------
 --阶梯打折---------阶梯打折0.69
   ----国内业务
   select sum(amount) into v_AMOUNT_sum from info_payable where datemon like '2016%' ;--除这个月之前所有的结算金额

   select sum(CAL_AMOUNT) into v_AMOUNT_bc from info_payables where TYPE_AMOUNT=0 and ACCTMON=szDate;  --这个月结算基本金额
      
   
   Dbms_Output.put_line('v_AMOUNT_sum: '||v_AMOUNT_sum);
   Dbms_Output.put_line('v_AMOUNT_bc: '||v_AMOUNT_bc);
   
   
   v_AMOUNT_s :=v_AMOUNT_sum+v_AMOUNT_bc;
   v_AMOUNT_new_mon  := v_AMOUNT_bc;
   v_aly:=ceil(v_AMOUNT_s/10000000);
   
   v_wy:=ceil(v_AMOUNT_sum/10000000);
  
   Dbms_Output.put_line('v_AMOUNT_s: '||v_AMOUNT_s);
   Dbms_Output.put_line('v_AMOUNT_new_mon: '||v_AMOUNT_new_mon);
    
     v_wys :=((0.7-0.01*(v_wy-1))) ;
     Dbms_Output.put_line('v_wys: '||v_wys);
     Dbms_Output.put_line('v_aly: '||v_aly);
     Dbms_Output.put_line('v_wy: '||v_wy);
     allus := (10000000*v_wy-v_AMOUNT_sum)*v_wys;
     Dbms_Output.put_line('allus: '||allus);

      if v_aly-v_wy-1 =  0 then 
       v_AMOUNT_new_mon := allus;
       Dbms_Output.put_line('v_AMOUNT_new_mon__0: '||v_AMOUNT_new_mon);
      elsif v_aly-v_wy-1 =  1 then 
       v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01);
       Dbms_Output.put_line('v_AMOUNT_new_mon__1: '||v_AMOUNT_new_mon);
      elsif v_aly-v_wy-1 =  2 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02);
        Dbms_Output.put_line('v_AMOUNT_new_mon__2: '||v_AMOUNT_new_mon);
      elsif v_aly-v_wy-1 =  3 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03);
         Dbms_Output.put_line('v_AMOUNT_new_mon__3: '||v_AMOUNT_new_mon);
      elsif v_aly-v_wy-1 =  4 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04);
      elsif v_aly-v_wy-1 =  5 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04)+10000000*(v_wys-0.05);
      elsif v_aly-v_wy-1 =  6 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04)+10000000*(v_wys-0.05)+10000000*(v_wys-0.06);
      elsif v_aly-v_wy-1 =  7 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04)+10000000*(v_wys-0.05)+10000000*(v_wys-0.06)+10000000*(v_wys-0.07);
      elsif v_aly-v_wy-1 =  8 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04)+10000000*(v_wys-0.05)+10000000*(v_wys-0.06)+10000000*(v_wys-0.07)+10000000*(v_wys-0.08);
      elsif v_aly-v_wy-1 =  9 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04)+10000000*(v_wys-0.05)+10000000*(v_wys-0.06)+10000000*(v_wys-0.07)+10000000*(v_wys-0.08)+10000000*(v_wys-0.09);

     end if; 
   
     v_AMOUNT_new_mon1 := (v_AMOUNT_bc-(10000000*v_wy-v_AMOUNT_sum)-((v_aly-v_wy-1)*10000000))*(0.7-0.01*(v_aly-1));
      Dbms_Output.put_line('v_AMOUNT_new_mon1_w: '||v_AMOUNT_new_mon1);
    

     v_AMOUNT_new_mon := v_AMOUNT_new_mon+v_AMOUNT_new_mon1;
       Dbms_Output.put_line('v_AMOUNT_new_mon_zg: '||v_AMOUNT_new_mon);
      
     if v_aly = v_wy  then
         v_AMOUNT_new_mon := v_AMOUNT_bc *v_wys; 
         Dbms_Output.put_line('v_AMOUNT_new_mon_11: '||v_AMOUNT_new_mon);
      end if; 
   
      if v_aly-2 >=  10 then 
        v_AMOUNT_new_mon := allus+10000000*(v_wys-0.01)+10000000*(v_wys-0.02)+10000000*(v_wys-0.03)+10000000*(v_wys-0.04)+10000000*(v_wys-0.05)+10000000*(v_wys-0.06)+10000000*(v_wys-0.07)+10000000*(v_wys-0.08)+10000000*(v_wys-0.09);
    
    v_AMOUNT_new_mon1 := (v_AMOUNT_bc-(10000000*v_wy-v_AMOUNT_sum)-((v_aly-2)*10000000))*0.60;
      Dbms_Output.put_line('v_AMOUNT_new_mon1_tw: '||v_AMOUNT_new_mon1);
   
     v_AMOUNT_new_mon := v_AMOUNT_new_mon+v_AMOUNT_new_mon1;
       Dbms_Output.put_line('v_AMOUNT_new_mon_tz: '||v_AMOUNT_new_mon);
    
    end if; 
   


   
   v_sql:='select CAST(nullif(nullif('||v_AMOUNT_new_mon||', 0)/nullif('||v_AMOUNT_bc||', 0), 0) AS decimal(10,4))  from dual ';
   Dbms_Output.put_line('v_AMOUNT_new_mon/v_AMOUNT_bcv_sql: '||v_sql);
  EXECUTE IMMEDIATE v_sql INTO v_AMOUNT_new_mons;
   
      
  v_sql:='select 0'||v_AMOUNT_new_mons||' from dual ';
  EXECUTE IMMEDIATE v_sql INTO v_ZK;
    v_ZK:= '0'||v_ZK;
    
  
    Dbms_Output.put_line('v_AMOUNT_s: '||v_AMOUNT_s);
    Dbms_Output.put_line('v_ZK: '||v_ZK);
   
   v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * '||v_ZK||') AS decimal(10,2)),''1'',1,'''||v_ZK||''' ,'|| szDate ||'
        from pys_idx_month a,PYS_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0'' or idx_code=''1'') and b.yw_type=''国内业务'' and a.SALE_FLAG=''1'' and rpt_date='|| szDate ||' group by a.SALE_FLAG,b.zyw   ';

  Dbms_Output.put_line('v_sql: '||v_sql);
   execute immediate v_sql;

      v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * '||v_ZK||') AS decimal(10,2)),''1'',0,'''||v_ZK||''' ,'|| szDate ||'
        from pys_idx_month a,PYS_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0'' or idx_code=''1'') and b.yw_type=''国内业务'' and a.SALE_FLAG=''0'' and rpt_date='|| szDate ||' group by a.SALE_FLAG,b.zyw   ';
Dbms_Output.put_line('v_sql: '||v_sql);
   execute immediate v_sql;



 ----附加业务


 v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * b.zk) AS decimal(10,2)),''1'',0,b.zk ,'|| szDate ||'
        from pys_idx_month a,PYS_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0'' or idx_code=''1'') and b.yw_type=''附加业务'' and a.SALE_FLAG=''0'' and rpt_date='|| szDate ||' group by a.SALE_FLAG,b.zyw,b.zk   ';

   execute immediate v_sql;

 ----其他

  v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * b.zk) AS decimal(10,2)),''1'',0,b.zk ,'|| szDate ||'
        from pys_idx_month a,PYS_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0'' or idx_code=''1'') and b.yw_type=''其他'' and a.SALE_FLAG=''0'' and rpt_date='|| szDate ||' group by a.SALE_FLAG,b.zyw,b.zk   ';

   execute immediate v_sql;


-----国内数据业务----------------------------------------0.9数据促销价格九折------------------------------------------------------------------------------
   
   update info_payables set AMOUNT= CAST(AMOUNT * 0.9 AS decimal(10,2))  where type_amount=1 and sale_flag=1 and acctmon=szDate and business_sub='国内数据业务';
   update info_payables set AMOUNT= CAST(AMOUNT * 0.9 AS decimal(10,2))  where type_amount=1 and sale_flag=0 and acctmon=szDate and business_sub='国内数据业务';

   
-----获取----------------------------------------------------------------------------------------------
   --ARPU=企业当月结算收入（不含税）/(（企业月初网上用户数+企业月末网上用户数）/2)。

   v_usnelw:=szDate;
   
   
   select sum(AMOUNT) into v_arpu_sumaus from INFO_PAYABLES where acctmon=v_usnelw and type_amount=1 ; --查询上个月69折的全部费用(企业当月结算收入)
   Dbms_Output.put_line('v_arpu_sumaus: '||v_arpu_sumaus);
  
   ---企业月初网上用户数
  --v_sql:='select count(distinct servid) from CDR_CDR200_'||to_char(to_date(szDate,'yyyymm'),'mm')||'  where timestamp like '''||szDate||'01%''';
   
   v_sql:='select count(distinct user_id) from end_info_user_'||to_char(add_months(sysdate,-2),'mm')||'  where user_status in(''101'') ';
   Dbms_Output.put_line('v_sql: '||v_sql);
  EXECUTE IMMEDIATE v_sql INTO v_arpu_user1;
  Dbms_Output.put_line('v_arpu_user1: '||v_arpu_user1);
  
  ----企业月末网上用户数
   --v_sql:='select count(distinct servid) from CDR_CDR200_'||to_char(to_date(szDate,'yyyymm'),'mm')||'  where timestamp like '''||v_monthday||'%''';
   
      v_sql:='select count(distinct user_id) from end_info_user where user_status in(''101'') ';
   
  EXECUTE IMMEDIATE v_sql INTO v_arpu_user2;
  Dbms_Output.put_line('v_arpu_user2: '||v_arpu_user2);
   
   
   
-----ARPU值计算--------------------------------------------------------------------------------------------------------------------------------------------
   select  nullif(nullif(v_arpu_sumaus, 0)/ nullif((nullif(v_arpu_user1, 0)+ nullif(v_arpu_user2, 0) ) /2, 0), 0)  into v_arpu from dual;

       
    Dbms_Output.put_line('v_arpu: '||v_arpu);

-----------------------------------新arpu打折
   if v_arpu >= 10 and v_arpu <= 100  then   
      update info_payables set AMOUNT= CAST(AMOUNT * (1-(v_arpu/100*0.2)) AS decimal(10,2)) where type_amount=1 and sale_flag=1 and acctmon=szDate and business_sub='国内数据业务';
      update info_payables set AMOUNT= CAST(AMOUNT * (1-(v_arpu/100*0.2)) AS decimal(10,2)) where type_amount=1 and sale_flag=0 and acctmon=szDate and business_sub='国内数据业务';
  elsif  v_arpu > 100 then
      update info_payables set AMOUNT= CAST(AMOUNT * 0.8 AS decimal(10,2)) where type_amount=1 and sale_flag=1 and acctmon=szDate and business_sub='国内数据业务';
      update info_payables set AMOUNT= CAST(AMOUNT * 0.8 AS decimal(10,2)) where type_amount=1 and sale_flag=0 and acctmon=szDate and business_sub='国内数据业务';
   end if;
------------------------------------------------------------------------------------------------------------------------------------------------------------------  
   

---type_amount =2 附加整体打折 累计费用
---select * from INFO_PAYABLES t where t.type_amount=1 业务附加规程计算过程
---附加各项费用累加---------------------------------------------------------------------------------------------------------------------------------------------------
     --国际业务基本费用打折计算

    v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(idx_value * b.zk) AS decimal(10,2)),''2'',0,b.zk ,'|| szDate ||'
        from pys_idx_month a,PYS_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0''or idx_code=''1'') and b.yw_type=''国际业务'' and rpt_date='|| szDate ||' group by b.zyw,b.zk   ';

   execute immediate v_sql;

   --呼转基本费基本费用打折计算
        
             v_sql:='insert into info_payables select a.business_sub, sum(a.data_amount) ,  sum(a.cal_amount) ,sum(a.amount) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a where a.business_sub=''呼叫转移'' and a.acctmon='|| szDate ||' and a.SALE_FLAG=0 and a.type_amount=''1'' group by a.business_sub';
          execute immediate v_sql;
          
          
             v_sql:='insert into info_payables select a.business_sub, sum(a.data_amount) ,  sum(a.cal_amount) ,sum(a.amount) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a,PYS_PAYABLE b  where a.business_sub=b.zyw and b.LJ_TYPE=''来电显示'' and a.acctmon='|| szDate ||' and a.SALE_FLAG=0 and a.type_amount=''1'' group by a.business_sub';
          execute immediate v_sql;
  
  /*
           v_sql:='insert into info_payables select a.business_sub, sum(a.data_amount) ,  sum(a.cal_amount) ,sum(a.amount) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a,PYS_PAYABLE b  where a.business_sub=b.zyw and b.yw_type=''附加业务'' and a.acctmon='|| szDate ||' and a.SALE_FLAG=0 and a.type_amount=''1'' group by a.business_sub';
        

       v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * b.zk) AS decimal(10,2)),''2'',0,b.zk ,'|| szDate ||'
        from pys_idx_month a,pys_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0''or idx_code=''1'') and b.yw_type=''附加业务'' and rpt_date='|| szDate ||' group by b.zyw,b.zk   ';
  */

   execute immediate v_sql;

   --其他基本费用打折计算
        v_sql:='insert into info_payables select a.business_sub, sum(a.data_amount) ,  sum(a.cal_amount) ,sum(a.amount) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a,PYS_PAYABLE b  where a.business_sub=b.zyw and b.yw_type=''其他'' and a.acctmon='|| szDate ||' and a.SALE_FLAG=0 and a.type_amount=''1'' group by a.business_sub';

      /*
       v_sql:='insert into info_payables select b.zyw, sum(idx_value) ,  sum(IDX_AMOUNT) ,CAST( sum(IDX_AMOUNT * b.zk) AS decimal(10,2)),''2'',0,b.zk ,'|| szDate ||'
        from pys_idx_month a,pys_PAYABLE b  where a.busi_code=b.zyw_code and (idx_code=''0''or idx_code=''1'') and b.yw_type=''其他'' and rpt_date='|| szDate ||' group by b.zyw,b.zk   ';
   */

   execute immediate v_sql;


     --国内短信基本费用打折计算
        v_sql:='insert into info_payables select ''国内短信'', sum((a.data_amount)) ,  sum((a.cal_amount)) ,sum((a.amount)) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a  where (a.business_sub=''国内短信''and a.acctmon='|| szDate ||' and a.SALE_FLAG in (''1'',''0'') and a.type_amount=''1'')  ';

   execute immediate v_sql;


      --国内语音基本费用打折计算
        v_sql:='insert into info_payables select ''国内语音'', sum((a.data_amount)) ,  sum((a.cal_amount)),sum((a.amount)) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a  where (a.business_sub=''国内语音''and a.acctmon='|| szDate ||' and a.SALE_FLAG in (''1'',''0'')  and a.type_amount=''1'')   ';

   execute immediate v_sql;


       --国内数据业务本费用打折计算
        v_sql:='insert into info_payables select ''国内数据业务'',sum(a.data_amount) , sum((a.cal_amount)) ,sum((a.amount)) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a  where (a.business_sub=''国内数据业务''and a.acctmon='|| szDate ||' and a.SALE_FLAG in (''1'',''0'')  and a.type_amount=''1'' )     ';

   execute immediate v_sql;


       --国内彩信业务费用打折计算
        v_sql:='insert into info_payables select ''国内彩信'', sum(a.data_amount) ,  sum(a.cal_amount) ,sum(a.amount) ,''2'',0,''*'' ,'|| szDate ||'
        from info_payables a  where (a.business_sub=''国内彩信''and a.acctmon='|| szDate ||' and a.SALE_FLAG in (''1'',''0'') and a.type_amount=''1'' )   ';


   execute immediate v_sql;
   ----select * from INFO_PAYABLES t where t.type_amount=2结果
------------------------------------------------------------------------------------------------------------------------------------------------------------------------

      --3)基本项打折查询是否超过阶梯



      --3)基本项打折查询是否超过阶梯




    -- if sql%rowcount <=0 then






  commit;
end pysMonthCdr;
/
