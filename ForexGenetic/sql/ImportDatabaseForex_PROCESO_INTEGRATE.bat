SET ORACLE_SID='FOREX2'
CD /D D:\APP\USER\PRODUCT\11.2.0\dbhome_1\BIN
IMP INTEGRATE/integrate@FOREX2 FILE=d:\ricardorq85\Informacion\FOREX\DATABASE\DMP\EXPDAT_PROCESO_20150216_0301.dmp fromuser=FOREX touser=INTEGRATE constraints=N tables=PROCESO
PAUSE