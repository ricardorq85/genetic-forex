DELETE FROM FOREX.OPERACION WHERE ID_INDIVIDUO IN (SELECT IND.ID FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL);
DELETE FROM FOREX.OPERACION_BASE WHERE ID_INDIVIDUO IN (SELECT IND.ID FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL);
DELETE FROM FOREX.PROCESO WHERE ID_INDIVIDUO IN (SELECT IND.ID FROM INDIVIDUO IND WHERE IND.CREATION_DATE IS NULL);
COMMIT;
