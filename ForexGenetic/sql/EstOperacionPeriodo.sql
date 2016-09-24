SELECT EOP.TIPO_OPERACION, FECHA_INICIAL, FECHA_FINAL, MIN(ID), MAX(ID), COUNT(*),
  MIN(FECHA), MAX(FECHA), MIN(PIPS_TOTALES), MAX(PIPS_TOTALES)
FROM FOREX.ESTRATEGIA_OPERACION_PERIODO EOP
GROUP BY EOP.FECHA_INICIAL, EOP.FECHA_FINAL, EOP.TIPO_OPERACION
ORDER BY MAX(ID) DESC
;

SELECT EOP.ID, EOP.PIPS_TOTALES, EOP.PIPS_TOTALES_PARALELAS, EOP.CANTIDAD_PARALELAS, EOP.FECHA_INICIAL, EOP.FILTRO_PIPS_X_SEMANA, EOP.* 
FROM ESTRATEGIA_OPERACION_PERIODO EOP 
WHERE --EOP.FILTRO_PIPS_X_SEMANA=500 AND EOP.FILTRO_PIPS_X_MES=3000 AND EOP.FILTRO_PIPS_X_ANYO=11200 and 
EOP.FILTRO_PIPS_TOTALES<0
AND EOP.ID>=1627690
--ORDER BY EOP.ID DESC
ORDER BY EOP.PIPS_TOTALES DESC
;

SELECT --COUNT(*)
EOP.FILTRO_PIPS_X_SEMANA, EOP.* 
FROM ESTRATEGIA_OPERACION_PERIODO EOP 
WHERE --EOP.FILTRO_PIPS_X_SEMANA=1000 AND EOP.FILTRO_PIPS_X_MES=-1000 AND EOP.FILTRO_PIPS_X_ANYO=5200 and EOP.FILTRO_PIPS_TOTALES=3000 AND
	(EOP.PIPS_TOTALES>5000 AND EOP.ID BETWEEN 1627690 AND 11627690 
	  AND EOP.PIPS_TOTALES_PARALELAS>0 AND EOP.PIPS_TOTALES>0 
	  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
	  --AND (EOP.PIPS_AGRUPADO_MINUTOS>EOP.PIPS_AGRUPADO_HORAS AND EOP.PIPS_AGRUPADO_HORAS>EOP.PIPS_AGRUPADO_DIAS)
	  AND (EOP.CANTIDAD_PARALELAS/EOP.CANTIDAD)<10)
ORDER BY EOP.FECHA_FINAL, EOP.PIPS_TOTALES DESC
--ORDER BY EOP.PIPS_TOTALES DESC
;

--SELECT COUNT(*) FROM (
SELECT MIN(FECHA_FINAL), MAX(FECHA_FINAL), 
  FLOOR(FILTRO_PIPS_X_SEMANA/ 100)*100 R_SEMANA, FLOOR(FILTRO_PIPS_X_MES/ 100)*100 R_MES,
  FLOOR(FILTRO_PIPS_X_ANYO/ 100)*100 R_ANYO, FLOOR(FILTRO_PIPS_TOTALES/ 100)*100 R_TOTALES,
  MIN(EOP.PIPS_TOTALES) MINI_TOTALES, MIN(EOP.PIPS_TOTALES_PARALELAS) MINI_PARALELAS, 
  MAX(EOP.PIPS_TOTALES) MAXI_TOTALES, MAX(EOP.PIPS_TOTALES_PARALELAS) MAXI_PARALELAS,
  COUNT(*), SUM(EOP.PIPS_TOTALES) SUMA_TOTALES, SUM(EOP.PIPS_TOTALES_PARALELAS) SUMA_PARALELAS, 
  ROUND(FLOOR(AVG(EOP.PIPS_TOTALES)/1000))*1000 TOT, ROUND(FLOOR(AVG(EOP.PIPS_TOTALES_PARALELAS)/1000))*1000 TOTP,
  ROUND(AVG(EOP.PIPS_TOTALES)) PROM_TOTALES, ROUND(AVG(EOP.PIPS_TOTALES_PARALELAS)) PROM_PARALELAS,
  ROUND(AVG(EOP.PIPS_AGRUPADO_MINUTOS)) PROM_PAGMIN, ROUND(AVG(EOP.PIPS_AGRUPADO_HORAS)) PROM_PAGHORAS, 
  ROUND(AVG(EOP.PIPS_AGRUPADO_DIAS)) PROM_PAGDIAS,
  ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_DIAS)/100))*100 D, ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_HORAS)/100))*100 H,
  ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_MINUTOS)/100))*100 M,
  ROUND(AVG(EOP.CANTIDAD_PARALELAS)/AVG(EOP.CANTIDAD), 2) CANT, (MAX(FECHA_FINAL)-TO_DATE('2015','YYYY')) FE
