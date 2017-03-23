SET echo OFF;
SET feedback off;
SET term off;
SET pagesize 0;
SET linesize 5000;

ALTER SESSION SET NLS_NUMERIC_CHARACTERS = '.,';

SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias6H.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*0.25/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
	FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;
SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias12H.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*0.5/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
		FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;
SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias1D.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*1/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
		FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;
SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias2D.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*2/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
		FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO		
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		--AND WEEK_MINUTES(TT.FECHA, TEN.FECHA_BASE)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;
SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias3D.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*3/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
		FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;
SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias5D.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*5/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
		FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;

SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\Tendencias10D.csv;
WITH TIEMPO_TENDENCIA AS (SELECT (24*10/24)*24*60 TIEMPO, TO_DATE('2015.03.12 04:58','YYYY/MM/DD HH24:MI') FECHA,
		'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3,
		1020 PIPS_BASE
		FROM DUAL),
	BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA)
	SELECT 	'FECHA_TENDENCIA='||TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00,PRECIO_CALCULADO='
		--'FECHA_TENDENCIA='||TO_CHAR(GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120), 'YYYY.MM.DD HH24:MI')||',PRECIO_CALCULADO='
		||ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5)
	FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
		WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.TIEMPO
		--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO
		AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
		--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
		AND TEN.FECHA_TENDENCIA>TT.FECHA
		--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
		--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
	GROUP BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120)
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')
	ORDER BY 
		--GROUP_BY_MINUTES(TEN.FECHA_TENDENCIA, 120) ASC
		TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24') ASC
