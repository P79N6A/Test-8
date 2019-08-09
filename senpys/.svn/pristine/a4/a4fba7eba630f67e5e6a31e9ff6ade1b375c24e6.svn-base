create or replace procedure pysMonthCdr_ctc(szDate in varchar2 ---六位具体到月份
                                        ) is
  v_sql        varchar2(20000);  
    
  v_AMOUNT_voice_N  number;
  v_AMOUNT_voice_B  number;
  
  v_AMOUNT_flow_N  number;
  v_AMOUNT_flow_B  number;
  
  v_AMOUNT_disp_N  number;
  v_AMOUNT_disp_B  number;
  
  v_AMOUNT_sms_N  number;
  v_AMOUNT_sms_B  number;
  
  v_AMOUNT_inter_N  number;
  v_AMOUNT_inter_B  number;
  
  v_AMOUNT_inter_zz_N  number;
  v_AMOUNT_inter_zz_B  number;
  
  v_ZK_voice_N  number;
  v_ZK_voice_B  number;
  
  v_ZK_flow_N  number;
  v_ZK_flow_B  number;
  
  v_ZK_disp_N  number;
  v_ZK_disp_B  number;

  v_ZK_sms_N  number;
  v_ZK_sms_B  number;
  
  v_ZK_AMOUNT_N  number;
  v_ZK_AMOUNT_B  number;
  
  
  
  v_month varchar2(5);
  v_lastday varchar2(5);
  v_daymonth varchar2(10);

begin

  v_month := to_char(to_date(szDate, 'yyyymm'),'mm');
  v_daymonth := to_char(sysdate,'yyyymm');
  v_lastday := to_char(last_day(to_date(szDate, 'yyyymm')),'dd');
  ------1）语音南方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''voice'' ,''voice_inner'',''n'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||''' and t.acct_item_code in(1,91,94,84,137,140,143,173)
and  c.tele_type=''CTC''
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;     
       

  ------2）语音北方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''voice'' ,''voice_inner'',''b'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(1,91,94,84,137,140,143,173)
and  c.tele_type=''CTC''
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;     
   
  ----------语音月租南方 
   
     v_sql:='insert into PYS_IDX_MONTH_CTC select ''*'',nvl(sum(substr(a.tariffinfo,INSTR(a.tariffinfo,'':'')+1)),''0'')/100,''voice'',''voice_inner'',''n'','||szDate||'
from cdr_cdr500_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b,code_local_net d,CRM_PRODUCT.CODE_PRODUCT s where a.masterproductid =s.PRODUCT_ID and a.servid=b.user_id and b.local_net=d.local_net
and  s.tele_type=''CTC'' and s.PROD_SUB_TYPE=''P''and s.PRODUCT_STATUS=''101'' and a.masterproductid in (''PP422'',''PP423'',''PP424'',''PP425'',''PP426'',''PP427'',''PP428'',''PP429'',''PP430'',''PP431'',''PP434'',''PP435'',''PP436'',''PP437'',''PP438'',''PP432'',''PP433'') 
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
 ';

 execute immediate v_sql;  
   
   

   
  ----------语音月租南方 
   
     v_sql:='insert into PYS_IDX_MONTH_CTC select ''*'',nvl(sum(substr(a.tariffinfo,INSTR(a.tariffinfo,'':'')+1)),''0'')/100,''voice'',''voice_inner'',''b'','||szDate||'
from cdr_cdr500_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b,code_local_net d,CRM_PRODUCT.CODE_PRODUCT s where a.masterproductid =s.PRODUCT_ID and a.servid=b.user_id and b.local_net=d.local_net
and  s.tele_type=''CTC'' and s.PROD_SUB_TYPE=''P''and s.PRODUCT_STATUS=''101'' and a.masterproductid in (''PP422'',''PP423'',''PP424'',''PP425'',''PP426'',''PP427'',''PP428'',''PP429'',''PP430'',''PP431'',''PP434'',''PP435'',''PP436'',''PP437'',''PP438'',''PP432'',''PP433'') 
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
 ';

 execute immediate v_sql;  
   
   
   
  ------3）数据南方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100 ,''flow'',''inner_flow'',''n'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(103)
