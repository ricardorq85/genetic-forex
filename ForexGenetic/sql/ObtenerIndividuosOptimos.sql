SELECT ID_INDIVIDUO, FECHA_VIGENCIA, MINUTOS_FUTURO MINUTOS,
  CANTIDAD_TOTAL_OPERACIONES CANTIDAD, FACTOR_PIPS, FACTOR_CANTIDAD, NOMBRE_ESTRATEGIA
FROM INDIVIDUOS_ESTRATEGIA
--WHERE FECHA_VIGENCIA<SYSDATE-1200
--WHERE ID_INDIVIDUO='1338606159887.26590'
ORDER BY FECHA_VIGENCIA ASC, FACTOR_PIPS DESC
--ORDER BY ID_INDIVIDUO, FECHA_VIGENCIA ASC
;

SELECT TO_CHAR(FECHA_VIGENCIA, 'YYYY'), COUNT(*)
FROM INDIVIDUOS_ESTRATEGIA GROUP BY TO_CHAR(FECHA_VIGENCIA, 'YYYY')
ORDER BY TO_CHAR(FECHA_VIGENCIA, 'YYYY') ASC;

SELECT IND.ID, CONTADOR_OPER.CANTIDAD CANTIDAD_3MESES, (OPER_POS.CANTIDAD+OPER_NEG.CANTIDAD) CANTIDAD_TOTAL,
  OPER_POS.SUMA SUMA_POSITIVOS, OPER_NEG.SUMA SUMA_NEGATIVOS,
  OPER_POS.CANTIDAD CANTIDAD_POSITIVOS, OPER_NEG.CANTIDAD CANTIDAD_NEGATIVOS,
  ROUND(ABS(OPER_POS.SUMA/OPER_NEG.SUMA),5) FACTOR_PIPS,  
  ROUND((OPER_POS.CANTIDAD/(OPER_POS.CANTIDAD+OPER_NEG.CANTIDAD)),5) FACTOR_CANTIDAD
FROM INDIVIDUO IND  
  INNER JOIN 
    (SELECT OPER1.ID_INDIVIDUO, COUNT(*) CANTIDAD FROM OPERACION OPER1 
    WHERE OPER1.FECHA_APERTURA<TO_DATE('2009/01/01', 'YYYY/MM/DD')
    AND OPER1.FECHA_APERTURA>ADD_MONTHS(TO_DATE('2009/01/01', 'YYYY/MM/DD'),-3)
--    AND OPER1.ID_INDIVIDUO='1341548450906.66208'
    HAVING COUNT(*)>20
    GROUP BY OPER1.ID_INDIVIDUO) CONTADOR_OPER 
      ON CONTADOR_OPER.ID_INDIVIDUO=IND.ID
  LEFT JOIN
    (SELECT P.ID_INDIVIDUO, SUM(P.PIPS) SUMA, COUNT(*) CANTIDAD FROM OPERACION P 
      WHERE P.PIPS>0 AND P.FECHA_APERTURA<TO_DATE('2009/01/01', 'YYYY/MM/DD')
      GROUP BY P.ID_INDIVIDUO) OPER_POS 
      ON OPER_POS.ID_INDIVIDUO=IND.ID
  LEFT JOIN 
    (SELECT P.ID_INDIVIDUO, SUM(P.PIPS) SUMA, COUNT(*) CANTIDAD FROM OPERACION P 
    WHERE P.PIPS<0 AND P.FECHA_APERTURA<TO_DATE('2009/01/01', 'YYYY/MM/DD')
    GROUP BY P.ID_INDIVIDUO) OPER_NEG 
      ON OPER_NEG.ID_INDIVIDUO=IND.ID
WHERE ABS(OPER_POS.SUMA/OPER_NEG.SUMA)>1.8
AND (OPER_POS.CANTIDAD/(OPER_POS.CANTIDAD+OPER_NEG.CANTIDAD))>=0.7
--ORDER BY ABS(OPER_POS.SUMA/OPER_NEG.SUMA) DESC
;

SELECT --COUNT(*) C
  CONTADOR_OPER.PERIODO_ACTUAL, CONTADOR_OPER.ID_INDIVIDUO, SUM(CONTADOR_OPER.CANTIDAD) CANTIDAD_PERIODO
--  (SUM(OPER_POS.CANTIDAD)+SUM(OPER_NEG.CANTIDAD)) CANTIDAD_TOTAL,
  --ROUND(ABS(SUM(OPER_POS.SUMA)/SUM(OPER_NEG.SUMA)),5) FACTOR_PIPS,  
  --ROUND((SUM(OPER_POS.CANTIDAD)/SUM(OPER_NEG.CANTIDAD)),5) FACTOR_CANTIDAD
