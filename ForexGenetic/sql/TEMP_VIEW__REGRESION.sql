--LAG AND LEAD: obtener datos de fila anterior y siguiente

SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
   MIN(TMP.FECHA_SEMANA) MINFE, MAX(TMP.FECHA_SEMANA) MAXFS,
   ROUND(SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO),2) DIFF,
   ADD_MONTHS(SEMANAS.FECHA_SEMANA,-12) ADDM,
--       ROUND(REGR_COUNT(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO))
--        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' YEAR PRECEDING AND CURRENT ROW),5) AS R_COUNT,
      ROUND(REGR_R2(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO))
        --OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' YEAR PRECEDING AND CURRENT ROW)
        ,5) AS R2
--      ROUND(REGR_SLOPE(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO))
--        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' YEAR PRECEDING AND CURRENT ROW),5) AS PENDIENTE,
--      ROUND(REGR_INTERCEPT(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO))
--        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' YEAR PRECEDING AND CURRENT ROW),5) AS INTERCEPCION
 FROM SEMANAS INNER JOIN OPERACION_X_SEMANA TMP
 ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-12)
  AND TMP.FECHA_SEMANA<=SEMANAS.FECHA_SEMANA
 INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO AND TMP.ID_INDIVIDUO='1463541693654.351'
-- NOT EXISTS (SELECT 1 FROM OPERACIONES_ACUM_SEMANA_MES OPC WHERE OPC.ID_INDIVIDUO=TMP.ID_INDIVIDUO) AND
WHERE SEMANAS.FECHA_SEMANA BETWEEN TO_DATE('20150223','YYYYMMDD') AND TO_DATE('20160222','YYYYMMDD')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA
 --HAVING SUM(TMP.PIPS)>=-1000
 --AND MIN(SEMANAS.FECHA_SEMANA)>=TO_DATE('20110101','YYYYMMDD')
ORDER BY SEMANAS.FECHA_SEMANA ASC;

SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS), COUNT(*),
      ROUND(REGR_R2(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO))
        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' YEAR PRECEDING AND CURRENT ROW),5) AS R2,
      ROUND(REGR_SLOPE(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-MAX(P.FECHA_HISTORICO))
        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' YEAR PRECEDING AND CURRENT ROW),5) AS PEND
 FROM SEMANAS INNER JOIN OPERACION TMP
 ON TRUNC((TMP.FECHA_CIERRE),'IW')>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-12)
  AND TRUNC((TMP.FECHA_CIERRE),'IW')<=SEMANAS.FECHA_SEMANA
 INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO AND TMP.ID_INDIVIDUO='1463541693654.351'
WHERE SEMANAS.FECHA_SEMANA BETWEEN TO_DATE('20150223','YYYYMMDD') AND TO_DATE('20160222','YYYYMMDD')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA
ORDER BY SEMANAS.FECHA_SEMANA ASC;

SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
   MIN(TMP.FECHA_SEMANA), MAX(TMP.FECHA_SEMANA),
   MAX(P.FECHA_HISTORICO) FECHA_HISTORICO, ROUND(SEMANAS.FECHA_SEMANA-SYSDATE,2) DIFF,
       ROUND(REGR_COUNT(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-SYSDATE)
        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' MONTH PRECEDING AND CURRENT ROW),5) AS R_COUNT,
      ROUND(REGR_R2(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-SYSDATE)
        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' MONTH PRECEDING AND CURRENT ROW),5) AS R2,
      ROUND(REGR_SLOPE(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-SYSDATE)
        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' MONTH PRECEDING AND CURRENT ROW),5) AS PENDIENTE,
      ROUND(REGR_INTERCEPT(SUM(TMP.PIPS), SEMANAS.FECHA_SEMANA-SYSDATE)
        OVER (ORDER BY SEMANAS.FECHA_SEMANA ASC RANGE BETWEEN INTERVAL '1' MONTH PRECEDING AND CURRENT ROW),5) AS INTERCEPCION
 FROM SEMANAS INNER JOIN OPERACION_X_SEMANA
  --OPERACIONES_ACUM_SEMANA_MES 
  TMP
 ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-1)
  AND 
  TMP.FECHA_SEMANA<=SEMANAS.FECHA_SEMANA
 INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO AND TMP.ID_INDIVIDUO='1463541693654.351'
 --WHERE NOT EXISTS (SELECT 1 FROM OPERACIONES_ACUM_SEMANA_MES OPC WHERE OPC.ID_INDIVIDUO=TMP.ID_INDIVIDUO)
-- AND SEMANAS.FECHA_SEMANA>=TO_DATE('20160829','YYYYMMDD')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA
ORDER BY SEMANAS.FECHA_SEMANA ASC;

SELECT OPER.ID_INDIVIDUO, IND.TIPO_OPERACION, MAX(P.FECHA_HISTORICO) FECHA_HISTORICO,
			SUM(OPER.PIPS) PIPS, COUNT(*) CANTIDAD, TRUNC(MIN(FECHA_CIERRE),'IW'),
      MIN(FECHA_CIERRE), MAX(FECHA_CIERRE), ROUND(TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO),2) DIFF,

			ROUND(REGR_COUNT(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5
      )
      AS R_COUNT          

--			ROUND(REGR_COUNT(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
--			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS R_COUNT,
--			ROUND(REGR_R2(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
--			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS R2,
--			ROUND(REGR_SLOPE(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
--			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS PENDIENTE,
--			ROUND(REGR_INTERCEPT(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO)) 
--			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS INTERCEPCION  
			FROM OPERACION OPER
			INNER JOIN INDIVIDUO IND ON IND.ID=OPER.ID_INDIVIDUO
			INNER JOIN PROCESO P ON P.ID_INDIVIDUO=OPER.ID_INDIVIDUO  WHERE OPER.FECHA_CIERRE IS NOT NULL
			--AND NOT EXISTS (SELECT 1 FROM OPERACION_X_SEMANA OPC WHERE OPC.ID_INDIVIDUO=OPER.ID_INDIVIDUO) 
			AND IND.TIPO_INDIVIDUO = 'INDICADORES' AND OPER.ID_INDIVIDUO='1461280102688.61' --'1463541693654.351'
			GROUP BY OPER.ID_INDIVIDUO, IND.TIPO_OPERACION, TRUNC(FECHA_CIERRE,'IW');

SELECT OPER.ID_INDIVIDUO, IND.TIPO_OPERACION, MAX(P.FECHA_HISTORICO) FECHA_HISTORICO,
			SUM(OPER.PIPS) PIPS, COUNT(*) CANTIDAD, TRUNC(MIN(FECHA_CIERRE),'IW'), 
      MIN(FECHA_CIERRE), MAX(FECHA_CIERRE), ROUND(TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO),2) DIFF,

			ROUND(REGR_COUNT(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS R_COUNT,
			ROUND(REGR_R2(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS R2,
			ROUND(REGR_SLOPE(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO))
			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS PENDIENTE,
			ROUND(REGR_INTERCEPT(SUM(OPER.PIPS), TRUNC((FECHA_CIERRE),'IW')-MAX(P.FECHA_HISTORICO)) 
			OVER (ORDER BY TRUNC((FECHA_CIERRE),'IW') ASC RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW),5) AS INTERCEPCION  
      
			FROM OPERACION OPER
			INNER JOIN INDIVIDUO IND ON IND.ID=OPER.ID_INDIVIDUO
			INNER JOIN PROCESO P ON P.ID_INDIVIDUO=OPER.ID_INDIVIDUO  WHERE OPER.FECHA_CIERRE IS NOT NULL
			--AND NOT EXISTS (SELECT 1 FROM OPERACION_X_SEMANA OPC WHERE OPC.ID_INDIVIDUO=OPER.ID_INDIVIDUO) 
			AND IND.TIPO_INDIVIDUO = 'INDICADORES' AND OPER.ID_INDIVIDUO='1461280102688.61' --'1463541693654.351'
			GROUP BY OPER.ID_INDIVIDUO, IND.TIPO_OPERACION, TRUNC(FECHA_CIERRE,'IW');
