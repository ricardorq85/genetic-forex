SET feedback off;
SET term off;
SET pagesize 0;
SET linesize 2000;
SET newpage 0;
SET space 0;

SPOOL d:/ricardorq85/Informacion/FOREX/DATABASE/SecuenciaPips/log/CrearEstructurasPipsX_2014.txt;

DROP MATERIALIZED VIEW PDI_MVIEW_2014;
DROP MATERIALIZED VIEW PSI_MVIEW_2014;
DROP MATERIALIZED VIEW PMI_MVIEW_2014;

DROP MATERIALIZED VIEW MV_PIPS_DIARIOS_INDIVIDUO;
DROP MATERIALIZED VIEW MV_PIPS_SEMANALES_INDIVIDUO;
DROP MATERIALIZED VIEW MV_PIPS_MENSUALES_INDIVIDUO;

CREATE MATERIALIZED VIEW MV_PIPS_DIARIOS_INDIVIDUO AS
  SELECT OPER.ID_INDIVIDUO, TO_CHAR(OPER.FECHA_CIERRE, 'YYYYMMDD') DIA, 
    TRUNC(MIN(OPER.FECHA_CIERRE)) MIN_FECHA,
    COUNT(*) NUM_OPERACIONES, SUM(OPER.PIPS) PIPS
   FROM FOREX.OPERACION OPER
  WHERE OPER.FECHA_CIERRE IS NOT NULL
    AND EXISTS (
      SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO=OPER.ID_INDIVIDUO AND II.TIPO='OPEN'
    )
    AND FECHA_CIERRE>=TO_DATE('2014/01/01', 'YYYY/MM/DD')
    AND FECHA_CIERRE<=TO_DATE('2015/01/01', 'YYYY/MM/DD')
  HAVING SUM(PIPS)>0
  GROUP BY ID_INDIVIDUO, TO_CHAR(OPER.FECHA_CIERRE, 'YYYYMMDD')
;

CREATE MATERIALIZED VIEW MV_PIPS_SEMANALES_INDIVIDUO AS
  SELECT OPER.ID_INDIVIDUO, TO_CHAR(OPER.FECHA_CIERRE, 'YYYY WW') SEMANA, 
    TRUNC(MIN(OPER.FECHA_CIERRE)) MIN_FECHA,
    COUNT(*) NUM_OPERACIONES, SUM(OPER.PIPS) PIPS
   FROM FOREX.OPERACION OPER
  WHERE OPER.FECHA_CIERRE IS NOT NULL
    AND EXISTS (
      SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO=OPER.ID_INDIVIDUO AND II.TIPO='OPEN'
    )
    AND FECHA_CIERRE>=TO_DATE('2013/01/01', 'YYYY/MM/DD')
    AND FECHA_CIERRE<=TO_DATE('2015/01/01', 'YYYY/MM/DD')
  GROUP BY OPER.ID_INDIVIDUO, TO_CHAR(OPER.FECHA_CIERRE, 'YYYY WW')
;

CREATE MATERIALIZED VIEW MV_PIPS_MENSUALES_INDIVIDUO AS
  SELECT OPER.ID_INDIVIDUO, TO_CHAR(OPER.FECHA_CIERRE, 'YYYYMM') MES, 
    TRUNC(MIN(OPER.FECHA_CIERRE)) MIN_FECHA,
    COUNT(*) NUM_OPERACIONES, SUM(OPER.PIPS) PIPS
   FROM FOREX.OPERACION OPER
  WHERE OPER.FECHA_CIERRE IS NOT NULL
    AND EXISTS (
      SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO=OPER.ID_INDIVIDUO AND II.TIPO='OPEN'
    )
    AND FECHA_CIERRE>=TO_DATE('2013/01/01', 'YYYY/MM/DD')
    AND FECHA_CIERRE<=TO_DATE('2015/01/01', 'YYYY/MM/DD')
  GROUP BY OPER.ID_INDIVIDUO, TO_CHAR(OPER.FECHA_CIERRE, 'YYYYMM')
