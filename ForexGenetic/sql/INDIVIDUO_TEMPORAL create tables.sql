CREATE TABLE INDIVIDUO_TEMPORAL AS
SELECT * FROM INDIVIDUO IND INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID
WHERE IND.TIPO_INDIVIDUO='INDICADORES';

CREATE TABLE INDICADOR_INDIVIDUO_TEMPORAL AS
SELECT II.* FROM INDICADOR_INDIVIDUO II INNER JOIN PROCESO P ON P.ID_INDIVIDUO=II.ID_INDIVIDUO;

CREATE TABLE INDIVIDUO_INDICADORES_TEMPORAL AS
SELECT IC.* FROM INDIVIDUO_INDICADORES IC INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IC.ID
WHERE IC.TIPO_INDIVIDUO='INDICADORES';