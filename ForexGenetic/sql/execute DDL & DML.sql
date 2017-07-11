SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'Altering OPERACION_ESTRATEGIA_PERIODO ...' FROM DUAL;

ALTER TABLE OPERACION_ESTRATEGIA_PERIODO 
DROP CONSTRAINT OPER_EST_PER_ID_FK;

ALTER TABLE OPERACION_ESTRATEGIA_PERIODO
ADD CONSTRAINT OPER_EST_PER_ID_FK FOREIGN KEY
(
  ESTRATEGIA_PERIODO 
)
REFERENCES ESTRATEGIA_OPERACION_PERIODO
(
  ID 
)
ON DELETE CASCADE ENABLE;
/
CREATE INDEX EOP_TOTALES_IDX 
	ON ESTRATEGIA_OPERACION_PERIODO (ID, TIPO_OPERACION DESC, FECHA_INICIAL DESC, FECHA_FINAL DESC, 
		CANTIDAD DESC, PIPS_TOTALES DESC, PIPS_TOTALES_PARALELAS DESC, 
		PIPS_AGRUPADO_MINUTOS DESC, PIPS_AGRUPADO_HORAS DESC, PIPS_AGRUPADO_DIAS DESC,
		CANTIDAD_INDIVIDUOS DESC, CANTIDAD_PARALELAS DESC
	);
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'Altering PREVIO_TOFILESTRING ...' FROM DUAL;
/
DROP INDEX PTFS_IDX1;
CREATE INDEX PTFS_IDX1 ON PREVIO_TOFILESTRING (ID_INDIVIDUO ASC, FECHA_SEMANA ASC, TIPO_OPERACION ASC, 
	NVL(PIPS_SEMANA, 0) ASC, NVL(PIPS_MES,0) ASC, NVL(PIPS_ANYO,0) ASC, PIPS_TOTALES ASC,
	NVL(R2_SEMANA, 0) ASC, NVL(R2_MES,0) ASC, NVL(R2_ANYO,0) ASC, R2_CONSOL ASC,
	NVL(PENDIENTE_SEMANA, 0) ASC, NVL(PENDIENTE_MES,0) ASC, NVL(PENDIENTE_ANYO,0) ASC, PENDIENTE_CONSOL ASC) 
LOGGING 
TABLESPACE TS_FOREX 
PCTFREE 10 
INITRANS 2 
STORAGE 
( 
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1 
  MAXEXTENTS UNLIMITED 
  BUFFER_POOL DEFAULT 
) 
NOPARALLEL;
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'Altering ESTRATEGIA_OPERACION_PERIODO ...' FROM DUAL;
/

ALTER TABLE ESTRATEGIA_OPERACION_PERIODO ADD (EOP_VERSION VARCHAR2(10 CHAR) );

CREATE INDEX EOP_VERSION_IDX ON ESTRATEGIA_OPERACION_PERIODO (EOP_VERSION ASC);
ALTER TABLE ESTRATEGIA_OPERACION_PERIODO 
DROP CONSTRAINT ESTRATEGIA_OPERACION_PERIO_PK;

ALTER TABLE ESTRATEGIA_OPERACION_PERIODO  
MODIFY (ID NOT NULL);

ALTER TABLE ESTRATEGIA_OPERACION_PERIODO
ADD CONSTRAINT ESTRATEGIA_OPERACION_PERIO_PK PRIMARY KEY 
(
  FILTRO_PIPS_X_SEMANA 
, FILTRO_PIPS_X_MES 
, FILTRO_PIPS_X_ANYO 
, FILTRO_PIPS_TOTALES 
, FIRST_ORDER 
, FECHA_INICIAL 
, FECHA_FINAL 
, TIPO_OPERACION 
, FILTRO_PENDIENTE_SEMANA 
, FILTRO_PENDIENTE_MES 
, FILTRO_PENDIENTE_ANYO 
, FILTRO_PENDIENTE_TOTALES 
, FILTRO_R2_SEMANA 
, FILTRO_R2_MES 
, FILTRO_R2_ANYO 
, FILTRO_R2_TOTALES 
, MAX_FECHA_CIERRE 
, ID 
)
ENABLE;

UPDATE ESTRATEGIA_OPERACION_PERIODO SET EOP_VERSION = '2016' WHERE EOP_VERSION IS NULL AND ROWNUM<10000;
COMMIT;
/

SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'Creating VIEW LOGS...' FROM DUAL;
/
CREATE MATERIALIZED VIEW LOG ON ESTRATEGIA_OPERACION_PERIODO 
WITH ROWID 
INCLUDING NEW VALUES;
CREATE MATERIALIZED VIEW LOG ON PREVIO_TOFILESTRING 
WITH ROWID 
INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW FILTERED_PTFS 
NOCACHE 
USING INDEX 
REFRESH ON DEMAND 
FAST 
WITH ROWID 
USING DEFAULT ROLLBACK SEGMENT 
DISABLE QUERY REWRITE AS 
SELECT PTFS.ID_INDIVIDUO,PTFS.FECHA_SEMANA,PTFS.PIPS_SEMANA,PTFS.PIPS_MES,PTFS.PIPS_ANYO,PTFS.PIPS_TOTALES,PTFS.TIPO_OPERACION,PTFS.FECHA_HISTORICO,PTFS.R_COUNT_SEMANA,PTFS.R2_SEMANA,PTFS.PENDIENTE_SEMANA,PTFS.INTERCEPCION_SEMANA,PTFS.R_COUNT_MES,PTFS.R2_MES,PTFS.PENDIENTE_MES,PTFS.INTERCEPCION_MES,PTFS.R_COUNT_ANYO,PTFS.R2_ANYO,PTFS.PENDIENTE_ANYO,PTFS.INTERCEPCION_ANYO,PTFS.R_COUNT_CONSOL,PTFS.R2_CONSOL,PTFS.PENDIENTE_CONSOL,PTFS.INTERCEPCION_CONSOL,PTFS.ROWID PTFS_ROWID
FROM PREVIO_TOFILESTRING PTFS
WHERE PTFS.FECHA_SEMANA>TO_DATE('20141231','YYYYMMDD')
	AND NVL(PIPS_SEMANA,0)>=-5000	AND NVL(PIPS_MES,0)>=-5000 
	AND NVL(PIPS_ANYO,0)>=-5000 AND PIPS_TOTALES>=-5000
	AND NVL(PENDIENTE_SEMANA,0)>=-5000 AND NVL(PENDIENTE_MES,0)>=-5000 
	AND NVL(PENDIENTE_ANYO,0)>=-5000 AND PENDIENTE_CONSOL>=-5000;

