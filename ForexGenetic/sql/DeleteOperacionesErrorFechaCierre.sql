
DELETE FROM OPERACION WHERE ID_INDIVIDUO IN (
SELECT OPER.ID_INDIVIDUO FROM OPERACION OPER
WHERE OPER.FECHA_CIERRE IS NULL AND OPER.FECHA_APERTURA < 
  (SELECT MAX(OPER2.FECHA_APERTURA) FROM OPERACION OPER2 WHERE OPER2.ID_INDIVIDUO=OPER2.ID_INDIVIDUO)
  );

commit;