and  c.tele_type=''CTC''
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;   
   
   
   
  ------4）数据北方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''flow'' ,''inner_flow'',''b'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(103)
and  c.tele_type=''CTC''
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;     
   
 
  ----------数据月租南方 


     v_sql:='insert into PYS_IDX_MONTH_CTC select ''*'',nvl(sum(substr(a.tariffinfo,INSTR(a.tariffinfo,'':'')+1)),''0'')/100,''flow'',''flow'',''n'','||szDate||'
from cdr_cdr500_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b,code_local_net d,CRM_PRODUCT.CODE_PRODUCT s where a.masterproductid =s.PRODUCT_ID and a.servid=b.user_id and b.local_net=d.local_net
and  s.tele_type=''CTC'' and s.PROD_SUB_TYPE=''P''and s.PRODUCT_STATUS=''101'' and a.masterproductid  in (''PP439'',''PP440'',''PP441'',''PP444'',''PP445'',''PP446'',''PP447'',''PP448'',''PP442'',''PP443'')
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
 ';


 execute immediate v_sql;   
 
 
 
   ----------数据月租北方 
   
     v_sql:='insert into PYS_IDX_MONTH_CTC select ''*'',nvl(sum(substr(a.tariffinfo,INSTR(a.tariffinfo,'':'')+1)),''0'')/100,''flow'',''flow'',''b'','||szDate||'
from cdr_cdr500_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b,code_local_net d,CRM_PRODUCT.CODE_PRODUCT s where a.masterproductid =s.PRODUCT_ID and a.servid=b.user_id and b.local_net=d.local_net
and  s.tele_type=''CTC'' and s.PROD_SUB_TYPE=''P''and s.PRODUCT_STATUS=''101'' and a.masterproductid  in (''PP439'',''PP440'',''PP441'',''PP444'',''PP445'',''PP446'',''PP447'',''PP448'',''PP442'',''PP443'')
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
 ';

 execute immediate v_sql;   
   
  
   
  ------5）短信南方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''sms'' ,''inner_sms'',''n'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(109,136,118)
and  c.tele_type=''CTC''
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;   
   

   
  ------6）短信北方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''sms'' ,''inner_sms'',''b'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(109,136,118)
and  c.tele_type=''CTC''
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;     
 
 
 ----------短信月租南方 



     v_sql:='insert into PYS_IDX_MONTH_CTC select ''*'',nvl(sum(substr(a.tariffinfo,INSTR(a.tariffinfo,'':'')+1)),''0'')/100,''sms'',''inner_sms'',''n'','||szDate||'
from cdr_cdr500_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b,code_local_net d,CRM_PRODUCT.CODE_PRODUCT s where a.masterproductid =s.PRODUCT_ID and a.servid=b.user_id and b.local_net=d.local_net
and  s.tele_type=''CTC'' and s.PROD_SUB_TYPE=''P''and s.PRODUCT_STATUS=''101'' and a.masterproductid  in (''PP449'',''PP450'',''PP451'')
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
 ';


 execute immediate v_sql;   
 
 
  ----------短信月租北方



     v_sql:='insert into PYS_IDX_MONTH_CTC select ''*'',nvl(sum(substr(a.tariffinfo,INSTR(a.tariffinfo,'':'')+1)),''0'')/100,''sms'',''inner_sms'',''b'','||szDate||'
