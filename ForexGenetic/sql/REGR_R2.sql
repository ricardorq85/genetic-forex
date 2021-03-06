SELECT --OPER.ID_INDIVIDUO, 
  REGR_R2((SYSDATE-FECHA_APERTURA), (OPER.SU)) --OVER (PARTITION BY OPER.ID_INDIVIDUO) 
    REG_R2
 FROM (SELECT (
  SELECT SUM(OP2.PIPS) S FROM OPERACION OP2 
  WHERE OP2.ID_INDIVIDUO=OP.ID_INDIVIDUO AND OP2.FECHA_APERTURA<=OP.FECHA_APERTURA
  ) SU, 
  OP.*
FROM OPERACION OP
WHERE OP.ID_INDIVIDUO='1422746069706.51'
ORDER BY OP.FECHA_APERTURA ASC
) OPER
--GROUP BY OPER.ID_INDIVIDUO
;

SELECT (SELECT SUM(OP2.PIPS) S FROM OPERACION OP2 WHERE OP2.ID_INDIVIDUO=OP.ID_INDIVIDUO AND OP2.FECHA_APERTURA<=OP.FECHA_APERTURA) SU, OP.*
FROM OPERACION OP
WHERE OP.ID_INDIVIDUO='1422746069706.38'
;
