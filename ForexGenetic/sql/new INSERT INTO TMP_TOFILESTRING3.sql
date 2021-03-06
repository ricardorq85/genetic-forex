SET ECHO ON;
SET feedback ON;
SPOOL ON;
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'REFRESHING FILTERED_EOP...' FROM DUAL;
/
BEGIN DBMS_SNAPSHOT.REFRESH( '"FOREX"."FILTERED_EOP"','F'); end;
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'REFRESHING FILTERED_PTFS...' FROM DUAL;
/
BEGIN DBMS_SNAPSHOT.REFRESH( '"FOREX"."FILTERED_PTFS"','F'); end;
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'TRUNCATING TMP_TOFILESTRING2...' FROM DUAL;
/
TRUNCATE TABLE TMP_TOFILESTRING2;
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'INSERTING INTO TMP_TOFILESTRING2...' FROM DUAL;
/

INSERT INTO TMP_TOFILESTRING2 (ID_INDIVIDUO, CRITERIO_ORDER1, CRITERIO_ORDER2, VIGENCIA1, VIGENCIA2, FECHA_ORDER1, FECHA_ORDER2)
  	SELECT PTFS.ID_INDIVIDUO, 
		MIN(PTFS.PIPS_SEMANA),
		ROUND((MIN(PTFS.PIPS_SEMANA)+MIN(PTFS.PIPS_MES)+MIN(PTFS.PIPS_ANYO)+MIN(PTFS.PIPS_TOTALES))/4),
		PTFS.FECHA_SEMANA, PTFS.FECHA_SEMANA+7, 
  MAX(PERI.FECHA), MAX(PERI.FECHA_FINAL)
  FROM FILTERED_PTFS PTFS 
  --PREVIO_TOFILESTRING PTFS
	INNER JOIN FILTERED_EOP PERI
	--INNER JOIN ESTRATEGIA_OPERACION_PERIODO PERI
		ON ( PTFS.TIPO_OPERACION=PERI.TIPO_OPERACION
			AND NVL(PTFS.PIPS_SEMANA,0)>PERI.FILTRO_PIPS_X_SEMANA 
			AND PTFS.PIPS_MES>PERI.FILTRO_PIPS_X_MES
			AND PTFS.PIPS_ANYO>PERI.FILTRO_PIPS_X_ANYO AND PTFS.PIPS_TOTALES>PERI.FILTRO_PIPS_TOTALES
			AND PTFS.FECHA_SEMANA BETWEEN PERI.FECHA_FINAL AND (PERI.FECHA_FINAL+7)
			AND PTFS.FECHA_SEMANA BETWEEN PERI.MAX_FECHA_CIERRE AND (PERI.MAX_FECHA_CIERRE+7)
			AND NVL(PTFS.R2_SEMANA,0)>PERI.FILTRO_R2_SEMANA AND PTFS.R2_MES>PERI.FILTRO_R2_MES 
			AND PTFS.R2_ANYO>PERI.FILTRO_R2_ANYO AND PTFS.R2_CONSOL>PERI.FILTRO_R2_TOTALES
			AND NVL(PTFS.PENDIENTE_SEMANA,0)>PERI.FILTRO_PENDIENTE_SEMANA AND PTFS.PENDIENTE_MES>PERI.FILTRO_PENDIENTE_MES 
			AND PTFS.PENDIENTE_ANYO>PERI.FILTRO_PENDIENTE_ANYO AND PTFS.PENDIENTE_CONSOL>PERI.FILTRO_PENDIENTE_TOTALES
			)
		--AND PERI.PIPS_TOTALES>1000 AND PERI.PIPS_TOTALES_PARALELAS>3000
	GROUP BY PTFS.ID_INDIVIDUO, PTFS.FECHA_SEMANA;
commit;
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'CALLING ToFileStringFromTEMP_TOFILESTRING...' FROM DUAL;
/
@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\ToFileStringFromTEMP_TOFILESTRING.sql";
/