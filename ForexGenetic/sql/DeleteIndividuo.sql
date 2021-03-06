DELETE FROM OPERACIONES_ACUM_SEMANA_CONSOL OPX
WHERE OPX.ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL
--WHERE CREATION_DATE>TO_DATE('2016/10/12 00:00', 'YYYY/MM/DD HH24:MI')
)
--AND OPX.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
 ;

DELETE FROM OPERACIONES_ACUM_SEMANA_ANYO OPX
WHERE OPX.ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL)
--AND OPX.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
 ;

DELETE FROM OPERACIONES_ACUM_SEMANA_MES OPX
WHERE OPX.ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL)
--AND OPX.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
 ;

DELETE FROM OPERACION_X_SEMANA OPX
WHERE OPX.ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL)
--AND OPX.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
 ;

DELETE FROM PROCESO WHERE ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL);

DELETE FROM OPERACION OP WHERE OP.ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL)
--AND OP.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
;

DELETE FROM INDIVIDUO_INDICADORES II WHERE II.ID IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL)
--AND II.ID NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
;

DELETE FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO IN (SELECT ID FROM INDIVIDUO WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL)
--AND II.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM PROCESO P)
;

--
--
--DELETE FROM OPERACION_ESTRATEGIA_PERIODO WHERE ID_INDIVIDUO IN (
--  SELECT IND.ID FROM INDIVIDUO IND 
--  --WHERE CREATION_DATE>TO_DATE('2016/02/27 00:00', 'YYYY/MM/DD HH24:MI')
--  WHERE IND.TIPO_INDIVIDUO IS NULL
--)
--;

DELETE INDIVIDUO IND WHERE ID LIKE '1482338659329.%' AND PARENT_ID_1 IS NOT NULL
--WHERE IND.CREATION_DATE>TO_DATE('2016/10/12 00:00', 'YYYY/MM/DD HH24:MI')
--AND IND.ID NOT IN (SELECT P.ID_INDIVIDUO FROM PROCESO P)
;

--COMMIT;
--
--SELECT IND.ID FROM INDIVIDUO IND 
----WHERE CREATION_DATE>TO_DATE('2016/02/27 00:00', 'YYYY/MM/DD HH24:MI')
--WHERE IND.TIPO_INDIVIDUO IS NULL
--;


