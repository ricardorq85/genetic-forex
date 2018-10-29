drop table "FOREX"."BCK_TPO";
drop table "FOREX"."BCK_TPO2";

--materialized view log
drop materialized view log on "FOREX"."FILTERED_EOP";
drop materialized view log on "FOREX"."FILTERED_PARALELAS_EOP";
drop materialized view log on "FOREX"."FILTERED_PTFS";
drop materialized view log on "FOREX"."PREVIO_TOFILESTRING";
drop materialized view log on "FOREX"."TENDENCIA";

--VISTAS MATERIALIZADAS
drop materialized view "FOREX"."FILTERED_EOP";
drop materialized view "FOREX"."FILTERED_PARA_OPERAR_BOTH";
drop materialized view "FOREX"."FILTERED_PARA_OPERAR_BUY";
drop materialized view "FOREX"."FILTERED_PARA_OPERAR_SELL";
drop materialized view "FOREX"."FILTERED_PARALELAS_EOP";
drop materialized view "FOREX"."FILTERED_PTFS";
drop materialized view "FOREX"."TENDENCIA_ULTIMOMES";

drop table "FOREX"."FILTERED_PARA_OPERAR_BOTH";
drop table "FOREX"."FILTERED_PARA_OPERAR_BUY";
drop table "FOREX"."FILTERED_PARA_OPERAR_SELL";
drop table "FOREX"."FILTERED_PARALELAS_EOP";
drop table "FOREX"."FILTERED_PTFS";
drop table "FOREX"."TENDENCIA_ULTIMOMES";

drop table "FOREX"."MLOG$_ESTRATEGIA_OPERACION";

truncate table "OPERACIONES_ACUM_SEMANA_ANYO" ;
truncate table "OPERACIONES_ACUM_SEMANA_CONSOL";
truncate table "OPERACIONES_ACUM_SEMANA_MES";
truncate table "OPERACION_ESTRATEGIA_PERIODO";
truncate table "PREVIO_TOFILESTRING";
