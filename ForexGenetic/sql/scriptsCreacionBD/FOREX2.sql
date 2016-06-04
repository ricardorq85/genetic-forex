set verify off
ACCEPT sysPassword CHAR PROMPT 'Enter new password for SYS: ' HIDE
ACCEPT systemPassword CHAR PROMPT 'Enter new password for SYSTEM: ' HIDE
ACCEPT sysmanPassword CHAR PROMPT 'Enter new password for SYSMAN: ' HIDE
ACCEPT dbsnmpPassword CHAR PROMPT 'Enter new password for DBSNMP: ' HIDE
host D:\app\USER\product\11.2.0\dbhome_1\bin\orapwd.exe file=D:\app\USER\product\11.2.0\dbhome_1\database\PWDFOREX2.ora force=y
@D:\app\USER\admin\FOREX2\scripts\CloneRmanRestore.sql
@D:\app\USER\admin\FOREX2\scripts\cloneDBCreation.sql
@D:\app\USER\admin\FOREX2\scripts\postScripts.sql
@D:\app\USER\admin\FOREX2\scripts\lockAccount.sql
@D:\app\USER\admin\FOREX2\scripts\postDBCreation.sql
