SELECT TEN31.ID_INDIVIDUO, TEN31.FECHA_TENDENCIA, TEN31.PIPS, TEN31.PRECIO_CALCULADO, TEN31.PIPS_ACTUALES,
	TEN25.FECHA_TENDENCIA, TEN25.PIPS, TEN25.PRECIO_CALCULADO, TEN25.PIPS_ACTUALES,
	TEN31.PROBABILIDAD_POSITIVOS, TEN25.PROBABILIDAD_POSITIVOS,
	TEN31.PROBABILIDAD_NEGATIVOS, TEN25.PROBABILIDAD_NEGATIVOS,
	TEN31.*, TEN25.*
FROM TENDENCIA TEN31
	INNER JOIN TENDENCIA TEN25 ON TEN31.ID_INDIVIDUO=TEN25.ID_INDIVIDUO
WHERE TEN31.FECHA_BASE=TO_DATE('2017/01/27 23:56', 'YYYY/MM/DD HH24:MI')
AND TEN25.FECHA_BASE=TO_DATE('2017/01/27 23:58', 'YYYY/MM/DD HH24:MI')
AND TEN31.FECHA_TENDENCIA<=TO_DATE('2017/01/27 23:56', 'YYYY/MM/DD HH24:MI')+5
AND TEN25.FECHA_TENDENCIA<=TO_DATE('2017/01/27 23:58', 'YYYY/MM/DD HH24:MI')+5
AND TEN31.TIPO_TENDENCIA='BUY_SELL_20170201'		
AND TEN25.TIPO_TENDENCIA='BUY_SELL_20170125'		
AND TEN31.PRECIO_CALCULADO>1.31410
AND TEN25.PRECIO_CALCULADO<1.31410
ORDER BY ABS(TEN31.PRECIO_CALCULADO-TEN25.PRECIO_CALCULADO) DESC, TEN31.ID_INDIVIDUO ASC;

SELECT * FROM TENDENCIA TEN
WHERE --TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYYMMDD HH24')='20170130 10'
--AND 
TEN.FECHA_BASE=TO_DATE('2017/01/27 23:56', 'YYYY/MM/DD HH24:MI')
AND 
TEN.ID_INDIVIDUO IN(SELECT TEN.ID_INDIVIDUO FROM TENDENCIA TEN
								WHERE TO_CHAR(TEN.FECHA_TENDENCIA, 'YYYYMMDD HH24')='20170130 10'
								AND 
								TEN.FECHA_BASE=TO_DATE('2017/01/27 23:58', 'YYYY/MM/DD HH24:MI')
								AND TEN.TIPO_TENDENCIA=--'BUY_SELL_20170131-3'
										'BUY_SELL_20170125'
								AND TEN.PRECIO_CALCULADO<1.31410
	)
AND TEN.TIPO_TENDENCIA='BUY_SELL_20170201'
		--'BUY_SELL_20170125'
--AND TEN.PRECIO_CALCULADO<1.31410
ORDER BY TEN.ID_INDIVIDUO ASC
--ORDER BY TEN.FECHA_TENDENCIA ASC;