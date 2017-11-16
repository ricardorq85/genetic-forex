SELECT IND.ID, IND.TIPO_OPERACION, T.FECHA_BASE, T.PRECIO_BASE,
	T.FECHA_TENDENCIA, T.PIPS, T.PIPS_ACTUALES, T.PRECIO_CALCULADO,
T.*, TO_CHAR(T.FECHA, 'YYYY/MM/DD HH24:MI:SS')
FROM TENDENCIA T
	INNER JOIN INDIVIDUO IND ON IND.ID=T.ID_INDIVIDUO
WHERE T.TIPO_TENDENCIA='BUY_SELL_20170204-2'
--AND T.FECHA_TENDENCIA=TO_DATE('2017.01.17 02:15', 'YYYY/MM/DD HH24:MI')
AND T.FECHA_BASE>=TO_DATE('2017/04/03 01:31', 'YYYY/MM/DD HH24:MI')
ORDER BY T.FECHA DESC, T.FECHA_BASE DESC, T.FECHA_TENDENCIA asc, T.TIPO_TENDENCIA DESC
--ORDER BY T.FECHA_TENDENCIA DESC
--ORDER BY FECHA DESC
--ORDER BY PIPS ASC
--ORDER BY FECHA_APERTURA ASC
;

SELECT * FROM TENDENCIA 
WHERE FECHA_BASE>TO_DATE('2017/04/03 01:31', 'YYYY/MM/DD HH24:MI')
ORDER BY FECHA_BASE ASC
;

SELECT TO_CHAR(TEN.FECHA_BASE, 'YYYYMMDD'), COUNT(*),
	MIN(TEN.FECHA_BASE) MINFEBASE, MAX(TEN.FECHA_BASE) MAXFEBASE, MAX(TEN.FECHA) MAXFE,	SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA_BASE, 'YYYYMMDD')
ORDER BY TO_CHAR(TEN.FECHA_BASE, 'YYYYMMDD') DESC;

SELECT TO_CHAR(TEN.FECHA_BASE, 'YYYY'), COUNT(*), 
	MIN(TEN.FECHA_BASE) MINFEBASE, MAX(TEN.FECHA_BASE) MAXFEBASE, SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA_BASE, 'YYYY')
ORDER BY TO_CHAR(TEN.FECHA_BASE, 'YYYY') DESC;

SELECT TO_CHAR(TEN.FECHA_BASE, 'YYYYMM'), COUNT(*), 
	MIN(TEN.FECHA_BASE) MINFEBASE, MAX(TEN.FECHA_BASE) MAXFEBASE, SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA_BASE, 'YYYYMM')
ORDER BY TO_CHAR(TEN.FECHA_BASE, 'YYYYMM') DESC;

SELECT TO_CHAR(TEN.FECHA_BASE, 'YYYYMMDD HH24'), COUNT(*), SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
WHERE TEN.FECHA_BASE<=TO_DATE('2016/12/21 23:59', 'YYYY/MM/DD HH24:MI')
GROUP BY TO_CHAR(TEN.FECHA_BASE, 'YYYYMMDD HH24')
ORDER BY TO_CHAR(TEN.FECHA_BASE, 'YYYYMMDD HH24') DESC;

SELECT --COUNT(*)
TEN.FECHA, TEN.FECHA_BASE, TEN.PRECIO_BASE, TEN.FECHA_TENDENCIA, TEN.PRECIO_CALCULADO, TEN.PIPS, 
TEN.FECHA_APERTURA, TEN.FECHA_CIERRE, TEN.OPEN_PRICE, TEN.PIPS_ACTUALES, TEN.PIPS_REALES,
TEN.ID_INDIVIDUO, TEN.*
FROM TENDENCIA TEN
WHERE TEN.TIPO_TENDENCIA='BUY_SELL_20170204-2'
AND TRUNC(TEN.FECHA_BASE,'HH24')=TO_DATE('2016/12/15 03:00', 'YYYY/MM/DD HH24:MI') 
AND TEN.FECHA_TENDENCIA>TO_DATE('2016/12/15 21:59', 'YYYY/MM/DD HH24:MI')
--AND TEN.FECHA_TENDENCIA<=TO_DATE('2017/02/03 11:53', 'YYYY/MM/DD HH24:MI')+7/24
--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<(24*0.25/24)*24*60
--AND TEN.PRECIO_CALCULADO<TEN.PRECIO_BASE
ORDER BY TEN.FECHA_TENDENCIA ASC
--ORDER BY TEN.PRECIO_CALCULADO ASC, TEN.FECHA DESC
;

