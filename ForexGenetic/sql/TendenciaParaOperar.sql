SELECT TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') FEBASE, COUNT(*),
	MIN(TPO.FECHA_BASE) MINFEBASE, MAX(TPO.FECHA_BASE) MAXFEBASE,
	MAX(TPO.FECHA) MAXFECHA
FROM TENDENCIA_PARA_OPERAR TPO
--WHERE TPO.FECHA_BASE>=TO_DATE('20171101', 'YYYYMMDD')
--WHERE MAX_EXTREMO_SINFILTRAR IS NULL
GROUP BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD')
ORDER BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') desc
;

SELECT TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') FEBASE, COUNT(*), 
	MIN(TPO.FECHA_BASE) MINFEBASE, MAX(TPO.FECHA_BASE) MAXFEBASE,
	MAX(TPO.FECHA) MAXFECHA
FROM DATO_ADICIONAL_TPO TPO
--WHERE TPO.FECHA_BASE>=TO_DATE('20171101', 'YYYYMMDD')
--WHERE MAX_EXTREMO_SINFILTRAR IS NULL
GROUP BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD')
ORDER BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') desc
;

SELECT TO_CHAR(TPO.FECHA_BASE, 'YYYYMM') FEBASE, COUNT(*), 
	MIN(TPO.FECHA_BASE) MINFEBASE, MAX(TPO.FECHA_BASE) MAXFEBASE,
	MAX(TPO.FECHA) MAXFECHA
FROM DATO_ADICIONAL_TPO TPO
--WHERE TPO.FECHA_BASE>=TO_DATE('20171101', 'YYYYMMDD')
--WHERE MAX_EXTREMO_SINFILTRAR IS NULL
GROUP BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMM')
ORDER BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMM') desc --COUNT(*) deSC
;

SELECT TPO.TIPO_TENDENCIA, TPO.CANTIDAD, TPO.CANTIDAD_FILTRADA, 
	TPO.PRECIO_CALCULADO, TPO.TAKE_PROFIT, TPO.LIMIT_APERTURA,
	ABS(TPO.PRECIO_CALCULADO-TPO.TAKE_PROFIT)*100000 DIFF, TPO.ACTIVA,
	TPO.*
FROM TENDENCIA_PARA_OPERAR TPO
WHERE PERIODO LIKE 'EXT%'
--WHERE --ACTIVA=1
AND TPO.FECHA_BASE>=TO_DATE('2018/02/02 00:06', 'YYYY/MM/DD HH24:MI')
--and tipo_operacion='SELL'
--ORDER BY FECHA_BASE aSC,TPO.ACTIVA ASC, TIPO_OPERACION ASC, PERIODO ASC
ORDER BY FECHA_BASE ASC
;

SELECT R2,R2_JAVA, PENDIENTE, PENDIENTE_JAVA, R2_FILTRADA, R2FILTRADA_JAVA, PENDIENTE, PENDIENTE_FILTRADA,
    TPO.*
FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.PENDIENTE_JAVA IS NOT NULL
AND (R2<>R2_JAVA
    OR PENDIENTE<>PENDIENTE_JAVA
    OR R2_FILTRADA<>R2FILTRADA_JAVA
    OR PENDIENTE_FILTRADA<>PENDIENTE_FILTRADA
    OR DESVIACION<>DESVIACION_JAVA
    OR DESVIACION_FILTRADA<>DESVIACIONFILTRADA_JAVA
);

SELECT TPO.TIPO_TENDENCIA, TPO.CANTIDAD, TPO.CANTIDAD_FILTRADA CANT_FILT, TPO.*
FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.LOTE>=0.1 AND LIMIT_APERTURA IS NOT NULL
--TPO.PERIODO='10.0D'
ORDER BY TPO.CANTIDAD DESC
;

SELECT TPO.TIPO_TENDENCIA, TPO.PRECIO_CALCULADO, TPO.LIMIT_APERTURA, TPO.TAKE_PROFIT, TPO.STOP_LOSS, TPO.STOP_APERTURA,
	TPO.*
FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('2018/12/27 09:12', 'YYYY/MM/DD HH24:MI') --AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
--AND ACTIVA=1
--AND TPO.LIMIT_APERTURA IS NULL
--AND PERIODO NOT LIKE '%FILTRAR' --AND TPO.ACTIVA=1 --AND TPO.FECHA>SYSDATE-0.1
ORDER BY --FECHA ASC,
FECHA_BASE aSC, TIPO_OPERACION ASC, ACTIVA DESC, PERIODO ASC;

SELECT FACTOR_DATOS, DATPO.* FROM DATO_ADICIONAL_TPO DATPO
	WHERE FECHA_BASE>=TO_DATE('2018/10/03 06:00', 'YYYY/MM/DD HH24:MI') --AND FECHA_BASE<=TO_DATE('2017/07/04 00:00', 'YYYY/MM/DD HH24:MI') --AND DIFF_PRECIO_EXTREMO_SUPERIOR IS NOT NULL
	--AND MAX_EXTREMO_SINFILTRAR IS NULL
--AND FACTOR_DATOS IS NOT NULL AND FACTOR_DATOS<>0
ORDER BY FECHA_BASE ASC --ORDER BY ABS(DIFF_PRECIO_EXTREMO_SUPERIOR) ASC
;
--193802
--0,05112

SELECT MAX(FACTOR_DATOS) FROM DATO_ADICIONAL_TPO DATPO
;

SELECT TPO.TIPO_TENDENCIA, TPO.PRECIO_CALCULADO, TPO.LIMIT_APERTURA, ROUND(TPO.PRECIO_CALCULADO-TPO.LIMIT_APERTURA,5), 
	TPO.*
FROM TENDENCIA_PARA_OPERAR TPO
WHERE LOTE=0.01 AND ACTIVA=0;

SELECT TPO.TIPO_TENDENCIA, TPO.* FROM TENDENCIA_PARA_OPERAR TPO
WHERE TO_DATE('2017/11/24 12:30', 'YYYY/MM/DD HH24:MI') BETWEEN TPO.VIGENCIA_LOWER AND TPO.VIGENCIA_HIGHER
AND TPO.TIPO_OPERACION = 'BUY' AND TPO.PRECIO_CALCULADO<1.2790
ORDER BY TPO.ACTIVA DESC
;

SELECT TPO.TIPO_TENDENCIA, DATPO.MIN_EXTREMO_FILTRADO, TPO.*, DATPO.* FROM DATO_ADICIONAL_TPO DATPO
INNER JOIN TENDENCIA_PARA_OPERAR TPO ON TPO.FECHA_BASE=DATPO.FECHA_BASE
WHERE TO_DATE('2018/01/05 13:30', 'YYYY/MM/DD HH24:MI') BETWEEN TPO.VIGENCIA_LOWER AND TPO.VIGENCIA_HIGHER
AND TPO.TIPO_OPERACION = 'BUY' AND DATPO.MIN_EXTREMO_FILTRADO<1.24700
ORDER BY DATPO.MIN_EXTREMO_FILTRADO ASC, TPO.ACTIVA DESC
;

SELECT TPO.FECHA_BASE, TPO.PERIODO, TPO.TIPO_OPERACION, TPO.PRECIO_CALCULADO, TPO.TAKE_PROFIT, TPO.STOP_LOSS, TPO.LIMIT_APERTURA,
	DA.CANTIDAD_TOTAL_TENDENCIAS,
	DA.MIN_EXTREMO_EXTREMO, DA.MAX_EXTREMO_EXTREMO, DA.MIN_EXTREMO_FILTRADO, DA.MAX_EXTREMO_FILTRADO,
	DA.MIN_EXTREMO_INTERMEDIO, DA.MAX_EXTREMO_INTERMEDIO, DA.MIN_EXTREMO_SINFILTRAR, DA.MAX_EXTREMO_SINFILTRAR
FROM TENDENCIA_PARA_OPERAR TPO INNER JOIN DATO_ADICIONAL_TPO DA ON TPO.FECHA_BASE=DA.FECHA_BASE
WHERE TPO.FECHA_BASE>=TO_DATE('2017/05/11 15:34', 'YYYY/MM/DD HH24:MI') 
--AND TPO.TIPO_OPERACION='BUY' 
AND TPO.PERIODO NOT LIKE 'EXT%'
ORDER BY TPO.FECHA_BASE ASC
--ORDER BY DA.CANTIDAD_TOTAL_TENDENCIAS DESC
;

