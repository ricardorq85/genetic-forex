SELECT SYSDATE FROM DUAL;
/
DECLARE

BEGIN
  FOR i IN 1..1000 LOOP
    --SELECT SYSDATE FROM DUAL;
    DELETE FROM ESTRATEGIA_OPERACION_PERIODO WHERE CANTIDAD=0 AND ROWNUM<1000; -- AND FECHA<SYSDATE-10;
    COMMIT;
    --SELECT SYSDATE FROM DUAL;
  END LOOP;
END;
/
SELECT SYSDATE FROM DUAL;
/
SELECT COUNT(*) FROM ESTRATEGIA_OPERACION_PERIODO WHERE CANTIDAD=0;
/
SELECT SYSDATE FROM DUAL;
/
