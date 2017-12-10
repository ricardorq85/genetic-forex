WITH
PARAMETROS AS
	(SELECT
				--:PARAM_PERIODO 
		'XD' PERIODO,
		--:PARAM_TIEMPO_TENDENCIA 
		(1440*3) TIEMPO_TENDENCIA,
		(1440*10) TIEMPO_COMPARACION,
		--TO_DATE(:PARAM_FECHA_PROCESO, 'YYYY/MM/DD HH24:MI') FECHA_PROCESO,
		TO_DATE('2017.02.09 16:00', 'YYYY/MM/DD HH24:MI') FECHA_PROCESO,
		--:PARAM_TIPO_TENDENCIA 
		'BUY_SELL_20170204-2' TIPO_TENDENCIA 
	FROM
		DUAL
	),
TENDENCIA_COMPARACION AS
	(SELECT
		PARAM.FECHA_PROCESO FECHA_BASE, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA, ROUND(SUM(
		TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO, COUNT(*)
		CANTIDAD, MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
	FROM
		PARAMETROS PARAM, TENDENCIA TEN
	WHERE
		TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<PARAM.TIEMPO_COMPARACION
		AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO
		AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO
	GROUP BY
		TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO
	),
TENDENCIA_CALCULADA AS
	(SELECT
		PARAM.FECHA_PROCESO FECHA_BASE, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA, ROUND(SUM(
		TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO, COUNT(*)
		CANTIDAD, MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
	FROM
		PARAMETROS PARAM, TENDENCIA TEN
	WHERE
		TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA
		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<PARAM.TIEMPO_TENDENCIA
		AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO
		AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO
	GROUP BY
		TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO
	),
PROMEDIOS AS
	(SELECT ROUND(AVG(TEN.CANTIDAD)) AVGCANTIDAD FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TEN
	), 
REGRESION AS
	(SELECT
		ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2, ROUND(
		REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE, MIN(
		TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA, MAX(
		TEN.PRECIO_CALCULADO) MAXPRECIO, MIN(TEN.PRECIO_CALCULADO) MINPRECIO, COUNT(*)
		CANTIDAD_TENDENCIAS
	FROM
		PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_CALCULADA TEN
	),
MAXIMO_MINIMO AS (SELECT MIN(TEN.PRECIO_CALCULADO) MINPRECIO, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO
	FROM TENDENCIA_CALCULADA TEN
),
MAXIMO_MINIMO_COMPARACION AS (SELECT MIN(TEN.PRECIO_CALCULADO) MINPRECIO, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO
	FROM PARAMETROS PARAM, TENDENCIA_COMPARACION TEN 
	WHERE WEEK_MINUTES(TEN.FECHA_TENDENCIA,PARAM.FECHA_PROCESO)<PARAM.TIEMPO_TENDENCIA
)
SELECT REG.R2, REG.PENDIENTE, REG.MINFETENDENCIA, REG.MAXFETENDENCIA, MAXMIN.MINPRECIO, MAXMIN.MAXPRECIO
FROM REGRESION REG, MAXIMO_MINIMO MAXMIN, MAXIMO_MINIMO_COMPARACION MAXMINCOMP
WHERE (REG.PENDIENTE<0 AND MAXMIN.MINPRECIO>MAXMINCOMP.MAXPRECIO)
	OR (REG.PENDIENTE>0 AND MAXMIN.MINPRECIO<MAXMINCOMP.MAXPRECIO)
	;
