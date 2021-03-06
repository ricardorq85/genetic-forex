SELECT EOP.ID, EOP.PIPS_TOTALES, EOP.PIPS_TOTALES_PARALELAS, EOP.FECHA, EOP.* 
FROM FILTERED_EOP EOP 
WHERE PIPS_TOTALES>1000 AND PIPS_TOTALES_PARALELAS>1000
--AND EOP.PIPS_TOTALES_PARALELAS>EOP.PIPS_TOTALES
--AND TIPO_OPERACION='SELL'
AND FECHA>TRUNC(SYSDATE)
ORDER BY EOP.ID DESC
--EOP.PIPS_TOTALES DESC
;

SELECT * FROM FILTERED_EOP ORDER BY FECHA DESC;
SELECT * FROM FILTERED_PTFS;
SELECT * FROM FILTERED_PARA_OPERAR_BUY;
SELECT * FROM FILTERED_PARA_OPERAR_SELL;
SELECT * FROM FILTERED_EOP;

SELECT * FROM FILTERED_PTFS OPER
WHERE OPER.ID_INDIVIDUO LIKE  '1484511934277.502'
--AND OPER.PIPS_TOTALES>0
ORDER BY OPER.FECHA_SEMANA DESC, OPER.PIPS_TOTALES DESC;

SELECT ROUND(BUY.CANTIDAD/((BUY.FECHA_FINAL-BUY.FECHA_INICIAL)/30),2) CANT,
	ROUND((BUY.PIPS_TOTALES_PARALELAS/BUY.CANTIDAD_PARALELAS),2) PROMPIPSPARAL,
	BUY.PIPS_TOTALES, BUY.PIPS_TOTALES_PARALELAS, BUY.CANTIDAD, BUY.CANTIDAD_PARALELAS, BUY.*
FROM FILTERED_PARA_OPERAR_BUY BUY
WHERE BUY.FECHA_SEMANA=TO_DATE('20160905','YYYYMMDD')
--BUY.ID>=1250470 --AND BUY.ID_INDIVIDUO='1454889600000.878'
ORDER BY ROUND((BUY.PIPS_TOTALES_PARALELAS/BUY.CANTIDAD_PARALELAS),2) ASC, BUY.ID DESC;

SELECT * FROM FILTERED_PTFS PTFS
WHERE PTFS.FECHA_SEMANA=TO_DATE('20160905','YYYYMMDD')
AND PTFS.TIPO_OPERACION='BUY'
ORDER BY PTFS.PIPS_TOTALES DESC;

--SELECT FIL.TIPO_OPERACION, COUNT(*) FROM (
SELECT 
FILTERED.ID_INDIVIDUO, FILTERED.TIPO_OPERACION,
			NVL(MIN(FILTERED.PIPS_SEMANA),0),
			ROUND((NVL(MIN(FILTERED.PIPS_MES),0)+NVL(MIN(FILTERED.PIPS_ANYO),0)+MIN(PIPS_TOTALES))/3),
			FILTERED.FECHA_SEMANA, FILTERED.FECHA_SEMANA+7,
			MAX(FILTERED.FECHA), MAX(FILTERED.FECHA_FINAL)
	  FROM FILTERED_PARA_OPERAR_SELL FILTERED
		WHERE FILTERED.ID>=1250470
	GROUP BY FILTERED.ID_INDIVIDUO, FILTERED.TIPO_OPERACION, FILTERED.FECHA_SEMANA
	UNION ALL
	SELECT 
	FILTERED.ID_INDIVIDUO, FILTERED.TIPO_OPERACION,
			NVL(MIN(FILTERED.PIPS_SEMANA),0),
			ROUND((NVL(MIN(FILTERED.PIPS_MES),0)+NVL(MIN(FILTERED.PIPS_ANYO),0)+MIN(PIPS_TOTALES))/3),
			FILTERED.FECHA_SEMANA, FILTERED.FECHA_SEMANA+7,
			MAX(FILTERED.FECHA), MAX(FILTERED.FECHA_FINAL)
	  FROM FILTERED_PARA_OPERAR_BUY FILTERED
		WHERE FILTERED.ID>=1250470
	GROUP BY FILTERED.ID_INDIVIDUO, FILTERED.TIPO_OPERACION, FILTERED.FECHA_SEMANA
--) FIL GROUP BY FIL.TIPO_OPERACION
	;