create or replace procedure pysBilData_sale(szDate in varchar2 ---六位具体到月份
                                        ) is
v_begin varchar2(8);
v_end   varchar2(8);
v_curr  varchar2(8);
begin
    v_begin:=szDate||'01';
    v_end:=to_char(last_day(to_date(szDate,'yyyymm')),'yyyymmdd');
    for v_curr in v_begin .. v_end loop
       --dbms_output.put_line(v_curr);
       pysBilData(v_curr);
    end loop;
end pysBilData_sale;
/
