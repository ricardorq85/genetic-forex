CREATE TABLE PROCESO
(ID_INDIVIDUO VARCHAR2(50), FECHA_HISTORICO DATE, FECHA_PROCESO DATE);

alter table "FOREX"."PROCESO" add constraint PROCESO_PK primary key("ID_INDIVIDUO","FECHA_PROCESO") ;