SELECT COUNT(*) FROM TMP_TOFILESTRING2;

SELECT OPER.* FROM OPERACION OPER 
    INNER JOIN TMP_TOFILESTRING2 TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO 
    AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 
    WHERE OPER.FECHA_APERTURA>TO_DATE('20130617', 'YYYYMMDD') 
      AND OPER.FECHA_APERTURA<=TO_DATE('20130624', 'YYYYMMDD') 
    AND OPER.FECHA_CIERRE IS NOT NULL 
    ORDER BY OPER.FECHA_APERTURA ASC, TFS.CRITERIO_ORDER1 DESC, TFS.CRITERIO_ORDER2 DESC;