SELECT * FROM TENDENCIA_PARA_OPERAR TPO
ORDER BY FECHA_BASE DESC;
--ORDER BY FECHA DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.ID_EJECUCION='1499623306945'
ORDER BY FECHA_BASE DESC, PERIODO DESC;

SELECT * FROM TENDENCIA_PARA_OPERAR TPO
WHERE TPO.FECHA_BASE>=TO_DATE('2017/05/31 21:59', 'YYYY/MM/DD HH24:MI')
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

--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=1;
UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=1 WHERE ACTIVA IS NULL;
--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=0 WHERE PERIODO='EXTREMO_SINFILTRAR';