FROM (SELECT DH.PERIODO_ACTUAL, OPER1.ID_INDIVIDUO, COUNT(*) CANTIDAD
      FROM OPERACION OPER1 
      INNER JOIN (SELECT TO_CHAR(DH1.FECHA, 'YYYYMM') PERIODO_ACTUAL FROM DATOHISTORICO DH1 GROUP BY TO_CHAR(DH1.FECHA, 'YYYYMM')) DH ON
          OPER1.FECHA_APERTURA<TO_DATE(DH.PERIODO_ACTUAL, 'YYYYMM')
          AND OPER1.FECHA_APERTURA>ADD_MONTHS(TO_DATE(DH.PERIODO_ACTUAL, 'YYYYMM'),-3)
          AND OPER1.FECHA_APERTURA<TO_DATE('2010', 'YYYY')
      WHERE OPER1.ID_INDIVIDUO='1330999046124.78'
      HAVING COUNT(*)>20
      GROUP BY DH.PERIODO_ACTUAL, OPER1.ID_INDIVIDUO  ) CONTADOR_OPER 
  LEFT JOIN
    (SELECT P.ID_INDIVIDUO, DH.PERIODO PERIODO, SUM(P.PIPS) SUMA, COUNT(*) CANTIDAD 
      FROM OPERACION P 
      INNER JOIN (SELECT TO_CHAR(DH1.FECHA, 'YYYYMM') PERIODO FROM DATOHISTORICO DH1 GROUP BY TO_CHAR(DH1.FECHA, 'YYYYMM')) DH ON
          P.FECHA_APERTURA<TO_DATE(DH.PERIODO, 'YYYYMM')
      WHERE P.PIPS>0 
      AND P.FECHA_APERTURA<TO_DATE('2010', 'YYYY')
      GROUP BY P.ID_INDIVIDUO, DH.PERIODO) OPER_POS 
      ON OPER_POS.ID_INDIVIDUO=CONTADOR_OPER.ID_INDIVIDUO AND OPER_POS.PERIODO=CONTADOR_OPER.PERIODO_ACTUAL
  LEFT JOIN 
    (SELECT P.ID_INDIVIDUO, DH.PERIODO PERIODO, SUM(P.PIPS) SUMA, COUNT(*) CANTIDAD 
      FROM OPERACION P 
      INNER JOIN (SELECT TO_CHAR(DH1.FECHA, 'YYYYMM') PERIODO FROM DATOHISTORICO DH1 GROUP BY TO_CHAR(DH1.FECHA, 'YYYYMM')) DH ON
          P.FECHA_APERTURA<TO_DATE(DH.PERIODO, 'YYYYMM')
      WHERE P.PIPS<0 
      AND P.FECHA_APERTURA<TO_DATE('2010', 'YYYY')
      GROUP BY P.ID_INDIVIDUO, DH.PERIODO) OPER_NEG
      ON OPER_NEG.ID_INDIVIDUO=CONTADOR_OPER.ID_INDIVIDUO AND OPER_NEG.PERIODO=CONTADOR_OPER.PERIODO_ACTUAL      
GROUP BY CONTADOR_OPER.PERIODO_ACTUAL, CONTADOR_OPER.ID_INDIVIDUO
;

SELECT P.ID_INDIVIDUO, TO_CHAR(P.FECHA_APERTURA, 'YYYYMM') PERIODO, 
  SUM(P.PIPS) SUMA, COUNT(*) CANTIDAD FROM OPERACION P 
WHERE P.PIPS>0 
AND P.ID_INDIVIDUO='1330999046124.78'
GROUP BY P.ID_INDIVIDUO, TO_CHAR(P.FECHA_APERTURA, 'YYYYMM')    
ORDER BY TO_CHAR(P.FECHA_APERTURA, 'YYYYMM')
;

SELECT DH.PERIODO, OPER1.ID_INDIVIDUO, COUNT(*) CANTIDAD
FROM OPERACION OPER1 
INNER JOIN (SELECT TO_CHAR(DH1.FECHA, 'YYYYMM') PERIODO FROM DATOHISTORICO DH1 GROUP BY TO_CHAR(DH1.FECHA, 'YYYYMM')) DH ON
    OPER1.FECHA_APERTURA<TO_DATE(DH.PERIODO, 'YYYYMM')
    AND OPER1.FECHA_APERTURA>ADD_MONTHS(TO_DATE(DH.PERIODO, 'YYYYMM'),-3)
--WHERE OPER1.ID_INDIVIDUO='1330999046124.78'    
HAVING COUNT(*)>20
GROUP BY DH.PERIODO, OPER1.ID_INDIVIDUO
ORDER BY PERIODO
;

SELECT P.ID_INDIVIDUO, DH.PERIODO PERIODO, SUM(P.PIPS) SUMA, COUNT(*) CANTIDAD 
FROM OPERACION P 
INNER JOIN (SELECT TO_CHAR(DH1.FECHA, 'YYYYMM') PERIODO FROM DATOHISTORICO DH1 GROUP BY TO_CHAR(DH1.FECHA, 'YYYYMM')) DH ON
    P.FECHA_APERTURA<TO_DATE(DH.PERIODO, 'YYYYMM')
WHERE P.PIPS>0 
AND P.FECHA_APERTURA<TO_DATE('2010', 'YYYY')
GROUP BY P.ID_INDIVIDUO, DH.PERIODO
ORDER BY PERIODO
;
