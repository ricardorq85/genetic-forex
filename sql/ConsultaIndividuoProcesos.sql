SELECT * FROM (SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NOT NULL AND IND.CREATION_DATE BETWEEN TO_DATE('2012/06/07', 'YYYY/MM/DD') AND TO_DATE('2012/08/06', 'YYYY/MM/DD')
GROUP BY IND.ID 
ORDER BY IND.ID DESC)
WHERE ROWNUM<1000;

SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NOT NULL AND IND.CREATION_DATE BETWEEN TO_DATE('2012/04/07', 'YYYY/MM/DD') AND TO_DATE('2012/06/07', 'YYYY/MM/DD')
GROUP BY IND.ID 
ORDER BY IND.ID DESC ;

SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NOT NULL AND IND.CREATION_DATE BETWEEN TO_DATE('2012/02/07', 'YYYY/MM/DD') AND TO_DATE('2012/04/07', 'YYYY/MM/DD')
GROUP BY IND.ID 
ORDER BY IND.ID DESC ;

SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NOT NULL AND IND.CREATION_DATE BETWEEN TO_DATE('2011/10/01', 'YYYY/MM/DD') AND TO_DATE('2011/12/07', 'YYYY/MM/DD')
GROUP BY IND.ID 
ORDER BY IND.ID DESC ;

SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NOT NULL AND IND.PARENT_ID_2 IS NOT NULL
GROUP BY IND.ID 
ORDER BY IND.ID DESC ;

SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NOT NULL AND IND.PARENT_ID_2 IS NULL
GROUP BY IND.ID 
ORDER BY IND.ID DESC ;



