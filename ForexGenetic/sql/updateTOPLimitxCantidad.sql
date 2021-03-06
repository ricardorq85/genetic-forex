--UPDATE TENDENCIA_PARA_OPERAR TPO SET LIMIT_APERTURA=NULL;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,
	LIMIT_APERTURA=(SELECT MIN_EXTREMO_SINFILTRAR FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,
	LIMIT_APERTURA=(SELECT MAX_EXTREMO_SINFILTRAR FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='SELL';

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-500/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-500/100000>LIMIT_APERTURA)
AND LOTE=0.01;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, 
LIMIT_APERTURA=PRECIO_CALCULADO+500/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-500/100000<LIMIT_APERTURA)
AND LOTE=0.01;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.01;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-450/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-450/100000>LIMIT_APERTURA)
AND LOTE=0.02;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+450/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-450/100000<LIMIT_APERTURA)
AND LOTE=0.02;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.02;
--UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE LOTE<>0.02;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-400/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-400/100000>LIMIT_APERTURA)
AND LOTE=0.03;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+400/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-400/100000<LIMIT_APERTURA)
AND LOTE=0.03;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.03;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-350/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-350/100000>LIMIT_APERTURA)
AND LOTE=0.04;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+350/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-350/100000<LIMIT_APERTURA)
AND LOTE=0.04;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.04;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-300/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-300/100000>LIMIT_APERTURA)
AND LOTE=0.05;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+300/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-300/100000<LIMIT_APERTURA)
AND LOTE=0.05;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.05;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-250/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-250/100000>LIMIT_APERTURA)
AND LOTE=0.06;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+250/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-250/100000<LIMIT_APERTURA)
AND LOTE=0.06;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.06;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-200/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-200/100000>LIMIT_APERTURA)
AND LOTE=0.07;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+200/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-200/100000<LIMIT_APERTURA)
AND LOTE=0.07;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.07;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-150/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-150/100000>LIMIT_APERTURA)
AND LOTE=0.08;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+150/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-150/100000<LIMIT_APERTURA)
AND LOTE=0.08;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.08;

UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO-100/100000
WHERE TPO.TIPO_OPERACION='BUY' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-100/100000>LIMIT_APERTURA)
AND LOTE=0.09;
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, LIMIT_APERTURA=PRECIO_CALCULADO+100/100000
WHERE TPO.TIPO_OPERACION='SELL' AND (LIMIT_APERTURA IS NULL OR PRECIO_CALCULADO-100/100000<LIMIT_APERTURA)
AND LOTE=0.09;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.09;

/*UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, 
	LIMIT_APERTURA=(SELECT MIN_EXTREMO_SINFILTRAR FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='BUY'
AND LOTE IN (0.1, 0.11, 0.12);
UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, 
	LIMIT_APERTURA=(SELECT MAX_EXTREMO_SINFILTRAR FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
WHERE TPO.TIPO_OPERACION='SELL'
AND LOTE IN (0.1, 0.11, 0.12);
*/
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE IN (0.1, 0.11, 0.12);
--UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE LOTE<>0.12;

UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE LIMIT_APERTURA IS NULL;

UPDATE TENDENCIA_PARA_OPERAR TPO SET LOTE=0.01;

--UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA';

--UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, 
--	LIMIT_APERTURA=(SELECT MAX_EXTREMO_SINFILTRAR FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
	--WHERE TPO.TIPO_OPERACION='BUY';
	
--UPDATE TENDENCIA_PARA_OPERAR TPO SET UPDATE_TEMPORAL=1,VALOR_TEMPORAL=LIMIT_APERTURA, 
--	LIMIT_APERTURA=(SELECT MIN_EXTREMO_SINFILTRAR FROM DATO_ADICIONAL_TPO DATOP WHERE DATOP.FECHA_BASE=TPO.FECHA_BASE)
	--WHERE TPO.TIPO_OPERACION='SELL';
	
COMMIT;