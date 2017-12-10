SET VERIFY OFF
connect "SYS"/"&&sysPassword" as SYSDBA
set echo on
spool D:\app\USER\admin\FOREX2\scripts\cloneDBCreation.log append
Create controlfile reuse set database "FOREX2"
MAXINSTANCES 8
MAXLOGHISTORY 1
MAXLOGFILES 16
MAXLOGMEMBERS 3
MAXDATAFILES 100
Datafile 
'D:\app\USER\oradata\FOREX2\SYSTEM01.DBF',
'D:\app\USER\oradata\FOREX2\SYSAUX01.DBF',
'D:\app\USER\oradata\FOREX2\UNDOTBS01.DBF',
'D:\app\USER\oradata\FOREX2\USERS01.DBF'
LOGFILE GROUP 1 ('D:\app\USER\oradata\FOREX2\redo01.log') SIZE 51200K,
GROUP 2 ('D:\app\USER\oradata\FOREX2\redo02.log') SIZE 51200K,
GROUP 3 ('D:\app\USER\oradata\FOREX2\redo03.log') SIZE 51200K RESETLOGS;
exec dbms_backup_restore.zerodbid(0);
shutdown immediate;
startup nomount pfile="D:\app\USER\admin\FOREX2\scripts\initFOREX2Temp.ora";
Create controlfile reuse set database "FOREX2"
MAXINSTANCES 8
MAXLOGHISTORY 1
MAXLOGFILES 16
MAXLOGMEMBERS 3
MAXDATAFILES 100
Datafile 
'D:\app\USER\oradata\FOREX2\SYSTEM01.DBF',
'D:\app\USER\oradata\FOREX2\SYSAUX01.DBF',
'D:\app\USER\oradata\FOREX2\UNDOTBS01.DBF',
'D:\app\USER\oradata\FOREX2\USERS01.DBF'
LOGFILE GROUP 1 ('D:\app\USER\oradata\FOREX2\redo01.log') SIZE 51200K,
GROUP 2 ('D:\app\USER\oradata\FOREX2\redo02.log') SIZE 51200K,
GROUP 3 ('D:\app\USER\oradata\FOREX2\redo03.log') SIZE 51200K RESETLOGS;
alter system enable restricted session;
alter database "FOREX2" open resetlogs;
exec dbms_service.delete_service('seeddata');
exec dbms_service.delete_service('seeddataXDB');
alter database rename global_name to "FOREX2";
ALTER TABLESPACE TEMP ADD TEMPFILE 'D:\app\USER\oradata\FOREX2\TEMP01.DBF' SIZE 20480K REUSE AUTOEXTEND ON NEXT 640K MAXSIZE UNLIMITED;
select tablespace_name from dba_tablespaces where tablespace_name='USERS';
select sid, program, serial#, username from v$session;
alter database character set INTERNAL_CONVERT WE8MSWIN1252;
alter database national character set INTERNAL_CONVERT AL16UTF16;
alter user sys account unlock identified by "&&sysPassword";
alter user system account unlock identified by "&&systemPassword";
alter system disable restricted session;