;
SPOOL OFF;
/*
SPOOL c:\Users\USER\AppData\Roaming\MetaQuotes\Terminal\Common\Files\estrategias\TendenciasTotal.csv;
WITH TIEMPO_TENDENCIA AS (
	SELECT (24*0.25/24)*24*60 T6H, (24*0.5/24)*24*60 T12H, (24*1/24)*24*60 T1D,
		(24*2/24)*24*60 T2D, (24*3/24)*24*60 T3D, (24*5/24)*24*60 T5D, (24*10/24)*24*60 T10D,
		0.05 PORC6H, 0.05 PORC12H, 0.1 PORC1D, 0.2 PORC2D, 0.2 PORC3D, 0.2 PORC5D, 0.2 PORC10D,
		--0.4 PORC6H, 0.3 PORC12H, 0.1 PORC1D, 0.1 PORC2D, 0.05 PORC5D, 0.05 PORC10D,
			1020 PIPS_BASE,
			TO_DATE('2015.03.12 04:58', 'YYYY/MM/DD HH24:MI') FECHA,
			'xBUY_SELL_20170131-3' TIPO_TENDENCIA1, 'xBUY_SELL_20170125' TIPO_TENDENCIA2, 'BUY_SELL_20170204-2' TIPO_TENDENCIA3 
			FROM DUAL),
		BASE_TENDENCIA AS (SELECT AVG(TEN.PRECIO_BASE) PRECIO_BASE
						FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN WHERE TEN.FECHA_BASE=TT.FECHA),
		TENDENCIA_6H AS (SELECT TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
					ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
					ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
				FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
					WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
					AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T6H
					--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T6H
					AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
					AND TEN.FECHA_TENDENCIA>TT.FECHA
					--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
					----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
				GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')),
		TENDENCIA_12H AS (SELECT TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
					ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
					ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
				FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
					WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
					AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T12H
					--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T12H
					AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
					AND TEN.FECHA_TENDENCIA>TT.FECHA
					--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
					----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
				GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')),
		TENDENCIA_1D AS (SELECT TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
					ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
					ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
				FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
					WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
					AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T1D
					--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T1D
					AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
					AND TEN.FECHA_TENDENCIA>TT.FECHA
					--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
					----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
				GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')),
		TENDENCIA_2D AS (SELECT 	TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
					ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
					ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
				FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
					WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
					AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T2D
					--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T2D
					AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
					AND TEN.FECHA_TENDENCIA>TT.FECHA
					--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
					----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
				GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')),
		TENDENCIA_3D AS (SELECT 	TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
					ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
					ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
				FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
					WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
					AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T3D
					--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T3D
					AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					AND TEN.FECHA_TENDENCIA>TT.FECHA
					--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
					----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
				GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')),				
		TENDENCIA_5D AS (SELECT 	TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
					ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
					ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
				FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
					WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
					AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T5D
					--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T5D
					AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
					AND TEN.FECHA_TENDENCIA>TT.FECHA
					--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
					----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
					--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
				GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')),
	TENDENCIA_10D AS (SELECT 	TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24')||':00' FECHA_TENDENCIA,
				ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
				ROUND(AVG(TEN.PROBABILIDAD),2) PROBABILIDAD
			FROM BASE_TENDENCIA BASE, TIEMPO_TENDENCIA TT, TENDENCIA TEN
				WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
				AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T10D
				--AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TT.FECHA)<TT.T10D
				AND TEN.FECHA_BASE<=TT.FECHA --AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
				--AND (TT.FECHA-TEN.FECHA_BASE)/24<1
				AND TEN.FECHA_TENDENCIA>TT.FECHA
				--AND ABS(TEN.PRECIO_BASE-BASE.PRECIO_BASE)*10000<TT.PIPS_BASE
				----AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)
				--AND TRUNC(TEN.FECHA_TENDENCIA, 'HH24')>TRUNC(TT.FECHA, 'HH24')
			GROUP BY TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYY.MM.DD HH24'))			
SELECT 'FECHA_TENDENCIA='||(TEN10D.FECHA_TENDENCIA)||',PRECIO_CALCULADO='
		||ROUND((NVL(TEN6H.PRECIO_CALCULADO,MULTIPLE_NVL(TEN12H.PRECIO_CALCULADO,TEN1D.PRECIO_CALCULADO,TEN2D.PRECIO_CALCULADO,TEN3D.PRECIO_CALCULADO,NVL(TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)))*TT.PORC6H
			+NVL(TEN12H.PRECIO_CALCULADO,MULTIPLE_NVL(TEN1D.PRECIO_CALCULADO,TEN2D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO))*TT.PORC12H
			+MULTIPLE_NVL(TEN1D.PRECIO_CALCULADO,TEN2D.PRECIO_CALCULADO,TEN3D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)*TT.PORC1D
			+MULTIPLE_NVL(TEN2D.PRECIO_CALCULADO,TEN3D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)*TT.PORC2D
			+MULTIPLE_NVL(TEN3D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)*TT.PORC3D
			+NVL(TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)*TT.PORC5D
			+TEN10D.PRECIO_CALCULADO*TT.PORC10D),5) POND
FROM TIEMPO_TENDENCIA TT, TENDENCIA_10D TEN10D
	LEFT JOIN TENDENCIA_6H TEN6H ON TEN10D.FECHA_TENDENCIA=TEN6H.FECHA_TENDENCIA
	LEFT JOIN TENDENCIA_12H TEN12H ON TEN10D.FECHA_TENDENCIA=TEN12H.FECHA_TENDENCIA
	LEFT JOIN TENDENCIA_1D TEN1D ON TEN10D.FECHA_TENDENCIA=TEN1D.FECHA_TENDENCIA
	LEFT JOIN TENDENCIA_2D TEN2D ON TEN10D.FECHA_TENDENCIA=TEN2D.FECHA_TENDENCIA
	LEFT JOIN TENDENCIA_3D TEN3D ON TEN10D.FECHA_TENDENCIA=TEN3D.FECHA_TENDENCIA
	LEFT JOIN TENDENCIA_5D TEN5D ON TEN10D.FECHA_TENDENCIA=TEN5D.FECHA_TENDENCIA
ORDER BY TO_DATE(TEN10D.FECHA_TENDENCIA, 'YYYY/MM/DD HH24:MI') ASC
;
SPOOL OFF;
*/
/
--ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.';
--SET echo ON; 
--/