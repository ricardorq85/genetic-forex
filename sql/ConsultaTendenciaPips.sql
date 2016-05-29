--TRUNCATE TABLE TENDENCIA_PIPS;
DELETE FROM TENDENCIA_PIPS WHERE FECHA_OPERACION>TO_DATE('2009', 'YYYY');
--COMMIT;

SELECT FECHA_OPERACION, SUMA_PIPS, NUMERO_OPERACIONES FROM TENDENCIA_PIPS
ORDER BY FECHA_OPERACION DESC;

SELECT SUM(PIPS) FROM OPERACION;

SELECT (T2.SUMA_PIPS-T1.SUMA_PIPS) DIFF, T2.FECHA_OPERACION, T1.FECHA_OPERACION,
  OPER2.PIPS, OPER1.PIPS, ROUND(T2.FECHA_OPERACION-T1.FECHA_OPERACION, 4) DIFF_FECHA
FROM TENDENCIA_PIPS T1, TENDENCIA_PIPS T2, OPERACION OPER1, OPERACION OPER2
WHERE T2.FECHA_OPERACION > T1.FECHA_OPERACION
AND OPER1.FECHA_APERTURA=T1.FECHA_OPERACION AND OPER2.FECHA_APERTURA=T2.FECHA_OPERACION
AND (T2.SUMA_PIPS - T1.SUMA_PIPS)=685
ORDER BY T1.FECHA_OPERACION
;

SELECT (T2.SUMA_PIPS-T1.SUMA_PIPS) DIFF, COUNT(*)
FROM TENDENCIA_PIPS T1, TENDENCIA_PIPS T2
WHERE T2.FECHA_OPERACION > T1.FECHA_OPERACION
--AND (T2.SUMA_PIPS-T1.SUMA_PIPS)>0
GROUP BY (T2.SUMA_PIPS-T1.SUMA_PIPS)
ORDER BY COUNT(*) DESC
;

SELECT --OPER.*
TO_CHAR(OPER.FECHA_APERTURA, 'YYYY.MM') FECHA, SUM(OPER.PIPS)
FROM OPERACION OPER
  INNER JOIN (SELECT MIN(ID_INDIVIDUO) ID_IND, FECHA_APERTURA FROM TMP_TABLE
    GROUP BY FECHA_APERTURA) TMP ON OPER.ID_INDIVIDUO=TMP.ID_IND AND OPER.FECHA_APERTURA=TMP.FECHA_APERTURA
--WHERE OPER.FECHA_APERTURA < SYSDATE - 4.2*360
GROUP BY TO_CHAR(OPER.FECHA_APERTURA, 'YYYY.MM')
--ORDER BY OPER.FECHA_APERTURA ASC;
ORDER BY TO_CHAR(OPER.FECHA_APERTURA, 'YYYY.MM') ASC;

SELECT MIN(ID_INDIVIDUO), FECHA_APERTURA FROM TMP_TABLE
GROUP BY FECHA_APERTURA;

CREATE TABLE TMP_TABLE AS SELECT * FROM TMP_VIEW;

--CREATE OR REPLACE VIEW TMP_VIEW AS
SELECT 
OPER1.*, CONTADOR.CO
--DISTINCT OPER1.FECHA_APERTURA, OPER1.PIPS, CONTADOR.CO
--CONTADOR.CO, CONTADOR.DIFF, SUM(OPER1.PIPS) SUMA
  FROM TENDENCIA_PIPS T1, TENDENCIA_PIPS T2, OPERACION OPER1,
    (SELECT * FROM (SELECT (T2.SUMA_PIPS-T1.SUMA_PIPS) DIFF, COUNT(*) CO
      FROM TENDENCIA_PIPS T1, TENDENCIA_PIPS T2
      WHERE T2.FECHA_OPERACION > T1.FECHA_OPERACION
      --AND (T2.SUMA_PIPS-T1.SUMA_PIPS)>0
      GROUP BY (T2.SUMA_PIPS-T1.SUMA_PIPS)
      HAVING COUNT(*)=283
      ORDER BY COUNT(*) DESC) 
      --WHERE ROWNUM<3
      ) CONTADOR
  WHERE T2.FECHA_OPERACION > T1.FECHA_OPERACION
  AND OPER1.FECHA_APERTURA=(SELECT MIN(OPER2.FECHA_APERTURA) FROM OPERACION OPER2 WHERE OPER2.FECHA_APERTURA>T2.FECHA_OPERACION)
  AND (T2.SUMA_PIPS - T1.SUMA_PIPS)=CONTADOR.DIFF
--  GROUP BY CONTADOR.CO, CONTADOR.DIFF
  ORDER BY CONTADOR.CO DESC
--  , OPER1.FECHA_APERTURA ASC
;

SELECT * FROM OPERACION OPER1 WHERE
OPER1.FECHA_APERTURA=(SELECT MIN(OPER2.FECHA_APERTURA) FROM OPERACION OPER2 WHERE OPER2.FECHA_APERTURA>SYSDATE-4000);

SELECT * FROM OPERACION WHERE FECHA_APERTURA=TO_DATE('2008/08/08 12:57','YYYY/MM/DD HH24:MI');