from cdr_cdr500_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b,code_local_net d,CRM_PRODUCT.CODE_PRODUCT s where a.masterproductid =s.PRODUCT_ID and a.servid=b.user_id and b.local_net=d.local_net
and  s.tele_type=''CTC'' and s.PROD_SUB_TYPE=''P''and s.PRODUCT_STATUS=''101'' and a.masterproductid  in (''PP449'',''PP450'',''PP451'')
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
 ';


 execute immediate v_sql;   
 
   
    
  ------7）国际长漫南方基础
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''inter'' ,''inter_roaming_voice'',''n'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(97,100)
and  c.tele_type=''CTC''
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;   
    
    
    
  ------8）国际长漫北方基础
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''inter'' ,''inter_roaming_voice'',''b'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(97,100)
and  c.tele_type=''CTC''
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 
 execute immediate v_sql;         
    
    
    
    
  ------）国际长漫南方增值业务
    
    
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''inter'' ,''inter_roaming_zz'',''n'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(106,112,115,121,124)
and  c.tele_type=''CTC''
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;   
    
    
    
  ------）国际长漫北方增值业务
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''inter'' ,''inter_roaming_zz'',''b'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(106,112,115,121,124)
and  c.tele_type=''CTC''
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 
 execute immediate v_sql;     
    
    
    

