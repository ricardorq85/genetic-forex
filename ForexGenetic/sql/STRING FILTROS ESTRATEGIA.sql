-- STRING FILTROS ESTRATEGIA
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY EOP.PIPS_TOTALES DESC, EOP.PIPS_TOTALES_PARALELAS DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY EOP.PIPS_TOTALES_PARALELAS DESC, EOP.PIPS_TOTALES DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_TOTALES*0.7+EOP.PIPS_TOTALES_PARALELAS*0.3) DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000  
  AND CANTIDAD>0
  ORDER BY (EOP.CANTIDAD_PARALELAS/(EOP.CANTIDAD+1)) ASC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000  
  ORDER BY (EOP.PIPS_AGRUPADO_MINUTOS+EOP.PIPS_AGRUPADO_HORAS+EOP.PIPS_AGRUPADO_DIAS)/3 DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_TOTALES*0.4
    +EOP.PIPS_TOTALES_PARALELAS*0.2
    +EOP.PIPS_AGRUPADO_MINUTOS*0.15+EOP.PIPS_AGRUPADO_HORAS*0.15+EOP.PIPS_AGRUPADO_DIAS*0.1) DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_AGRUPADO_MINUTOS) DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_AGRUPADO_HORAS) DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  --AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  --AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY (EOP.PIPS_AGRUPADO_DIAS) DESC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  --AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  --AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)  
  --AND ID>143000
ORDER BY ABS(EOP.PIPS_TOTALES-EOP.PIPS_TOTALES_PARALELAS) ASC)
WHERE ROWNUM<6;
SELECT * FROM (   SELECT ' OR ( OPER_SEMANA.PIPS>'||FILTRO_PIPS_X_SEMANA||' AND OPER_MES.PIPS>'||FILTRO_PIPS_X_MES ||' AND OPER_ANYO.PIPS>'||FILTRO_PIPS_X_ANYO ||' AND OPER.PIPS>'||FILTRO_PIPS_TOTALES||')' 
  FROM ESTRATEGIA_OPERACION_PERIODO EOP WHERE ID>=150086
  --AND PIPS_TOTALES_PARALELAS>0 AND PIPS_TOTALES>0
  --AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)
  --AND ID>143000
  ORDER BY EOP.PIPS_TOTALES DESC, EOP.PIPS_TOTALES_PARALELAS DESC)
WHERE ROWNUM<6;