SELECT * FROM DATO_ADICIONAL_TPO
WHERE CANTIDAD_TOTAL_TENDENCIAS IS NOT NULL
ORDER BY CANTIDAD_TOTAL_TENDENCIAS DESC;

SELECT ROUND(AVG(CANTIDAD_TOTAL_TENDENCIAS)) AVG_CANT, MAX(CANTIDAD_TOTAL_TENDENCIAS) MAX_CANT, MIN(CANTIDAD_TOTAL_TENDENCIAS) MIN_CANT
FROM DATO_ADICIONAL_TPO;

SELECT * FROM DATO_ADICIONAL_TPO
ORDER BY FECHA deSC --ORDER BY FECHA_BASE DESC
;

SELECT TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') FEBASE, COUNT(*), 
	MIN(TPO.FECHA_BASE) MINFEBASE, MAX(TPO.FECHA_BASE) MAXFEBASE,
	MAX(TPO.FECHA) MAXFECHA
FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('20180201', 'YYYYMMDD')
--WHERE LIMIT_APERTURA IS NULL
--WHERE STOP_APERTURA IS NOT NULL
GROUP BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD')
--ORDER BY MAX(TPO.FECHA) DESC
ORDER BY MAX(TPO.FECHA_BASE) DESC
--ORDER BY COUNT(*) ASC
;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE<=TO_DATE('2017/05/18 00:00', 'YYYY/MM/DD HH24:MI')
--AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
AND PERIODO NOT LIKE '%FILTRAR'--AND TPO.ACTIVA=1 
AND TPO.FECHA>SYSDATE-1
ORDER BY 
--FECHA ASC,
FECHA_BASE DESC, ACTIVA ASC, TIPO_OPERACION ASC,
PERIODO ASC;

SELECT * FROM DATO_ADICIONAL_TPO 
WHERE DIFF_PRECIO_EXTREMO_SUPERIOR IS NOT NULL AND DIFF_PRECIO_EXTREMO_INFERIOR IS NOT NULL
AND (DIFF_PRECIO_EXTREMO_SUPERIOR*DIFF_PRECIO_EXTREMO_INFERIOR)<0
ORDER BY ABS(DIFF_PRECIO_EXTREMO_SUPERIOR-DIFF_PRECIO_EXTREMO_INFERIOR) ASC,
	ABS(DIFF_PRECIO_EXTREMO_SUPERIOR) ASC,
	ABS(DIFF_PRECIO_EXTREMO_INFERIOR) ASC;

SELECT * FROM DATO_ADICIONAL_TPO 
--WHERE PENDIENTE_PROMEDIO<0 AND (NUM_PENDIENTES_NEGATIVAS-NUM_PENDIENTES_POSITIVAS)>5
WHERE --DIFF_PRECIO_EXTREMO_SUPERIOR<=0 AND PENDIENTE_PROMEDIO<0
DIFF_PRECIO_EXTREMO_INFERIOR<=0 AND PENDIENTE_PROMEDIO>0
ORDER BY FECHA_BASE DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.ID_EJECUCION in ('1510366015457')
ORDER BY FECHA_BASE DESC, PERIODO DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.VIGENCIA_HIGHER>TO_DATE('2017.10.17 00:00', 'YYYY/MM/DD HH24:MI')
AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
--AND PRECIO_CALCULADO BETWEEN 1.27160 AND 1.27350
ORDER BY FECHA_BASE ASC;


SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.PERIODO='EXTREMO'
ORDER BY FECHA_BASE ASC;

SELECT TRUNC(FECHA_BASE, 'HH24') FROM TENDENCIA_PARA_OPERAR TPO;

SELECT COUNT(*) FROM TENDENCIA_PARA_OPERAR ;

--UPDATE TENDENCIA_PARA_OPERAR SET ID_EJECUCION='1';

SELECT TPO.FECHA_BASE, 
	TPO.PRECIO_CALCULADO, TPO.TAKE_PROFIT, DH.HIGH VALOR_COMPRA,
	TPO.MIN_PRECIO, TPO.MAX_PRECIO,
	CASE TPO.TIPO_OPERACION
		WHEN 'SELL' THEN ROUND(((TPO.MAX_PRECIO-DH.HIGH)/(DH.HIGH-TPO.MIN_PRECIO)), 5)
		WHEN 'BUY' THEN	ROUND(((TPO.MAX_PRECIO-DH.HIGH)/(DH.HIGH-TPO.MIN_PRECIO)), 5)
	END RELACION, 
	TPO.TIPO_OPERACION