-- INDIVIDUOS POR HORA
SELECT TO_CHAR(TEN.FECHA, 'YYYY/MM/DD HH24') FECHA, COUNT(*), 
	ROUND((MAX(TEN.FECHA)-MIN(TEN.FECHA))*24*60) MINUTOS,
  ROUND((COUNT(*))/((MAX(TEN.FECHA)-MIN(TEN.FECHA))*24*60+1),2) IND_X_MINUTO,
  ROUND((COUNT(*))/((MAX(TEN.FECHA)-MIN(TEN.FECHA))*24*60+1),2)*60 IND_X_HORA,
  ROUND((COUNT(*))/((MAX(TEN.FECHA)-MIN(TEN.FECHA))*24*60+1),2)*60*24 IND_X_DIA,
  ROUND(((MAX(TEN.FECHA)-MIN(TEN.FECHA))*24*60)/(COUNT(*)),2) MINUTO_X_IND,
	MIN(TEN.FECHA) MINFE, MAX(TEN.FECHA) MAXFE
FROM TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA, 'YYYY/MM/DD HH24') 
ORDER BY TO_CHAR(TEN.FECHA, 'YYYY/MM/DD HH24') DESC
;

SELECT * FROM TENDENCIA T1
	INNER JOIN TENDENCIA T2 ON T1.ID_INDIVIDUO=T2.ID_INDIVIDUO
		AND T1.FECHA_BASE=T2.FECHA_BASE
		AND T1.TIPO_TENDENCIA<>T2.TIPO_TENDENCIA
ORDER BY T1.ID_INDIVIDUO, T1.FECHA_BASE DESC
		;

SELECT * FROM TENDENCIA T1
WHERE TIPO_TENDENCIA NOT LIKE 'BUY_SELL_20170125%';

SELECT * FROM TENDENCIA T
WHERE T.ID_INDIVIDUO='1492959519987.779'
AND T.FECHA_BASE>TO_DATE('2016/03/11 15:00','YYYY/MM/DD HH24:MI')
ORDER BY FECHA_BASE ASC
--ORDER BY FECHA_BASE DESC
;

SELECT T.FECHA_BASE, T.FECHA_TENDENCIA, MIN(T.PRECIO_BASE), COUNT(*),
	ROUND(AVG(T.PRECIO_CALCULADO),5), MIN(T.PRECIO_CALCULADO), MAX(T.PRECIO_CALCULADO)
FROM TENDENCIA T
WHERE T.FECHA_BASE=TO_DATE('2016/11/18 23:59','YYYY/MM/DD HH24:MI')
GROUP BY T.FECHA_BASE, T.FECHA_TENDENCIA
ORDER BY T.FECHA_TENDENCIA ASC
--ORDER BY COUNT(*) DESC
;

SELECT T.FECHA_BASE, MAX(T.FECHA_TENDENCIA), MIN(T.PRECIO_BASE), 
	ROUND(AVG(T.PRECIO_CALCULADO),5), MIN(T.PRECIO_CALCULADO), MAX(T.PRECIO_CALCULADO),
	COUNT(*)
FROM TENDENCIA T
WHERE T.FECHA_BASE=TO_DATE('2016/11/18 23:59','YYYY/MM/DD HH24:MI')
GROUP BY T.FECHA_BASE
--ORDER BY COUNT(*) DESC
;

SELECT COUNT(*) FROM FOREX.TENDENCIA;

SELECT * FROM TENDENCIA
WHERE ID_INDIVIDUO ='1454791775805.1667'
--AND FECHA_BASE=TO_DATE('2012/01/16 15:00', 'YYYY/MM/DD HH24:MI')
ORDER BY FECHA_BASE DESC
;

SELECT TO_CHAR(TEN.FECHA_APERTURA, 'YYYY'), COUNT(*), SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA_APERTURA, 'YYYY')
ORDER BY TO_CHAR(TEN.FECHA_APERTURA, 'YYYY') DESC;

SELECT TO_CHAR(TEN.FECHA_APERTURA, 'YYYYMM'), COUNT(*), SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA_APERTURA, 'YYYYMM')
ORDER BY TO_CHAR(TEN.FECHA_APERTURA, 'YYYYMM') DESC;

SELECT TO_CHAR(TEN.FECHA_APERTURA, 'YYYYMMDD'), COUNT(*), SUM(TEN.PIPS) PIPS
FROM FOREX.TENDENCIA TEN
GROUP BY TO_CHAR(TEN.FECHA_APERTURA, 'YYYYMMDD')
ORDER BY TO_CHAR(TEN.FECHA_APERTURA, 'YYYYMMDD') DESC;

