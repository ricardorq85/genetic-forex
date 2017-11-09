set ORACLE_SID=FOREX
C:\app\Angela\product\11.2.0\dbhome_1\BIN\exp.exe forex/forex file=EXPDAT_PROCESO.dmp tables=PROCESO QUERY=\"WHERE FECHA_PROCESO>=TO_DATE('2015/02/07 15:50', 'YYYY/MM/DD HH24:MI')\" FEEDBACK=20000
pause
