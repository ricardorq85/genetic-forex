UPDATE OPERACIONES_ACUM_SEMANA_MES OPER SET FECHA_HISTORICO=(SELECT MAX(P.FECHA_HISTORICO) FROM PROCESO P
  WHERE P.ID_INDIVIDUO=OPER.ID_INDIVIDUO)
;
COMMIT;
UPDATE OPERACIONES_ACUM_SEMANA_ANYO OPER SET FECHA_HISTORICO=(SELECT MAX(P.FECHA_HISTORICO) FROM PROCESO P
  WHERE P.ID_INDIVIDUO=OPER.ID_INDIVIDUO)
;
COMMIT;
UPDATE OPERACIONES_ACUM_SEMANA_CONSOL OPER SET FECHA_HISTORICO=(SELECT MAX(P.FECHA_HISTORICO) FROM PROCESO P
  WHERE P.ID_INDIVIDUO=OPER.ID_INDIVIDUO)
;
COMMIT;

SELECT * FROM OPERACIONES_ACUM_SEMANA_CONSOL OPER
WHERE OPER.ID_INDIVIDUO='1341279489283.202893';