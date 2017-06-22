SELECT DH.* 
FROM DATOHISTORICO DH 
WHERE DH.	FECHA>=TO_DATE('2016.03.29 10:00', 'YYYY/MM/DD HH24:MI')
--WHERE DH.FECHA>=TO_DATE('2016/02/15 17:21', 'YYYY/MM/DD HH24:MI')
  --DH.FECHA<=TO_DATE('2014/08/01 03:08', 'YYYY/MM/DD HH24:MI')
--AND ROWNUM<5
ORDER BY DH.FECHA ASC
;

SELECT COUNT(*), 25240/COUNT(*) FROM DATOHISTORICO;

SELECT COMPARE_VALUE, AVERAGE_COMPARE, AVERAGE_COMPARE1200, DH.* 
FROM DATOHISTORICO DH 
WHERE LENGTH(COMPARE_VALUE)<7 AND LENGTH(AVERAGE_COMPARE)<7 AND LENGTH(AVERAGE_COMPARE1200)<7
AND DH.FECHA>=TO_DATE('2014/12/05 00:00', 'YYYY/MM/DD HH24:MI')
--WHERE DH.FECHA>=TO_DATE('2016/04/19 00:05', 'YYYY/MM/DD HH24:MI')
  --AND DH.FECHA<=TO_DATE('2009/08/01 03:08', 'YYYY/MM/DD HH24:MI')
--AND ROWNUM<5
ORDER BY DH.FECHA ASC
;

SELECT TO_CHAR(DH.FECHA, 'YYYYMMDD'), COUNT(*)
FROM DATOHISTORICO DH 
WHERE LENGTH(COMPARE_VALUE)<7 AND LENGTH(AVERAGE_COMPARE)<7 AND LENGTH(AVERAGE_COMPARE1200)<7
AND DH.FECHA>=TO_DATE('2014/11/01 00:00', 'YYYY/MM/DD HH24:MI')
--WHERE DH.FECHA>=TO_DATE('2016/04/19 00:05', 'YYYY/MM/DD HH24:MI')
  --AND DH.FECHA<=TO_DATE('2009/08/01 03:08', 'YYYY/MM/DD HH24:MI')
--AND ROWNUM<5
GROUP BY TO_CHAR(DH.FECHA, 'YYYYMMDD')
ORDER BY TO_CHAR(DH.FECHA, 'YYYYMMDD') ASC
;

SELECT MIN(FECHA) MINIMA, MAX(FECHA) MAXIMA, count(*)
FROM DATOHISTORICO
--WHERE MA1200 IS NOT NULL
;

SELECT MIN(ICHIMOKUCHINKOUSPAN6) MINIMA, MAX(ICHIMOKUCHINKOUSPAN6) MAXIMA
FROM DATOHISTORICO;

SELECT MIN(FECHA) MINIMA, MAX(FECHA) MAXIMA,
  ROUND((MAX(FECHA) - MIN(FECHA)), 2) DIAS,
  ROUND((MAX(FECHA) - MIN(FECHA))/30, 2) MESES,
  ROUND((MAX(FECHA) - MIN(FECHA))/30/12, 2) ANOS
FROM DATOHISTORICO;

SELECT MIN(LOW) MINIMO, MAX(HIGH) MAXIMO, (MAX(HIGH)-MIN(LOW))*100000 DIFF FROM DATOHISTORICO
WHERE FECHA>=TO_DATE('2012/06/21 07:22', 'YYYY/MM/DD HH24:MI') AND FECHA<=TO_DATE('2012/06/21 07:52', 'YYYY/MM/DD HH24:MI');