SELECT FECHA_BASE, COUNT(*), AVG(PRECIO_BASE) FROM TENDENCIA 
WHERE FECHA_BASE=TO_DATE('2010/02/16 10:03','YYYY/MM/DD HH24:MI')
GROUP BY FECHA_BASE
ORDER BY FECHA_BASE ASC;

SELECT FECHA_BASE, TO_CHAR(FECHA_TENDENCIA, 'YYYYMMDD') FECHA_TENDENCIA,
  COUNT(*), AVG(PRECIO_BASE) 
FROM TENDENCIA 
WHERE FECHA_BASE>=TO_DATE('2012/05/01 16:46','YYYY/MM/DD HH24:MI')
GROUP BY FECHA_BASE, TO_CHAR(FECHA_TENDENCIA, 'YYYYMMDD')
ORDER BY FECHA_BASE ASC
;

SELECT TO_CHAR(FECHA_BASE, 'YYYYMMDD HH24'), COUNT(*)
FROM TENDENCIA
WHERE FECHA_BASE>=TO_DATE('2010/01/07 01:59','YYYY/MM/DD HH24:MI')
GROUP BY TO_CHAR(FECHA_BASE, 'YYYYMMDD HH24')
ORDER BY TO_CHAR(FECHA_BASE, 'YYYYMMDD HH24') ASC
;


SELECT * --TO_CHAR(FECHA_BASE, 'YYYY/MM/DD HH24'), -- ID_INDIVIDUO, 
  --ROUND(AVG(PROBABILIDAD),2), COUNT(*) 
FROM TENDENCIA TEN 
WHERE 
 --ID_INDIVIDUO='1327893721830.417'
 FECHA_BASE=TO_DATE('2012/10/19 16:11', 'YYYY/MM/DD HH24:MI')
--AND FECHA_TENDENCIA>=TO_DATE('2012/10/25 07:10', 'YYYY/MM/DD HH24:MI')
--AND PIPS>0
--AND PIPS_ACTUALES>0
--GROUP BY ID_INDIVIDUO
--GROUP BY TO_CHAR(FECHA_BASE, 'YYYY/MM/DD HH24')
--ORDER BY COUNT(*) DESC
ORDER BY FECHA_TENDENCIA ASC
;

SELECT FECHA_BASE, 
COUNT(*) FROM TENDENCIA 
WHERE TIPO_TENDENCIA='ESTADISTICAS_MAXMIN_23'
GROUP BY FECHA_BASE
ORDER BY FECHA_BASE DESC;

SELECT --TIPO_CALCULO, --FECHA_APERTURA,
  TEN.FECHA_TENDENCIA FETEN, 
  --COUNT(*), SUM(PIPS), ROUND(AVG(PROBABILIDAD),4) AVG_PROB 
FROM TENDENCIA TEN
WHERE FECHA_BASE=TO_DATE(
'2012/04/03 16:50'
,'YYYY/MM/DD HH24:MI')
AND TIPO_TENDENCIA='ESTADISTICAS_MAXMIN_23'
--GROUP BY TIPO_CALCULO --TEN.FECHA_APERTURA, 
--,TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY/MM/DD HH24')
ORDER BY 
PRECIO_CALCULADO DESC
--TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY/MM/DD HH24') ASC, 
--TIPO_CALCULO DESC
;

SELECT FECHA_BASE, FECHA_APERTURA, FECHA_TENDENCIA,
  PRECIO_CALCULADO, PIPS, DURACION, PIPS_ACTUALES, DURACION_ACTUAL,
  PROBABILIDAD_POSITIVOS PROBPOS, PROBABILIDAD_NEGATIVOS PROBNEG,
  ID_INDIVIDUO, FECHA_CIERRE
  --, TEN.* 
  FROM TENDENCIA TEN WHERE 
--TEN.ID_INDIVIDUO IN ('1323318036979.26670') AND
FECHA_BASE=TO_DATE('2012/06/29 08:05', 'YYYY/MM/DD HH24:MI')
--AND FECHA_TENDENCIA>=TO_DATE('2012/04/04 07:13', 'YYYY/MM/DD HH24:MI')
--AND FECHA_TENDENCIA<TO_DATE('2012/04/03 00:01', 'YYYY/MM/DD HH24:MI')
--AND FECHA_APERTURA=TO_DATE('2012/06/04 01:52', 'YYYY/MM/DD HH24:MI')
--AND PIPS_ACTUALES>0
--AND TEN.TIPO_CALCULO=1 
--AND TEN.PROBABILIDAD>0.4
ORDER BY 
--TEN.FECHA_BASE ASC
--TEN.PIPS_ACTUALES DESC
--TEN.PROBABILIDAD DESC,
TEN.FECHA_TENDENCIA ASC 
--TEN.FECHA_APERTURA ASC,
--PRECIO_CALCULADO DESC
--ABS(PIPS) DESC
--OPEN_PRICE ASC
;

