/*INSERT INTO INDIVIDUO_BORRADO(ID, PARENT_ID_1, PARENT_ID_2, TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE, TIPO_BORRADO, FECHA_BORRADO)
  SELECT IND.ID, IND.PARENT_ID_1, IND.PARENT_ID_2, IND.TAKE_PROFIT, IND.STOP_LOSS, IND.LOTE, IND.INITIAL_BALANCE, IND.CREATION_DATE, 'ANTIGUO', SYSDATE
    FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NULL AND IND.PARENT_ID_2 IS NULL;
COMMIT;
INSERT INTO INDICADOR_INDIVIDUO_BORRADO(ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO)
  SELECT ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO
  FROM INDICADOR_INDIVIDUO WHERE ID_INDIVIDUO IN (
    SELECT IND.ID
    FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NULL AND IND.PARENT_ID_2 IS NULL);
COMMIT;*/

DELETE FROM INDICADOR_INDIVIDUO WHERE ID_INDIVIDUO IN (
    SELECT IND.ID
    FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NULL AND IND.PARENT_ID_2 IS NULL);
DELETE FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NULL AND IND.PARENT_ID_2 IS NULL;
COMMIT;