FROM ESTRATEGIA_OPERACION_PERIODO EOP 
GROUP BY FLOOR(FILTRO_PIPS_X_SEMANA/ 100), FLOOR(FILTRO_PIPS_X_MES/ 100), 
  FLOOR(FILTRO_PIPS_X_ANYO/ 100), FLOOR(FILTRO_PIPS_TOTALES/ 100)
HAVING MAX(EOP.PIPS_TOTALES)>=5000
	  --AND AVG(EOP.PIPS_TOTALES_PARALELAS)>0 AND AVG(EOP.PIPS_TOTALES)>0 
	 -- AND (AVG(EOP.PIPS_AGRUPADO_MINUTOS)>0 AND AVG(EOP.PIPS_AGRUPADO_HORAS)>0 AND AVG(EOP.PIPS_AGRUPADO_DIAS)>0)
--	  AND (PERI.CANTIDAD_PARALELAS/PERI.CANTIDAD)<10)
--AND FLOOR(FILTRO_PIPS_X_SEMANA/ 100)*100=0
--AND FLOOR(FILTRO_PIPS_X_MES/ 100)*100=-1000
--AND FLOOR(FILTRO_PIPS_X_ANYO/ 100)*100=1600
--AND FLOOR(FILTRO_PIPS_TOTALES/ 100)*100=3000
ORDER BY 
ROUND(FLOOR(AVG(EOP.PIPS_TOTALES)/1000))*1000 DESC, ROUND(FLOOR(AVG(EOP.PIPS_TOTALES_PARALELAS)/1000))*1000 DESC,
ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_DIAS)/100))*100 DESC, ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_HORAS)/100))*100 DESC,
ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_MINUTOS)/100))*100 DESC
--MAX(EOP.PIPS_TOTALES) DESC
--)
;

SELECT PRE_TFS.ID_INDIVIDUO, PRE_TFS.FECHA_SEMANA
  --PERI.ID, PRE_TFS.ID_INDIVIDUO, PRE_TFS.FECHA_SEMANA,
  --PERI.FILTRO_PIPS_X_SEMANA FP_X_SEMANA, PERI.FILTRO_PIPS_X_MES, PERI.FILTRO_PIPS_X_ANYO, PERI.FILTRO_PIPS_TOTALES ,
  --PERI.PIPS_TOTALES, PERI.PIPS_TOTALES_PARALELAS
  FROM PREVIO_TOFILESTRING PRE_TFS
	INNER JOIN ESTRATEGIA_OPERACION_PERIODO PERI
		ON ( NVL(PRE_TFS.PIPS_SEMANA,0)>PERI.FILTRO_PIPS_X_SEMANA AND PRE_TFS.PIPS_MES>PERI.FILTRO_PIPS_X_MES 
			AND PRE_TFS.PIPS_ANYO>PERI.FILTRO_PIPS_X_ANYO AND PRE_TFS.PIPS_TOTALES>PERI.FILTRO_PIPS_TOTALES) 
			AND PRE_TFS.TIPO_OPERACION=PERI.TIPO_OPERACION
      AND PRE_TFS.FECHA_SEMANA BETWEEN PERI.FECHA_FINAL AND ADD_MONTHS(PERI.FECHA_FINAL,1)
      --AND PRE_TFS.FECHA_SEMANA BETWEEN ADD_MONTHS(PERI.FECHA_FINAL,-1) AND ADD_MONTHS(PERI.FECHA_FINAL,1)
  WHERE 
  (--PERI.PIPS_TOTALES>5000 AND 
  PERI.ID BETWEEN 1627690 AND 11627690 
	  --AND PERI.PIPS_TOTALES_PARALELAS>0 AND PERI.PIPS_TOTALES>0 
	  --AND (PERI.PIPS_AGRUPADO_MINUTOS>0 AND PERI.PIPS_AGRUPADO_HORAS>0 AND PERI.PIPS_AGRUPADO_DIAS>0)
	  --AND (PERI.CANTIDAD_PARALELAS/PERI.CANTIDAD)<10
    )
  AND PRE_TFS.ID_INDIVIDUO='1462031621723.649'
