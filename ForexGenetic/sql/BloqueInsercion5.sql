DECLARE
  FECHA_INICIAL DATE;
  FECHA_MAX_HISTORIA DATE;
  FECHA_MAX_HIST_PROCESO DATE;
  REG_INDIVIDUO FOREX.INDIVIDUO%ROWTYPE;
  COUNT_DATE NUMBER;
  REG_INDIVIDUO_OPERACION FOREX.OPERACION_BASE%ROWTYPE;

  CURSOR C_INDIVIDUOS(FECHA_OPERACIONES IN DATE) IS
    SELECT IND.*
    FROM FOREX.INDIVIDUO IND WHERE IND.ID NOT IN (SELECT ID_INDIVIDUO FROM OPERACION_BASE WHERE FECHA_APERTURA=FECHA_OPERACIONES)
    AND IND.ID NOT IN (SELECT ID_INDIVIDUO FROM PROCESO WHERE FECHA_HISTORICO=FECHA_OPERACIONES)
    AND IND.ID='1344092068979.92'
    ORDER BY IND.ID DESC;

  CURSOR C_INDIVIDUOS_OPERACION IS
    SELECT DISTINCT ID_INDIVIDUO FROM FOREX.OPERACION_BASE OPERB
      WHERE NOT EXISTS (SELECT 1 FROM OPERACION OP WHERE OP.ID_INDIVIDUO=OPERB.ID_INDIVIDUO 
        AND OP.FECHA_APERTURA=OBTENER_FECHA_OPERACION(OPERB.FECHA_APERTURA))
        ORDER BY ID_INDIVIDUO;
  
BEGIN
  --DBMS_OUTPUT.PUT_LINE('RRQ -3');
  FECHA_INICIAL := TO_DATE('2008/07/10 09:16','YYYY/MM/DD HH24:MI');
  --SELECT TO_DATE(VALOR, 'YYYY/MM/DD HH24:MI') INTO FECHA_INICIAL FROM FOREX.PARAMETRO WHERE NOMBRE='INSERCION_OPERACIONES';
  SELECT MAX(FECHA) INTO FECHA_MAX_HISTORIA FROM FOREX.DATOHISTORICO;
  SELECT MAX(FECHA_HISTORICO) INTO FECHA_MAX_HIST_PROCESO FROM FOREX.PROCESO;

  --FECHA_MAX_HISTORIA := TO_DATE('2008/06/01 00:00','YYYY/MM/DD HH24:MI');

  WHILE(FECHA_INICIAL < FECHA_MAX_HISTORIA) LOOP 
    IF (FECHA_MAX_HIST_PROCESO <> FECHA_INICIAL) THEN
      DELETE FROM FOREX.PROCESO;
      COMMIT;
    END IF;
    --DBMS_OUTPUT.PUT_LINE('RRQ -2 FECHA_INICIAL='||FECHA_INICIAL);
    
    SELECT COUNT(*) INTO COUNT_DATE FROM FOREX.DATOHISTORICO DH WHERE DH.FECHA=FECHA_INICIAL;
      --WHERE TO_CHAR(DH.FECHA,'YYYY/MM/DD HH24:MI')=TO_CHAR(FECHA_INICIAL,'YYYY/MM/DD HH24:MI');
    --DBMS_OUTPUT.PUT_LINE('RRQ -1 COUNT_DATE='||COUNT_DATE);
    UPDATE FOREX.PARAMETRO SET VALOR=FECHA_INICIAL,FPARAMETRO=SYSDATE WHERE NOMBRE='INSERCION_OPERACIONES';
    COMMIT;

    IF(COUNT_DATE>0) THEN    
      FOR REG_INDIVIDUO IN C_INDIVIDUOS(FECHA_INICIAL) LOOP 
      
        INSERT INTO OPERACION_BASE(ID_INDIVIDUO, FECHA_APERTURA, FECHA_CIERRE) 
          SELECT V_OPERACIONES.ID, V_OPERACIONES.OPEN_FECHA, V_OPERACIONES.CLOSE_FECHA
          FROM V_OPERACIONES WHERE V_OPERACIONES.OPEN_FECHA=FECHA_INICIAL
          AND V_OPERACIONES.ID=REG_INDIVIDUO.ID
          AND NOT EXISTS (
            SELECT 1 FROM OPERACION_BASE OP WHERE OP.ID_INDIVIDUO=V_OPERACIONES.ID 
            AND OP.FECHA_APERTURA=V_OPERACIONES.OPEN_FECHA AND OP.FECHA_CIERRE=V_OPERACIONES.CLOSE_FECHA
            )
          AND NOT EXISTS (
            SELECT 1 FROM OPERACION_BASE OP2 WHERE OP2.ID_INDIVIDUO=V_OPERACIONES.ID 
            AND V_OPERACIONES.OPEN_FECHA > OP2.FECHA_APERTURA AND V_OPERACIONES.OPEN_FECHA <= OP2.FECHA_CIERRE
            );
            --DBMS_OUTPUT.PUT_LINE('RRQ 1');
            
        INSERT INTO PROCESO(ID_INDIVIDUO, FECHA_HISTORICO, FECHA_PROCESO)
        VALUES (REG_INDIVIDUO.ID, FECHA_INICIAL, SYSDATE);
            
        COMMIT;          
        
      END LOOP;
    END IF;
        
    SELECT NVL(MAX(FECHA_CIERRE),FECHA_INICIAL+(1/1440)) INTO FECHA_INICIAL FROM OPERACION_BASE WHERE ID_INDIVIDUO='1344092068979.92';
    --DBMS_OUTPUT.PUT_LINE('RRQ 2 FECHA_INICIAL='||FECHA_INICIAL);

    FOR REG_INDIVIDUO_OPERACION IN C_INDIVIDUOS_OPERACION LOOP
      
      INSERT INTO OPERACION(ID_INDIVIDUO, TAKE_PROFIT, STOP_LOSS, FECHA_APERTURA, FECHA_CIERRE, SPREAD, OPEN_PRICE, PIPS)
        SELECT OPCOM.ID_INDIVIDUO, OPCOM.TAKE_PROFIT, OPCOM.STOP_LOSS, 
          OBTENER_FECHA_OPERACION(OPCOM.FECHA_APERTURA), OBTENER_FECHA_OPERACION(OPCOM.FECHA_CIERRE), OPCOM.SPREAD, OPCOM.OPEN_PRICE, OPCOM.PIPS
        FROM OPERACIONES_COMPLETAS OPCOM
        WHERE NOT EXISTS (SELECT 1 FROM OPERACION OP WHERE OP.ID_INDIVIDUO=OPCOM.ID_INDIVIDUO 
              AND OP.FECHA_APERTURA=OBTENER_FECHA_OPERACION(OPCOM.FECHA_APERTURA))
        AND OPCOM.ID_INDIVIDUO=REG_INDIVIDUO_OPERACION.ID_INDIVIDUO;
        COMMIT;
    END LOOP;

  END LOOP;

END;
/