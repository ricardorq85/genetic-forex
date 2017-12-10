select * from dba_hist_sqltext
--where sql_text like '%MES%OPERACION_X_SEMANA%'
;

select v.SQL_TEXT,v.PARSING_SCHEMA_NAME,v.FIRST_LOAD_TIME,v.DISK_READS,v.ROWS_PROCESSED,v.ELAPSED_TIME,v.service
           --*
      from v$sql v      
where --to_date(v.FIRST_LOAD_TIME,'YYYY-MM-DD hh24:mi:ss')>sysdate-3 --ADD_MONTHS(trunc(sysdate,'MM'),-2)
--AND 
PARSING_SCHEMA_NAME='FOREX'
ORDER BY to_date(v.FIRST_LOAD_TIME,'YYYY-MM-DD hh24:mi:ss') ASC
;