GROUP BY PRE_TFS.ID_INDIVIDUO, PRE_TFS.FECHA_SEMANA
ORDER BY PRE_TFS.FECHA_SEMANA DESC
  ;

SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
FROM FOREX.ESTRATEGIA_OPERACION_PERIODO EOP --WHERE ID=1366540



ORDER BY EOP.ID DESC;

SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
FROM FOREX.ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID BETWEEN 198916	AND 236267
AND (EOP.PIPS_TOTALES>5000 AND EOP.ID BETWEEN 198916	AND 236267
	  AND EOP.PIPS_TOTALES_PARALELAS>0 AND EOP.PIPS_TOTALES>0 
	  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
	  AND (EOP.CANTIDAD_PARALELAS/EOP.CANTIDAD)<10)
ORDER BY EOP.PIPS_TOTALES DESC;

SELECT EOP.ID, EOP.PIPS_TOTALES, EOP.* FROM ESTRATEGIA_OPERACION_PERIODO EOP 
  INNER JOIN ESTRATEGIA_OPERACION_PERIODO PERI
    ON ( EOP.FILTRO_PIPS_X_SEMANA=PERI.FILTRO_PIPS_X_SEMANA AND EOP.FILTRO_PIPS_X_MES=PERI.FILTRO_PIPS_X_MES 
			AND EOP.FILTRO_PIPS_X_ANYO=PERI.FILTRO_PIPS_X_ANYO AND EOP.FILTRO_PIPS_TOTALES=PERI.FILTRO_PIPS_TOTALES)
WHERE PERI.ID IN (18443)
ORDER BY EOP.ID DESC;

SELECT MAX(ID), EOP.TIPO_OPERACION, EOP.FILTRO_PIPS_X_SEMANA FP_X_SEMANA, EOP.FILTRO_PIPS_X_MES, EOP.FILTRO_PIPS_X_ANYO, EOP.FILTRO_PIPS_TOTALES, 
  SUM(PIPS_TOTALES) PIPS, SUM(PIPS_TOTALES_PARALELAS) PIPS_PARAL,   
  SUM(EOP.PIPS_AGRUPADO_MINUTOS), SUM(EOP.PIPS_AGRUPADO_HORAS), SUM(EOP.PIPS_AGRUPADO_DIAS),
  SUM(ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2)) CANT
  FROM ESTRATEGIA_OPERACION_PERIODO EOP 
  WHERE EOP.TIPO_OPERACION = 'SELL'
GROUP BY EOP.TIPO_OPERACION, EOP.FILTRO_PIPS_X_SEMANA, EOP.FILTRO_PIPS_X_MES, EOP.FILTRO_PIPS_X_ANYO, EOP.FILTRO_PIPS_TOTALES
HAVING MAX(ID) >= 124701
--AND SUM(PIPS_TOTALES_PARALELAS) > 0
ORDER BY SUM(EOP.PIPS_TOTALES) DESC, SUM(EOP.PIPS_TOTALES_PARALELAS) DESC
--SUM(ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2)) ASC
;

SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID IN (389360)
  --(24494,15521)
ORDER BY EOP.TIPO_OPERACION, EOP.ID DESC
;

SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 1429981 AND 1455771
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0 
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  ORDER BY EOP.PIPS_TOTALES DESC, EOP.PIPS_TOTALES_PARALELAS DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0 
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY EOP.PIPS_TOTALES_PARALELAS DESC, EOP.PIPS_TOTALES DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_TOTALES*0.7+EOP.PIPS_TOTALES_PARALELAS*0.3) DESC;

--TENER EN CUENTA ESTA MANERA DE FILTRAR POR CANTIDAD. ES MAS ESTABLE.
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000  
  AND CANTIDAD>0
  AND CANTIDAD_PARALELAS>CANTIDAD
  ORDER BY (EOP.CANTIDAD_PARALELAS/(EOP.CANTIDAD)) ASC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000  
  ORDER BY (EOP.PIPS_AGRUPADO_MINUTOS+EOP.PIPS_AGRUPADO_HORAS+EOP.PIPS_AGRUPADO_DIAS)/3 DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027 AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_TOTALES*0.4
    +EOP.PIPS_TOTALES_PARALELAS*0.2
    +EOP.PIPS_AGRUPADO_MINUTOS*0.15+EOP.PIPS_AGRUPADO_HORAS*0.15+EOP.PIPS_AGRUPADO_DIAS*0.1) DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027 
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_AGRUPADO_MINUTOS) DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027 
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_AGRUPADO_HORAS) DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027 
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_AGRUPADO_DIAS) DESC;
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027 
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)  
  --AND ID>143000
ORDER BY ABS(EOP.PIPS_TOTALES-EOP.PIPS_TOTALES_PARALELAS) ASC;  
SELECT EOP.ID, PIPS_TOTALES PIPS, PIPS_TOTALES_PARALELAS PIPS_PARAL, ROUND(CANTIDAD_PARALELAS/(CANTIDAD+1),2) CANT, FILTRO_PIPS_X_SEMANA FILTR_SEM, EOP.* 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE TIPO_OPERACION='SELL'
  AND ID BETWEEN 385676 AND 423027 
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)  
  --AND ID>143000
ORDER BY CANTIDAD DESC; 

SELECT ESTRATEGIA_PERIODO, TO_CHAR(FECHA_APERTURA, 'YYYY'), SUM(PIPS), COUNT(*) FROM OPERACION_ESTRATEGIA_PERIODO 
WHERE ESTRATEGIA_PERIODO=1639726
--AND TO_CHAR(FECHA_APERTURA,'YYYYMM')='201306'
--ORDER BY FECHA_APERTURA ASC
GROUP BY ESTRATEGIA_PERIODO, TO_CHAR(FECHA_APERTURA, 'YYYY')
ORDER BY TO_CHAR(FECHA_APERTURA, 'YYYY') ASC
;

SELECT * FROM ESTRATEGIA_OPERACION_PERIODO
WHERE ID=148115;

SELECT * FROM OPERACION_ESTRATEGIA_PERIODO
WHERE ESTRATEGIA_PERIODO=1639726
ORDER BY FECHA_APERTURA ASC;

SELECT * FROM OPERACION_ESTRATEGIA_PERIODO OPER
WHERE OPER.FECHA_APERTURA BETWEEN TO_DATE('20160321', 'YYYYMMDD') AND TO_DATE('20160401', 'YYYYMMDD')
AND OPER.TIPO='SELL'
ORDER BY FECHA_APERTURA ASC;

SELECT FLOOR(FILTRO_PIPS_X_SEMANA/ 100) RANGO, FLOOR(FILTRO_PIPS_X_SEMANA/ 100)*100 R, 
  COUNT(*), SUM(PIPS_TOTALES), ROUND(AVG(PIPS_TOTALES)), MIN(PIPS_TOTALES), MAX(PIPS_TOTALES)
FROM ESTRATEGIA_OPERACION_PERIODO EOP 
GROUP BY FLOOR(FILTRO_PIPS_X_SEMANA/ 100)
ORDER BY SUM(PIPS_TOTALES) DESC;

