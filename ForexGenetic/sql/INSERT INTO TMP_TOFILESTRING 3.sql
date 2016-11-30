SET feedback ON;
SET ECHO ON;
SPOOL ON;

TRUNCATE TABLE TMP_TOFILESTRING2;

--SELECT COUNT(*) FROM TMP_TOFILESTRING2;

INSERT INTO TMP_TOFILESTRING2 (ID_INDIVIDUO, CRITERIO_ORDER1, CRITERIO_ORDER2, VIGENCIA1, VIGENCIA2)
	SELECT OPER.ID_INDIVIDUO, OPER.PIPS, OPER.PIPS, 
		OPER.FECHA_SEMANA, OPER.FECHA_SEMANA+7
	FROM OPERACIONES_ACUM_SEMANA_CONSOL OPER
  INNER JOIN 
  (SELECT FECHA_SEMANA, MAX(PIPS) PIPS FROM OPERACIONES_ACUM_SEMANA_CONSOL
  WHERE FECHA_SEMANA>=TO_DATE('20140101', 'YYYYMMDD')
  GROUP BY FECHA_SEMANA
  ) TMP ON OPER.FECHA_SEMANA=TMP.FECHA_SEMANA AND OPER.PIPS=TMP.PIPS
;

commit;

@"d:\ricardorq85\Informacion\FOREX\DATABASE\ToFileStringFromTEMP_TOFILESTRING.sql";