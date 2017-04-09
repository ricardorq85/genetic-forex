--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_ESTADISTICAS', '2009/04/24 22:51', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_INDIVIDUO_OPTIMO', '2009/01/01 00:00', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_INICIO_TENDENCIA', '2009/01/01 00:00', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('STEP_TENDENCIA', '5', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('PERIODO_OPTIMOS', '10080', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_INICIO_PROCESAR_TENDENCIA', '2012/06/04 06:00', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('STEP_PROCESAR_TENDENCIA', '1440', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('SN_UPDATE_TENDENCIA', 'TRUE', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('RANGO_MAX_MIN_TENDENCIA', '7200', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('INDIVIDUOS_X_TENDENCIA', '1000', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('RETROCESO_ESTADISTICAS', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('DURACION_ESTADISTICAS', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_INDIVIDUO_PERIODO', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_FIN_INDIVIDUO_PERIODO', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('MESES_INDIVIDUO_PERIODO', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('MESES_RANGOOPERACIONINDICADOR', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('RETROCESO_RANGOOPERACIONINDICADOR', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('PIPS_RANGOOPERACIONINDICADOR', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('DIAS_ROTACION_INDIVIDUO_PERIODO', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('MONEDA', 'EURJPY', SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('MINIMO_INCLUSIONES', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('CANTIDAD_MUTAR', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('CANTIDAD_CRUZAR', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('TIPOS_OPERACION', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('DIAS_INDIVIDUO_PERIODO', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_FIN_PROCESAR_TENDENCIA', NULL, SYSDATE);
--INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('TIPO_EXPORTACION_TENDENCIA', NULL, SYSDATE);

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2016/08/27 23:59' WHERE NOMBRE='FECHA_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2016/11/19 23:00' WHERE NOMBRE='FECHA_FIN_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='364' WHERE NOMBRE='DIAS_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='1000' WHERE NOMBRE='MINIMO_INCLUSIONES';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='7' WHERE NOMBRE='DIAS_ROTACION_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='SELL,BUY' WHERE NOMBRE='TIPOS_OPERACION';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='3' WHERE NOMBRE='MESES_RANGOOPERACIONINDICADOR';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='1000' WHERE NOMBRE='RETROCESO_RANGOOPERACIONINDICADOR';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='500' WHERE NOMBRE='PIPS_RANGOOPERACIONINDICADOR';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2016/02/01 00:00' WHERE NOMBRE='FECHA_MINIMA_CREAR_INDIVIDUO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2016/10/26 23:59' WHERE NOMBRE='FECHA_MAXIMA_CREAR_INDIVIDUO';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='10' WHERE NOMBRE='CANTIDAD_MUTAR';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='10' WHERE NOMBRE='CANTIDAD_CRUZAR';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='USDCAD' WHERE NOMBRE='MONEDA';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2017/04/07 23:56' WHERE NOMBRE='FECHA_INICIO_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='30' WHERE NOMBRE='STEP_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='196' WHERE NOMBRE='INDIVIDUOS_X_TENDENCIA';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2017.03.31 00:00' WHERE NOMBRE='FECHA_INICIO_PROCESAR_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2017.04.08 00:00' WHERE NOMBRE='FECHA_FIN_PROCESAR_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='9' WHERE NOMBRE='STEP_PROCESAR_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager' 
--UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='forex.genetic.tendencia.manager.ExportarTendenciaFiltradaManager' 
--UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='forex.genetic.tendencia.manager.ExportarTendenciaFiltroFechaTendenciaFechaBaseManager' 
--UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='forex.genetic.tendencia.manager.ExportarTendenciaFiltroFechaTendenciaFechaCierreManager' 
--UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='forex.genetic.tendencia.manager.ExportarTendenciaCrucesManager' 
WHERE NOMBRE='TIPO_EXPORTACION_TENDENCIA';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='FALSE' WHERE NOMBRE='SN_UPDATE_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='1080' WHERE NOMBRE='RANGO_MAX_MIN_TENDENCIA';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR=NULL WHERE NOMBRE='RETROCESO_ESTADISTICAS';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='1' WHERE NOMBRE='DURACION_ESTADISTICAS';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR=null --'1422746069706.38' 
WHERE NOMBRE='INDIVIDUOS_TENDENCIA';

UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2016/12/31 23:59' WHERE NOMBRE='FECHA_ESTADISTICAS';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='1440' WHERE NOMBRE='PERIODO_OPTIMOS';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2012/01/02 00:00' WHERE NOMBRE='FECHA_INDIVIDUO_OPTIMO';

--COMMIT;

SELECT NOMBRE, VALOR, FECHA FROM PARAMETRO ORDER BY FECHA DESC;