SELECT FLOOR(FILTRO_PIPS_X_MES/ 100) RANGO, FLOOR(FILTRO_PIPS_X_MES/ 100)*100 R, 
  COUNT(*), SUM(PIPS_TOTALES), ROUND(AVG(PIPS_TOTALES))
FROM ESTRATEGIA_OPERACION_PERIODO EOP --WHERE ID>=1421
GROUP BY FLOOR(FILTRO_PIPS_X_MES/ 100)
ORDER BY SUM(PIPS_TOTALES) DESC;

SELECT SUM(PIPS_TOTALES) FROM ESTRATEGIA_OPERACION_PERIODO EOP 
WHERE FILTRO_PIPS_X_MES BETWEEN 2901 AND 3000;

SELECT FLOOR(FILTRO_PIPS_X_ANYO/ 100) RANGO, FLOOR(FILTRO_PIPS_X_ANYO/ 100)*100 R, 
  COUNT(*), SUM(PIPS_TOTALES), ROUND(AVG(PIPS_TOTALES))
FROM ESTRATEGIA_OPERACION_PERIODO EOP --WHERE ID>=1421
GROUP BY FLOOR(FILTRO_PIPS_X_ANYO/ 100)
ORDER BY SUM(PIPS_TOTALES) DESC;

SELECT FLOOR(FILTRO_PIPS_TOTALES/ 100) RANGO, FLOOR(FILTRO_PIPS_TOTALES/ 100)*100 R, 
  COUNT(*), SUM(PIPS_TOTALES), ROUND(AVG(PIPS_TOTALES))
FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=1421
GROUP BY FLOOR(FILTRO_PIPS_TOTALES/ 100)
ORDER BY SUM(PIPS_TOTALES) DESC;

SELECT OPER.* FROM OPERACION OPER 
  INNER JOIN TMP_TOFILESTRING2 TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO 
  AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 
  WHERE OPER.FECHA_APERTURA>TO_DATE('2011/01/31 23:59', 'YYYY/MM/DD HH24:MI') AND OPER.FECHA_CIERRE IS NOT NULL 
  AND OPER.ID_INDIVIDUO='1462763219878.291'
  ORDER BY OPER.FECHA_APERTURA ASC, TFS.CRITERIO_ORDER1 DESC, TFS.CRITERIO_ORDER2 DESC;

SELECT OPER.* FROM OPERACION OPER 
  INNER JOIN TMP_TOFILESTRING2 TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO 
  AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 
  WHERE OPER.FECHA_APERTURA<TO_DATE('2012/06/01 00:00', 'YYYY/MM/DD HH24:MI') AND OPER.FECHA_CIERRE>TO_DATE('2012/06/01 00:00', 'YYYY/MM/DD HH24:MI')
  --AND OPER.ID_INDIVIDUO='1455148800000.2625'
  ORDER BY OPER.FECHA_APERTURA ASC, TFS.CRITERIO_ORDER1 DESC, TFS.CRITERIO_ORDER2 DESC;
     
SELECT TO_CHAR(FECHA_APERTURA, 'YYYY'), SUM(PIPS), COUNT(*) FROM OPERACION_ESTRATEGIA_PERIODO 
WHERE ESTRATEGIA_PERIODO=2930
--AND FECHA_APERTURA>TO_DATE('20130101', 'YYYYMMDD')
GROUP BY TO_CHAR(FECHA_APERTURA, 'YYYY')
ORDER BY TO_CHAR(FECHA_APERTURA, 'YYYY') ASC;

SELECT * FROM OPERACION_ESTRATEGIA_PERIODO WHERE ID_INDIVIDUO='1463541693654.6069'
--AND FECHA_APERTURA>TO_DATE('20130101', 'YYYYMMDD')
ORDER BY ESTRATEGIA_PERIODO DESC
;

SELECT * FROM OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO IN (150842, 150376)
ORDER BY FECHA_APERTURA ASC;

SELECT FECHA_APERTURA, PIPS FROM OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO=150376
MINUS
SELECT FECHA_APERTURA, PIPS FROM OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO=150842;

