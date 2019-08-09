create or replace procedure pysBilVoice(szDate varchar2) is
type cursor_type is ref cursor;
v_voice    cursor_type;
v_sql      varchar2(2000);
v_tarrif   VARCHAR2(512);
v_callduriation NUMBER(10);
v_area_code   VARCHAR2(6);

v_longdist  VARCHAR2(2);
v_roaming   VARCHAR2(2);
v_busi_code VARCHAR2(50);
v_free_flag number;
v_busi_type VARCHAR2(10);
v_payflag number(10);

v_value      number(20,2);
v_tem        varchar2(20);
v_colpos        number;
v_semipos       number;
v_arraycnt      number;
v_sale_flag     number;
begin
  v_sale_flag:=0; --优惠打折区域标志

    v_sql:='select tariffinfo,CALLDURIATION,decode(payflag,1,CALLEDPARTYLOCATIONCITY,callingpartylocationcity),nvl (trim(longdistancetype),''0''),nvl(trim(roamingtype),''0''), payflag from
(
(select tariffinfo,CALLDURIATION,CALLEDPARTYLOCATIONCITY,callingpartylocationcity,longdistancetype,roamingtype,payflag from cdr_cdr100_'||to_char(to_date(szDate,'yyyymm'),'mm')||'  a ,info_user b  where not call_type in(1,2,3) and  a.servid=b.user_id and  not b.tele_type=''CTC'' and roamingtype in(''5'',''4'') and payflag=0)
)
';

  open v_voice for v_sql;
  loop
    fetch v_voice into v_tarrif,v_callduriation,v_area_code,v_longdist,v_roaming, v_payflag;
    exit when v_voice%notfound;
    if(v_payflag=0 or v_roaming='4' or v_roaming='5') then
    v_colpos:=1;
    v_semipos:=1;
    v_arraycnt:=1;
    loop
      v_colpos:=instr(v_tarrif,':',-1,v_arraycnt);

      v_semipos:=instr(v_tarrif,';',-1,v_arraycnt);

      v_value:=to_number(substr(v_tarrif,v_colpos+1,v_semipos-v_colpos-1));

      v_arraycnt:=v_arraycnt+1;

      v_semipos:=instr(v_tarrif,';',-1,v_arraycnt);

      v_tem:=trim(substr(v_tarrif,v_semipos+1,v_colpos-v_semipos-1));

      begin
      select busi_code,free_flag,busi_type into v_busi_code,v_free_flag,v_busi_type  from PYS_RULE_ACCT_ITEM_IDX where

acct_item_code=v_tem;
      if v_free_flag<>1 then
          update pys_bil_cdr set bil_value=bil_value+ceil(v_callduriation/60)*60
                         where  busi_code=v_busi_code and
                                free_flag=v_free_flag and
                                busi_type=v_busi_type and
                                longdistancetype=v_longdist and
                                roamingtype=v_roaming and
                                area_code=v_area_code and
                                sale_flag=v_sale_flag and
                                insert_date=szDate;


           if sql%rowcount <=0 then
              insert into pys_bil_cdr

(busi_type,busi_code,free_flag,longdistancetype,roamingtype,area_code,bil_value,sale_flag,insert_date)
              values(v_busi_type,v_busi_code,v_free_flag,v_longdist,v_roaming,v_area_code,ceil(v_callduriation/60)*60,v_sale_flag,szDate);
           end if;
           exit;
      end if;
      update pys_bil_cdr set bil_value=bil_value+((v_value/60)*60)
                     where  busi_code=v_busi_code and
                            free_flag=v_free_flag and
                            busi_type=v_busi_type and
                            longdistancetype=v_longdist and
                            roamingtype=v_roaming and
                            area_code=v_area_code and
                            sale_flag=v_sale_flag and
                            insert_date=szDate;
       if sql%rowcount <=0 then
          insert into pys_bil_cdr

(busi_type,busi_code,free_flag,longdistancetype,roamingtype,area_code,bil_value,sale_flag,insert_date)
          values(v_busi_type,v_busi_code,v_free_flag,v_longdist,v_roaming,v_area_code,(v_value/60)*60,v_sale_flag,szDate);
       end if;
       v_callduriation:=v_callduriation-((v_value/60)*60);
       if v_callduriation<0 then
          v_callduriation:=0;
       end if;
       exit when  v_semipos=0;
      exception
        when NO_DATA_FOUND THEN
             null;
      end;
    end loop;
    end if;
  end loop;
  close v_voice;
  commit;
end pysBilVoice;
/