---------------9）其他业务北方-----------------------------------------------------------------------------------------------
        
        -- 用户服务生命周期表（取本月服务有效的数据）
                -- 交集
        -- 用户状态生命周期（取本月状态为 401 201 101）
 v_sql:=' insert into PYS_IDX_MONTH_CTC select COUNT(*) , COUNT(*)*5,''disp'',''inner_disp'',''b'','||szDate||' from (   

        SELECT USER_ID
          FROM CRM_USER.LIFE_SERVICE_CYCLE
         WHERE SERVICE_ID = ''D4001''
           AND EFF_FLAG = ''0''
           AND TO_CHAR(EFF_DATE, ''YYYYMM'') <= '||szDate||'
           AND TO_CHAR(EXP_DATE, ''YYYYMM'') >= '||szDate||'
	   AND USER_ID IN
               (SELECT a.USER_ID
                  FROM INFO_USER A,code_local_net d
                 WHERE a.local_net=d.local_net and A.TELE_TYPE = ''CTC''
		       and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
		       )

        INTERSECT 

        SELECT USER_ID
          FROM CRM_USER.LIFE_USER_STATUS
         WHERE TO_CHAR(EFF_DATE, ''YYYYMM'') <= '||szDate||'
           AND TO_CHAR(EXP_DATE, ''YYYYMM'') >= '||v_daymonth||'
           AND USER_STATUS IN (''201'', ''101'', ''401'')
           AND USER_ID IN
               (SELECT a.USER_ID
               FROM INFO_USER A,crm_order.info_order b ,code_local_net c
                  WHERE a.local_net=c.local_net  AND a.user_id=b.user_id
                       and A.TELE_TYPE = ''CTC''
                       AND to_char(b.finish_date,''yyyymmdd'')<'''||szDate||'01''
		       and c.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
		       )
    )';

dbms_output.put_line(v_sql);
execute immediate v_sql;     
----当月开户来电显示  

 v_sql:=' insert into PYS_IDX_MONTH_CTC select nvl(SUM(CEIL(TO_DATE(TO_CHAR(LAST_DAY(TO_DATE('||szDate||', ''yyyymm'')),''yyyymmdd'') || ''235959'', ''yyyymmddHH24:mi:ss'') - FINISH_DATE) ) ,0),
 nvl(SUM((CEIL(TO_DATE(TO_CHAR(LAST_DAY(TO_DATE('||szDate||', ''yyyymm'')),''yyyymmdd'') || ''235959'', ''yyyymmddHH24:mi:ss'') - FINISH_DATE) / '||v_lastday||') * 5) ,0)
  ,''disp'',''inner_disp'',''b'','||szDate||'

  FROM CRM_ORDER.INFO_ORDER A
 WHERE A.USER_ID IN
       (SELECT USER_ID FROM INFO_USER A,code_local_net d WHERE a.local_net=d.local_net and
       A.TELE_TYPE = ''CTC''
       and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
       )
   AND A.USER_ID NOT IN
       (SELECT A.USER_ID
          FROM CRM_USER.INFO_USER        A,
               CRM_ORDER.INFO_ORDER      B,
               CRM_USER.LIFE_USER_STATUS C
         WHERE A.USER_ID = B.USER_ID
           AND B.USER_ID = C.USER_ID
           AND A.TELE_TYPE = ''CTC''
           AND B.USER_EVENT_CODE = ''1''
           AND B.ORDER_STATUS = ''C''
           AND TO_CHAR(B.FINISH_DATE, ''yyyymm'') = '||szDate||'
           AND C.USER_STATUS IN (''401'')
           AND TO_CHAR(C.EFF_DATE, ''yyyymm'') = '||szDate||')
   AND TO_CHAR(A.FINISH_DATE, ''yyyymm'') = '||szDate||'
   AND A.USER_EVENT_CODE = ''1''
   AND A.ORDER_STATUS = ''C''
';

dbms_output.put_line(v_sql);
execute immediate v_sql;     
----当月开户销户 
 v_sql:=' insert into PYS_IDX_MONTH_CTC select SUM(CEIL(C.EFF_DATE - B.FINISH_DATE)) , SUM(CEIL(C.EFF_DATE - B.FINISH_DATE) * 5 / '||v_lastday||'),''disp'',''inner_disp'',''b'','||szDate||' 

  FROM CRM_USER.INFO_USER        A,
       CRM_ORDER.INFO_ORDER      B,
       CRM_USER.LIFE_USER_STATUS C,
      code_local_net d 
 WHERE A.USER_ID = B.USER_ID
   AND a.local_net=d.local_net
   AND B.USER_ID = C.USER_ID
   AND A.TELE_TYPE = ''CTC''
   AND d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
   AND B.USER_EVENT_CODE = ''1''
   AND B.ORDER_STATUS = ''C''
   AND TO_CHAR(B.FINISH_DATE, ''yyyymm'') = '||szDate||'
   AND C.USER_STATUS IN (''401'')
   AND TO_CHAR(C.EFF_DATE, ''yyyymm'') = '||szDate||'
';

dbms_output.put_line(v_sql);
 execute immediate v_sql;     


  -------------------10）其他业务南方----------------------------------------------------------------


  
       -- 用户服务生命周期表（取本月服务有效的数据）
                -- 交集
        -- 用户状态生命周期（取本月状态为 401 201 101）
 v_sql:=' insert into PYS_IDX_MONTH_CTC select COUNT(*) , COUNT(*)*5,''disp'',''inner_disp'',''n'','||szDate||' from (   

        SELECT USER_ID
          FROM CRM_USER.LIFE_SERVICE_CYCLE
         WHERE SERVICE_ID = ''D4001''
           AND EFF_FLAG = ''0''
           AND TO_CHAR(EFF_DATE, ''YYYYMM'') <= '||szDate||'
           AND TO_CHAR(EXP_DATE, ''YYYYMM'') >= '||szDate||'
	   AND USER_ID IN
               (SELECT a.USER_ID
                  FROM INFO_USER A,code_local_net d
                 WHERE a.local_net=d.local_net and A.TELE_TYPE = ''CTC''
		       and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
		       )

        INTERSECT 

        SELECT USER_ID
          FROM CRM_USER.LIFE_USER_STATUS
         WHERE TO_CHAR(EFF_DATE, ''YYYYMM'') <= '||szDate||'
           AND TO_CHAR(EXP_DATE, ''YYYYMM'') >= '||v_daymonth||'
           AND USER_STATUS IN (''201'', ''101'', ''401'')
           AND USER_ID IN
               (SELECT a.USER_ID
               FROM INFO_USER A,crm_order.info_order b ,code_local_net c
                  WHERE a.local_net=c.local_net  AND a.user_id=b.user_id
                       and A.TELE_TYPE = ''CTC''
                       AND to_char(b.finish_date,''yyyymmdd'')<'''||szDate||'01''
		       and not c.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
		       )
    )';

