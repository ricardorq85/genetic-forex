---------------------------------------------------------------------------------------------------
--DEPRECATED: YA SE USA POR SEMANA
---------------------------------------------------------------------------------------------------
/*
CREATE TABLE OPERACIONES_ACUM_MES_ANYO AS 
SELECT TMP.ID_INDIVIDUO, MESES.MES, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD
FROM OPERACION_X_MES TMP
  INNER JOIN (SELECT DISTINCT MESES.MES FROM OPERACION_X_MES MESES) MESES
    ON TO_DATE(TMP.MES,'YYYYMM')>=ADD_MONTHS(TO_DATE(MESES.MES,'YYYYMM'),-12)
      AND TO_DATE(TMP.MES,'YYYYMM')<TO_DATE(MESES.MES,'YYYYMM')
WHERE --TMP.ID_INDIVIDUO='1453664590875.284'
TO_DATE(MESES.MES,'YYYYMM')>=TO_DATE('201401','YYYYMM')
GROUP BY TMP.ID_INDIVIDUO, MESES.MES
ORDER BY MESES.MES DESC
;

CREATE TABLE OPERACIONES_ACUM_MES_CONSOL AS
SELECT TMP.ID_INDIVIDUO, MESES.MES, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD
FROM OPERACION_X_MES TMP
  INNER JOIN (SELECT DISTINCT MESES.MES FROM OPERACION_X_MES MESES) MESES
    ON TO_DATE(TMP.MES,'YYYYMM')<TO_DATE(MESES.MES,'YYYYMM')
WHERE --TMP.ID_INDIVIDUO='1453664590875.284'
TO_DATE(MESES.MES,'YYYYMM')>=TO_DATE('201401','YYYYMM')
GROUP BY TMP.ID_INDIVIDUO, MESES.MES
ORDER BY MESES.MES DESC
;

SELECT TMP.ID_INDIVIDUO, MESES.MES, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD
FROM OPERACION_X_MES TMP
  INNER JOIN (SELECT DISTINCT MESES.MES FROM OPERACION_X_MES MESES) MESES
    ON TO_DATE(TMP.MES,'YYYYMM')>=ADD_MONTHS(TO_DATE(MESES.MES,'YYYYMM'),-12)
      AND TO_DATE(TMP.MES,'YYYYMM')<TO_DATE(MESES.MES,'YYYYMM')
WHERE TMP.ID_INDIVIDUO='1453664590875.284' AND
TO_DATE(MESES.MES,'YYYYMM')>=TO_DATE('201401','YYYYMM')
GROUP BY TMP.ID_INDIVIDUO, MESES.MES
ORDER BY MESES.MES DESC
;
*/