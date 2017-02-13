ALTER SESSION SET NLS_NUMERIC_CHARACTERS = '.,';
/
DECLARE
	V_T1 VARCHAR2(1000);
	PARAM_DATE DATE;
	MAX_DATE DATE;
	CURSOR C1 (V_DATE DATE) IS
		WITH TIEMPO_TENDENCIA AS (
			SELECT (120) T2H, (24*0.25/24)*24*60 T6H, (24*0.5/24)*24*60 T12H, (24*1/24)*24*60 T1D,
				(24*2/24)*24*60 T2D, (24*5/24)*24*60 T5D, (24*10/24)*24*60 T10D,
				0.2 R2_MINIMO, 0.0001 PENDIENTE_MINIMA,
				0.1 PORC6H, 0.1 PORC12H, 0.1 PORC1D, 0.2 PORC2D, 0.2 PORC5D, 0.3 PORC10D,
				(60) DIASPROYECCION, (24*5/24)*24*60 TIEMPO_REGRESION, (24*2/24)*24*60 TIEMPO_MAX_MIN,
							V_DATE FECHA,
							'BUY_SELL_20170204-2' TIPO_TENDENCIA1, NULL TIPO_TENDENCIA2, NULL TIPO_TENDENCIA3 
							FROM DUAL),
						TENDENCIA_6H AS (SELECT '6H' PERIODO, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,
									ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
									COUNT(*) CANTIDAD,
									MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
								FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN
									WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
									AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T6H									
									AND TEN.FECHA_BASE<=TT.FECHA
									AND TEN.FECHA_TENDENCIA>TT.FECHA									
								GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24')),
						TENDENCIA_12H AS (SELECT '12H' PERIODO, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,
									ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
									COUNT(*) CANTIDAD, 
									MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
								FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN
									WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
									AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T12H
									AND TEN.FECHA_BASE<=TT.FECHA
									AND TEN.FECHA_TENDENCIA>TT.FECHA									
								GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24')),
						TENDENCIA_1D AS (SELECT '1D' PERIODO, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,
									ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
									COUNT(*) CANTIDAD, 
									MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
								FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN
									WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
									AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T1D									
									AND TEN.FECHA_BASE<=TT.FECHA
									AND TEN.FECHA_TENDENCIA>TT.FECHA									
								GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24')),
						TENDENCIA_2D AS (SELECT '2D' PERIODO, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,
									ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
									COUNT(*) CANTIDAD, 
									MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
								FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN
									WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
									AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T2D									
									AND TEN.FECHA_BASE<=TT.FECHA
									AND TEN.FECHA_TENDENCIA>TT.FECHA									
								GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24')),
						TENDENCIA_5D AS (SELECT '5D' PERIODO, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,
									ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
									COUNT(*) CANTIDAD, 
									MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
								FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN
									WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
									AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T5D									
									AND TEN.FECHA_BASE<=TT.FECHA
									AND TEN.FECHA_TENDENCIA>TT.FECHA
								GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24')),
				TENDENCIA_10D AS (SELECT '10D' PERIODO, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,
						ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,
						COUNT(*) CANTIDAD,
						MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA
					FROM TIEMPO_TENDENCIA TT, TENDENCIA TEN
						WHERE TEN.TIPO_TENDENCIA IN (TIPO_TENDENCIA1,TIPO_TENDENCIA2,TIPO_TENDENCIA3)
						AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<TT.T10D
						
						AND TEN.FECHA_BASE<=TT.FECHA
						AND TEN.FECHA_TENDENCIA>TT.FECHA
					GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24')),
					PONDERADO AS (SELECT 'TOTAL' PERIODO, TEN10D.FECHA_TENDENCIA FECHA_TENDENCIA,
								(ROUND((NVL(TEN6H.PRECIO_CALCULADO,MULTIPLE_NVL(TEN12H.PRECIO_CALCULADO,TEN1D.PRECIO_CALCULADO,TEN2D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO))*TT.PORC6H
								+MULTIPLE_NVL(TEN12H.PRECIO_CALCULADO,TEN1D.PRECIO_CALCULADO,TEN2D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)*TT.PORC12H
								+MULTIPLE_NVL(TEN1D.PRECIO_CALCULADO,TEN2D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO,NULL)*TT.PORC1D
								+MULTIPLE_NVL(TEN2D.PRECIO_CALCULADO,TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO,NULL)*TT.PORC2D
								+NVL(TEN5D.PRECIO_CALCULADO,TEN10D.PRECIO_CALCULADO)*TT.PORC5D
								+TEN10D.PRECIO_CALCULADO*TT.PORC10D),5)) PRECIO_CALCULADO,
								TEN10D.CANTIDAD CANTIDAD, TEN10D.MINFETENDENCIA, TEN10D.MAXFETENDENCIA
						FROM TIEMPO_TENDENCIA TT, TENDENCIA_10D TEN10D
							LEFT JOIN TENDENCIA_6H TEN6H ON TEN10D.FECHA_TENDENCIA=TEN6H.FECHA_TENDENCIA
							LEFT JOIN TENDENCIA_12H TEN12H ON TEN10D.FECHA_TENDENCIA=TEN12H.FECHA_TENDENCIA
							LEFT JOIN TENDENCIA_1D TEN1D ON TEN10D.FECHA_TENDENCIA=TEN1D.FECHA_TENDENCIA
							LEFT JOIN TENDENCIA_2D TEN2D ON TEN10D.FECHA_TENDENCIA=TEN2D.FECHA_TENDENCIA
							LEFT JOIN TENDENCIA_5D TEN5D ON TEN10D.FECHA_TENDENCIA=TEN5D.FECHA_TENDENCIA),
					REGRESION AS (SELECT ROUND(REGR_R2(POND.PRECIO_CALCULADO, POND.FECHA_TENDENCIA-TT.FECHA),5) R2_POND,
							ROUND(REGR_SLOPE(POND.PRECIO_CALCULADO, POND.FECHA_TENDENCIA-TT.FECHA),5) PENDIENTE_POND,
							MIN(POND.FECHA_TENDENCIA) MINFETENDENCIA, MAX(POND.FECHA_TENDENCIA) MAXFETENDENCIA
							FROM TIEMPO_TENDENCIA TT, PONDERADO POND
							WHERE WEEK_MINUTES(POND.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO_REGRESION
							ORDER BY POND.FECHA_TENDENCIA ASC
							),							
					UNION_TENDENCIAS AS (SELECT * FROM TENDENCIA_6H TEN
															UNION ALL SELECT * FROM TENDENCIA_12H TEN
															UNION ALL SELECT * FROM TENDENCIA_1D TEN
															UNION ALL SELECT * FROM TENDENCIA_2D TEN
															UNION ALL SELECT * FROM TENDENCIA_5D TEN
															UNION ALL SELECT * FROM TENDENCIA_10D TEN
															UNION ALL SELECT * FROM PONDERADO TEN),
					OPERAR_BUY AS (
						SELECT TEN.PERIODO, TEN.FECHA_TENDENCIA FECHA_TENDENCIA, TEN.PRECIO_CALCULADO, 
							(SELECT MAX(POND.PRECIO_CALCULADO) FROM TIEMPO_TENDENCIA TT, PONDERADO POND 
								WHERE POND.FECHA_TENDENCIA>=TEN.FECHA_TENDENCIA
								AND WEEK_MINUTES(POND.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO_MAX_MIN) TAKE_PROFIT, 
								LEAST((SELECT MIN(POND.PRECIO_CALCULADO) FROM PONDERADO POND 
											WHERE POND.FECHA_TENDENCIA>=TEN.FECHA_TENDENCIA AND WEEK_MINUTES(POND.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO_MAX_MIN), 
											TEN.PRECIO_CALCULADO-70/10000
											) STOP_LOSS,
							'BUY' TIPO_OPERACION
						FROM TIEMPO_TENDENCIA TT, REGRESION REG, UNION_TENDENCIAS TEN
						WHERE REG.PENDIENTE_POND>0 AND ABS(REG.PENDIENTE_POND)>TT.PENDIENTE_MINIMA AND REG.R2_POND>=TT.R2_MINIMO
						AND TEN.FECHA_TENDENCIA<=TT.FECHA+TT.DIASPROYECCION
					),
					OPERAR_SELL AS (
						SELECT TEN.PERIODO, TEN.FECHA_TENDENCIA FECHA_TENDENCIA, TEN.PRECIO_CALCULADO, 
							(SELECT MIN(POND.PRECIO_CALCULADO) FROM PONDERADO POND 
											WHERE POND.FECHA_TENDENCIA>=TEN.FECHA_TENDENCIA AND WEEK_MINUTES(POND.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO_MAX_MIN) TAKE_PROFIT, 
							GREATEST((SELECT MAX(POND.PRECIO_CALCULADO) FROM TIEMPO_TENDENCIA TT, PONDERADO POND 
								WHERE POND.FECHA_TENDENCIA>=TEN.FECHA_TENDENCIA
								AND WEEK_MINUTES(POND.FECHA_TENDENCIA,TT.FECHA)<TT.TIEMPO_MAX_MIN), 
								TEN.PRECIO_CALCULADO+70/10000
								) STOP_LOSS,
							'SELL' TIPO_OPERACION
						FROM TIEMPO_TENDENCIA TT, REGRESION REG, UNION_TENDENCIAS TEN
						WHERE REG.PENDIENTE_POND<0 AND ABS(REG.PENDIENTE_POND)>TT.PENDIENTE_MINIMA AND REG.R2_POND>=TT.R2_MINIMO
						AND TEN.FECHA_TENDENCIA<=TT.FECHA+TT.DIASPROYECCION
					),
					OPERAR AS (SELECT OP.*, 0.1 LOTE FROM (
							SELECT MIN(OPE.PERIODO) PERIODO, OPE.FECHA_TENDENCIA, OPE.PRECIO_CALCULADO,
								OPE.TAKE_PROFIT, MIN(OPE.STOP_LOSS) STOP_LOSS, OPE.TIPO_OPERACION
							FROM OPERAR_BUY OPE WHERE OPE.TAKE_PROFIT>OPE.PRECIO_CALCULADO
								GROUP BY OPE.FECHA_TENDENCIA, OPE.TIPO_OPERACION, OPE.PRECIO_CALCULADO, OPE.TAKE_PROFIT
							UNION ALL 
							SELECT MIN(OPE.PERIODO) PERIODO, OPE.FECHA_TENDENCIA, OPE.PRECIO_CALCULADO,
								OPE.TAKE_PROFIT, MAX(OPE.STOP_LOSS) STOP_LOSS, OPE.TIPO_OPERACION
							FROM OPERAR_SELL OPE WHERE OPE.TAKE_PROFIT<OPE.PRECIO_CALCULADO 
							GROUP BY OPE.FECHA_TENDENCIA, OPE.TIPO_OPERACION, OPE.PRECIO_CALCULADO, OPE.TAKE_PROFIT
						) OP
						WHERE ABS(OP.PRECIO_CALCULADO-OP.TAKE_PROFIT)*10000>20
						AND ABS(OP.PRECIO_CALCULADO-OP.STOP_LOSS)*10000>20
					)
					SELECT 'NAME='||OPERAR.PERIODO
							||',TIPO_OPERACION='||OPERAR.TIPO_OPERACION
							||',LOTE='||OPERAR.LOTE
							||',FECHA_TENDENCIA='||(TO_CHAR(OPERAR.FECHA_TENDENCIA, 'YYYY.MM.DD HH24:MI'))||',PRECIO_CALCULADO='
							||(OPERAR.PRECIO_CALCULADO)||',TAKE_PROFIT='||OPERAR.TAKE_PROFIT||',STOP_LOSS='||OPERAR.STOP_LOSS							
								||',VIGENCIALOWER='||(TO_CHAR(GREATEST(TT.FECHA,OPERAR.FECHA_TENDENCIA), 'YYYY.MM.DD HH24:MI'))||',VIGENCIAHIGHER='||(TO_CHAR(OPERAR.FECHA_TENDENCIA+(2/24+1/60/24), 'YYYY.MM.DD HH24:MI'))
								||',FECHA_BASE='||(TO_CHAR(TT.FECHA, 'YYYY.MM.DD HH24:MI'))
								||',R2='||REG.R2_POND||',PENDIENTE='||REG.PENDIENTE_POND
								||',WEEK_MINUTES='||WEEK_MINUTES(OPERAR.FECHA_TENDENCIA,TT.FECHA)
							VAL_TENDENCIA
					FROM TIEMPO_TENDENCIA TT, REGRESION REG, OPERAR OPERAR
					--WHERE WEEK_MINUTES(OPERAR.FECHA_TENDENCIA,TT.FECHA)>TT.T2H
					ORDER BY OPERAR.FECHA_TENDENCIA ASC;
BEGIN
	--DBMS_OUTPUT.PUT_LINE('CONSULTANDO...');
	PARAM_DATE:=TO_DATE('2017/02/07 05:57','YYYY/MM/DD HH24:MI');
	--PARAM_DATE:=TO_DATE('2017/01/19 14:06','YYYY/MM/DD HH24:MI');
	
	MAX_DATE:=TO_DATE('2017/02/09 14:46','YYYY/MM/DD HH24:MI');
	--MAX_DATE:=TO_DATE('2017/02/09 14:46','YYYY/MM/DD HH24:MI');
	WHILE ((PARAM_DATE <= MAX_DATE) AND (PARAM_DATE IS NOT NULL)) LOOP			
			FOR V_T1 IN C1((PARAM_DATE)) LOOP
				DBMS_OUTPUT.PUT_LINE(V_T1.VAL_TENDENCIA);
			END LOOP;
			DBMS_OUTPUT.PUT_LINE('PARAM_DATE: '||PARAM_DATE);
			--SELECT MIN(TEN.FECHA_BASE) INTO PARAM_DATE
			--FROM TENDENCIA TEN WHERE TEN.TIPO_TENDENCIA IN ('BUY_SELL_20170204-2')
			--AND TEN.FECHA_BASE>PARAM_DATE;

			SELECT MIN(T.FECHA_BASE) INTO PARAM_DATE FROM
			(SELECT TEN.FECHA_BASE, COUNT(*) FROM TENDENCIA TEN WHERE TEN.TIPO_TENDENCIA IN ('BUY_SELL_20170204-2')
				AND TEN.FECHA_BASE>PARAM_DATE+0/24
				GROUP BY TEN.FECHA_BASE
				HAVING COUNT(*)>1000) T
			;
	END LOOP;
END;
/
ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.';
/