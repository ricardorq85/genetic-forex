UPDATE DATO_ADICIONAL_TPO TPO SET FACTOR_DATOS=(
	SELECT MAX(TPO2.CANTIDAD_FILTRADA)/(24*13+1)
	FROM TENDENCIA_PARA_OPERAR TPO2 WHERE TPO.FECHA_BASE=TPO2.FECHA_BASE 
	AND TPO2.TIPO_TENDENCIA NOT LIKE 'EXTREMO%'
	)
--WHERE FACTOR_DATOS IS NULL
;
COMMIT;

UPDATE TENDENCIA_PARA_OPERAR TPO 
	SET LOTE=GREATEST(NVL((SELECT ROUND(FACTOR_DATOS/10,2) FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE),0),0.01)
;
COMMIT;

SELECT 'FACTOR DATOS', SYSDATE FROM DUAL;

--SELECT FECHA_BASE, FACTOR_DATOS, ROUND(FACTOR_DATOS/10,2), ROUND(FACTOR_DATOS/10,5) FROM DATO_ADICIONAL_TPO DATPO 
--ORDER BY FACTOR_DATOS ASC;

---------------------------------------------------------------------------------

UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	PRECIO_CALCULADO=(SELECT MIN_EXTREMO_FILTRADO FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	PRECIO_CALCULADO=(SELECT MAX_EXTREMO_FILTRADO FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	TAKE_PROFIT=(SELECT MAX_EXTREMO_INTERMEDIO FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	TAKE_PROFIT=(SELECT MIN_EXTREMO_INTERMEDIO FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	STOP_LOSS=(SELECT PRECIO_CALCULADO-GREATEST(DIFF_PRECIO_EXTREMO_SUPERIOR,200/100000) FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	STOP_LOSS=(SELECT PRECIO_CALCULADO+GREATEST(DIFF_PRECIO_EXTREMO_INFERIOR,200/100000) FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	STOP_APERTURA=(SELECT MAX_EXTREMO_FILTRADO FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET 
	STOP_APERTURA=(SELECT MIN_EXTREMO_FILTRADO FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='SELL';

COMMIT;

--------------------------------------------------------------------------------

--UPDATE TENDENCIA_PARA_OPERAR TPO SET LIMIT_APERTURA=PRECIO_CALCULADO;
UPDATE TENDENCIA_PARA_OPERAR TPO SET LIMIT_APERTURA=PRECIO_CALCULADO - LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.20*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
	----LEAST(1000/100000,(100/100000)*(NVL((SELECT (1/(FACTOR_DATOS)) FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1))/2)
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET LIMIT_APERTURA=PRECIO_CALCULADO + LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.20*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
	----LEAST(1000/100000,(100/100000)*(NVL((SELECT (1/(FACTOR_DATOS)) FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1))/2)
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO + LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.25*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO - LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.25*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET STOP_APERTURA=STOP_APERTURA - LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.15*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
	WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET STOP_APERTURA=STOP_APERTURA + LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.15*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
	WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET TAKE_PROFIT=TAKE_PROFIT - LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.15*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET TAKE_PROFIT=TAKE_PROFIT + LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.15*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET STOP_LOSS=STOP_LOSS - LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.25*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET STOP_LOSS=STOP_LOSS + LEAST(1000/100000,(100/100000)*(NVL((SELECT (1-FACTOR_DATOS)*0.25*10 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
WHERE TPO.TIPO_OPERACION='SELL';


--UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO + LEAST(1000/100000,(100/100000)*(NVL((SELECT (1/(FACTOR_DATOS*1.2)) FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
--WHERE TPO.TIPO_OPERACION='BUY';
--UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO - LEAST(1000/100000,(100/100000)*(NVL((SELECT (1/(FACTOR_DATOS*1.2)) FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS>0),1/0.1)))
--WHERE TPO.TIPO_OPERACION='SELL';

--UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_PENDIENTE' AND LIMIT_APERTURA IS NOT NULL;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LIMIT_APERTURA IS NOT NULL;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE EXISTS (SELECT 1 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TPO.FECHA_BASE AND FACTOR_DATOS=0);
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE LIMIT_APERTURA IS NULL;

UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE PRECIO_CALCULADO>=TAKE_PROFIT AND TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE PRECIO_CALCULADO<=TAKE_PROFIT AND TIPO_OPERACION='SELL';

--UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE TIPO_TENDENCIA='MEJOR_PENDIENTE';

--UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE LOTE<>0.02;
UPDATE TENDENCIA_PARA_OPERAR SET LOTE=0.01;

--UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=0 
--	WHERE FECHA_BASE NOT BETWEEN TO_DATE('2016/02/01 00:00', 'YYYY/MM/DD HH24:MI')
--	AND TO_DATE('2016/04/01 00:00', 'YYYY/MM/DD HH24:MI');

COMMIT;

SELECT 'TENDECIAS ACTIVAS', SYSDATE, COUNT(*) FROM TENDENCIA_PARA_OPERAR WHERE ACTIVA=1;