dbms_output.put_line(v_sql);
 execute immediate v_sql;     
----当月开户来电显示  

 v_sql:=' insert into PYS_IDX_MONTH_CTC select nvl(SUM(CEIL(TO_DATE(TO_CHAR(LAST_DAY(TO_DATE('||szDate||', ''yyyymm'')),''yyyymmdd'') || ''235959'', ''yyyymmddHH24:mi:ss'') - FINISH_DATE) ) ,0),
 nvl(SUM((CEIL(TO_DATE(TO_CHAR(LAST_DAY(TO_DATE('||szDate||', ''yyyymm'')),''yyyymmdd'') || ''235959'', ''yyyymmddHH24:mi:ss'') - FINISH_DATE) / '||v_lastday||') * 5) ,0),''disp'',''inner_disp'',''n'','||szDate||'

  FROM CRM_ORDER.INFO_ORDER A
 WHERE A.USER_ID IN
       (SELECT USER_ID FROM INFO_USER A,code_local_net d WHERE a.local_net=d.local_net and
       A.TELE_TYPE = ''CTC''
       and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
       )
   AND A.USER_ID NOT IN
       (SELECT A.USER_ID
          FROM CRM_USER.INFO_USER        A,
               CRM_ORDER.INFO_ORDER      B,
               CRM_USER.LIFE_USER_STATUS C
         WHERE A.USER_ID = B.USER_ID
           AND B.USER_ID = C.USER_ID
           AND A.TELE_TYPE = ''CTC''
           AND B.USER_EVENT_CODE = ''1''
           AND B.ORDER_STATUS = ''C''
           AND TO_CHAR(B.FINISH_DATE, ''yyyymm'') = '||szDate||'
           AND C.USER_STATUS IN (''401'')
           AND TO_CHAR(C.EFF_DATE, ''yyyymm'') = '||szDate||')
   AND TO_CHAR(A.FINISH_DATE, ''yyyymm'') = '||szDate||'
   AND A.USER_EVENT_CODE = ''1''
   AND A.ORDER_STATUS = ''C''
';

dbms_output.put_line(v_sql);
 execute immediate v_sql;     
----当月开户销户 
 v_sql:=' insert into PYS_IDX_MONTH_CTC select SUM(CEIL(C.EFF_DATE - B.FINISH_DATE)) , SUM(CEIL(C.EFF_DATE - B.FINISH_DATE) * 5 / '||v_lastday||'),''disp'',''inner_disp'',''b'','||szDate||' 

  FROM CRM_USER.INFO_USER        A,
       CRM_ORDER.INFO_ORDER      B,
       CRM_USER.LIFE_USER_STATUS C,
      code_local_net d 
 WHERE A.USER_ID = B.USER_ID
   AND a.local_net=d.local_net
   AND B.USER_ID = C.USER_ID
   AND A.TELE_TYPE = ''CTC''
   AND not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
   AND B.USER_EVENT_CODE = ''1''
   AND B.ORDER_STATUS = ''C''
   AND TO_CHAR(B.FINISH_DATE, ''yyyymm'') = '||szDate||'
   AND C.USER_STATUS IN (''401'')
   AND TO_CHAR(C.EFF_DATE, ''yyyymm'') = '||szDate||'
';

dbms_output.put_line(v_sql);
 execute immediate v_sql;     




  ------11）国内漫游语音南方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''voice'' ,''voice_inner_roaming'',''n'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(172)
and  c.tele_type=''CTC''
and not d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;     
       

  ------12）国内漫游语音北方
  
  v_sql:='insert into PYS_IDX_MONTH_CTC select t.user_id,sum(t.fee)/100,''voice'' ,''voice_inner_roaming'',''b'','||szDate||' from BIL_ACT_BILL t,code_acct_month b ,info_user c,code_local_net d