SELECT COUNT(*) --T.FECHA_BASE, T.PRECIO_BASE, T.ID_INDIVIDUO, T.FECHA_TENDENCIA, T.PIPS, T.PRECIO_CALCULADO, T.TIPO_TENDENCIA, 
    --T.FECHA_APERTURA, T.OPEN_PRICE, T.DURACION, T.PIPS_ACTUALES, T.DURACION_ACTUAL, T.PROBABILIDAD_POSITIVOS, 
    --T.PROBABILIDAD_NEGATIVOS, T.PROBABILIDAD, T.FECHA, T.FECHA_CIERRE, T.TIPO_CALCULO, T.PIPS_REALES
  FROM INTEGRATE.TENDENCIA T
   --WHERE --T.ID_INDIVIDUO='1322673642223.779003'
          --AND T.FECHA_BASE=TO_DATE('2012/01/16 15:00', 'YYYY/MM/DD HH24:MI')
        --ROWNUM<2
;


SELECT TEN1.FECHA_BASE, TEN1.PRECIO_BASE, TEN1.FECHA_TENDENCIA, TEN1.PRECIO_CALCULADO,	TEN1.PIPS, TEN1.PIPS_ACTUALES, TEN1.PROBABILIDAD_POSITIVOS,
	TEN1.*
FROM TENDENCIA TEN1 
WHERE TEN1.TIPO_TENDENCIA='BUY_SELL_20170204-2'
	AND TRUNC(TEN1.FECHA_TENDENCIA,'HH24')=TRUNC(TO_DATE('2017/02/02 00:59', 'YYYY/MM/DD HH24:MI'),'HH24')
	AND TEN1.FECHA_BASE<=TO_DATE('2017/02/01 23:59', 'YYYY/MM/DD HH24:MI')
	AND TEN1.FECHA_CIERRE>TO_DATE('2017/02/01 23:59', 'YYYY/MM/DD HH24:MI')
ORDER BY TRUNC(TEN1.FECHA_TENDENCIA,'HH24') ASC, TEN1.PRECIO_CALCULADO DESC
;

SELECT (TEN1.PRECIO_CALCULADO-TEN2.PRECIO_CALCULADO)*10000 DIFF, TEN1.FECHA_TENDENCIA, TEN2.FECHA_TENDENCIA, TEN1.PRECIO_CALCULADO, TEN2.PRECIO_CALCULADO,
	TEN1.PIPS, TEN2.PIPS, TEN1.PIPS_ACTUALES, TEN2.PIPS_ACTUALES,
	TEN1.PROBABILIDAD_POSITIVOS, TEN2.PROBABILIDAD_POSITIVOS,
	TEN1.*, TEN2.*
FROM TENDENCIA TEN1 INNER JOIN TENDENCIA TEN2
	ON TEN1.ID_INDIVIDUO=TEN2.ID_INDIVIDUO AND TEN1.TIPO_TENDENCIA=TEN2.TIPO_TENDENCIA
WHERE TEN1.TIPO_TENDENCIA='BUY_SELL_20170204-2'
	AND TRUNC(TEN1.FECHA_TENDENCIA,'HH24')=TRUNC(TO_DATE('2017/02/02 07:59', 'YYYY/MM/DD HH24:MI'),'HH24')
	AND TEN1.FECHA_BASE=TO_DATE('2017/02/01 23:59', 'YYYY/MM/DD HH24:MI')
	AND TEN2.FECHA_BASE=TO_DATE('2017/02/01 22:59', 'YYYY/MM/DD HH24:MI')	
	--AND ABS(TEN1.PRECIO_CALCULADO-TEN2.PRECIO_CALCULADO)*10000>10
--ORDER BY ABS(TEN1.PRECIO_CALCULADO-TEN2.PRECIO_CALCULADO) DESC
ORDER BY TEN1.FECHA_TENDENCIA ASC
;

SELECT TRUNC(TEN.FECHA_BASE), COUNT(*) FROM TENDENCIA TEN 
	 WHERE TEN.FECHA_BASE 
		BETWEEN to_date('2017/03/29 22:10', 'YYYY/MM/DD HH24:MI') AND to_date('2017/07/29 22:10', 'YYYY/MM/DD HH24:MI')
GROUP BY TRUNC(TEN.FECHA_BASE) 
ORDER BY COUNT(*) ASC;