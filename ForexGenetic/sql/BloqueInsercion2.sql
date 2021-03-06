DECLARE
  FECHA_INICIAL DATE;
  FECHA_MAX_HISTORIA DATE;
  REG_OPERACION FOREX.OPERACIONES%ROWTYPE;

  CURSOR C_OPERACIONES (FECHA_OPERACIONES IN DATE) IS
    SELECT V_OPERACIONES.ID, V_OPERACIONES.OPEN_FECHA, V_OPERACIONES.CLOSE_FECHA 
    FROM FOREX.V_OPERACIONES WHERE V_OPERACIONES.OPEN_FECHA = FECHA_OPERACIONES
    AND NOT EXISTS (
      SELECT 1 FROM FOREX.OPERACIONES OP WHERE OP.ID_INDIVIDUO=V_OPERACIONES.ID 
      AND OP.FECHA_APERTURA=V_OPERACIONES.OPEN_FECHA AND OP.FECHA_CIERRE=V_OPERACIONES.CLOSE_FECHA
      )
    AND NOT EXISTS (
      SELECT 1 FROM FOREX.V_OPERACIONES V_OPER2 WHERE V_OPER2.ID=V_OPERACIONES.ID 
      AND V_OPERACIONES.OPEN_FECHA > V_OPER2.OPEN_FECHA AND V_OPERACIONES.OPEN_FECHA <= V_OPER2.CLOSE_FECHA
      );
  
BEGIN
  DBMS_OUTPUT.PUT_LINE('BEGIN');
  FECHA_INICIAL := TO_DATE('2008/05/06 17:14','YYYY/MM/DD HH24:MI');
  --SELECT MAX(FECHA) INTO FECHA_MAX_HISTORIA FROM FOREX.DATOHISTORICO;
  FECHA_MAX_HISTORIA := TO_DATE('2008/06/01 00:00','YYYY/MM/DD HH24:MI');
  DBMS_OUTPUT.PUT_LINE('BEGIN2');

  WHILE(FECHA_INICIAL < FECHA_MAX_HISTORIA) LOOP 
    --DBMS_OUTPUT.PUT_LINE('FECHA '||FECHA_INICIAL);
    FOR REG_OPERACION IN C_OPERACIONES(FECHA_INICIAL) LOOP
      INSERT INTO FOREX.OPERACIONES(ID_INDIVIDUO, FECHA_APERTURA, FECHA_CIERRE) 
      VALUES (REG_OPERACION.ID, REG_OPERACION.OPEN_FECHA, REG_OPERACION.CLOSE_FECHA);
      COMMIT;    
    END LOOP;
    FECHA_INICIAL := FECHA_INICIAL+0.001;    
  END LOOP;

END;
/