where t.user_id=c.user_id and c.local_net=d.local_net and t.acct_month=b.acct_month and b.partition_no= '''||v_month||'''  and t.acct_item_code in(172)
and  c.tele_type=''CTC''
and d.top_flag in(022,010,0530,0531,0532,0533,0534,0535,0536,0537,0538,0539,0543,0546,0631,0632,0633,0634,0635,0370,0371,0372,0373,0374,0375,0376,0377,0378,0379,0391,0392,0393,0394,0395,0396,0397,0398,0310,0311,0312,0313,0314,0315,0316,0317,0318,0319,0335,0451,0452,0453,0454,0455,0456,0457,0458,0459,0464,0467,0468,0469,0431,0432,0433,0434,0435,0436,0437,0438,0439,0440,0448,024,0410,0411,0412,0413,0414,0415,0416,0417,0418,0419,0421,0427,0429,0349,0350,0351,0352,0353,0354,0355,0356,0357,0358,0359,0470,0471,0472,0473,0474,0475,0476,0477,0478,0479,0482,0483)
group by t.user_id ';
 
 execute immediate v_sql;     



/*----------------重批------------------------------------------------------------------------------------------------------------
toC漫游主被叫调整为国内0.15/元分钟。
toB漫游主被叫国内 0.3/元分

toC调整为0.15元/M；
toB一律按照 0.0002 元/KB 计费。 
*/
   ---国内漫游重批
   update PYS_IDX_MONTH_CTC set AMOUNT= AMOUNT * 2 where RPT_DATE=szDate and BUSI_CODE='voice_inner_roaming';

   ---套外流量重批
   update PYS_IDX_MONTH_CTC set AMOUNT= AMOUNT * 1.333334  where RPT_DATE=szDate and BUSI_CODE='inner_flow';