CREATE MATERIALIZED VIEW LOG ON FILTERED_PTFS WITH ROWID(PTFS_ROWID) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW FILTERED_EOP 
NOCACHE 
USING INDEX 
REFRESH ON DEMAND 
FAST 
WITH PRIMARY KEY 
USING DEFAULT ROLLBACK SEGMENT 
DISABLE QUERY REWRITE AS
SELECT EOP.CANTIDAD,EOP.FILTRO_PIPS_X_MES,EOP.FILTRO_PIPS_X_ANYO,
	EOP.FILTRO_PIPS_TOTALES,EOP.FIRST_ORDER,EOP.SECOND_ORDER,EOP.FECHA,
	EOP.PIPS_TOTALES PIPS_TOTALES,EOP.FECHA_INICIAL,EOP.FECHA_FINAL,EOP.ID,TIPO_OPERACION,
	EOP.CANTIDAD_PARALELAS,EOP.PIPS_TOTALES_PARALELAS,EOP.PIPS_AGRUPADO_MINUTOS,
	EOP.PIPS_AGRUPADO_HORAS,EOP.PIPS_AGRUPADO_DIAS,EOP.FILTRO_PIPS_X_SEMANA,
	EOP.FILTRO_PENDIENTE_SEMANA,EOP.FILTRO_PENDIENTE_MES,EOP.FILTRO_PENDIENTE_ANYO,
	EOP.FILTRO_PENDIENTE_TOTALES,EOP.FILTRO_R2_SEMANA,EOP.FILTRO_R2_MES,
	EOP.FILTRO_R2_ANYO,EOP.FILTRO_R2_TOTALES,EOP.CANTIDAD_INDIVIDUOS,
	EOP.MAX_FECHA_CIERRE,EOP.EOP_VERSION,EOP.ROWID EOP_ROWID
	FROM ESTRATEGIA_OPERACION_PERIODO EOP
  WHERE EOP.PIPS_TOTALES>0  
	  AND EOP.PIPS_TOTALES_PARALELAS>0	
	  AND EOP.FECHA_FINAL>TO_DATE('20141231','YYYYMMDD');
		
  CREATE INDEX "FOREX"."FILTERED_EOP_TOTALES_IDX" ON "FOREX"."FILTERED_EOP" ("ID", "TIPO_OPERACION" DESC, "FECHA_INICIAL" DESC, "FECHA_FINAL" DESC, "CANTIDAD" DESC, "PIPS_TOTALES" DESC, "PIPS_TOTALES_PARALELAS" DESC, "PIPS_AGRUPADO_MINUTOS" DESC, "PIPS_AGRUPADO_HORAS" DESC, "PIPS_AGRUPADO_DIAS" DESC, "CANTIDAD_INDIVIDUOS" DESC, "CANTIDAD_PARALELAS" DESC) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX";

CREATE MATERIALIZED VIEW LOG ON FILTERED_EOP WITH ROWID(EOP_VERSION, EOP_ROWID) INCLUDING NEW VALUES;

SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'CREATING FILTERED_PARA_OPERAR_SELL...' FROM DUAL;
/
DROP MATERIALIZED VIEW "FOREX"."FILTERED_PARA_OPERAR_SELL";

  CREATE MATERIALIZED VIEW "FOREX"."FILTERED_PARA_OPERAR_SELL" ("ID_INDIVIDUO", "FECHA_SEMANA", "PIPS_SEMANA", "PIPS_MES", "PIPS_ANYO", "PIPS_TOTALES", "TIPO_OPERACION", "FECHA_HISTORICO", "R_COUNT_SEMANA", "R2_SEMANA", "PENDIENTE_SEMANA", "INTERCEPCION_SEMANA", "R_COUNT_MES", "R2_MES", "PENDIENTE_MES", "INTERCEPCION_MES", "R_COUNT_ANYO", "R2_ANYO", "PENDIENTE_ANYO", "INTERCEPCION_ANYO", "R_COUNT_CONSOL", "R2_CONSOL", "PENDIENTE_CONSOL", "INTERCEPCION_CONSOL", "CANTIDAD", "FILTRO_PIPS_X_MES", "FILTRO_PIPS_X_ANYO", "FILTRO_PIPS_TOTALES", "FIRST_ORDER", "SECOND_ORDER", "FECHA", "EOP_PIPS_TOTALES", "FECHA_INICIAL", "FECHA_FINAL", "ID", "CANTIDAD_PARALELAS", "PIPS_TOTALES_PARALELAS", "PIPS_AGRUPADO_MINUTOS", "PIPS_AGRUPADO_HORAS", "PIPS_AGRUPADO_DIAS", "FILTRO_PIPS_X_SEMANA", "FILTRO_PENDIENTE_SEMANA", "FILTRO_PENDIENTE_MES", "FILTRO_PENDIENTE_ANYO", "FILTRO_PENDIENTE_TOTALES", "FILTRO_R2_SEMANA", "FILTRO_R2_MES", "FILTRO_R2_ANYO", "FILTRO_R2_TOTALES", "CANTIDAD_INDIVIDUOS", "MAX_FECHA_CIERRE", "EOP_VERSION", "PTFS_ROWID", "EOP_ROWID")
  ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" 
  BUILD IMMEDIATE
  USING INDEX 
  REFRESH FAST ON DEMAND
  USING DEFAULT LOCAL ROLLBACK SEGMENT
  USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
  AS SELECT PTFS.ID_INDIVIDUO,PTFS.FECHA_SEMANA,PTFS.PIPS_SEMANA,PTFS.PIPS_MES,PTFS.PIPS_ANYO,PTFS.PIPS_TOTALES,PTFS.TIPO_OPERACION,PTFS.FECHA_HISTORICO,PTFS.R_COUNT_SEMANA,PTFS.R2_SEMANA,PTFS.PENDIENTE_SEMANA,PTFS.INTERCEPCION_SEMANA,PTFS.R_COUNT_MES,PTFS.R2_MES,PTFS.PENDIENTE_MES,PTFS.INTERCEPCION_MES,PTFS.R_COUNT_ANYO,PTFS.R2_ANYO,PTFS.PENDIENTE_ANYO,PTFS.INTERCEPCION_ANYO,PTFS.R_COUNT_CONSOL,PTFS.R2_CONSOL,PTFS.PENDIENTE_CONSOL,PTFS.INTERCEPCION_CONSOL,
	EOP.CANTIDAD,EOP.FILTRO_PIPS_X_MES,EOP.FILTRO_PIPS_X_ANYO,EOP.FILTRO_PIPS_TOTALES,EOP.FIRST_ORDER,EOP.SECOND_ORDER,EOP.FECHA,EOP.PIPS_TOTALES EOP_PIPS_TOTALES,EOP.FECHA_INICIAL,EOP.FECHA_FINAL,EOP.ID,EOP.CANTIDAD_PARALELAS,EOP.PIPS_TOTALES_PARALELAS,EOP.PIPS_AGRUPADO_MINUTOS,EOP.PIPS_AGRUPADO_HORAS,EOP.PIPS_AGRUPADO_DIAS,EOP.FILTRO_PIPS_X_SEMANA,EOP.FILTRO_PENDIENTE_SEMANA,EOP.FILTRO_PENDIENTE_MES,EOP.FILTRO_PENDIENTE_ANYO,EOP.FILTRO_PENDIENTE_TOTALES,EOP.FILTRO_R2_SEMANA,EOP.FILTRO_R2_MES,EOP.FILTRO_R2_ANYO,EOP.FILTRO_R2_TOTALES,EOP.CANTIDAD_INDIVIDUOS,EOP.MAX_FECHA_CIERRE,EOP.EOP_VERSION,
	PTFS.ROWID PTFS_ROWID, EOP.ROWID EOP_ROWID
  FROM FILTERED_PTFS PTFS, --PREVIO_TOFILESTRING PTFS, 
	--ESTRATEGIA_OPERACION_PERIODO EOP
		FILTERED_EOP EOP
		WHERE ( NVL(PTFS.PIPS_SEMANA,0)>EOP.FILTRO_PIPS_X_SEMANA AND NVL(PTFS.PIPS_MES,0)>EOP.FILTRO_PIPS_X_MES 
			AND NVL(PTFS.PIPS_ANYO,0)>EOP.FILTRO_PIPS_X_ANYO AND PTFS.PIPS_TOTALES>EOP.FILTRO_PIPS_TOTALES)
			AND (NVL(PTFS.R2_SEMANA,0)>EOP.FILTRO_R2_SEMANA AND NVL(PTFS.R2_MES,0)>EOP.FILTRO_R2_MES 
			AND NVL(PTFS.R2_ANYO,0)>EOP.FILTRO_R2_ANYO AND PTFS.R2_CONSOL>EOP.FILTRO_R2_TOTALES)
			AND (NVL(PTFS.PENDIENTE_SEMANA,0)>EOP.FILTRO_PENDIENTE_SEMANA AND NVL(PTFS.PENDIENTE_MES,0)>EOP.FILTRO_PENDIENTE_MES 
			AND NVL(PTFS.PENDIENTE_ANYO,0)>EOP.FILTRO_PENDIENTE_ANYO AND PTFS.PENDIENTE_CONSOL>EOP.FILTRO_PENDIENTE_TOTALES)
			AND PTFS.TIPO_OPERACION=EOP.TIPO_OPERACION
			AND PTFS.FECHA_SEMANA BETWEEN EOP.FECHA_FINAL AND (EOP.FECHA_FINAL+7)
			AND PTFS.FECHA_SEMANA BETWEEN EOP.MAX_FECHA_CIERRE AND (EOP.MAX_FECHA_CIERRE+7)   
	AND ((EOP.TIPO_OPERACION='SELL' AND EOP.PIPS_TOTALES>0
--		AND EOP.PIPS_TOTALES_PARALELAS>EOP.PIPS_TOTALES
		AND EOP.PIPS_TOTALES_PARALELAS>0
	 )	)
	AND EOP.TIPO_OPERACION='SELL'
	AND PTFS.FECHA_SEMANA>TO_DATE('20141231','YYYYMMDD');

   COMMENT ON MATERIALIZED VIEW "FOREX"."FILTERED_PARA_OPERAR_SELL"  IS 'snapshot table for snapshot FOREX.FILTERED_PARA_OPERAR_SELL';

