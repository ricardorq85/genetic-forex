SELECT ROUND(AVG(OPER.MAX_PIPS_RETROCESO),3) PROM_RETRO, STATS_MODE(OPER.MAX_PIPS_RETROCESO) MODA_RETRO 
FROM OPERACION OPER
WHERE OPER.PIPS<0 AND MAX_PIPS_RETROCESO>0
;

--TRUNCATE MATERIALIZED VIEW TMP_TOFILESTRING;

CREATE TABLE TMP_TOFILESTRING AS
  SELECT ID_INDIVIDUO, SUM(OPER.PIPS) CRITERIO_ORDER1, COUNT(*) CRITERIO_ORDER2
  FROM FOREX.OPERACION OPER
    INNER JOIN INDIVIDUO IND ON IND.ID=OPER.ID_INDIVIDUO AND IND.CREATION_DATE>TRUNC(SYSDATE)-1
  GROUP BY OPER.ID_INDIVIDUO 
  HAVING SUM(OPER.PIPS)>10000
;

SELECT * FROM TMP_TOFILESTRING;

SELECT *
FROM OPERACION OPER
WHERE OPER.PIPS<0 AND MAX_PIPS_RETROCESO<0;


SELECT TEMP1.ROWNUM, TEMP2.ROWNUM, TEMP1.*, TEMP2.* --DISTINCT TEMP1.ID_INDIVIDUO, TEMP1.CRITERIO_ORDER, TEMP1.CRITERIO_ORDER2 
FROM TMP_TOFILESTRING TEMP1, TMP_TOFILESTRING TEMP2 
WHERE TEMP1.ID_INDIVIDUO!=TEMP2.ID_INDIVIDUO AND TEMP1.ROWID<TEMP2.ROWNM
  AND TEMP1.CRITERIO_ORDER=TEMP2.CRITERIO_ORDER AND TEMP1.CRITERIO_ORDER2=TEMP2.CRITERIO_ORDER2
ORDER BY TEMP1.CRITERIO_ORDER2 DESC;

--DROP MATERIALIZED VIEW TMP_TOFILESTRING;

SELECT OPER.ID_INDIVIDUO ID_INDIVIDUO,
  SUM(OPER.PIPS) CRITERIO_ORDER,
  COUNT(*) CRITERIO_ORDER2
FROM OPERACION OPER INNER JOIN (
  SELECT OPER4.ID_INDIVIDUO FROM OPERACION OPER4 
  GROUP BY OPER4.ID_INDIVIDUO
  HAVING SUM(OPER4.PIPS)>0 AND COUNT(*)>8  
    MINUS
  SELECT DISTINCT ID_INDIVIDUO FROM OPERACION OPER2 WHERE OPER2.FECHA_APERTURA IN (
    SELECT OPER3.FECHA_APERTURA FROM OPERACION OPER3
    GROUP BY OPER3.FECHA_APERTURA
    HAVING SUM(OPER3.PIPS)<0
    AND NOT EXISTS (
      SELECT 1 FROM OPERACION OPER4 WHERE OPER4.FECHA_APERTURA=OPER3.FECHA_APERTURA
      AND OPER4.PIPS>0
    )    
  )) OPER_FECHA ON OPER.ID_INDIVIDUO=OPER_FECHA.ID_INDIVIDUO  
GROUP BY OPER.ID_INDIVIDUO
--HAVING SUM(OPER.PIPS)>0 AND COUNT(*)>1  
--ORDER BY COUNT(*) DESC
--SUM(OPER.PIPS) DESC
;

SELECT OPER3.FECHA_APERTURA, SUM(OPER3.PIPS) SUMA, COUNT(*) CANTIDAD FROM OPERACION OPER3
GROUP BY OPER3.FECHA_APERTURA
HAVING SUM(OPER3.PIPS)<0
AND NOT EXISTS (
  SELECT 1 FROM OPERACION OPER4 WHERE OPER4.FECHA_APERTURA=OPER3.FECHA_APERTURA
  AND OPER4.PIPS>0
);


SELECT OPER.FECHA_APERTURA, COUNT(*) CANTIDAD, SUM(OPER.PIPS) PIPS FROM OPERACION OPER
GROUP BY OPER.FECHA_APERTURA
HAVING SUM(OPER.PIPS)<0
ORDER BY SUM(OPER.PIPS) ASC;

SELECT OPER.ID_INDIVIDUO, SUM(OPER.PIPS), COUNT(*), ROUND(SUM(OPER.PIPS)/COUNT(*),3) PROM_PIPS,
ROUND(AVG(OPER.MAX_PIPS_RETROCESO),3) PROM_RETRO, MIN(OPER.MAX_PIPS_RETROCESO) MIN_RETRO
FROM OPERACION OPER
GROUP BY OPER.ID_INDIVIDUO
HAVING SUM(OPER.PIPS)>0 AND AVG(OPER.MAX_PIPS_RETROCESO)>0
ORDER BY SUM(OPER.PIPS) DESC
;

SELECT OPER.ID_INDIVIDUO, SUM(OPER.PIPS), COUNT(*), ROUND(SUM(OPER.PIPS)/COUNT(*),3) PROM_PIPS,
ROUND(AVG(OPER.MAX_PIPS_RETROCESO),3) PROM_RETRO, MIN(OPER.MAX_PIPS_RETROCESO) MIN_RETRO
FROM OPERACION OPER 
WHERE ID_INDIVIDUO IN (  
'1331601592515.33107'
) AND OPER.PIPS>0
GROUP BY OPER.ID_INDIVIDUO
ORDER BY AVG(OPER.MAX_PIPS_RETROCESO) DESC, COUNT(*) DESC, SUM(OPER.PIPS) DESC;


SELECT OPER.FECHA_APERTURA, COUNT(*) CANTIDAD, SUM(OPER.PIPS) PIPS FROM OPERACION OPER
GROUP BY OPER.FECHA_APERTURA
HAVING SUM(OPER.PIPS)>0
ORDER BY COUNT(*) DESC;

SELECT OPER.ID_INDIVIDUO, SUM(OPER.PIPS), COUNT(*), ROUND(SUM(OPER.PIPS)/COUNT(*),3) PROM_PIPS,
ROUND(AVG(OPER.MAX_PIPS_RETROCESO),3) PROM_RETRO, MIN(OPER.MAX_PIPS_RETROCESO) MIN_RETRO
FROM OPERACION OPER 
GROUP BY OPER.ID_INDIVIDUO
HAVING SUM(OPER.PIPS)>0 AND COUNT(*)>10 AND SUM(OPER.PIPS)/COUNT(*)>100
ORDER BY AVG(OPER.MAX_PIPS_RETROCESO) DESC, COUNT(*) DESC, SUM(OPER.PIPS) DESC;

