OLD_UMASK=`umask`
umask 0027
mkdir D:\app\USER\admin\FOREX2\adump
mkdir D:\app\USER\admin\FOREX2\dpdump
mkdir D:\app\USER\admin\FOREX2\pfile
mkdir D:\app\USER\cfgtoollogs\dbca\FOREX2
mkdir D:\app\USER\fast_recovery_area
mkdir D:\app\USER\fast_recovery_area\FOREX2
mkdir D:\app\USER\oradata\FOREX2
mkdir D:\app\USER\product\11.2.0\dbhome_1\database
umask ${OLD_UMASK}
set ORACLE_SID=FOREX2
set PATH=%ORACLE_HOME%\bin;%PATH%
D:\app\USER\product\11.2.0\dbhome_1\bin\oradim.exe -new -sid FOREX2 -startmode manual -spfile 
D:\app\USER\product\11.2.0\dbhome_1\bin\oradim.exe -edit -sid FOREX2 -startmode auto -srvcstart system 
D:\app\USER\product\11.2.0\dbhome_1\bin\sqlplus /nolog @D:\app\USER\admin\FOREX2\scripts\FOREX2.sql
