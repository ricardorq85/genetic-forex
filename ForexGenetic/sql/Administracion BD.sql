SELECT
  q.SQL_FULLTEXT sql_text
FROM
  gv$session s,gv$sql q
WHERE
  s.sql_address    = q.address AND
  s.sql_hash_value = q.hash_value AND
  s.sid            = 21;
  
SELECT * FROM all_tables WHERE owner = 'FOREX' ORDER BY table_name ASC ;
SELECT * FROM all_tab_COLUMNS WHERE owner = 'FOREX' AND COLUMN_NAME='SYMBOL' ;
SELECT * FROM dba_tablespaces WHERE TABLESPACE_NAME='TEMP';
SELECT B.* FROM dba_data_files b ;
SELECT * FROM dba_indexes a WHERE a.index_name='OPERACION_INDIV_FECHA_IDX';
SELECT
  B.* --a.owner, a.index_name, b.file_name
FROM
  dba_indexes a, dba_data_files b
WHERE
  a.tablespace_name=b.tablespace_name AND
  a.index_name     ='OPERACION_INDIV_FECHA_IDX';
--ALTER TABLESPACE USERS ADD DATAFILE 'C:\APP\USER\ORADATA\FOREX3\USERS02.DBF' SIZE 32M;
SELECT
  *
FROM
  ALL_CONSTRAINTS
WHERE
  TABLE_NAME = 'OPERACION_ESTRATEGIA_PERIODO';
SELECT * FROM ALL_OBJECTS WHERE OBJECT_NAME = 'INDICADORES_COLUMNAS';
SELECT
  *
FROM
  all_constraints fk, all_constraints pk
WHERE
  fk.CONSTRAINT_TYPE   = 'R' AND
  pk.owner             = 'FOREX' AND
  fk.r_owner           = pk.owner AND
  fk.R_CONSTRAINT_NAME = pk.CONSTRAINT_NAME AND
  pk.TABLE_NAME        = 'INDIVIDUO';
