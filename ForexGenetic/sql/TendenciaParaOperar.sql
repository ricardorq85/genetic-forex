SELECT * FROM TENDENCIA_PARA_OPERAR TPO
ORDER BY FECHA_BASE DESC,ACTIVA ASC, TIPO_OPERACION ASC, PERIODO ASC
--ORDER BY FECHA DESC
;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('2017/04/03 23:00', 'YYYY/MM/DD HH24:MI')
--AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
AND PERIODO NOT LIKE '%FILTRAR' --AND TPO.ACTIVA=1
--AND TPO.FECHA>SYSDATE-0.1
ORDER BY 
--FECHA ASC,
FECHA_BASE aSC, ACTIVA ASC, TIPO_OPERACION ASC,
PERIODO ASC;

SELECT * FROM DATO_ADICIONAL_TPO
ORDER BY FECHA deSC
--ORDER BY FECHA_BASE DESC
;

SELECT * FROM DATO_ADICIONAL_TPO
WHERE FECHA_BASE>=TO_DATE('2017/01/16 00:01', 'YYYY/MM/DD HH24:MI')
--AND FECHA_BASE<=TO_DATE('2017/07/04 00:00', 'YYYY/MM/DD HH24:MI')
--AND DIFF_PRECIO_EXTREMO_SUPERIOR IS NOT NULL
ORDER BY FECHA_BASE ASC
--ORDER BY ABS(DIFF_PRECIO_EXTREMO_SUPERIOR) ASC
;

SELECT TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') FEBASE, COUNT(*), 
	MIN(TPO.FECHA_BASE) MINFEBASE, MAX(TPO.FECHA_BASE) MAXFEBASE,
	MAX(TPO.FECHA) MAXFECHA
FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('20171101', 'YYYYMMDD')
GROUP BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD')
--ORDER BY MAX(TPO.FECHA) DESC
ORDER BY MAX(TPO.FECHA_BASE) DESC
--ORDER BY COUNT(*) ASC
;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE<=TO_DATE('2017/05/18 00:00', 'YYYY/MM/DD HH24:MI')
--AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
AND PERIODO NOT LIKE '%FILTRAR'--AND TPO.ACTIVA=1 
AND TPO.FECHA>SYSDATE-1
ORDER BY 
--FECHA ASC,
FECHA_BASE DESC, ACTIVA ASC, TIPO_OPERACION ASC,
PERIODO ASC;

SELECT * FROM DATO_ADICIONAL_TPO 
WHERE DIFF_PRECIO_EXTREMO_SUPERIOR IS NOT NULL AND DIFF_PRECIO_EXTREMO_INFERIOR IS NOT NULL
AND (DIFF_PRECIO_EXTREMO_SUPERIOR*DIFF_PRECIO_EXTREMO_INFERIOR)<0
ORDER BY ABS(DIFF_PRECIO_EXTREMO_SUPERIOR-DIFF_PRECIO_EXTREMO_INFERIOR) ASC,
	ABS(DIFF_PRECIO_EXTREMO_SUPERIOR) ASC,
	ABS(DIFF_PRECIO_EXTREMO_INFERIOR) ASC;

SELECT * FROM DATO_ADICIONAL_TPO 
--WHERE PENDIENTE_PROMEDIO<0 AND (NUM_PENDIENTES_NEGATIVAS-NUM_PENDIENTES_POSITIVAS)>5
WHERE --DIFF_PRECIO_EXTREMO_SUPERIOR<=0 AND PENDIENTE_PROMEDIO<0
DIFF_PRECIO_EXTREMO_INFERIOR<=0 AND PENDIENTE_PROMEDIO>0
ORDER BY FECHA_BASE DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.ID_EJECUCION in ('1510366015457')
ORDER BY FECHA_BASE DESC, PERIODO DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.VIGENCIA_HIGHER>TO_DATE('2017.10.17 00:00', 'YYYY/MM/DD HH24:MI')
AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
--AND PRECIO_CALCULADO BETWEEN 1.27160 AND 1.27350
ORDER BY FECHA_BASE ASC;


SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.PERIODO='EXTREMO'
ORDER BY FECHA_BASE ASC;

SELECT TRUNC(FECHA_BASE, 'HH24') FROM TENDENCIA_PARA_OPERAR TPO;

SELECT COUNT(*) FROM TENDENCIA_PARA_OPERAR ;

--UPDATE TENDENCIA_PARA_OPERAR SET ID_EJECUCION='1';

SELECT TPO.FECHA_BASE, 
	TPO.PRECIO_CALCULADO, TPO.TAKE_PROFIT, DH.HIGH VALOR_COMPRA,
	TPO.MIN_PRECIO, TPO.MAX_PRECIO,
	CASE TPO.TIPO_OPERACION
		WHEN 'SELL' THEN ROUND(((TPO.MAX_PRECIO-DH.HIGH)/(DH.HIGH-TPO.MIN_PRECIO)), 5)
		WHEN 'BUY' THEN	ROUND(((TPO.MAX_PRECIO-DH.HIGH)/(DH.HIGH-TPO.MIN_PRECIO)), 5)
	END RELACION, 
	TPO.TIPO_OPERACION
FROM TENDENCIA_PARA_OPERAR TPO
INNER JOIN DATOHISTORICO DH ON DH.FECHA=TPO.FECHA_BASE
WHERE TPO.PERIODO<>'EXTREMO'
--AND TPO.FECHA_BASE BETWEEN TO_DATE('2017/04/25 00:00', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2017/06/16 00:00', 'YYYY/MM/DD HH24:MI')
AND TPO.FECHA_BASE BETWEEN TO_DATE('2017.02.27 13:00', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2017.02.27 15:12', 'YYYY/MM/DD HH24:MI')
ORDER BY TPO.FECHA_BASE ASC;

SELECT TPO.FECHA_BASE, COUNT(*) FROM TENDENCIA_PARA_OPERAR TPO 
WHERE TPO.PERIODO='EXTREMO'
GROUP BY TPO.FECHA_BASE HAVING COUNT(*)=2
ORDER BY TPO.FECHA_BASE DESC
;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO  WHERE  TPO.ACTIVA=1 ORDER BY TPO.FECHA_BASE ASC;