SELECT * FROM DATOHISTORICO
WHERE FECHA BETWEEN TO_DATE('2012/06/28 10:30', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/29 08:30', 'YYYY/MM/DD HH24:MI') 
  AND HIGH >= 1.0262
  AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2
  WHERE DH2.FECHA BETWEEN TO_DATE('2012/06/28 10:30', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/29 08:30', 'YYYY/MM/DD HH24:MI') 
    AND DH2.LOW < 1.0262-50/100000)
ORDER BY FECHA ASC
;

SELECT * FROM DATOHISTORICO
WHERE FECHA BETWEEN TO_DATE('2012/06/18 14:24', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/19 14:24', 'YYYY/MM/DD HH24:MI') 
  AND LOW <= 1.02688-50/100000
  AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2
  WHERE DH2.FECHA BETWEEN TO_DATE('2012/06/18 14:24', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/19 14:24', 'YYYY/MM/DD HH24:MI') 
    AND DH2.HIGH > 1.02688+50/100000)
ORDER BY FECHA ASC;

SELECT * FROM (
  SELECT * FROM (SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, 
                 OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, 
                 COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, 
                 RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, 
                 ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN
                 FROM DATOHISTORICO 
                 WHERE FECHA > NVL(TO_DATE('2012/09/24 01:33', 'YYYY/MM/DD HH24:MI'),(SELECT MIN(FECHA) FROM DATOHISTORICO)) 
                 ORDER BY FECHA ASC)
                 WHERE ROWNUM<5000)
ORDER BY VOLUME DESC
;

SELECT AVG(CALC) FROM (
  SELECT TO_CHAR(DH.FECHA, 'YYYYMMDD HH24'), MAX(DH.HIGH) H, MIN(DH.LOW) L,
  (MAX(DH.HIGH)-MIN(DH.LOW))*100000 CALC
  --AVG((DH.HIGH)-(DH.LOW))*100000 CALC2
  FROM DATOHISTORICO DH 
  --WHERE DH.FECHA<TO_DATE('2012/07/19 19:01', 'YYYY/MM/DD HH24:MI')
  GROUP BY TO_CHAR(DH.FECHA, 'YYYYMMDD HH24')
  --ORDER BY (MAX(DH.HIGH)-MIN(DH.LOW)) DESC
  )

;

--SELECT AVG(CALC) FROM (
  SELECT MAX((DH1.HIGH)-(DH2.LOW))*100000 CALC
  FROM DATOHISTORICO DH1
    INNER JOIN DATOHISTORICO DH2
    ON DH2.FECHA>DH1.FECHA AND DH2.FECHA<DH1.FECHA+33561
WHERE DH1.FECHA<TO_DATE('2012/07/19 19:01', 'YYYY/MM/DD HH24:MI')
  --GROUP BY TO_CHAR(DH.FECHA, 'YYYYMM')
  --ORDER BY (MAX(DH.HIGH)-MIN(DH.LOW)) DESC
  --)
;

SELECT DH2.FECHA, DH1.FECHA, DH2.AVERAGE, DH1.AVERAGE, (DH2.AVERAGE-DH1.AVERAGE) DIFF
FROM DATOHISTORICO DH1
  INNER JOIN DATOHISTORICO DH2
  ON DH2.FECHA-DH1.FECHA>100
  AND ABS(DH2.AVERAGE-DH1.AVERAGE)*10000<5
WHERE TO_CHAR(DH1.FECHA, 'YYYY')='2012'
;

SELECT ROUND(MIN(MAXIMO-MINIMO),3) DIFF_MIN, ROUND(MAX(MAXIMO-MINIMO),3) DIFF_MA, 
  ROUND(AVG(DIFF),3) PROMEDIO, STATS_MODE(DIFF) MODA
FROM (
  SELECT TO_CHAR(DH.FECHA, 'YYYYMMDD') FECHA, MIN(DH.LOW)*10000 MINIMO, MAX(DH.HIGH)*10000 MAXIMO, 
    (MAX(DH.HIGH)-MIN(DH.LOW))*10000 DIFF, ROUND((AVG(DH.HIGH)-MIN(DH.LOW))*10000,3) PROM
  FROM DATOHISTORICO DH 
  GROUP BY TO_CHAR(DH.FECHA, 'YYYYMMDD')
  ) 
  ORDER BY FECHA DESC
;

SELECT MIN(DH.LOW), MAX(DH.HIGH)
FROM DATOHISTORICO DH 
WHERE DH.FECHA>=TO_DATE('2011/07/01 00:10') 
  AND DH.FECHA<=TO_DATE('2011/07/01 00:10') +1
  ;

SELECT * FROM DATOHISTORICO DH WHERE HIGH=(SELECT MAX(HIGH) MAXIMO FROM DATOHISTORICO 
                  WHERE FECHA>TO_DATE('2011/07/13 09:32', 'YYYY/MM/DD HH24:MI')
                    AND FECHA<TO_DATE('2011/12/29 22:58', 'YYYY/MM/DD HH24:MI') AND HIGH>1.0232) 
                  AND FECHA>TO_DATE('2011/07/13 09:32', 'YYYY/MM/DD HH24:MI') AND FECHA<TO_DATE('2011/12/29 22:58', 'YYYY/MM/DD HH24:MI') 
                    AND HIGH>1.0232 AND ROWNUM<2;

SELECT * FROM DATOHISTORICO DH WHERE LOW=(SELECT MIN(LOW) MINIMO FROM DATOHISTORICO 
                    WHERE FECHA>TO_DATE('2011/07/13 09:32', 'YYYY/MM/DD HH24:MI')
                    AND FECHA<TO_DATE('2011/07/20 10:49', 'YYYY/MM/DD HH24:MI') AND LOW<0.95898) 
                     AND FECHA>TO_DATE('2011/07/13 09:32', 'YYYY/MM/DD HH24:MI')
                     AND FECHA<TO_DATE('2011/07/20 10:49', 'YYYY/MM/DD HH24:MI')
                     AND LOW<0.95898 AND ROWNUM<2;
                     
-- REGRESION LINEAL
SELECT PAR, DH.FECHA, DH.HIGH, DH.AVERAGE,
  /*ROUND(REGR_R2((DH.HIGH), DH.VOLUME),5) R2_BY_VOLUME,
  ROUND(REGR_SLOPE((DH.HIGH), DH.VOLUME),5) SLOPE_BY_VOLUME,*/
  
  ROUND(REGR_R2((DH.HIGH), (SYSDATE-DH.FECHA)) OVER (PARTITION BY DH.PAR),5) R2_BY_FECHA,
  ROUND(REGR_INTERCEPT((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR),5) INT_BY_AVERAGE,
  ROUND(REGR_R2((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR),5) R2_BY_AVERAGE,
  ROUND(REGR_SLOPE((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR),5) SLOPE_BY_AVERAGE,
  ROUND((-REGR_INTERCEPT((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR))*(REGR_SLOPE((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR)),5) B,
  ROUND((REGR_SLOPE((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR)) * 0.99237
      +(-((REGR_INTERCEPT((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR))*(REGR_SLOPE((DH.HIGH), DH.AVERAGE) OVER (PARTITION BY DH.PAR)))),5) Y
  

  /*
  ROUND(REGR_R2((DH.HIGH), DH.MACD_VALUE),5) R2_BY_MACDV,
  ROUND(REGR_SLOPE((DH.HIGH), DH.MACD_VALUE),5) SLOPE_BY_MACDV,
  ROUND(REGR_R2((DH.HIGH), DH.MACD_SIGNAL),5) R2_BY_MACDS,
  ROUND(REGR_SLOPE((DH.HIGH), DH.MACD_SIGNAL),5) SLOPE_BY_MACDS,  
  ROUND(REGR_R2((DH.HIGH), DH.COMPARE_VALUE),5) R2_BY_COMPARE,
  ROUND(REGR_SLOPE((DH.HIGH), DH.COMPARE_VALUE),5) SLOPE_BY_COMPARE,  
  ROUND(REGR_R2((DH.HIGH), DH.AVERAGE_COMPARE),5) R2_BY_COMPARE_AVG,
  ROUND(REGR_SLOPE((DH.HIGH), DH.AVERAGE_COMPARE),5) SLOPE_BY_COMPARE_AVG,  
  ROUND(REGR_R2((DH.HIGH), DH.SAR),5) R2_BY_SAR,
  ROUND(REGR_SLOPE((DH.HIGH), DH.SAR),5) SLOPE_BY_SAR,  
  ROUND(REGR_R2((DH.HIGH), DH.ADX_VALUE),5) R2_BY_ADX_VALUE,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ADX_VALUE),5) SLOPE_BY_ADX_VALUE,
  ROUND(REGR_R2((DH.HIGH), DH.ADX_PLUS),5) R2_BY_ADX_PLUS,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ADX_PLUS),5) SLOPE_BY_ADX_PLUS,  
  ROUND(REGR_R2((DH.HIGH), DH.ADX_MINUS),5) R2_BY_ADX_MINUS,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ADX_MINUS),5) SLOPE_BY_ADX_MINUS, 
  ROUND(REGR_R2((DH.HIGH), DH.RSI),5) R2_BY_RSI,
  ROUND(REGR_SLOPE((DH.HIGH), DH.RSI),5) SLOPE_BY_RSI,  
  ROUND(REGR_R2((DH.HIGH), DH.BOLLINGER_UPPER),5) R2_BY_BOLL_UPPER,
  ROUND(REGR_SLOPE((DH.HIGH), DH.BOLLINGER_UPPER),5) SLOPE_BY_BOLL_UPPER,
  ROUND(REGR_R2((DH.HIGH), DH.BOLLINGER_LOWER),5) R2_BY_BOLL_LOWER,
  ROUND(REGR_SLOPE((DH.HIGH), DH.BOLLINGER_LOWER),5) SLOPE_BY_BOLL_LOWER,  
  ROUND(REGR_R2((DH.HIGH), DH.MOMENTUM),5) R2_BY_MOMENTUM,
  ROUND(REGR_SLOPE((DH.HIGH), DH.MOMENTUM),5) SLOPE_BY_MOMENTUM,
  ROUND(REGR_R2((DH.HIGH), DH.ICHIMOKUTENKANSEN),5) R2_BY_ICHITENKANSEN,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ICHIMOKUTENKANSEN),5) SLOPE_BY_ICHITENKANSEN,  
  ROUND(REGR_R2((DH.HIGH), DH.ICHIMOKUKIJUNSEN),5) R2_BY_ICHIKIJUNSEN,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ICHIMOKUKIJUNSEN),5) SLOPE_BY_ICHIKIJUNSEN,
  ROUND(REGR_R2((DH.HIGH), DH.ICHIMOKUSENKOUSPANA),5) R2_BY_ICHISPANA,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ICHIMOKUSENKOUSPANA),5) SLOPE_BY_ICHISPANA,  
  ROUND(REGR_R2((DH.HIGH), DH.ICHIMOKUSENKOUSPANB),5) R2_BY_ICHISPANB,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ICHIMOKUSENKOUSPANB),5) SLOPE_BY_ICHISPANB,  
  ROUND(REGR_R2((DH.HIGH), DH.ICHIMOKUCHINKOUSPAN),5) R2_BY_ICHISPAN,
  ROUND(REGR_SLOPE((DH.HIGH), DH.ICHIMOKUCHINKOUSPAN),5) SLOPE_BY_ICHISPAN
  */
FROM DATOHISTORICO DH 
WHERE DH.FECHA > TO_DATE('2012/06/11 17:35') 
--AND TO_DATE('2012/06/12 11:41')
--GROUP BY DH.PAR
;

SELECT FECHA, TO_CHAR(FECHA,'D') FROM FOREX.DATOHISTORICO --WHERE ROWNUM < 10
where to_char(FECHA, 'D') IN (1) AND TO_CHAR(FECHA, 'HH24')<1
ORDER BY FECHA DESC
;

SELECT * FROM (SELECT PAR, MINUTOS, PAR_COMPARE, FECHA,
                OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL,
                COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS,
                RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN,
                ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN
                FROM DATOHISTORICO WHERE FECHA > NVL(TO_DATE('2012/08/15 06:00','YYYY/MM/DD HH24:MI'),(SELECT MIN(FECHA) FROM DATOHISTORICO)) ORDER BY FECHA ASC)
                WHERE ROWNUM<5000
;
                
SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, 
AVERAGE, MACD_VALUE, MACD_SIGNAL, COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, 
RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, 
ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN
FROM DATOHISTORICO WHERE FECHA > NVL(NULL,(SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2));


SELECT DH.* 
FROM FOREX.DATOHISTORICO DH WHERE DH.FECHA>TO_DATE('2008/07/29 03:11','YYYY/MM/DD HH24:MI')
ORDER BY DH.FECHA ASC;

SELECT DH.* 
FROM FOREX.DATOHISTORICO DH WHERE DH.FECHA IN (TO_DATE('2012/06/21 17:35','YYYY/MM/DD HH24:MI'), TO_DATE('2012/06/28 02:53','YYYY/MM/DD HH24:MI'))
ORDER BY DH.FECHA ASC;

SELECT *
FROM DATOHISTORICO DH WHERE DH.FECHA>=TO_DATE('2009/01/01 00:00', 'YYYY/MM/DD HH24:MI');

SELECT DH.* 
FROM FOREX.DATOHISTORICO DH WHERE AVERAGE_COMPARE IS NOT NULL
ORDER BY DH.FECHA ASC;

SELECT 
COUNT(*)
--DH1.* 
FROM DATOHISTORICO DH1
WHERE NOT EXISTS (SELECT * FROM DATOHISTORICO DH2
  WHERE  DH1.PAR=DH2.PAR AND DH1.MINUTOS=DH2.MINUTOS
  AND DH2.FECHA = (DH1.FECHA+1/1440)
  )
AND NOT ((TO_CHAR(DH1.FECHA, 'D')='5') AND (TO_CHAR(DH1.FECHA+1/24, 'HH24')=22) AND (TO_CHAR(DH1.FECHA+1/1440, 'MI')=59))
ORDER BY DH1.FECHA DESC;

SELECT 
TO_CHAR(DH1.FECHA, 'MM'), COUNT(*)
FROM DATOHISTORICO DH1
WHERE NOT EXISTS (SELECT * FROM DATOHISTORICO DH2
  WHERE  DH1.PAR=DH2.PAR AND DH1.MINUTOS=DH2.MINUTOS
  AND DH2.FECHA = (DH1.FECHA+1/1440)
  )
AND NOT ((TO_CHAR(DH1.FECHA, 'D')='5') AND (TO_CHAR(DH1.FECHA+1/24, 'HH24')=22) AND (TO_CHAR(DH1.FECHA+1/1440, 'MI')=59))
GROUP BY TO_CHAR(DH1.FECHA, 'MM')
ORDER BY TO_CHAR(DH1.FECHA, 'MM')
;


SELECT * FROM DATOHISTORICO 
WHERE FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
  AND LOW > 1.01734
  AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 
    WHERE DH2.FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
    AND DH2.LOW < 1.01734)
ORDER BY FECHA ASC
;

SELECT * FROM DATOHISTORICO 
WHERE FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
AND HIGH < 1.03057
AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 
  WHERE FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
  AND DH2.HIGH > 1.03057)
ORDER BY FECHA ASC;

SELECT * FROM DATOHISTORICO  
WHERE FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
AND HIGH < 1.0286
AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2  
  WHERE FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
  AND DH2.HIGH > 1.0296)
ORDER BY FECHA ASC;

SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 
    WHERE DH2.FECHA BETWEEN TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2012/06/25 13:02', 'YYYY/MM/DD HH24:MI')+1
    AND DH2.HIGH > 1.0296
ORDER BY FECHA ASC;

SELECT COUNT(*) CANTIDAD, MIN(FECHA) FEMIN, MAX(FECHA) FEMAX, 
AVERAGE
--,MACD_VALUE, MACD_SIGNAL
--, COMPARE_VALUE, AVERAGE_COMPARE, SAR
--, ADX_VALUE, ADX_PLUS, ADX_MINUS
--, RSI
, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM
--ICHIMOKUTENKANSEN, ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN
FROM DATOHISTORICO
GROUP BY AVERAGE
--,MACD_VALUE, MACD_SIGNAL
--, COMPARE_VALUE, AVERAGE_COMPARE, SAR
--, ADX_VALUE, ADX_PLUS, ADX_MINUS
--, RSI
, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM
--ICHIMOKUTENKANSEN, ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN
ORDER BY COUNT(*) DESC, MAX(FECHA)-MIN(FECHA) DESC
;

SELECT DH.CLOSE, COUNT(*) CANTIDAD, MIN(FECHA), MAX(FECHA) FROM DATOHISTORICO DH
GROUP BY DH.CLOSE
ORDER BY COUNT(*) DESC;

--DELETE FROM DATOHISTORICO DH WHERE FECHA>TO_DATE('2012/08/15 00:35');