;

CREATE MATERIALIZED VIEW PDI_MVIEW_2014
  AS SELECT PD1.ID_INDIVIDUO ID_INDIVIDUO, PD1.DIA DIA1, PD1.MIN_FECHA MIN_FECHA1, PD1.NUM_OPERACIONES NUM_OPERACIONES1, PD1.PIPS PIPS1,
      PD2.DIA DIA_F, PD2.MIN_FECHA MIN_FECHA_F, PD2.NUM_OPERACIONES NUM_OPERACIONES_F, PD2.PIPS PIPS_F
    FROM MV_PIPS_DIARIOS_INDIVIDUO PD1
  INNER JOIN MV_PIPS_DIARIOS_INDIVIDUO PD2 ON PD1.ID_INDIVIDUO=PD2.ID_INDIVIDUO
      AND TRUNC(PD2.MIN_FECHA)-TRUNC(PD1.MIN_FECHA)=1 AND PD2.MIN_FECHA>PD1.MIN_FECHA
WHERE PD2.DIA LIKE '2014____'
  AND PD1.PIPS>0
  AND PD2.PIPS>0
;

CREATE MATERIALIZED VIEW PSI_MVIEW_2014 AS
SELECT PS1.ID_INDIVIDUO ID_INDIVIDUO, PS1.SEMANA SEMANA1, PS1.MIN_FECHA MIN_FECHA1, PS1.NUM_OPERACIONES NUM_OPERACIONES1, PS1.PIPS PIPS1,
    PS1.SEMANA SEMANA_F, PS1.MIN_FECHA MIN_FECHA_F, PS1.NUM_OPERACIONES NUM_OPERACIONES_F, PS1.PIPS PIPS_F
    --PS2.SEMANA SEMANA_F, PS2.MIN_FECHA MIN_FECHA_F, PS2.NUM_OPERACIONES NUM_OPERACIONES_F, PS2.PIPS PIPS_F
  FROM MV_PIPS_SEMANALES_INDIVIDUO PS1
  --INNER JOIN MV_PIPS_SEMANALES_INDIVIDUO PS2 ON PS1.ID_INDIVIDUO=PS2.ID_INDIVIDUO
      --AND PS2.MIN_FECHA-8<PS1.MIN_FECHA AND PS2.MIN_FECHA>PS1.MIN_FECHA
  --INNER JOIN MV_PIPS_SEMANALES_INDIVIDUO PS3 ON PS1.ID_INDIVIDUO=PS3.ID_INDIVIDUO
    --  AND PS3.MIN_FECHA-8<PS2.MIN_FECHA AND PS3.MIN_FECHA>PS2.MIN_FECHA
WHERE PS1.SEMANA LIKE '2014___'  
  AND PS1.PIPS>0
  --AND PS2.PIPS>0
;

CREATE MATERIALIZED VIEW PMI_MVIEW_2014 AS
SELECT PM1.ID_INDIVIDUO ID_INDIVIDUO, PM1.MES MES_F, PM1.MIN_FECHA MIN_FECHA_F, 
  PM1.NUM_OPERACIONES NUM_OPERACIONES_F, PM1.PIPS PIPS_F
  FROM MV_PIPS_MENSUALES_INDIVIDUO PM1
WHERE (PM1.MES LIKE '2014__' OR PM1.MES LIKE '201312')
  AND PM1.PIPS>0
;

alter materialized view MV_PIPS_DIARIOS_INDIVIDUO compile;
alter materialized view MV_PIPS_SEMANALES_INDIVIDUO compile;
alter materialized view MV_PIPS_MENSUALES_INDIVIDUO compile;
alter materialized view PDI_MVIEW_2014 compile;
alter materialized view PSI_MVIEW_2014 compile;
alter materialized view PMI_MVIEW_2014 compile;

SPOOL OFF;