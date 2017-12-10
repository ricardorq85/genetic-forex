DECLARE
REG_INDIVIDUO INDIVIDUOS_PADRES%ROWTYPE;
CURSOR C_PADRES IS
  SELECT * FROM INDIVIDUOS_PADRES;
BEGIN

FOR REG_INDIVIDUO IN C_PADRES LOOP  
  UPDATE INDIVIDUO_BORRADO SET ID_INDIVIDUO_BASE=REG_INDIVIDUO.ID_PADRE
    WHERE ID=REG_INDIVIDUO.ID_HIJO;
  COMMIT;
END LOOP;

END;
/