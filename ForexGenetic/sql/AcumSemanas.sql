SELECT * FROM PREVIO_TOFILESTRING OPER
--WHERE OPER.PIPS_TOTALES>0 AND TIPO_OPERACION='SELL'
ORDER BY OPER.FECHA_SEMANA DESC, OPER.PIPS_TOTALES DESC;

SELECT * FROM PREVIO_TOFILESTRING OPER
WHERE OPER.ID_INDIVIDUO LIKE  '1452804197914.16'
--AND OPER.PIPS_TOTALES>0
ORDER BY OPER.FECHA_SEMANA DESC, OPER.PIPS_TOTALES DESC;

SELECT * FROM PREVIO_TOFILESTRING OPER
WHERE --OPER.ID_INDIVIDUO IN ('1477883335391.2558') AND 
FECHA_SEMANA>TO_DATE('20160620', 'YYYYMMDD')
AND PIPS_SEMANA>0
ORDER BY OPER.FECHA_SEMANA ASC, OPER.PIPS_SEMANA DESC, OPER.PIPS_TOTALES DESC;

SELECT COUNT(*) FROM PREVIO_TOFILESTRING PTFS;

SELECT OPER.FECHA_SEMANA, COUNT(*),
  ROUND(AVG(OPER.PIPS_SEMANA)) PIPSS, ROUND(AVG(OPER.PIPS_MES)) PIPSM, 
  ROUND(AVG(OPER.PIPS_ANYO)) PIPSA, ROUND(AVG(OPER.PIPS_TOTALES)) PIPST 
FROM PREVIO_TOFILESTRING OPER
GROUP BY OPER.FECHA_SEMANA
ORDER BY OPER.FECHA_SEMANA DESC
;

SELECT TO_CHAR(PTFS.FECHA_SEMANA,'YYYYMM') MES, COUNT(*), 
ROUND(AVG(PTFS.PIPS_SEMANA), 2) AVGPIPSSEM, ROUND(AVG(PTFS.PIPS_MES), 2) AVGPIPSMES, 
ROUND(AVG(PTFS.PIPS_ANYO), 2) AVGPIPSANYO, ROUND(AVG(PTFS.PIPS_TOTALES), 2) AVGPIPSTOT,
ROUND(AVG(PTFS.R2_SEMANA), 5) AVGR2SEM, ROUND(AVG(PTFS.R2_MES), 5) AVGR2MES,
ROUND(AVG(PTFS.R2_ANYO), 5) AVGR2ANYO, ROUND(AVG(PTFS.R2_CONSOL), 5) AVGR2TOT,
ROUND(MIN(PTFS.R2_SEMANA), 5) MINR2SEM, ROUND(MIN(PTFS.R2_MES), 5) MINR2MES,
ROUND(MIN(PTFS.R2_ANYO), 5) MINR2ANYO, ROUND(MIN(PTFS.R2_CONSOL), 5) MINR2TOT,
ROUND(MAX(PTFS.R2_SEMANA), 5) MAXR2SEM, ROUND(MAX(PTFS.R2_MES), 5) MAXR2MES,
ROUND(MAX(PTFS.R2_ANYO), 5) MAXR2ANYO, ROUND(MAX(PTFS.R2_CONSOL), 5) MAXR2TOT,
ROUND(AVG(PTFS.PENDIENTE_SEMANA), 5) AVGPENDSEM, ROUND(AVG(PTFS.PENDIENTE_MES), 5) AVGPENDMES, 
ROUND(AVG(PTFS.PENDIENTE_ANYO), 5) AVGPENDANYO, ROUND(AVG(PTFS.PENDIENTE_CONSOL), 5) AVGPENDTOT
FROM PREVIO_TOFILESTRING PTFS
WHERE (PTFS.PIPS_TOTALES>0 OR PTFS.PIPS_ANYO>0)
AND PTFS.PIPS_SEMANA IS NOT NULL
GROUP BY TO_CHAR(PTFS.FECHA_SEMANA,'YYYYMM')
ORDER BY TO_CHAR(PTFS.FECHA_SEMANA,'YYYYMM') DESC;

SELECT * FROM PREVIO_TOFILESTRING PTFS
WHERE PTFS.PIPS_SEMANA>0 AND PTFS.PIPS_MES>0 AND PTFS.PIPS_ANYO>0 AND PTFS.PIPS_TOTALES>0
ORDER BY PTFS.FECHA_SEMANA DESC, PTFS.R2_CONSOL DESC, PTFS.PENDIENTE_CONSOL DESC
;

SELECT * FROM PREVIO_TOFILESTRING PTFS
WHERE PENDIENTE_SEMANA IS NOT NULL
ORDER BY PENDIENTE_ANYO ASC;