------------------------------------------------------------------------------------------------------------------------------------------------------

  select sum(AMOUNT) into v_AMOUNT_voice_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='voice'and NB='b';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_voice_B ,0) AS decimal(10,2)),'语音业务','基础业务','北方',szDate);
     
  select sum(AMOUNT) into v_AMOUNT_voice_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='voice'and NB='n';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_voice_N ,0) AS decimal(10,2)),'语音业务','基础业务','南方',szDate);
  
  select CAST( nvl(sum(AMOUNT) ,0) AS decimal(10,2)) into v_ZK_AMOUNT_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB in('voice','flow','disp','sms') and NB='n';
  select CAST( nvl(sum(AMOUNT) ,0) AS decimal(10,2)) into v_ZK_AMOUNT_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB in('voice','flow','disp','sms') and NB='b';
  
  select CAST( nvl( nullif(v_ZK_AMOUNT_B, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_voice_B from dual;
  select CAST( nvl( nullif(v_ZK_AMOUNT_N, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_voice_N from dual;
  
  
    
--------------------------------------------  
 
  select sum(AMOUNT) into v_AMOUNT_flow_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='flow'and NB='n';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_flow_N ,0) AS decimal(10,2)),'数据业务','增值业务','南方',szDate);
  select sum(AMOUNT) into v_AMOUNT_flow_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='flow'and NB='b';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_flow_B ,0) AS decimal(10,2)),'数据业务','增值业务','北方',szDate);
  
  select CAST( nvl( nullif(v_ZK_AMOUNT_N, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_flow_N from dual;
  select CAST( nvl( nullif(v_ZK_AMOUNT_B, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_flow_B from dual;
  
  --select CAST( nvl(v_AMOUNT_flow_N/sum(AMOUNT) ,0) AS decimal(10,2)) into v_ZK_flow_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='flow';
  --select CAST( nvl(v_AMOUNT_flow_B/sum(AMOUNT) ,0) AS decimal(10,2)) into v_ZK_flow_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='flow';
  
------------------------------------------------  
 
  select sum(AMOUNT) into v_AMOUNT_disp_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='disp'and NB='n';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_disp_N ,0) AS decimal(10,2)),'其他业务','增值业务','南方',szDate);
  select sum(AMOUNT) into v_AMOUNT_disp_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='disp'and NB='b';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_disp_B ,0) AS decimal(10,2)),'其他业务','增值业务','北方',szDate);
  
  select CAST( nvl( nullif(v_ZK_AMOUNT_N, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_disp_N from dual;
  select CAST( nvl( nullif(v_ZK_AMOUNT_B, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_disp_B from dual;
  --select CAST( nvl(v_AMOUNT_disp_N/sum(AMOUNT) ,0) AS decimal(10,2)) into v_ZK_disp_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='disp';
  --select CAST( nvl(v_AMOUNT_disp_B/sum(AMOUNT) ,0) AS decimal(10,2)) into v_ZK_disp_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='disp';
  
----------------------------------------------  
  
  select sum(AMOUNT) into v_AMOUNT_sms_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='sms'and NB='n';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_sms_N ,0) AS decimal(10,2)),'短信业务','增值业务','南方',szDate);
  select sum(AMOUNT) into v_AMOUNT_sms_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='sms'and NB='b';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_sms_B ,0) AS decimal(10,2)),'短信业务','增值业务','北方',szDate);
  
  select CAST( nvl( nullif(v_ZK_AMOUNT_N, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_sms_N from dual;
  select CAST( nvl( nullif(v_ZK_AMOUNT_B, 0)/(v_ZK_AMOUNT_N+v_ZK_AMOUNT_B) ,0) AS decimal(10,2)) into v_ZK_sms_B from dual;
  
  --select CAST( nvl(v_AMOUNT_sms_N/sum(AMOUNT),0) AS decimal(10,2)) into v_ZK_sms_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='sms';
  --select CAST( nvl(v_AMOUNT_sms_B/sum(AMOUNT),0) AS decimal(10,2)) into v_ZK_sms_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSINESS_SUB='sms';

------------------------------------------------ 
 
 
  select CAST( nvl(sum(AMOUNT),0) AS decimal(10,2)) into v_AMOUNT_inter_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSI_CODE='inter_roaming_voice'and NB='n';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_inter_N ,0) AS decimal(10,2)),'国际长漫','基础业务','南方',szDate);
  select CAST( nvl(sum(AMOUNT),0) AS decimal(10,2)) into v_AMOUNT_inter_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSI_CODE='inter_roaming_voice'and NB='b';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_inter_B ,0) AS decimal(10,2)),'国际长漫','基础业务','北方',szDate);

  select CAST( nvl(sum(AMOUNT),0) AS decimal(10,2)) into v_AMOUNT_inter_zz_N from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSI_CODE='inter_roaming_zz'and NB='n';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_inter_zz_N ,0) AS decimal(10,2)),'国际长漫','增值业务','南方',szDate);
  select CAST( nvl(sum(AMOUNT),0) AS decimal(10,2)) into v_AMOUNT_inter_zz_B from PYS_IDX_MONTH_CTC where RPT_DATE=szDate and BUSI_CODE='inter_roaming_zz'and NB='b';
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl(v_AMOUNT_inter_zz_B ,0) AS decimal(10,2)),'国际长漫','增值业务','北方',szDate);

   
   /*  
     insert into PYS_IDX_MONTH_CTC
        select '*',CAST( nvl(sum(AMOUNT),0) AS decimal(10,2)),'国际长漫','增值业务','北方',szDate
           from PYS_IDX_MONTH_CTC
          where RPT_DATE=szDate 
          and BUSI_CODE='inter_roaming_zz'
          and NB='b';

     insert into PYS_IDX_MONTH_CTC
        select '*',CAST( nvl(sum(AMOUNT),0) AS decimal(10,2)),'国际长漫','增值业务','南方',szDate
           from PYS_IDX_MONTH_CTC
          where RPT_DATE=szDate 
          and BUSI_CODE='inter_roaming_zz'
          and NB='n';

     */
  
  
