SET VERIFY OFF
connect "SYS"/"&&sysPassword" as SYSDBA
set echo on
spool D:\app\USER\admin\FOREX2\scripts\postDBCreation.log append
@D:\app\USER\product\11.2.0\dbhome_1\rdbms\admin\catbundle.sql psu apply;
select 'utl_recomp_begin: ' || to_char(sysdate, 'HH:MI:SS') from dual;
execute utl_recomp.recomp_serial();
select 'utl_recomp_end: ' || to_char(sysdate, 'HH:MI:SS') from dual;
execute dbms_swrf_internal.cleanup_database(cleanup_local => FALSE);
commit;
connect "SYS"/"&&sysPassword" as SYSDBA
set echo on
create spfile='D:\app\USER\product\11.2.0\dbhome_1\database\spfileFOREX2.ora' FROM pfile='D:\app\USER\admin\FOREX2\scripts\init.ora';
shutdown immediate;
connect "SYS"/"&&sysPassword" as SYSDBA
startup ;
host D:\app\USER\product\11.2.0\dbhome_1\bin\emca.bat -config dbcontrol db -silent -DB_UNIQUE_NAME FOREX2 -PORT 1521 -EM_HOME D:\app\USER\product\11.2.0\dbhome_1 -LISTENER LISTENER -SERVICE_NAME FOREX2 -SID FOREX2 -ORACLE_HOME D:\app\USER\product\11.2.0\dbhome_1 -HOST localhost -LISTENER_OH D:\app\USER\product\11.2.0\dbhome_1 -LOG_FILE D:\app\USER\admin\FOREX2\scripts\emConfig.log;
spool off