SELECT PTFS.FECHA_SEMANA, ROUND(MAX(PTFS.R2_SEMANA),5) R2_SEMANA, ROUND(MAX(PTFS.R2_MES),5) R2_MES,
  ROUND(MAX(PTFS.R2_ANYO),5) R2_ANYO, ROUND(MAX(PTFS.R2_CONSOL),5) R2_CONSOL,
  ROUND(MAX(PTFS.PENDIENTE_SEMANA),5) PENDIENTE_SEMANA, ROUND(MAX(PTFS.PENDIENTE_MES),5) PENDIENTE_MES,
  ROUND(MAX(PTFS.PENDIENTE_ANYO),5) PENDIENTE_ANYO, ROUND(MAX(PTFS.PENDIENTE_CONSOL),5) PENDIENTE_CONSOL
FROM PREVIO_TOFILESTRING PTFS
  --INNER JOIN PROCESO P ON PTFS.ID_INDIVIDUO=P.ID_INDIVIDUO AND P.FECHA_PROCESO>TRUNC(SYSDATE)
WHERE PTFS.R2_CONSOL IS NOT NULL
GROUP BY PTFS.FECHA_SEMANA
--ORDER BY PTFS.FECHA_SEMANA DESC
ORDER BY MAX(PTFS.R2_CONSOL) DESC, MAX(PTFS.PENDIENTE_CONSOL) DESC
;

SELECT COUNT(*) FROM TMP_TOFILESTRING2;

SELECT PTFS.* FROM PREVIO_TOFILESTRING PTFS
  INNER JOIN PROCESO P ON PTFS.ID_INDIVIDUO=P.ID_INDIVIDUO
WHERE PTFS.R_COUNT_CONSOL>10 AND PTFS.PENDIENTE_CONSOL>0
--PTFS.PENDIENTE_SEMANA>0 AND 
AND PTFS.PENDIENTE_ANYO>PTFS.PENDIENTE_CONSOL 
AND PTFS.PENDIENTE_MES>PTFS.PENDIENTE_ANYO
--AND PTFS.PENDIENTE_CONSOL>0
AND PTFS.R2_CONSOL>0.6 --AND PTFS.R2_ANYO>0.5 AND PTFS.R2_MES>0.5
AND PTFS.FECHA_SEMANA<SYSDATE-1
ORDER BY PTFS.FECHA_SEMANA DESC, PTFS.R2_CONSOL DESC;

SELECT TO_CHAR(OPER.FECHA_SEMANA, 'YYYYMM') FE, 
  ROUND(AVG(OPER.PIPS_SEMANA)) PIPSS, ROUND(AVG(OPER.PIPS_MES)) PIPSM, 
  ROUND(AVG(OPER.PIPS_ANYO)) PIPSA, ROUND(AVG(OPER.PIPS_TOTALES)) PIPST 
FROM PREVIO_TOFILESTRING OPER
GROUP BY TO_CHAR(OPER.FECHA_SEMANA, 'YYYYMM')
ORDER BY TO_CHAR(OPER.FECHA_SEMANA, 'YYYYMM') DESC
;

SELECT OPER.FECHA_SEMANA-TO_DATE('20160901','YYYYMMDD') DIFF, OPER.* FROM OPERACION_X_SEMANA OPER
WHERE OPER.ID_INDIVIDUO LIKE '1452804197914.16'
ORDER BY OPER.FECHA_SEMANA ASC;

SELECT COUNT(DISTINCT ID_INDIVIDUO) FROM OPERACION_X_SEMANA OPER;

SELECT * FROM OPERACION_X_SEMANA OPER 
WHERE OPER.ID_INDIVIDUO LIKE '1452804197914.16'
--AND FECHA_SEMANA>=TO_DATE('20120101', 'YYYYMMDD')
ORDER BY FECHA_SEMANA DESC
;

SELECT * FROM OPERACIONES_ACUM_SEMANA_MES OPER
WHERE OPER.ID_INDIVIDUO='1452804197914.16'
--AND FECHA_SEMANA>=TO_DATE('20160713', 'YYYYMMDD')
ORDER BY FECHA_SEMANA DESC
;

SELECT * FROM OPERACIONES_ACUM_SEMANA_ANYO OPER
WHERE OPER.ID_INDIVIDUO='1452804197914.16'
--AND FECHA_SEMANA>=TO_DATE('20150913', 'YYYYMMDD')
ORDER BY FECHA_SEMANA DESC
;

SELECT OPER.*
FROM OPERACIONES_ACUM_SEMANA_CONSOL OPER
WHERE OPER.ID_INDIVIDUO IN ('1452804197914.16')
--AND FECHA_SEMANA>=TO_DATE('20160713', 'YYYYMMDD')
ORDER BY FECHA_SEMANA DESC
;

SELECT * FROM TMP_TOFILESTRING 
ORDER BY VIGENCIA1 DESC, CRITERIO_ORDER1 DESC;