FROM TENDENCIA_PARA_OPERAR TPO
INNER JOIN DATOHISTORICO DH ON DH.FECHA=TPO.FECHA_BASE
WHERE TPO.PERIODO<>'EXTREMO'
--AND TPO.FECHA_BASE BETWEEN TO_DATE('2017/04/25 00:00', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2017/06/16 00:00', 'YYYY/MM/DD HH24:MI')
AND TPO.FECHA_BASE BETWEEN TO_DATE('2017.02.27 13:00', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2017.02.27 15:12', 'YYYY/MM/DD HH24:MI')
ORDER BY TPO.FECHA_BASE ASC;

SELECT TPO.FECHA_BASE, COUNT(*) FROM TENDENCIA_PARA_OPERAR TPO 
WHERE TPO.PERIODO='EXTREMO'
GROUP BY TPO.FECHA_BASE HAVING COUNT(*)=2
ORDER BY TPO.FECHA_BASE DESC
;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO  WHERE  TPO.ACTIVA=1 ORDER BY TPO.FECHA_BASE ASC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
	WHERE TPO.TIPO_OPERACION='SELL' 
	AND EXISTS (SELECT 1 FROM DATO_ADICIONAL_TPO DA
		WHERE DA.FECHA_BASE=TPO.FECHA_BASE
		AND DA.DIFF_MAX_PRIMERA_TENDENCIA<0 AND DA.DIFF_AVG_PRIMERA_TENDENCIA<0)
		;

SELECT LOTE, COUNT(*) FROM TENDENCIA_PARA_OPERAR TPO GROUP BY LOTE
ORDER BY LOTE ASC;

SELECT TOP.* FROM TENDENCIA_PARA_OPERAR TOP
 WHERE TIPO_EXPORTACION='ProcesarTendenciasGrupalManager' 
 AND TRUNC(FECHA_BASE,'HH24')=TRUNC(TO_DATE('2018/04/05 15:25', 'YYYY/MM/DD HH24:MI'),'HH24') 
 AND ID_EJECUCION<>'1534609182175'
 AND FECHA<TO_DATE('2018/08/16 14:38', 'YYYY/MM/DD HH24:MI')
-- AND FECHA_BASE BETWEEN TO_DATE('2018/03/05 13:32', 'YYYY/MM/DD HH24:MI')
	--AND TO_DATE('2018/04/05 13:32', 'YYYY/MM/DD HH24:MI')
ORDER BY FECHA_BASE ASC
 ;

	SELECT MAX(TPO2.CANTIDAD_FILTRADA)/(24*13+1)
	FROM TENDENCIA_PARA_OPERAR TPO2 WHERE TO_DATE('2018/10/01 00:06', 'YYYY/MM/DD HH24:MI')=TPO2.FECHA_BASE 
	AND TPO2.TIPO_TENDENCIA NOT LIKE 'EXTREMO%';
	
	SELECT (TPO2.CANTIDAD_FILTRADA)/(24*13+1), TPO2.CANTIDAD_FILTRADA, TPO2.TIPO_TENDENCIA, TPO2.*
	FROM TENDENCIA_PARA_OPERAR TPO2 WHERE TO_DATE('2018/10/05 15:44', 'YYYY/MM/DD HH24:MI')=TPO2.FECHA_BASE 
	AND TPO2.TIPO_TENDENCIA NOT LIKE 'EXTREMO%';
	
select count(*)
FROM TENDENCIA_PARA_OPERAR TPO;

SELECT * 
FROM TENDENCIA_PARA_OPERAR TPO;

select JSON_OBJECT('periodo' VALUE PERIODO) --, 'tipoOperacion' VALUE tpo.tipo_operacion)
FROM TENDENCIA_PARA_OPERAR;

select JSON_OBJECT('periodo' VALUE 'PERIODO') --, 'tipoOperacion' VALUE tpo.tipo_operacion)
FROM DUAL;