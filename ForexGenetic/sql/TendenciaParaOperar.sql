SELECT * FROM TENDENCIA_PARA_OPERAR TPO
--ORDER BY FECHA_BASE DESC;
ORDER BY FECHA DESC;

SELECT * FROM DATO_ADICIONAL_TPO
ORDER BY FECHA ASC
--ORDER BY FECHA_BASE DESC
;

SELECT * FROM DATO_ADICIONAL_TPO
WHERE FECHA_BASE>=TO_DATE('2017/04/24 11:59', 'YYYY/MM/DD HH24:MI')
ORDER BY FECHA_BASE ASC;

SELECT * FROM DATO_ADICIONAL_TPO 
--WHERE PENDIENTE_PROMEDIO<0 AND (NUM_PENDIENTES_NEGATIVAS-NUM_PENDIENTES_POSITIVAS)>5
WHERE DIFF_PRECIO_EXTREMO_SUPERIOR<0 OR DIFF_PRECIO_EXTREMO_INFERIOR<0
ORDER BY FECHA_BASE ASC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.ID_EJECUCION in ('1510366015457')
ORDER BY FECHA_BASE DESC, PERIODO DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('2017/04/24 10:59', 'YYYY/MM/DD HH24:MI')
--AND TPO.TIPO_OPERACION='SELL' AND PERIODO NOT LIKE 'EXTREMO%'
ORDER BY FECHA_BASE ASC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.VIGENCIA_HIGHER>TO_DATE('2017.10.17 00:00', 'YYYY/MM/DD HH24:MI')
AND TPO.TIPO_OPERACION='SELL' --AND PERIODO NOT LIKE 'EXTREMO%'
--AND PRECIO_CALCULADO BETWEEN 1.27160 AND 1.27350
ORDER BY FECHA_BASE ASC;


SELECT TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD') FEBASE, COUNT(*), 
	MIN(TPO.FECHA_BASE) MINFEBASE, MAX(TPO.FECHA_BASE) MAXFEBASE,
	MAX(TPO.FECHA) MAXFECHA
FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('20170101', 'YYYYMMDD')
GROUP BY TO_CHAR(TPO.FECHA_BASE, 'YYYYMMDD')
ORDER BY MAX(TPO.FECHA) DESC;

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

DELETE 
--SELECT * 
FROM TENDENCIA_PARA_OPERAR TOP 
WHERE NOT EXISTS (SELECT TEN.FECHA_BASE FROM TENDENCIA TEN WHERE TOP.FECHA_BASE=TEN.FECHA_BASE)
AND TOP.FECHA_BASE>TO_DATE('2017/04/24 12:13', 'YYYY/MM/DD HH24:MI')
;

--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=0;
UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=0 WHERE PERIODO='EXTREMO_SINFILTRAR' AND ACTIVA=1;

--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=1;
--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=1 WHERE ACTIVA IS NULL;
--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=0 WHERE PERIODO='EXTREMO_SINFILTRAR' AND ACTIVA=1;

--UPDATE TENDENCIA_PARA_OPERAR SET LOTE=0.01;
--UPDATE TENDENCIA_PARA_OPERAR SET LOTE_CALCULADO=LOTE;
