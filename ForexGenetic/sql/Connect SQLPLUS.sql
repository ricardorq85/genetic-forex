--D:\app\USER\product\11.2.0\eurjpy_home\BIN\sqlplus FOREX/forex@localhost:1521/FOREX.EURJPY

--C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3
--d:\ricardorq85\app\product\11.2.0\dbhome_5\\BIN\sqlplus SYS/ForexAdmin2018@localhost:1521/orcl.forex
d:\ricardorq85\app\product\11.2.0\dbhome_5\\BIN\sqlplus FOREX/forex@localhost:1521/orcl.usdcad

c:\app\rrq\product\11.2.0\dbhome_1\BIN\sqlplus FOREX/forex@localhost:1521/usdcad

c:\app\rrq\product\11.2.0\dbhome_1\BIN\sqlplus sys as SYSDBA/ForexAdmin2018@localhost:1521/usdcad

--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\CreateUserFOREX.sql"

--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\new INSERT INTO TMP_TOFILESTRING.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Regresiones INSERT INTO TMP_TOFILESTRING.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\INSERT INTO TMP_TOFILESTRING 3.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\insert into OPERACION_X_SEMANA POR MESES.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\insert into OPERACIONES_ACUM_SEMANA_X_ POR MESES.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Export Multiples Estrategia Operacion.sql" 
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\new DELETE AND INSERT INTO OPERACION CONSOLIDADAS.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\OPERACION_POSITIVAS_NEGATIVAS.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\ToFileString.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Borrar datos historicosX.sql"
--@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Insertar Acumulados Semana.sql"

--Export tendencias
--C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3 @"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\ExportTendencias.sql"
C:\app\Angela\product\11.2.0\dbhome_1\BIN\sqlplus FOREX/forex@localhost:1521/orclforex @"C:\Users\Angela\Documents\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\ExportTendencias.sql"

--Consulta Individuos Para Operar
C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3 @"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Consulta Individuos Para Operar.sql"
--C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3 @"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\temp1.sql"
--C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3 @"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\new INSERT INTO TMP_TOFILESTRING2.sql"

--Delete ESTRATEGIA_OPERACION_PERIODO pips negativos
C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3 @"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Bloque Anonimo Delete ESTRATEGIA_OPERACION_PERIODO pips negativos.sql"

--Conexion
C:\app\USER\product\11.2.0\dbhome_2\BIN\sqlplus FOREX/forex@localhost:1522/FOREX3
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2018/08/07 20:59' WHERE NOMBRE='FECHA_INICIO_TENDENCIA';