SELECT TO_CHAR(FECHA_APERTURA, 'YYYYMM') MES, SUM(PIPS) PIPS, COUNT(*) CANT 
FROM OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO=4165
--AND FECHA_APERTURA BETWEEN TO_DATE('20130101', 'YYYYMMDD') AND TO_DATE('20140101', 'YYYYMMDD')
GROUP BY TO_CHAR(FECHA_APERTURA, 'YYYYMM')
ORDER BY TO_CHAR(FECHA_APERTURA, 'YYYYMM') ASC
;

SELECT ROWNUM, ID, PIPS_TOTALES, EOP.* FROM ESTRATEGIA_OPERACION_PERIODO EOP 
ORDER BY EOP.PIPS_TOTALES DESC;

--UPDATE ESTRATEGIA_OPERACION_PERIODO EOP SET EOP.FILTRO_PIPS_TOTALES=-1+-1*EOP.FILTRO_PIPS_TOTALES
--where EOP.FILTRO_PIPS_TOTALES=0;
--WHERE EOP.FILTRO_PIPS_X_MES=400 AND EOP.FILTRO_PIPS_X_ANYO=2600 AND EOP.FILTRO_PIPS_TOTALES=1000;
--DELETE FROM ESTRATEGIA_OPERACION_PERIODO  WHERE ID =42953;

SELECT TO_CHAR(FECHA_APERTURA, 'YYYYMM') F, SUM(PIPS), COUNT(*) FROM OPERACION_ESTRATEGIA_PERIODO 
--WHERE ESTRATEGIA_PERIODO=43742
GROUP BY TO_CHAR(FECHA_APERTURA, 'YYYYMM')
ORDER BY TO_CHAR(FECHA_APERTURA, 'YYYYMM') ASC
;

SELECT MAX(TO_CHAR(FECHA_APERTURA, 'YYYYMMDD')) MAX_DATE, SUM(PIPS), COUNT(*) FROM OPERACION_ESTRATEGIA_PERIODO
WHERE ESTRATEGIA_PERIODO=42955
AND TO_CHAR(FECHA_APERTURA, 'YYYYMM')<'201402'
ORDER BY TO_CHAR(FECHA_APERTURA, 'YYYYMM') ASC
;

SELECT * FROM TMP_TOFILESTRING2 
WHERE ID_INDIVIDUO='1462763219878.291'
;
   
SELECT EOP.* FROM 
  (SELECT ROWNUM, E.* FROM ESTRATEGIA_OPERACION_PERIODO E ORDER BY FECHA DESC) EOP
WHERE ROWNUM=1;

SELECT EST_OPER_PERIODO_TRG_SEQ.nextval NEXT_ID FROM dual;


SELECT * FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'ESTRATEGIA_OPERACION_PERIODO';

SELECT * FROM ESTRATEGIA_OPERACION_PERIODO 
WHERE FILTRO_PIPS_X_MES=5000 
--AND FILTRO_PIPS_X_ANYO=9800 AND FILTRO_PIPS_TOTALES=6500 
;

SELECT *
FROM ESTRATEGIA_OPERACION_PERIODO
WHERE FILTRO_PIPS_X_MES>4000 AND CANTIDAD<>0
AND PIPS_TOTALES>0
ORDER BY FILTRO_PIPS_X_MES DESC
;

SELECT MIN(ID), MAX(ID) FROM FOREX.ESTRATEGIA_OPERACION_PERIODO WHERE FECHA_INICIAL>TO_DATE('2012', 'YYYY');

SELECT COUNT(*) FROM FOREX.OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO IN (
  SELECT ID FROM FOREX.ESTRATEGIA_OPERACION_PERIODO WHERE FECHA_INICIAL>TO_DATE('2012', 'YYYY')
);
SELECT COUNT(*) FROM FOREX.ESTRATEGIA_OPERACION_PERIODO WHERE ID>=142147086;
SELECT COUNT(*) FROM FOREX.ESTRATEGIA_OPERACION_PERIODO WHERE CANTIDAD=0;

