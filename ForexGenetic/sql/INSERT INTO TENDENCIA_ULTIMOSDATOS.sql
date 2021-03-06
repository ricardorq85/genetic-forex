INSERT INTO TENDENCIA_ULTIMOSDATOS(FECHA_BASE, PRECIO_BASE, ID_INDIVIDUO, FECHA_TENDENCIA, PIPS,
					 PRECIO_CALCULADO, TIPO_TENDENCIA, FECHA_APERTURA, OPEN_PRICE,
					 DURACION, PIPS_ACTUALES, DURACION_ACTUAL,
					 PROBABILIDAD_POSITIVOS, PROBABILIDAD_NEGATIVOS, PROBABILIDAD,
					 FECHA, FECHA_CIERRE, TIPO_CALCULO, PIPS_REALES) 
 SELECT FECHA_BASE, PRECIO_BASE, ID_INDIVIDUO, FECHA_TENDENCIA, PIPS,
					 PRECIO_CALCULADO, TIPO_TENDENCIA, FECHA_APERTURA, OPEN_PRICE,
					 DURACION, PIPS_ACTUALES, DURACION_ACTUAL,
					 PROBABILIDAD_POSITIVOS, PROBABILIDAD_NEGATIVOS, PROBABILIDAD,
					 FECHA, FECHA_CIERRE, TIPO_CALCULO, PIPS_REALES
	FROM TENDENCIA WHERE FECHA_BASE BETWEEN 
	TO_DATE('2017/09/01 00:00', 'YYYY/MM/DD HH24:MI') AND TO_DATE('2017/11/26 23:59', 'YYYY/MM/DD HH24:MI')
 ;
 
commit;