/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'CREATED FILTERED_PARA_OPERAR_BUY...' FROM DUAL;
/
  CREATE MATERIALIZED VIEW "FOREX"."FILTERED_PARA_OPERAR_BUY" ("ID_INDIVIDUO", "FECHA_SEMANA", "PIPS_SEMANA", "PIPS_MES", "PIPS_ANYO", "PIPS_TOTALES", "TIPO_OPERACION", "FECHA_HISTORICO", "R_COUNT_SEMANA", "R2_SEMANA", "PENDIENTE_SEMANA", "INTERCEPCION_SEMANA", "R_COUNT_MES", "R2_MES", "PENDIENTE_MES", "INTERCEPCION_MES", "R_COUNT_ANYO", "R2_ANYO", "PENDIENTE_ANYO", "INTERCEPCION_ANYO", "R_COUNT_CONSOL", "R2_CONSOL", "PENDIENTE_CONSOL", "INTERCEPCION_CONSOL", "CANTIDAD", "FILTRO_PIPS_X_MES", "FILTRO_PIPS_X_ANYO", "FILTRO_PIPS_TOTALES", "FIRST_ORDER", "SECOND_ORDER", "FECHA", "EOP_PIPS_TOTALES", "FECHA_INICIAL", "FECHA_FINAL", "ID", "CANTIDAD_PARALELAS", "PIPS_TOTALES_PARALELAS", "PIPS_AGRUPADO_MINUTOS", "PIPS_AGRUPADO_HORAS", "PIPS_AGRUPADO_DIAS", "FILTRO_PIPS_X_SEMANA", "FILTRO_PENDIENTE_SEMANA", "FILTRO_PENDIENTE_MES", "FILTRO_PENDIENTE_ANYO", "FILTRO_PENDIENTE_TOTALES", "FILTRO_R2_SEMANA", "FILTRO_R2_MES", "FILTRO_R2_ANYO", "FILTRO_R2_TOTALES", "CANTIDAD_INDIVIDUOS", "MAX_FECHA_CIERRE", "EOP_VERSION", "PTFS_ROWID", "EOP_ROWID")
  ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" 
  BUILD IMMEDIATE
  USING INDEX 
  REFRESH FAST ON DEMAND
  USING DEFAULT LOCAL ROLLBACK SEGMENT
  USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
  AS SELECT PTFS.ID_INDIVIDUO,PTFS.FECHA_SEMANA,PTFS.PIPS_SEMANA,PTFS.PIPS_MES,PTFS.PIPS_ANYO,PTFS.PIPS_TOTALES,PTFS.TIPO_OPERACION,PTFS.FECHA_HISTORICO,PTFS.R_COUNT_SEMANA,PTFS.R2_SEMANA,PTFS.PENDIENTE_SEMANA,PTFS.INTERCEPCION_SEMANA,PTFS.R_COUNT_MES,PTFS.R2_MES,PTFS.PENDIENTE_MES,PTFS.INTERCEPCION_MES,PTFS.R_COUNT_ANYO,PTFS.R2_ANYO,PTFS.PENDIENTE_ANYO,PTFS.INTERCEPCION_ANYO,PTFS.R_COUNT_CONSOL,PTFS.R2_CONSOL,PTFS.PENDIENTE_CONSOL,PTFS.INTERCEPCION_CONSOL,
	EOP.CANTIDAD,EOP.FILTRO_PIPS_X_MES,EOP.FILTRO_PIPS_X_ANYO,EOP.FILTRO_PIPS_TOTALES,EOP.FIRST_ORDER,EOP.SECOND_ORDER,EOP.FECHA,EOP.PIPS_TOTALES EOP_PIPS_TOTALES,EOP.FECHA_INICIAL,EOP.FECHA_FINAL,EOP.ID,EOP.CANTIDAD_PARALELAS,EOP.PIPS_TOTALES_PARALELAS,EOP.PIPS_AGRUPADO_MINUTOS,EOP.PIPS_AGRUPADO_HORAS,EOP.PIPS_AGRUPADO_DIAS,EOP.FILTRO_PIPS_X_SEMANA,EOP.FILTRO_PENDIENTE_SEMANA,EOP.FILTRO_PENDIENTE_MES,EOP.FILTRO_PENDIENTE_ANYO,EOP.FILTRO_PENDIENTE_TOTALES,EOP.FILTRO_R2_SEMANA,EOP.FILTRO_R2_MES,EOP.FILTRO_R2_ANYO,EOP.FILTRO_R2_TOTALES,EOP.CANTIDAD_INDIVIDUOS,EOP.MAX_FECHA_CIERRE,EOP.EOP_VERSION,
	PTFS.ROWID PTFS_ROWID, EOP.ROWID EOP_ROWID
  FROM FILTERED_PTFS PTFS, --PREVIO_TOFILESTRING PTFS, 
	--ESTRATEGIA_OPERACION_PERIODO EOP
		FILTERED_EOP EOP
		WHERE ( NVL(PTFS.PIPS_SEMANA,0)>EOP.FILTRO_PIPS_X_SEMANA AND NVL(PTFS.PIPS_MES,0)>EOP.FILTRO_PIPS_X_MES 
			AND NVL(PTFS.PIPS_ANYO,0)>EOP.FILTRO_PIPS_X_ANYO AND PTFS.PIPS_TOTALES>EOP.FILTRO_PIPS_TOTALES)
			AND (NVL(PTFS.R2_SEMANA,0)>EOP.FILTRO_R2_SEMANA AND NVL(PTFS.R2_MES,0)>EOP.FILTRO_R2_MES 
			AND NVL(PTFS.R2_ANYO,0)>EOP.FILTRO_R2_ANYO AND PTFS.R2_CONSOL>EOP.FILTRO_R2_TOTALES)
			AND (NVL(PTFS.PENDIENTE_SEMANA,0)>EOP.FILTRO_PENDIENTE_SEMANA AND NVL(PTFS.PENDIENTE_MES,0)>EOP.FILTRO_PENDIENTE_MES 
			AND NVL(PTFS.PENDIENTE_ANYO,0)>EOP.FILTRO_PENDIENTE_ANYO AND PTFS.PENDIENTE_CONSOL>EOP.FILTRO_PENDIENTE_TOTALES)
			AND PTFS.TIPO_OPERACION=EOP.TIPO_OPERACION
			AND PTFS.FECHA_SEMANA BETWEEN EOP.FECHA_FINAL AND (EOP.FECHA_FINAL+7)
			AND PTFS.FECHA_SEMANA BETWEEN EOP.MAX_FECHA_CIERRE AND (EOP.MAX_FECHA_CIERRE+7)   
	AND ((EOP.TIPO_OPERACION='BUY' AND EOP.PIPS_TOTALES>1000 
	  AND EOP.PIPS_TOTALES_PARALELAS>EOP.PIPS_TOTALES
	  AND EOP.PIPS_TOTALES_PARALELAS>3000
	  AND (EOP.PIPS_AGRUPADO_MINUTOS>1000 AND EOP.PIPS_AGRUPADO_HORAS>1000 AND EOP.PIPS_AGRUPADO_DIAS>1000)
	  AND (EOP.CANTIDAD_PARALELAS/EOP.CANTIDAD)<10	  
	  AND (EOP.CANTIDAD_PARALELAS/EOP.CANTIDAD)>1
	  AND EOP.CANTIDAD_INDIVIDUOS>1
	  AND EOP.CANTIDAD/((EOP.FECHA_FINAL-EOP.FECHA_INICIAL)/30)>1
	  AND (EOP.PIPS_TOTALES/EOP.CANTIDAD)>200
	  --AND (EOP.PIPS_TOTALES_PARALELAS/EOP.CANTIDAD_PARALELAS)>200
	 )	)
	AND EOP.TIPO_OPERACION='BUY'
	AND PTFS.FECHA_SEMANA>TO_DATE('20141231','YYYYMMDD');
