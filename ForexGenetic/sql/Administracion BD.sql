SELECT * FROM ALL 

select * from all_tables
where owner = 'FOREX'
order by table_name asc
;

select * from all_tab_COLUMNS
where owner = 'FOREX'
AND COLUMN_NAME='SYMBOL'
;

select *
from dba_tablespaces WHERE TABLESPACE_NAME='TEMP';

select B.*
from dba_data_files b ;

select *
from dba_indexes a
where a.index_name='OPERACION_INDIV_FECHA_IDX';

select B.* --a.owner, a.index_name, b.file_name 
from dba_indexes a, dba_data_files b 
where a.tablespace_name=b.tablespace_name and a.index_name='OPERACION_INDIV_FECHA_IDX';

--ALTER TABLESPACE USERS ADD DATAFILE 'C:\APP\USER\ORADATA\FOREX3\USERS02.DBF' SIZE 32M;
