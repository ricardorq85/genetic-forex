SET ORACLE_SID='FOREX'
CD /D D:\app\USER\product\11.2.0\eurjpy_home\BIN
IMP FOREX/forex@FOREX FILE=d:\ricardorq85\Informacion\FOREX\DATABASE\DMP\EXPDAT_20160416.DMP full=y rows=n
PAUSE