/
SELECT TO_CHAR(SYSDATE,'YYYYMMDD HH24:MI'), 'CREATED FILTERED_PARA_OPERAR...' FROM DUAL;
/
DROP MATERIALIZED VIEW "FOREX"."FILTERED_PARALELAS_EOP";

  CREATE MATERIALIZED VIEW "FOREX"."FILTERED_PARALELAS_EOP" ("CANTIDAD", "FILTRO_PIPS_X_MES", "FILTRO_PIPS_X_ANYO", "FILTRO_PIPS_TOTALES", "FIRST_ORDER", "SECOND_ORDER", "FECHA", "PIPS_TOTALES", "FECHA_INICIAL", "FECHA_FINAL", "ID", "TIPO_OPERACION", "CANTIDAD_PARALELAS", "PIPS_TOTALES_PARALELAS", "PIPS_AGRUPADO_MINUTOS", "PIPS_AGRUPADO_HORAS", "PIPS_AGRUPADO_DIAS", "FILTRO_PIPS_X_SEMANA", "FILTRO_PENDIENTE_SEMANA", "FILTRO_PENDIENTE_MES", "FILTRO_PENDIENTE_ANYO", "FILTRO_PENDIENTE_TOTALES", "FILTRO_R2_SEMANA", "FILTRO_R2_MES", "FILTRO_R2_ANYO", "FILTRO_R2_TOTALES", "CANTIDAD_INDIVIDUOS", "MAX_FECHA_CIERRE", "EOP_VERSION", "EOP_ROWID")
  ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" 
  BUILD IMMEDIATE
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" 
  REFRESH FAST ON DEMAND
  WITH ROWID USING DEFAULT LOCAL ROLLBACK SEGMENT
  USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
  AS	SELECT EOP.CANTIDAD,EOP.FILTRO_PIPS_X_MES,EOP.FILTRO_PIPS_X_ANYO,
	EOP.FILTRO_PIPS_TOTALES,EOP.FIRST_ORDER,EOP.SECOND_ORDER,EOP.FECHA,
	EOP.PIPS_TOTALES PIPS_TOTALES,EOP.FECHA_INICIAL,EOP.FECHA_FINAL,EOP.ID,TIPO_OPERACION,
	EOP.CANTIDAD_PARALELAS,EOP.PIPS_TOTALES_PARALELAS,EOP.PIPS_AGRUPADO_MINUTOS,
	EOP.PIPS_AGRUPADO_HORAS,EOP.PIPS_AGRUPADO_DIAS,EOP.FILTRO_PIPS_X_SEMANA,
	EOP.FILTRO_PENDIENTE_SEMANA,EOP.FILTRO_PENDIENTE_MES,EOP.FILTRO_PENDIENTE_ANYO,
	EOP.FILTRO_PENDIENTE_TOTALES,EOP.FILTRO_R2_SEMANA,EOP.FILTRO_R2_MES,
	EOP.FILTRO_R2_ANYO,EOP.FILTRO_R2_TOTALES,EOP.CANTIDAD_INDIVIDUOS,
	EOP.MAX_FECHA_CIERRE,EOP.EOP_VERSION,EOP.ROWID EOP_ROWID
	FROM ESTRATEGIA_OPERACION_PERIODO EOP
  WHERE EOP.PIPS_TOTALES<=1000
	  AND EOP.PIPS_TOTALES_PARALELAS>0
		AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
		AND EOP.CANTIDAD_INDIVIDUOS>1
		AND (EOP.PIPS_TOTALES_PARALELAS/EOP.CANTIDAD_PARALELAS)>200
	  AND EOP.FECHA_FINAL>TO_DATE('20141231','YYYYMMDD');

  CREATE UNIQUE INDEX "FOREX"."I_SNAP$_FILTERED_PARALELAS" ON "FOREX"."FILTERED_PARALELAS_EOP" ("M_ROW$$") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" ;
  CREATE UNIQUE INDEX "FOREX"."FILT_PARAL_EOP_IDX1" ON "FOREX"."FILTERED_PARALELAS_EOP" ("FILTRO_PIPS_X_SEMANA", "FILTRO_PIPS_X_MES", "FILTRO_PIPS_X_ANYO", "FILTRO_PIPS_TOTALES", "FIRST_ORDER", "FECHA_INICIAL", "FECHA_FINAL", "TIPO_OPERACION", "FILTRO_PENDIENTE_SEMANA", "FILTRO_PENDIENTE_MES", "FILTRO_PENDIENTE_ANYO", "FILTRO_PENDIENTE_TOTALES", "FILTRO_R2_SEMANA", "FILTRO_R2_MES", "FILTRO_R2_ANYO", "FILTRO_R2_TOTALES", "MAX_FECHA_CIERRE", "ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" ;
  CREATE INDEX "FOREX"."FILT_PARAL_EOP_TOTALES_IDX2" ON "FOREX"."FILTERED_PARALELAS_EOP" ("ID", "TIPO_OPERACION" DESC, "FECHA_INICIAL" DESC, "FECHA_FINAL" DESC, "CANTIDAD" DESC, "PIPS_TOTALES" DESC, "PIPS_TOTALES_PARALELAS" DESC, "PIPS_AGRUPADO_MINUTOS" DESC, "PIPS_AGRUPADO_HORAS" DESC, "PIPS_AGRUPADO_DIAS" DESC, "CANTIDAD_INDIVIDUOS" DESC, "CANTIDAD_PARALELAS" DESC) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" ;

   COMMENT ON MATERIALIZED VIEW "FOREX"."FILTERED_PARALELAS_EOP"  IS 'snapshot table for snapshot FOREX.FILTERED_PARALELAS_EOP';

  CREATE MATERIALIZED VIEW LOG ON "FOREX"."FILTERED_PARALELAS_EOP"
 PCTFREE 10 PCTUSED 30 INITRANS 1 MAXTRANS 255 LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_FOREX" 
  WITH ROWID ( "EOP_ROWID", "EOP_VERSION" ) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW FILTERED_PARA_OPERAR_BOTH 
NOCACHE 
USING INDEX 
REFRESH ON DEMAND 
FAST 
WITH ROWID 
USING DEFAULT ROLLBACK SEGMENT 
DISABLE QUERY REWRITE AS 
SELECT PTFS.ID_INDIVIDUO,PTFS.FECHA_SEMANA,PTFS.PIPS_SEMANA,PTFS.PIPS_MES,PTFS.PIPS_ANYO,PTFS.PIPS_TOTALES,PTFS.TIPO_OPERACION,PTFS.FECHA_HISTORICO,PTFS.R_COUNT_SEMANA,PTFS.R2_SEMANA,PTFS.PENDIENTE_SEMANA,PTFS.INTERCEPCION_SEMANA,PTFS.R_COUNT_MES,PTFS.R2_MES,PTFS.PENDIENTE_MES,PTFS.INTERCEPCION_MES,PTFS.R_COUNT_ANYO,PTFS.R2_ANYO,PTFS.PENDIENTE_ANYO,PTFS.INTERCEPCION_ANYO,PTFS.R_COUNT_CONSOL,PTFS.R2_CONSOL,PTFS.PENDIENTE_CONSOL,PTFS.INTERCEPCION_CONSOL,
	EOP.CANTIDAD,EOP.FILTRO_PIPS_X_MES,EOP.FILTRO_PIPS_X_ANYO,EOP.FILTRO_PIPS_TOTALES,EOP.FIRST_ORDER,EOP.SECOND_ORDER,EOP.FECHA,EOP.PIPS_TOTALES EOP_PIPS_TOTALES,EOP.FECHA_INICIAL,EOP.FECHA_FINAL,EOP.ID,EOP.CANTIDAD_PARALELAS,EOP.PIPS_TOTALES_PARALELAS,EOP.PIPS_AGRUPADO_MINUTOS,EOP.PIPS_AGRUPADO_HORAS,EOP.PIPS_AGRUPADO_DIAS,EOP.FILTRO_PIPS_X_SEMANA,EOP.FILTRO_PENDIENTE_SEMANA,EOP.FILTRO_PENDIENTE_MES,EOP.FILTRO_PENDIENTE_ANYO,EOP.FILTRO_PENDIENTE_TOTALES,EOP.FILTRO_R2_SEMANA,EOP.FILTRO_R2_MES,EOP.FILTRO_R2_ANYO,EOP.FILTRO_R2_TOTALES,EOP.CANTIDAD_INDIVIDUOS,EOP.MAX_FECHA_CIERRE,EOP.EOP_VERSION,
	PTFS.ROWID PTFS_ROWID, EOP.ROWID EOP_ROWID
  FROM FILTERED_PTFS PTFS,
		FILTERED_PARALELAS_EOP EOP
		WHERE ( NVL(PTFS.PIPS_SEMANA,0)>EOP.FILTRO_PIPS_X_SEMANA AND NVL(PTFS.PIPS_MES,0)>EOP.FILTRO_PIPS_X_MES 
			AND NVL(PTFS.PIPS_ANYO,0)>EOP.FILTRO_PIPS_X_ANYO AND PTFS.PIPS_TOTALES>EOP.FILTRO_PIPS_TOTALES)
			AND (NVL(PTFS.R2_SEMANA,0)>EOP.FILTRO_R2_SEMANA AND NVL(PTFS.R2_MES,0)>EOP.FILTRO_R2_MES 
			AND NVL(PTFS.R2_ANYO,0)>EOP.FILTRO_R2_ANYO AND PTFS.R2_CONSOL>EOP.FILTRO_R2_TOTALES)
			AND (NVL(PTFS.PENDIENTE_SEMANA,0)>EOP.FILTRO_PENDIENTE_SEMANA AND NVL(PTFS.PENDIENTE_MES,0)>EOP.FILTRO_PENDIENTE_MES 
			AND NVL(PTFS.PENDIENTE_ANYO,0)>EOP.FILTRO_PENDIENTE_ANYO AND PTFS.PENDIENTE_CONSOL>EOP.FILTRO_PENDIENTE_TOTALES)
			AND PTFS.TIPO_OPERACION=EOP.TIPO_OPERACION
			AND PTFS.FECHA_SEMANA BETWEEN EOP.FECHA_FINAL AND (EOP.FECHA_FINAL+7)
			AND PTFS.FECHA_SEMANA BETWEEN EOP.MAX_FECHA_CIERRE AND (EOP.MAX_FECHA_CIERRE+7)   
	AND PTFS.FECHA_SEMANA>TO_DATE('20141231','YYYYMMDD');
/

CREATE UNIQUE INDEX PROCESO_IND_FEHIST_IDX ON PROCESO (ID_INDIVIDUO ASC, FECHA_HISTORICO DESC);
/


CREATE OR REPLACE FORCE VIEW "FOREX"."DETALLE_ESTADISTICAS" ("ID_INDIVIDUO", "CANTIDAD_POSITIVOS", "CANTIDAD_NEGATIVOS", "CANTIDAD_TOTAL", "PIPS_POSITIVOS", "PIPS_NEGATIVOS", "PIPS_TOTALES", "PIPS_MINIMOS_POS", "PIPS_MINIMOS_NEG", "PIPS_MINIMOS", "PIPS_RETROCESO_MINIMO_POS", "PIPS_RETROCESO_MINIMO_NEG", "PIPS_RETROCESO_MINIMO_TOTAL", "PIPS_MAXIMOS_POS", "PIPS_MAXIMOS_NEG", "PIPS_MAXIMOS", "PIPS_RETROCESO_MAXIMO_POS", "PIPS_RETROCESO_MAXIMO_NEG", "PIPS_RETROCESO_MAXIMO_TOTAL", "AVG_PIPS_POS", "AVG_PIPS_NEG", "AVG_PIPS", "PIPS_RETROCESO_PROMEDIO_POS", "PIPS_RETROCESO_PROMEDIO_NEG", "PIPS_RETROCESO_PROMEDIO_TOTAL", "PIPS_MODA_POS", "PIPS_MODA_NEG", "PIPS_MODA", "PIPS_RETROCESO_MODA_POS", "PIPS_RETROCESO_MODA_NEG", "PIPS_RETROCESO_MODA_TOTAL", "DUR_MIN_POS", "DUR_MIN_NEG", "DUR_MIN", "DUR_MAX_POS", "DUR_MAX_NEG", "DUR_MAX", "DUR_PROM_POS", "DUR_PROM_NEG", "DUR_PROM", "DUR_MODA_POS", "DUR_MODA_NEG", "DUR_MODA", "DUR_DESV_POS", "DUR_DESV_NEG", "DUR_DESV") AS 
  SELECT IND.ID ID_INDIVIDUO, 
  NVL(SUM(POSITIVOS.CANTIDAD),0) CANTIDAD_POSITIVOS, NVL(SUM(NEGATIVOS.CANTIDAD),0) CANTIDAD_NEGATIVOS, NVL(SUM(OPER.CANTIDAD),0) CANTIDAD_TOTAL,
  NVL(SUM(POSITIVOS.PIPS),0) PIPS_POSITIVOS, NVL(SUM(NEGATIVOS.PIPS),0) PIPS_NEGATIVOS, NVL(SUM(POSITIVOS.PIPS),0)+NVL(SUM(NEGATIVOS.PIPS),0) PIPS_TOTALES,
  NVL(MIN(POSITIVOS.PIPS_MINIMOS),0) PIPS_MINIMOS_POS, NVL(MIN(NEGATIVOS.PIPS_MINIMOS),0) PIPS_MINIMOS_NEG, NVL(MIN(OPER.PIPS_MINIMOS),0) PIPS_MINIMOS,
    NVL(MIN(POSITIVOS.PIPS_RETROCESO_MINIMO),0) PIPS_RETROCESO_MINIMO_POS,NVL(MIN(NEGATIVOS.PIPS_RETROCESO_MINIMO),0) PIPS_RETROCESO_MINIMO_NEG, NVL(MIN(OPER.PIPS_RETROCESO_MINIMO),0) PIPS_RETROCESO_MINIMO_TOTAL,
  NVL(MAX(POSITIVOS.PIPS_MAXIMOS),0) PIPS_MAXIMOS_POS, NVL(MAX(NEGATIVOS.PIPS_MAXIMOS),0) PIPS_MAXIMOS_NEG, NVL(MAX(OPER.PIPS_MAXIMOS),0) PIPS_MAXIMOS,
    NVL(MIN(POSITIVOS.PIPS_RETROCESO_MAXIMO),0) PIPS_RETROCESO_MAXIMO_POS,NVL(MIN(NEGATIVOS.PIPS_RETROCESO_MAXIMO),0) PIPS_RETROCESO_MAXIMO_NEG, NVL(MIN(OPER.PIPS_RETROCESO_MAXIMO),0) PIPS_RETROCESO_MAXIMO_TOTAL,
  ROUND(NVL(AVG(POSITIVOS.PIPS_PROMEDIO),0),5) AVG_PIPS_POS, ROUND(NVL(AVG(NEGATIVOS.PIPS_PROMEDIO),0),0) AVG_PIPS_NEG, ROUND(NVL(AVG(OPER.PIPS_PROMEDIO),0),0) AVG_PIPS,
    ROUND(NVL(MIN(POSITIVOS.PIPS_RETROCESO_PROMEDIO),0),5) PIPS_RETROCESO_PROMEDIO_POS,ROUND(NVL(MIN(NEGATIVOS.PIPS_RETROCESO_PROMEDIO),0),5) PIPS_RETROCESO_PROMEDIO_NEG, ROUND(NVL(MIN(OPER.PIPS_RETROCESO_PROMEDIO),0),5) PIPS_RETROCESO_PROMEDIO_TOTAL,
  NVL(SUM(POSITIVOS.PIPS_MODA),0) PIPS_MODA_POS, NVL(SUM(NEGATIVOS.PIPS_MODA),0) PIPS_MODA_NEG, NVL(SUM(OPER.PIPS_MODA),0) PIPS_MODA,
    NVL(MIN(POSITIVOS.PIPS_RETROCESO_MODA),0) PIPS_RETROCESO_MODA_POS,NVL(MIN(NEGATIVOS.PIPS_RETROCESO_MODA),0) PIPS_RETROCESO_MODA_NEG, NVL(MIN(OPER.PIPS_RETROCESO_MODA),0) PIPS_RETROCESO_MODA_TOTAL,
  ROUND(NVL(SUM(POSITIVOS.DUR_MINIMA),0),5) DUR_MIN_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MINIMA),0),5) DUR_MIN_NEG, ROUND(NVL(SUM(OPER.DUR_MINIMA),0),5) DUR_MIN, 
  ROUND(NVL(SUM(POSITIVOS.DUR_MAXIMA),0),5) DUR_MAX_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MAXIMA),0),5) DUR_MAX_NEG, ROUND(NVL(SUM(OPER.DUR_MAXIMA),0),5) DUR_MAX,
  ROUND(NVL(SUM(POSITIVOS.DUR_PROMEDIO),0),5) DUR_PROM_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_PROMEDIO),0),5) DUR_PROM_NEG, ROUND(NVL(SUM(OPER.DUR_PROMEDIO),0),5) DUR_PROM,
  ROUND(NVL(SUM(POSITIVOS.DUR_MODA),0),5) DUR_MODA_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_MODA),0),5) DUR_MODA_NEG, ROUND(NVL(SUM(OPER.DUR_MODA),0),5) DUR_MODA, 
  ROUND(NVL(SUM(POSITIVOS.DUR_DESV),0),5) DUR_DESV_POS, ROUND(NVL(SUM(NEGATIVOS.DUR_DESV),0),5) DUR_DESV_NEG, ROUND(NVL(SUM(OPER.DUR_DESV),0),5) DUR_DESV
