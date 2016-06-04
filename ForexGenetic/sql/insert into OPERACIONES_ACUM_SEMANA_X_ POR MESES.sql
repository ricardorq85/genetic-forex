DECLARE
  minDate DATE;
  maxDate DATE;
  currentDate DATE;

BEGIN
  SELECT TRUNC(TO_DATE('20110101','YYYYMMDD'),'IW'), TRUNC(MAX(FECHA),'IW') INTO minDate, maxDate FROM DATOHISTORICO;
  currentDate := minDate;
  
  WHILE currentDate <= maxDate LOOP
	INSERT INTO OPERACIONES_ACUM_SEMANA_MES (ID_INDIVIDUO, FECHA_SEMANA, PIPS, CANTIDAD, FECHA_HISTORICO)
	SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
	MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
	FROM SEMANAS
		INNER JOIN OPERACION_X_SEMANA TMP
			ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-1)
			AND TMP.FECHA_SEMANA<=SEMANAS.FECHA_SEMANA
		INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO  		  
	WHERE SEMANAS.FECHA_SEMANA=currentDate
	AND SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')
	GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA
	HAVING SUM(TMP.PIPS)>=-1000;
	COMMIT;
	
	INSERT INTO OPERACIONES_ACUM_SEMANA_ANYO (ID_INDIVIDUO, FECHA_SEMANA, PIPS, CANTIDAD, FECHA_HISTORICO)
	SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
	MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
	FROM SEMANAS		
		INNER JOIN OPERACION_X_SEMANA TMP
			ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,-12)
			AND TMP.FECHA_SEMANA<=SEMANAS.FECHA_SEMANA
		INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO  
	WHERE SEMANAS.FECHA_SEMANA=currentDate
	AND SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')
	GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA
	HAVING SUM(TMP.PIPS)>=-2000;
	COMMIT;

    INSERT INTO OPERACIONES_ACUM_SEMANA_CONSOL (ID_INDIVIDUO, FECHA_SEMANA, PIPS, CANTIDAD, FECHA_HISTORICO)
      SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,
      MAX(P.FECHA_HISTORICO) FECHA_HISTORICO
      FROM SEMANAS
        INNER JOIN OPERACION_X_SEMANA TMP
			ON TMP.FECHA_SEMANA<=SEMANAS.FECHA_SEMANA
		INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO  
      WHERE SEMANAS.FECHA_SEMANA=currentDate
	  AND SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')
      GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA
      HAVING SUM(TMP.PIPS)>=-3000;   
	COMMIT;
	
	currentDate := (currentDate+7);
  END LOOP;
END;
/