--(1)语音业务出账金额   
  if v_ZK_voice_N < 0.45 then 
     v_ZK_voice_N:=0.50;
     else
     v_ZK_voice_N:=0.50;
  end if;

  if v_ZK_voice_B < 0.45 then 
       v_ZK_voice_B:=0.50;
  elsif v_ZK_voice_B > 0.45 then 
       v_ZK_voice_B:=0.47;
  end if;

  
--(2)数据业务出账金额 

  if v_ZK_flow_N < 0.45 then 
     v_ZK_flow_N:=0.60;
     else
     v_ZK_flow_N:=0.60;
  end if;
  
  if v_ZK_flow_B < 0.45 then 
       v_ZK_flow_B:=0.60;
  elsif v_ZK_flow_B > 0.45 then 
       v_ZK_flow_B:=0.57;
  end if;
  
  
--(3)短信业务出账金额 
  
  if v_ZK_sms_N < 0.45 then 
     v_ZK_sms_N:=0.50;
     else
     v_ZK_sms_N:=0.50;
  end if;
  
  if v_ZK_sms_B < 0.45 then 
       v_ZK_sms_B:=0.50;
  elsif v_ZK_sms_B > 0.45 then 
       v_ZK_sms_B:=0.47;
  end if;
  
  
--(4)其他业务出账金额 
  
  if v_ZK_disp_N < 0.45 then 
     v_ZK_disp_N:=0.50;
     else
     v_ZK_disp_N:=0.50;
  end if;
  
  if v_ZK_disp_B < 0.45 then 
       v_ZK_disp_B:=0.50;
  elsif v_ZK_disp_B > 0.45 then 
       v_ZK_disp_B:=0.47;
  end if;
  
  ---结算资金基础业务     
 -- I=(语音业务出账金额*语音折扣+(B2B出账金额-语音业务出账金额-国际长漫出账金额）*其他基础业务折扣)*(1+11%)+国际长漫出账金额基础业务
    
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl((v_AMOUNT_voice_B*v_ZK_voice_B+(v_AMOUNT_voice_B-v_AMOUNT_voice_B-0)*v_ZK_disp_B)*1.11+v_AMOUNT_inter_B,0) AS decimal(10,2)) ,'结算资金','基础业务','北方',szDate);
  insert into PYS_IDX_MONTH_CTC values  ('*',CAST( nvl((v_AMOUNT_voice_N*v_ZK_voice_N+(v_AMOUNT_voice_N-v_AMOUNT_voice_N-0)*v_ZK_disp_N)*1.11+v_AMOUNT_inter_N,0) AS decimal(10,2)),'结算资金','基础业务','南方',szDate);
      

-----结算资金增值业务（元）
----J=(数据业务出账金额增值业务*流量折扣+短信业务出账金额增值业务*短信折扣+其他业务出账金额*其他增值业务折扣)*(1+6%)+国际长漫出账金额增值业务

  insert into PYS_IDX_MONTH_CTC values ('*',CAST( nvl((v_AMOUNT_flow_N*v_ZK_flow_N+v_AMOUNT_sms_N*v_ZK_sms_N+v_AMOUNT_disp_N*v_ZK_disp_N)*1.06+v_AMOUNT_inter_zz_N,0) AS decimal(10,2)),'结算资金','增值业务','南方',szDate);
  insert into PYS_IDX_MONTH_CTC values ('*',CAST( nvl((v_AMOUNT_flow_B*v_ZK_flow_B+v_AMOUNT_sms_B*v_ZK_sms_B+v_AMOUNT_disp_B*v_ZK_disp_B)*1.06+v_AMOUNT_inter_zz_B,0) AS decimal(10,2)),'结算资金','增值业务','北方',szDate);




  commit;
end pysMonthCdr_ctc;
/