FROM INDIVIDUO IND
  INNER JOIN (SELECT OPER.ID_INDIVIDUO, MIN(OPER.PIPS) PIPS_MINIMOS, MAX(OPER.PIPS) PIPS_MAXIMOS, AVG(OPER.PIPS) PIPS_PROMEDIO, STATS_MODE(PIPS) PIPS_MODA,
    MIN(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MINIMA, MAX(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MAXIMA,
    AVG(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_PROMEDIO, STATS_MODE(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MODA,
    STDDEV(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_DESV,
    MIN(OPER.MAX_PIPS_RETROCESO) PIPS_RETROCESO_MINIMO, MAX(OPER.MAX_PIPS_RETROCESO) PIPS_RETROCESO_MAXIMO, AVG(OPER.MAX_PIPS_RETROCESO) PIPS_RETROCESO_PROMEDIO, STATS_MODE(OPER.MAX_PIPS_RETROCESO) PIPS_RETROCESO_MODA,    
    COUNT(*) CANTIDAD
    FROM OPERACION OPER, PARAMETROS_ESTADISTICAS PARAM
		WHERE OPER.FECHA_CIERRE IS NOT NULL AND OPER.FECHA_CIERRE < PARAM.PARAM_FECHA
    AND (WEEK_MINUTES(OPER.FECHA_CIERRE,OPER.FECHA_APERTURA))>=NVL(PARAM.PARAM_DURACION,0)
    AND (PARAM.PARAM_RETROCESO IS NULL OR
      (OPER.PIPS<0 AND PARAM.PARAM_RETROCESO/1>0 
      AND OPER.MAX_PIPS_RETROCESO>=NVL(PARAM.PARAM_RETROCESO,0)) 
      OR (OPER.PIPS>0 AND PARAM.PARAM_RETROCESO/-1>0 
      AND OPER.MAX_PIPS_RETROCESO<=NVL(PARAM.PARAM_RETROCESO,0))
      OR ((OPER.PIPS<0 AND PARAM.PARAM_RETROCESO/1<0 
      AND OPER.PIPS<=NVL(PARAM.PARAM_RETROCESO,0)))
      OR ((OPER.PIPS>0 AND PARAM.PARAM_RETROCESO/-1<0 
      AND OPER.PIPS>=NVL(PARAM.PARAM_RETROCESO,0))))
	GROUP BY OPER.ID_INDIVIDUO) OPER ON OPER.ID_INDIVIDUO=IND.ID
  LEFT JOIN (SELECT P.ID_INDIVIDUO, SUM(P.PIPS) PIPS, MIN(P.PIPS) PIPS_MINIMOS, MAX(P.PIPS) PIPS_MAXIMOS, AVG(P.PIPS) PIPS_PROMEDIO, STATS_MODE(P.PIPS) PIPS_MODA,
    MIN(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MINIMA, MAX(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MAXIMA,
    AVG(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_PROMEDIO, STATS_MODE(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MODA,
    STDDEV(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_DESV,
    MIN(MAX_PIPS_RETROCESO) PIPS_RETROCESO_MINIMO, MAX(MAX_PIPS_RETROCESO) PIPS_RETROCESO_MAXIMO, AVG(MAX_PIPS_RETROCESO) PIPS_RETROCESO_PROMEDIO, STATS_MODE(MAX_PIPS_RETROCESO) PIPS_RETROCESO_MODA,    
    COUNT(*) CANTIDAD
    FROM OPERACION P, PARAMETROS_ESTADISTICAS PARAM 
    WHERE P.PIPS>0 
    AND P.FECHA_CIERRE IS NOT NULL AND P.FECHA_CIERRE < PARAM.PARAM_FECHA    
    AND (WEEK_MINUTES(P.FECHA_CIERRE,P.FECHA_APERTURA))>=NVL(PARAM.PARAM_DURACION,0)
    AND ((PARAM.PARAM_RETROCESO IS NULL)
     OR (PARAM.PARAM_RETROCESO>0
      AND (P.PIPS>=PARAM.PARAM_RETROCESO))
     OR (PARAM.PARAM_RETROCESO<0
      AND P.MAX_PIPS_RETROCESO<=NVL(PARAM.PARAM_RETROCESO,0)))
	GROUP BY P.ID_INDIVIDUO) POSITIVOS ON POSITIVOS.ID_INDIVIDUO=IND.ID
  LEFT JOIN (SELECT N.ID_INDIVIDUO, SUM(N.PIPS) PIPS, MIN(N.PIPS) PIPS_MINIMOS, MAX(N.PIPS) PIPS_MAXIMOS, AVG(N.PIPS) PIPS_PROMEDIO, STATS_MODE(N.PIPS) PIPS_MODA,
    MIN(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MINIMA, MAX(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MAXIMA,
    AVG(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_PROMEDIO, STATS_MODE(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_MODA,
    STDDEV(WEEK_MINUTES(FECHA_CIERRE,FECHA_APERTURA)) DUR_DESV,
    MIN(MAX_PIPS_RETROCESO) PIPS_RETROCESO_MINIMO, MAX(MAX_PIPS_RETROCESO) PIPS_RETROCESO_MAXIMO, AVG(MAX_PIPS_RETROCESO) PIPS_RETROCESO_PROMEDIO, STATS_MODE(MAX_PIPS_RETROCESO) PIPS_RETROCESO_MODA,    
    COUNT(*) CANTIDAD
    FROM OPERACION N, PARAMETROS_ESTADISTICAS PARAM
		WHERE N.PIPS<=0 
    AND N.FECHA_CIERRE IS NOT NULL AND N.FECHA_CIERRE < PARAM.PARAM_FECHA
    AND (WEEK_MINUTES(N.FECHA_CIERRE,N.FECHA_APERTURA))>=NVL(PARAM.PARAM_DURACION,0)
    AND ((PARAM.PARAM_RETROCESO IS NULL)
      OR (PARAM.PARAM_RETROCESO<0
        AND(N.PIPS<=PARAM.PARAM_RETROCESO))
     OR (PARAM.PARAM_RETROCESO>0
      AND (N.MAX_PIPS_RETROCESO>=NVL(PARAM.PARAM_RETROCESO,0))))		
  GROUP BY N.ID_INDIVIDUO) NEGATIVOS ON NEGATIVOS.ID_INDIVIDUO=IND.ID
GROUP BY IND.ID;
/

CREATE OR REPLACE FORCE VIEW PARAMETROS_ESTADISTICAS AS
SELECT TO_NUMBER(P1.VALOR) PARAM_RETROCESO, TO_NUMBER(P2.VALOR) PARAM_DURACION, TO_DATE(P3.VALOR, 'YYYY/MM/DD HH24:MI') PARAM_FECHA
FROM PARAMETRO P1
	INNER JOIN PARAMETRO P2 ON P2.NOMBRE='DURACION_ESTADISTICAS'
	INNER JOIN PARAMETRO P3 ON P3.NOMBRE='FECHA_ESTADISTICAS'
WHERE P1.NOMBRE='RETROCESO_ESTADISTICAS';
/

ALTER TABLE TENDENCIA  
MODIFY (TIPO_CALCULO VARCHAR2(50) );
/

INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('DURACION_ESTADISTICAS', NULL, SYSDATE);
INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('FECHA_ESTADISTICAS', NULL, SYSDATE);
INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('RETROCESO_ESTADISTICAS', NULL, SYSDATE);
COMMIT;

INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('TIPOS_OPERACION', NULL, SYSDATE);
INSERT INTO PARAMETRO (NOMBRE, VALOR, FECHA) VALUES ('DIAS_INDIVIDUO_PERIODO', NULL, SYSDATE);
DELETE FROM PARAMETRO WHERE NOMBRE='MESES_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2015/12/21 23:59' WHERE NOMBRE='FECHA_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='2016/01/25 22:00' WHERE NOMBRE='FECHA_FIN_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='7' WHERE NOMBRE='DIAS_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='30000' WHERE NOMBRE='MINIMO_INCLUSIONES';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='7' WHERE NOMBRE='DIAS_ROTACION_INDIVIDUO_PERIODO';
UPDATE PARAMETRO SET FECHA=SYSDATE, VALOR='SELL' WHERE NOMBRE='TIPOS_OPERACION';
COMMIT;

create or replace FUNCTION WEEK_MINUTES(
              in_end_dt IN DATE DEFAULT SYSDATE ,
                in_start_dt IN DATE DEFAULT SYSDATE )
            RETURN NUMBER DETERMINISTIC
          IS
            d          NUMBER; 
            END_DATE     DATE := GREATEST (in_start_dt, in_end_dt); 
            return_val NUMBER;  
            START_DATE   DATE := LEAST (in_start_dt, in_end_dt); 
          BEGIN          
						WITH ALL_DATES AS	(SELECT (END_DATE-START_DATE)*24 HORAS_TOTALES FROM DUAL),
							WEEKEND_DATES AS 
							(
								SELECT START_DATE, END_DATE, TO_CHAR(START_DATE+LEVEL-1, 'HH24') HORAS,
								START_DATE+LEVEL-1 WEEK_DAY
								FROM DUAL
								CONNECT BY START_DATE+LEVEL-1 <= END_DATE
								)
								SELECT ROUND((AD.HORAS_TOTALES-HORAS)*60) 
								INTO return_val
								FROM ALL_DATES AD,
								(
									SELECT COUNT(*)*24 HORAS
									FROM WEEKEND_DATES WHERE TO_CHAR(WEEK_DAY, 'DY', 'nls_date_language=AMERICAN') IN ('SUN', 'SAT')
								)
						;
          IF in_start_dt > in_end_dt THEN
            return_val  := -return_val;
          END IF;
          RETURN return_val;
        END WEEK_MINUTES ;
/

ALTER TABLE DATOHISTORICO 
ADD PARTITION DH_ANYO_2017 VALUES LESS THAN (TO_DATE('2018-01-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'));
/

create or replace FUNCTION MULTIPLE_NVL(
              IN_VAL1 IN NUMBER DEFAULT NULL,
							IN_VAL2 IN NUMBER DEFAULT NULL,
							IN_VAL3 IN NUMBER DEFAULT NULL,
							IN_VAL4 IN NUMBER DEFAULT NULL,
							IN_VAL5 IN NUMBER DEFAULT NULL
                 ) RETURN NUMBER DETERMINISTIC
		IS
			return_val NUMBER;  
		BEGIN         
			RETURN (NVL(IN_VAL1,NVL(IN_VAL2,NVL(IN_VAL3,NVL(IN_VAL4,NVL(IN_VAL5,0))))));
		RETURN return_val;
END MULTIPLE_NVL;
/

CREATE INDEX TENDENCIA_IDX1 ON TENDENCIA (FECHA_BASE DESC, FECHA_TENDENCIA ASC, TRUNC(FECHA_TENDENCIA, 'HH24') ASC,
	WEEK_MINUTES(FECHA_TENDENCIA, FECHA_BASE) DESC, TIPO_TENDENCIA);
/

CREATE TABLE TENDENCIA_PARA_OPERAR 
(
TIPO_EXPORTACION VARCHAR2(200) NOT NULL
,  PERIODO VARCHAR2(20) NOT NULL 
, TIPO_OPERACION VARCHAR2(50) NOT NULL 
, FECHA_BASE DATE NOT NULL 
, FECHA_TENDENCIA DATE NOT NULL 
, VIGENCIA_LOWER DATE NOT NULL 
, VIGENCIA_HIGHER DATE NOT NULL 
, PRECIO_CALCULADO NUMBER (10,5)
, TAKE_PROFIT NUMBER (10,5)
, STOP_LOSS NUMBER (10,5)
, TIEMPO_TENDENCIA NUMBER (10,5)
, R2 NUMBER (10,5)
, PENDIENTE NUMBER (10,5)
, DESVIACION NUMBER (10,5)
, MIN_PRECIO NUMBER (10,5)
, MAX_PRECIO NUMBER (10,5)
, CANTIDAD NUMBER (10,5)
, FECHA DATE
);

ALTER TABLE TENDENCIA_PARA_OPERAR
ADD CONSTRAINT TENDENCIA_PARA_OPERAR_PK PRIMARY KEY 
(
TIPO_EXPORTACION
,  PERIODO 
, FECHA_BASE 
, TIPO_OPERACION 
)
ENABLE;

ALTER TABLE TENDENCIA_PARA_OPERAR 
ADD (ACTIVA NUMBER(1) );