--DELETE FROM ESTRATEGIA_OPERACION_PERIODO WHERE FECHA_INICIAL>TO_DATE('2012', 'YYYY');
SELECT DISTINCT FECHA_INICIAL FROM ESTRATEGIA_OPERACION_PERIODO EOP ORDER BY FECHA_INICIAL ASC;

SELECT FILTRO_PIPS_X_SEMANA, FILTRO_PIPS_X_MES, FILTRO_PIPS_X_ANYO, FILTRO_PIPS_TOTALES, 
  SUM(EOP.PIPS_TOTALES), SUM(EOP.PIPS_TOTALES_PARALELAS)
FROM ESTRATEGIA_OPERACION_PERIODO EOP 
WHERE FECHA_INICIAL<TO_DATE('2012', 'YYYY') AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
--AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
GROUP BY FILTRO_PIPS_X_SEMANA, FILTRO_PIPS_X_MES, FILTRO_PIPS_X_ANYO, FILTRO_PIPS_TOTALES
ORDER BY SUM(EOP.PIPS_TOTALES) DESC, SUM(EOP.PIPS_TOTALES_PARALELAS) DESC;

SELECT FECHA_INICIAL, COUNT(*), SUM(PIPS_TOTALES), SUM(PIPS_TOTALES_PARALELAS) FROM ESTRATEGIA_OPERACION_PERIODO EOP
GROUP BY FECHA_INICIAL
ORDER BY SUM(PIPS_TOTALES) DESC, SUM(PIPS_TOTALES_PARALELAS) DESC
;

UPDATE ESTRATEGIA_OPERACION_PERIODO SET FECHA_FINAL=TO_DATE('20150202 02:41','YYYYMMDD HH24:MI')
WHERE FECHA_FINAL=TO_DATE('20151231 23:59','YYYYMMDD HH24:MI')
--WHERE FECHA_INICIAL=TO_DATE('2011/12/31 23:59', 'YYYY/MM/DD HH24:MI')
;

SELECT COUNT(*) FROM TMP_TOFILESTRING2;

SELECT * FROM (
  SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')'
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=1421
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY EOP.PIPS_TOTALES DESC, EOP.PIPS_TOTALES_PARALELAS DESC)
WHERE ROWNUM<6;

SELECT * FROM OPERACION_ESTRATEGIA_PERIODO EOPSELL
  INNER JOIN OPERACION_ESTRATEGIA_PERIODO EOPSELL
    ON EOPSELL.ESTRATEGIA_PERIODO=EOPSELL.ESTRATEGIA_PERIODO AND EOPSELL.TIPO='SELL'
  WHERE EOPSELL.TIPO='SELL'
;

SELECT PRE_TFS.ID_INDIVIDUO, PRE_TFS.FECHA_SEMANA
  FROM PREVIO_TOFILESTRING PRE_TFS
	WHERE
		( NVL(PRE_TFS.PIPS_SEMANA,0)>-1500 
    AND PRE_TFS.PIPS_MES>-1000
		AND PRE_TFS.PIPS_ANYO>2800
    AND PRE_TFS.PIPS_TOTALES>12000
    )      AND 
    --PRE_TFS.TIPO_OPERACION='BUY'    AND 
      PRE_TFS.FECHA_SEMANA BETWEEN TO_DATE('2016/09/01 00:00', 'YYYY/MM/DD HH24:MI') 
        AND ADD_MONTHS(TO_DATE('2016/10/10 02:41', 'YYYY/MM/DD HH24:MI'),1)
ORDER BY PRE_TFS.FECHA_SEMANA DESC
;

SELECT *
  FROM PREVIO_TOFILESTRING PRE_TFS
--WHERE ID_INDIVIDUO='1462763219878.291'
ORDER BY FECHA_SEMANA DESC;

select count(*), min(id), max(id) FROM ESTRATEGIA_OPERACION_PERIODO EOP 
WHERE EOP.FECHA_FINAL>TO_DATE('2016.04.19 00:04', 'YYYY.MM.DD HH24:MI');

SELECT COUNT(*) FROM OPERACION_ESTRATEGIA_PERIODO
WHERE FECHA_APERTURA>TO_DATE('20141208','YYYYMMDD')
;
