DECLARE
COUNTER number;
BEGIN  
  --SELECT COUNT(*) INTO COUNTER FROM DATOHISTORICO WHERE FECHA_REGISTRO IS NULL AND ROWNUM<2;
  --WHILE (COUNTER>0) LOOP
  WHILE(TRUE) LOOP
    UPDATE DATOHISTORICO SET FECHA_REGISTRO=SYSDATE-2 WHERE FECHA_REGISTRO IS NULL AND ROWNUM<100000;
    COMMIT;
  END LOOP;
END;


--SELECT COUNT(*) FROM DH_TEMPORAL WHERE FECHA_REGISTRO IS NULL ;

