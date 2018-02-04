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

UPDATE TENDENCIA_PARA_OPERAR TPO SET LIMIT_APERTURA=PRECIO_CALCULADO;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+500/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.01;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-500/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.01;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.01;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+450/100000,TAKE_PROFIT=TAKE_PROFIT+50/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.02;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-450/100000,TAKE_PROFIT=TAKE_PROFIT-50/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.02;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.02;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+400/100000,TAKE_PROFIT=TAKE_PROFIT+60/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.03;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-400/100000,TAKE_PROFIT=TAKE_PROFIT-60/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.03;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.03;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+350/100000,TAKE_PROFIT=TAKE_PROFIT+70/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.04;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-350/100000,TAKE_PROFIT=TAKE_PROFIT-70/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.04;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.04;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+300/100000,TAKE_PROFIT=TAKE_PROFIT+80/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.05;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-300/100000,TAKE_PROFIT=TAKE_PROFIT-80/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.05;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.05;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+250/100000,TAKE_PROFIT=TAKE_PROFIT+90/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.06;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-250/100000,TAKE_PROFIT=TAKE_PROFIT-90/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.06;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.06;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+200/100000,TAKE_PROFIT=TAKE_PROFIT+100/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.07;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-200/100000,TAKE_PROFIT=TAKE_PROFIT-100/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.07;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.07;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+150/100000,TAKE_PROFIT=TAKE_PROFIT+120/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.08;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-150/100000,TAKE_PROFIT=TAKE_PROFIT-120/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.08;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.08;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+100/100000,TAKE_PROFIT=TAKE_PROFIT+150/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.09;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-100/100000,TAKE_PROFIT=TAKE_PROFIT-150/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.09;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.09;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+90/100000,TAKE_PROFIT=TAKE_PROFIT+200/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.1;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-90/100000,TAKE_PROFIT=TAKE_PROFIT-200/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.1;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.1;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+80/100000,TAKE_PROFIT=TAKE_PROFIT+250/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.11;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-80/100000,TAKE_PROFIT=TAKE_PROFIT-250/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.11;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.11;

UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO+70/100000,TAKE_PROFIT=TAKE_PROFIT+250/100000
WHERE TPO.TIPO_OPERACION='BUY' AND LOTE=0.12;
UPDATE TENDENCIA_PARA_OPERAR TPO SET PRECIO_CALCULADO=PRECIO_CALCULADO-70/100000,TAKE_PROFIT=TAKE_PROFIT-250/100000
WHERE TPO.TIPO_OPERACION='SELL' AND LOTE=0.12;
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=1 WHERE TIPO_TENDENCIA='MEJOR_TENDENCIA' AND LOTE=0.12;

UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE LIMIT_APERTURA IS NULL;

UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE PRECIO_CALCULADO>=TAKE_PROFIT AND TIPO_OPERACION='BUY';
UPDATE TENDENCIA_PARA_OPERAR TPO SET ACTIVA=0 WHERE PRECIO_CALCULADO<=TAKE_PROFIT AND TIPO_OPERACION='SELL';

--UPDATE TENDENCIA_PARA_OPERAR TPO SET LOTE=0.01;

COMMIT;