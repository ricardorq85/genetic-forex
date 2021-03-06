SELECT
	OPER.ID_INDIVIDUO, ROUND(AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)*(24*60),5) AVGDIFF, ROUND(MAX
	(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)*(24*60),5) MAXDIFF
FROM
	OPERACION OPER
WHERE
	OPER.ID_INDIVIDUO='1477365411387.7303'
GROUP BY
	OPER.ID_INDIVIDUO
	--HAVING AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)<(5/24/60)
	;
	
SELECT OPER.ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE
FROM OPERACION OPER
	INNER JOIN PROCESO P ON OPER.ID_INDIVIDUO=P.ID_INDIVIDUO AND P.FECHA_HISTORICO>TO_DATE('20150101', 'YYYYMMDD')
GROUP BY OPER.ID_INDIVIDUO
HAVING AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)<(5/24/60);

SELECT
	OPER.ID_INDIVIDUO, ROUND(AVG(OPER.PIPS)) AVGPIPS, MAX(OPER.PIPS) MAXPIPS, MIN(OPER.PIPS) MINPIPS, COUNT(*)
FROM	OPERACION OPER
	INNER JOIN PROCESO P ON OPER.ID_INDIVIDUO=P.ID_INDIVIDUO AND P.FECHA_HISTORICO>TO_DATE('20150101', 'YYYYMMDD')
	--WHERE OPER.PIPS<0
	--OPER.ID_INDIVIDUO='1477365411387.7303'
GROUP BY OPER.ID_INDIVIDUO
HAVING
	MAX(OPER.PIPS)<200
	AND (MIN(OPER.PIPS)>-200 AND MIN(OPER.PIPS)<0) ;
	
SELECT
	OPER.ID_INDIVIDUO, ROUND(AVG(OPER.PIPS)) AVGPIPS, MAX(OPER.PIPS) MAXPIPS, MIN(OPER.PIPS) MINPIPS, COUNT(*)
FROM	OPERACION OPER
	INNER JOIN PROCESO P ON OPER.ID_INDIVIDUO=P.ID_INDIVIDUO AND P.FECHA_HISTORICO>TO_DATE('20150101', 'YYYYMMDD')
	WHERE OPER.PIPS<0
	--OPER.ID_INDIVIDUO='1477365411387.7303'
GROUP BY OPER.ID_INDIVIDUO
HAVING
	AVG(OPER.PIPS)>-200
	--AND (MIN(OPER.PIPS)>-200 AND MIN(OPER.PIPS)<0) 
	;	
	
