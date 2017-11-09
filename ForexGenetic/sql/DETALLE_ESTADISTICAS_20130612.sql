
  CREATE OR REPLACE FORCE VIEW "FOREX"."DETALLE_ESTADISTICAS" ("ID_INDIVIDUO", "CANTIDAD_POSITIVOS", "CANTIDAD_NEGATIVOS", "CANTIDAD_TOTAL", "PIPS_POSITIVOS", "PIPS_NEGATIVOS", "PIPS_TOTALES", "PIPS_MINIMOS_POS", "PIPS_MINIMOS_NEG", "PIPS_MINIMOS", "PIPS_MAXIMOS_POS", "PIPS_MAXIMOS_NEG", "PIPS_MAXIMOS", "AVG_PIPS_POS", "AVG_PIPS_NEG", "AVG_PIPS", "PIPS_MODA_POS", "PIPS_MODA_NEG", "PIPS_MODA", "DUR_MIN_POS", "DUR_MIN_NEG", "DUR_MIN", "DUR_MAX_POS", "DUR_MAX_NEG", "DUR_MAX", "DUR_PROM_POS", "DUR_PROM_NEG", "DUR_PROM", "DUR_MODA_POS", "DUR_MODA_NEG", "DUR_MODA", "DUR_DESV_POS", "DUR_DESV_NEG", "DUR_DESV") AS 
  SELECT IND.ID ID_INDIVIDUO, 
  NVL(SUM(POSITIVOS.CANTIDAD),0) CANTIDAD_POSITIVOS, NVL(SUM(NEGATIVOS.CANTIDAD),0) CANTIDAD_NEGATIVOS, NVL(SUM(OPER.CANTIDAD),0) CANTIDAD_TOTAL,
  NVL(SUM(POSITIVOS.PIPS),0) PIPS_POSITIVOS, NVL(SUM(NEGATIVOS.PIPS),0) PIPS_NEGATIVOS, NVL((SUM(POSITIVOS.PIPS)+SUM(NEGATIVOS.PIPS)),0) PIPS_TOTALES,
  NVL(MIN(POSITIVOS.PIPS_MINIMOS),0) PIPS_MINIMOS_POS, NVL(MIN(NEGATIVOS.PIPS_MINIMOS),0) PIPS_MINIMOS_NEG, NVL(MIN(OPER.PIPS_MINIMOS),0) PIPS_MINIMOS,
  NVL(MAX(POSITIVOS.PIPS_MAXIMOS),0) PIPS_MAXIMOS_POS, NVL(MAX(NEGATIVOS.PIPS_MAXIMOS),0) PIPS_MAXIMOS_NEG, NVL(MAX(OPER.PIPS_MAXIMOS),0) PIPS_MAXIMOS,
  ROUND(NVL(AVG(POSITIVOS.PIPS_PROMEDIO),0),5) AVG_PIPS_POS, ROUND(NVL(AVG(NEGATIVOS.PIPS_PROMEDIO),0),0) AVG_PIPS_NEG, ROUND(NVL(AVG(OPER.PIPS_PROMEDIO),0),0) AVG_PIPS,
  NVL(SUM(POSITIVOS.PIPS_MODA),0) PIPS_MODA_POS, NVL(SUM(NEGATIVOS.PIPS_MODA),0) PIPS_MODA_NEG, NVL(SUM(OPER.PIPS_MODA),0) PIPS_MODA,
  ROUND(NVL(SUM(POSITIVOS.DUR_MINIMA),5)) DUR_MIN_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MINIMA),5)) DUR_MIN_NEG, ROUND(NVL(SUM(OPER.DUR_MINIMA),5)) DUR_MIN, 
  ROUND(NVL(SUM(POSITIVOS.DUR_MAXIMA),5)) DUR_MAX_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MAXIMA),5)) DUR_MAX_NEG, ROUND(NVL(SUM(OPER.DUR_MAXIMA),5)) DUR_MAX,
  ROUND(NVL(SUM(POSITIVOS.DUR_PROMEDIO),5)) DUR_PROM_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_PROMEDIO),5)) DUR_PROM_NEG, ROUND(NVL(SUM(OPER.DUR_PROMEDIO),5)) DUR_PROM,
  ROUND(NVL(SUM(POSITIVOS.DUR_MODA),5)) DUR_MODA_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MODA),5)) DUR_MODA_NEG, ROUND(NVL(SUM(OPER.DUR_MODA),5)) DUR_MODA, 
  ROUND(NVL(SUM(POSITIVOS.DUR_DESV),5)) DUR_DESV_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_DESV),5)) DUR_DESV_NEG, ROUND(NVL(SUM(OPER.DUR_DESV),5)) DUR_DESV
FROM INDIVIDUO IND
  INNER JOIN (SELECT OPER.ID_INDIVIDUO, MIN(OPER.PIPS) PIPS_MINIMOS, MAX(OPER.PIPS) PIPS_MAXIMOS, AVG(OPER.PIPS) PIPS_PROMEDIO, STATS_MODE(PIPS) PIPS_MODA,
    MIN(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MINIMA, MAX(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MAXIMA,
    AVG(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_PROMEDIO, STATS_MODE(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MODA,
    STDDEV(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)*24*60 DUR_DESV,
    COUNT(*) CANTIDAD
    FROM OPERACION OPER WHERE OPER.FECHA_CIERRE IS NOT NULL AND OPER.FECHA_APERTURA < (SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') FROM PARAMETRO WHERE NOMBRE='FECHA_ESTADISTICAS')
	GROUP BY OPER.ID_INDIVIDUO) OPER ON OPER.ID_INDIVIDUO=IND.ID
  LEFT JOIN (SELECT P.ID_INDIVIDUO, SUM(P.PIPS) PIPS, MIN(P.PIPS) PIPS_MINIMOS, MAX(P.PIPS) PIPS_MAXIMOS, AVG(P.PIPS) PIPS_PROMEDIO, STATS_MODE(P.PIPS) PIPS_MODA,
    MIN(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MINIMA, MAX(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MAXIMA,
    AVG(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_PROMEDIO, STATS_MODE(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MODA,
    STDDEV(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_DESV,
    COUNT(*) CANTIDAD
    FROM OPERACION P WHERE P.PIPS>0 AND P.FECHA_CIERRE IS NOT NULL AND P.FECHA_APERTURA < (SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') FROM PARAMETRO WHERE NOMBRE='FECHA_ESTADISTICAS')
	GROUP BY P.ID_INDIVIDUO) POSITIVOS ON POSITIVOS.ID_INDIVIDUO=IND.ID
  LEFT JOIN (SELECT N.ID_INDIVIDUO, SUM(N.PIPS) PIPS, MIN(N.PIPS) PIPS_MINIMOS, MAX(N.PIPS) PIPS_MAXIMOS, AVG(N.PIPS) PIPS_PROMEDIO, STATS_MODE(N.PIPS) PIPS_MODA,
    MIN(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MINIMA, MAX(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MAXIMA,
    AVG(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_PROMEDIO, STATS_MODE(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MODA,
    STDDEV(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_DESV,
    COUNT(*) CANTIDAD
    FROM OPERACION N WHERE N.PIPS<=0 AND N.FECHA_CIERRE IS NOT NULL AND N.FECHA_APERTURA < (SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') FROM PARAMETRO WHERE NOMBRE='FECHA_ESTADISTICAS')
	GROUP BY N.ID_INDIVIDUO) NEGATIVOS ON NEGATIVOS.ID_INDIVIDUO=IND.ID
GROUP BY IND.ID;
