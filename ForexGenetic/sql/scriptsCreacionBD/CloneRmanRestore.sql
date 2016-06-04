SET VERIFY OFF
connect "SYS"/"&&sysPassword" as SYSDBA
set echo on
spool D:\app\USER\admin\FOREX2\scripts\CloneRmanRestore.log append
startup nomount pfile="D:\app\USER\admin\FOREX2\scripts\init.ora";
@D:\app\USER\admin\FOREX2\scripts\rmanRestoreDatafiles.sql;
spool off
