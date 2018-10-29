SET ORACLE_SID='usdcad'
REM CD C:\APP\RICARDORQ85\PRODUCT\11.2.0\dbhome_3\BIN
CD C:\app\rrq\product\11.2.0\dbhome_1\BIN
IMP FOREX/forex@localhost:1521/usdcad FILE=C:\Users\Angela\Documents\ricardorq85\USDCAD_EXPDAT_20181017.dmp full=yes
rem IMP FOREX/forex@localhost:1521/orcl.forex FILE=d:\ricardorq85\Informacion\FOREX\Backups\DMP\USDCAD_EXPDAT_20181015.dmp tables=DATOHISTORICO
PAUSE