SELECT OPER.FECHA_SEMANA, OPER.ID_INDIVIDUO FROM
  OPERACIONES_ACUM_SEMANA_CONSOL OPER
  INNER JOIN 
  (SELECT FECHA_SEMANA, MAX(PIPS) PIPS FROM OPERACIONES_ACUM_SEMANA_CONSOL
  WHERE FECHA_SEMANA>=TO_DATE('20140101', 'YYYYMMDD')
  GROUP BY FECHA_SEMANA
--ORDER BY FECHA_SEMANA DESC
  ) TMP ON OPER.FECHA_SEMANA=TMP.FECHA_SEMANA AND OPER.PIPS=TMP.PIPS
;

SELECT 'SEMANA', count(*), MAX(FECHA_SEMANA), AVG(PIPS) FROM OPERACION_X_SEMANA SEM
--WHERE SEM.PIPS>0
;
UNION ALL
SELECT 'MES', COUNT(*), MAX(FECHA_SEMANA), AVG(PIPS) FROM OPERACIONES_ACUM_SEMANA_MES OPER
--WHERE OPER.PIPS>-1000
;
UNION ALL
SELECT 'ANYO', COUNT(*), MAX(FECHA_SEMANA), AVG(PIPS) FROM OPERACIONES_ACUM_SEMANA_ANYO OPER
--WHERE OPER.PIPS>-2000
;
SELECT 'CONSOL', COUNT(*), MAX(FECHA_SEMANA), AVG(PIPS) FROM OPERACIONES_ACUM_SEMANA_CONSOL OPER
--WHERE OPER.PIPS>-3000
;

SELECT * FROM OPERACION_X_SEMANA OPER
ORDER BY FECHA_SEMANA DESC
;
SELECT * FROM OPERACIONES_ACUM_SEMANA_MES OPER
ORDER BY FECHA_SEMANA DESC
;
SELECT * FROM OPERACIONES_ACUM_SEMANA_ANYO OPER
ORDER BY FECHA_SEMANA DESC
;
SELECT * FROM OPERACIONES_ACUM_SEMANA_CONSOL OPER
ORDER BY PIPS DESC
;
SELECT * FROM PREVIO_TOFILESTRING TFS 
ORDER BY TFS.FECHA_SEMANA DESC;

SELECT * FROM SEMANAS ORDER BY FECHA_SEMANA DESC;

SELECT * FROM OPERACION_X_SEMANA OPER
WHERE --OPER.ID_INDIVIDUO IN ('1453687605734.178', '1453044410021.4') AND 
OPER.FECHA_SEMANA<=TO_DATE('20160401', 'YYYYMMDD')
AND OPER.PIPS>0
AND OPER.TIPO_OPERACION='SELL'
ORDER BY OPER.FECHA_SEMANA DESC
--ORDER BY OPER.PIPS DESC
;

SELECT * FROM OPERACIONES_ACUM_SEMANA_MES OPER
WHERE --OPER.ID_INDIVIDUO='1460948821579.426' AND 
OPER.FECHA_SEMANA>=TO_DATE('20160321', 'YYYYMMDD')
AND OPER.PIPS>0
ORDER BY OPER.FECHA_SEMANA ASC
;

SELECT * FROM OPERACIONES_ACUM_SEMANA_ANYO OPER
WHERE --OPER.ID_INDIVIDUO='1460948821579.426' AND 
OPER.FECHA_SEMANA>=TO_DATE('20160321', 'YYYYMMDD')
AND OPER.PIPS>0
ORDER BY OPER.FECHA_SEMANA ASC
;

SELECT * FROM OPERACIONES_ACUM_SEMANA_CONSOL OPER
WHERE --OPER.ID_INDIVIDUO='1460948821579.426' AND 
OPER.FECHA_SEMANA>=TO_DATE('20160321', 'YYYYMMDD')
AND OPER.PIPS>0
ORDER BY OPER.FECHA_SEMANA ASC
;

SELECT SQ.ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE FROM (
  SELECT IND.ID ID_INDIVIDUO, AVG(PTFS.PIPS_TOTALES) PIPS_TOTALES FROM INDIVIDUO IND 
				INNER JOIN PREVIO_TOFILESTRING PTFS 
				ON IND.ID=PTFS.ID_INDIVIDUO WHERE 
				PTFS.PIPS_TOTALES>3000 
				AND PTFS.FECHA_SEMANA BETWEEN TO_DATE('2016/11/07 23:59', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2016/12/07 23:59', 'YYYY/MM/DD HH24:MI') 
  GROUP BY IND.ID) SQ
WHERE ROWNUM<300
ORDER BY SQ.PIPS_TOTALES DESC, SQ.ID_INDIVIDUO DESC
		;
    
SELECT DISTINCT IND.ID FROM INDIVIDUO IND
  INNER JOIN PROCESO P ON IND.ID=P.ID_INDIVIDUO AND P.FECHA_HISTORICO=TO_DATE('2016/11/11 23:59', 'YYYY/MM/DD HH24:MI')
  LEFT JOIN PREVIO_TOFILESTRING OPER ON IND.ID=OPER.ID_INDIVIDUO
WHERE OPER.PIPS_TOTALES IS NULL AND ROWNUM<2
;