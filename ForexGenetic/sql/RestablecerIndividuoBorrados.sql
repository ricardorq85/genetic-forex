INSERT INTO FOREX.INDIVIDUO (ID, PARENT_ID_1, PARENT_ID_2, TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE)
  SELECT ID, PARENT_ID_1, PARENT_ID_2, TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE
  FROM FOREX.INDIVIDUO_BORRADO IND WHERE IND.TIPO_BORRADO='ANTIGUO'; 
INSERT INTO FOREX.INDICADOR_INDIVIDUO (ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO)
  SELECT ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO 
  FROM FOREX.INDICADOR_INDIVIDUO_BORRADO IND WHERE IND.ID_INDIVIDUO IN (
    SELECT IND2.ID
    FROM FOREX.INDIVIDUO_BORRADO IND2 WHERE IND2.TIPO_BORRADO='ANTIGUO'
  ); 
COMMIT;
DELETE FROM FOREX.INDICADOR_INDIVIDUO_BORRADO IND WHERE IND.ID_INDIVIDUO IN (
    SELECT IND2.ID
    FROM FOREX.INDIVIDUO_BORRADO IND2 WHERE IND2.TIPO_BORRADO='ANTIGUO'
  ); 
DELETE FROM FOREX.INDIVIDUO_BORRADO IND WHERE IND.TIPO_BORRADO='ANTIGUO'; 
COMMIT;
