 
DROP MATERIALIZED VIEW OPERACION_POSITIVAS;

CREATE MATERIALIZED VIEW OPERACION_POSITIVAS AS 
  SELECT OPER.TAKE_PROFIT, OPER.STOP_LOSS, OPER.FECHA_APERTURA, OPER.PIPS, OPER.MAX_PIPS_RETROCESO, OPER.OPEN_PRICE
  FROM OPERACION OPER
  WHERE OPER.PIPS>=200 AND OPER.FECHA_CIERRE IS NOT NULL
  --AND OPER.TAKE_PROFIT>=100 AND OPER.STOP_LOSS>=100
  AND OPER.FECHA_APERTURA BETWEEN TO_DATE('20120101', 'YYYYMMDD') AND TO_DATE('20160501', 'YYYYMMDD');

DROP MATERIALIZED VIEW OPERACION_NEGATIVAS;

CREATE MATERIALIZED VIEW OPERACION_NEGATIVAS AS 
  SELECT OPER.TAKE_PROFIT, OPER.STOP_LOSS, OPER.FECHA_APERTURA, OPER.PIPS, OPER.MAX_PIPS_RETROCESO, OPER.OPEN_PRICE
  FROM OPERACION OPER
  WHERE OPER.PIPS<=-200 AND OPER.FECHA_CIERRE IS NOT NULL
  --AND OPER.TAKE_PROFIT>=100 AND OPER.STOP_LOSS>=100
  AND OPER.FECHA_APERTURA BETWEEN TO_DATE('20120101', 'YYYYMMDD') AND TO_DATE('20160501', 'YYYYMMDD');


