--CREATE OR REPLACE VIEW DETALLE_ESTADISTICAS AS
SELECT IND.ID ID_INDIVIDUO, 
  NVL(SUM(POSITIVOS.PIPS),0) PIPS_POSITIVOS, NVL(SUM(NEGATIVOS.PIPS),0) PIPS_NEGATIVOS, NVL((SUM(POSITIVOS.PIPS)+SUM(NEGATIVOS.PIPS)),0) PIPS_TOTALES,
  NVL(MIN(POSITIVOS.PIPS_MINIMOS),0) PIPS_MINIMOS_POS, NVL(MIN(NEGATIVOS.PIPS_MINIMOS),0) PIPS_MINIMOS_NEG, NVL(MIN(OPER.PIPS_MINIMOS),0) PIPS_MINIMOS,
  NVL(MAX(POSITIVOS.PIPS_MAXIMOS),0) PIPS_MAXIMOS_POS, NVL(MAX(NEGATIVOS.PIPS_MAXIMOS),0) PIPS_MAXIMOS_NEG, NVL(MAX(OPER.PIPS_MAXIMOS),0) PIPS_MAXIMOS,
  ROUND(NVL(AVG(POSITIVOS.PIPS_PROMEDIO),0),5) AVG_PIPS_POS, ROUND(NVL(AVG(NEGATIVOS.PIPS_PROMEDIO),0),0) AVG_PIPS_NEG, ROUND(NVL(AVG(OPER.PIPS_PROMEDIO),0),0) AVG_PIPS,
  NVL(SUM(POSITIVOS.PIPS_MODA),0) PIPS_MODA_POS, NVL(SUM(NEGATIVOS.PIPS_MODA),0) PIPS_MODA_NEG, NVL(SUM(OPER.PIPS_MODA),0) PIPS_MODA,
  ROUND(NVL(SUM(POSITIVOS.DUR_MINIMA),5)) DUR_MIN_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MINIMA),5)) DUR_MIN_NEG, ROUND(NVL(SUM(OPER.DUR_MINIMA),5)) DUR_MIN, 
  ROUND(NVL(SUM(POSITIVOS.DUR_MAXIMA),5)) DUR_MAX_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MAXIMA),5)) DUR_MAX_NEG, ROUND(NVL(SUM(OPER.DUR_MAXIMA),5)) DUR_MAX,
  ROUND(NVL(SUM(POSITIVOS.DUR_PROMEDIO),5)) DUR_PROM_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_PROMEDIO),5)) DUR_PROM_NEG, ROUND(NVL(SUM(OPER.DUR_PROMEDIO),5)) DUR_PROM,
  ROUND(NVL(SUM(POSITIVOS.DUR_MODA),5)) DUR_MODA_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MODA),5)) DUR_MODA_NEG, ROUND(NVL(SUM(OPER.DUR_MODA),5)) DUR_MODA 
FROM INDIVIDUO IND
  INNER JOIN (SELECT OPER.ID_INDIVIDUO, MIN(OPER.PIPS) PIPS_MINIMOS, MAX(OPER.PIPS) PIPS_MAXIMOS, AVG(OPER.PIPS) PIPS_PROMEDIO, STATS_MODE(PIPS) PIPS_MODA,
    MIN(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MINIMA, MAX(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MAXIMA,
    AVG(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_PROMEDIO, STATS_MODE(OPER.FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MODA
    FROM OPERACION OPER WHERE OPER.FECHA_CIERRE IS NOT NULL AND OPER.FECHA_APERTURA < (SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') FROM PARAMETRO WHERE NOMBRE='FECHA_ESTADISTICAS')
	GROUP BY OPER.ID_INDIVIDUO) OPER ON OPER.ID_INDIVIDUO=IND.ID
  LEFT JOIN (SELECT P.ID_INDIVIDUO, SUM(P.PIPS) PIPS, MIN(P.PIPS) PIPS_MINIMOS, MAX(P.PIPS) PIPS_MAXIMOS, AVG(P.PIPS) PIPS_PROMEDIO, STATS_MODE(P.PIPS) PIPS_MODA,
    MIN(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MINIMA, MAX(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MAXIMA,
    AVG(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_PROMEDIO, STATS_MODE(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MODA
    FROM OPERACION P WHERE P.PIPS>0 AND P.FECHA_CIERRE IS NOT NULL AND P.FECHA_APERTURA < (SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') FROM PARAMETRO WHERE NOMBRE='FECHA_ESTADISTICAS')
	GROUP BY P.ID_INDIVIDUO) POSITIVOS ON POSITIVOS.ID_INDIVIDUO=IND.ID
  LEFT JOIN (SELECT N.ID_INDIVIDUO, SUM(N.PIPS) PIPS, MIN(N.PIPS) PIPS_MINIMOS, MAX(N.PIPS) PIPS_MAXIMOS, AVG(N.PIPS) PIPS_PROMEDIO, STATS_MODE(N.PIPS) PIPS_MODA,
    MIN(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MINIMA, MAX(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MAXIMA,
    AVG(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_PROMEDIO, STATS_MODE(FECHA_CIERRE-FECHA_APERTURA)*24*60 DUR_MODA
    FROM OPERACION N WHERE N.PIPS<=0 AND N.FECHA_CIERRE IS NOT NULL AND N.FECHA_APERTURA < (SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') FROM PARAMETRO WHERE NOMBRE='FECHA_ESTADISTICAS')
	GROUP BY N.ID_INDIVIDUO) NEGATIVOS ON NEGATIVOS.ID_INDIVIDUO=IND.ID
WHERE IND.ID='1333069696483.983'
GROUP BY IND.ID
ORDER BY PIPS_TOTALES DESC
;

SELECT * FROM DETALLE_ESTADISTICAS WHERE ID_INDIVIDUO='1329927210938.5087';
SELECT P.ID_INDIVIDUO, ROUND(MIN(FECHA_CIERRE-FECHA_APERTURA)*24*60,5),SUM(P.PIPS) PIPS, AVG(PIPS), STATS_MODE(PIPS) FROM OPERACION P WHERE P.PIPS>0 GROUP BY P.ID_INDIVIDUO;
SELECT N.ID_INDIVIDUO, SUM(N.PIPS) PIPS, AVG(PIPS), STATS_MODE(PIPS) FROM OPERACION N WHERE N.PIPS<=0 GROUP BY N.ID_INDIVIDUO;