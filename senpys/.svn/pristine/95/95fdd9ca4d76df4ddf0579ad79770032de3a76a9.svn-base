create or replace procedure pysBilSms(szDate varchar2) is
type cursor_type is ref cursor;
v_sms    cursor_type;
v_sql      varchar2(2000);
v_tarrif   VARCHAR2(512);
v_total_flow number(30,2);
v_area_code   VARCHAR2(6);
v_payflag number(10);

v_longdist  VARCHAR2(2);
v_roaming   VARCHAR2(2);
v_busi_code VARCHAR2(50);
v_free_flag number;
v_busi_type VARCHAR2(10);

v_value      number(20,2);
v_tem        varchar2(20);
v_colpos        number;
v_semipos       number;
v_arraycnt      number;
v_sale_flag     number;
begin
  v_sale_flag:=0; --优惠打折区域标志
  v_sql:='select tariffinfo,1,c.top_flag,''0'',nvl(trim(roamingtype),''0''), payflag from'||
         ' cdr_cdr300_'||to_char(to_date(szDate,'yyyymm'),'mm')||' a,info_user b ,code_local_net c where a.servid=b.user_id'||
         ' and b.local_net=c.local_net and a.call_type in(18,19)  and  a.roamingtype in(5,4,0)  and a.payflag=0 and not b.tele_type=''CTC'' ';
  dbms_output.put_line(v_sql);
  open v_sms for v_sql;
  loop
    fetch v_sms into v_tarrif,v_total_flow,v_area_code,v_longdist,v_roaming,v_payflag;

    exit when v_sms%notfound;
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
      select busi_code,free_flag,busi_type into v_busi_code,v_free_flag,v_busi_type  from rule_acct_item_idx where acct_item_code=v_tem;
      if v_free_flag<>1 then
          update pys_bil_cdr set bil_value=bil_value+1
                         where  busi_code=v_busi_code and
                                free_flag=v_free_flag and
                                busi_type=v_busi_type and
                                longdistancetype=v_longdist and
                                roamingtype=v_roaming and
                                area_code=v_area_code and
                                sale_flag=v_sale_flag and
                                insert_date=szDate;
           if sql%rowcount <=0 then
              insert into pys_bil_cdr (busi_type,busi_code,free_flag,longdistancetype,roamingtype,area_code,bil_value,sale_flag,insert_date)
              values(v_busi_type,v_busi_code,v_free_flag,v_longdist,v_roaming,v_area_code,1,v_sale_flag,szDate);
           end if;
           exit;
      end if;
      update pys_bil_cdr set bil_value=bil_value+1
                     where  busi_code=v_busi_code and
                            free_flag=v_free_flag and
                            busi_type=v_busi_type and
                            longdistancetype=v_longdist and
                            roamingtype=v_roaming and
                            area_code=v_area_code and
                            sale_flag=v_sale_flag and
                            insert_date=szDate;
       if sql%rowcount <=0 then
          insert into pys_bil_cdr (busi_type,busi_code,free_flag,longdistancetype,roamingtype,area_code,bil_value,sale_flag,insert_date)
          values(v_busi_type,v_busi_code,v_free_flag,v_longdist,v_roaming,v_area_code,1,v_sale_flag,szDate);
       end if;
       v_total_flow:=v_total_flow-1;
       if v_total_flow<0 then
          v_total_flow:=0;
       end if;
       exit when  v_semipos=0;
      exception
        when NO_DATA_FOUND THEN
             null;
      end;
    end loop;
    end if;
  end loop;
  close v_sms;
  commit;
end pysBilSms;
/
