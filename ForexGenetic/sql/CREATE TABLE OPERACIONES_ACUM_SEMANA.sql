CREATE TABLE OPERACIONES_ACUM_SEMANA_MES AS
SELECT TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
	MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
FROM OPERACION_X_SEMANA TMP
	INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO
  INNER JOIN (SELECT DISTINCT SEM.FECHA_SEMANA, SEMANA FROM OPERACION_X_SEMANA SEM) SEMANAS
    ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-1)
      AND TMP.FECHA_SEMANA<SEMANAS.FECHA_SEMANA
WHERE --TMP.ID_INDIVIDUO='1453664590875.284' AND
SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SEMANAS.FECHA_SEMANA
--ORDER BY SEMANAS.SEMANA DESC
;

CREATE TABLE OPERACIONES_ACUM_SEMANA_ANYO AS
SELECT TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
	MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
FROM OPERACION_X_SEMANA TMP
	INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO
  INNER JOIN (SELECT DISTINCT SEM.FECHA_SEMANA, SEMANA FROM OPERACION_X_SEMANA SEM) SEMANAS
    ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-12)
      AND TMP.FECHA_SEMANA<SEMANAS.FECHA_SEMANA
WHERE --TMP.ID_INDIVIDUO='1453664590875.284' AND
SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SEMANAS.FECHA_SEMANA
--ORDER BY SEMANAS.SEMANA DESC
;

CREATE TABLE OPERACIONES_ACUM_SEMANA_CONSOL AS
SELECT TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
	MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
FROM OPERACION_X_SEMANA TMP
	INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO
  INNER JOIN (SELECT DISTINCT SEM.FECHA_SEMANA, SEMANA FROM OPERACION_X_SEMANA SEM) SEMANAS
    ON TMP.FECHA_SEMANA<SEMANAS.FECHA_SEMANA
WHERE --TMP.ID_INDIVIDUO='1453664590875.284' AND
SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SEMANAS.FECHA_SEMANA
--ORDER BY SEMANAS.SEMANA DESC
;

SELECT TMP.ID_INDIVIDUO, SEMANAS.SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
	MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
FROM OPERACION_X_SEMANA TMP
	INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO
  INNER JOIN (SELECT DISTINCT SEM.SEMANA FROM OPERACION_X_SEMANA SEM) SEMANAS
    ON TO_DATE(TMP.SEMANA,'YYYYWW')<TO_DATE(SEMANAS.SEMANA,'YYYYWW')
WHERE --TMP.ID_INDIVIDUO='1453664590875.284' AND
TO_DATE(SEMANAS.SEMANA,'YYYYWW')>=TO_DATE('2011 01','YYYYWW')
GROUP BY TMP.ID_INDIVIDUO, SEMANAS.SEMANA
--ORDER BY SEMANAS.SEMANA DESC
;
