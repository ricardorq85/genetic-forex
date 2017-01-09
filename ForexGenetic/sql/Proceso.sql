SELECT ID_INDIVIDUO, FECHA_HISTORICO, TO_CHAR(FECHA_PROCESO, 'YYYY/MM/DD HH24:MI:SS') FECHA_PROCESO FROM FOREX.PROCESO ORDER BY FECHA_PROCESO DESC;
SELECT ID_INDIVIDUO, FECHA_HISTORICO, TO_CHAR(FECHA_PROCESO, 'YYYY/MM/DD HH24:MI:SS') FECHA_PROCESO FROM FOREX.PROCESO ORDER BY FECHA_HISTORICO ASC;
SELECT TO_CHAR(FECHA_PROCESO, 'YYYYMMDD HH24') DIA, COUNT(DISTINCT ID_INDIVIDUO), MIN(FECHA_PROCESO) MINFECHA, MAX(FECHA_PROCESO) MAXFECHA, 
  ROUND((MAX(FECHA_PROCESO)-MIN(FECHA_PROCESO))*24*60) MINUTOS,
  ROUND(COUNT(DISTINCT ID_INDIVIDUO)/((MAX(FECHA_PROCESO)-MIN(FECHA_PROCESO))*24*60+1)) IND_X_MINUTO,
  ROUND(((MAX(FECHA_PROCESO)-MIN(FECHA_PROCESO))*24*60)/COUNT(DISTINCT ID_INDIVIDUO),2) MINUTO_X_IND
FROM FOREX.PROCESO GROUP BY TO_CHAR(FECHA_PROCESO, 'YYYYMMDD HH24') ORDER BY TO_CHAR(FECHA_PROCESO, 'YYYYMMDD HH24') DESC;
SELECT TO_CHAR(FECHA_PROCESO, 'YYYY/MM') MES, COUNT(DISTINCT ID_INDIVIDUO) FROM FOREX.PROCESO GROUP BY TO_CHAR(FECHA_PROCESO, 'YYYY/MM') 
  ORDER BY TO_CHAR(FECHA_PROCESO, 'YYYY/MM') DESC;
SELECT TO_CHAR(FECHA_PROCESO, 'YYYY/MM/DD HH24') FECHA, COUNT(DISTINCT ID_INDIVIDUO) FROM FOREX.PROCESO GROUP BY TO_CHAR(FECHA_PROCESO, 'YYYY/MM/DD HH24')
  ORDER BY TO_CHAR(FECHA_PROCESO, 'YYYY/MM/DD HH24') DESC;
SELECT SYSDATE, FECHA_HISTORICO, COUNT(ID_INDIVIDUO) FROM FOREX.PROCESO GROUP BY FECHA_HISTORICO
  ORDER BY FECHA_HISTORICO DESC;

SELECT P.FE, P.CANTIDAD PROCESADOS, NVL(B.CANTIDAD,0) BORRADOS, (NVL(B.CANTIDAD,0)+P.CANTIDAD) TOTAL_PROCESADOS,
  ROUND((GREATEST(P.MAXFE, NVL(B.MAXFE,P.MAXFE))-P.FE)*24*60,2) MINUTOS,
  ROUND((NVL(B.CANTIDAD,0)+P.CANTIDAD)/((GREATEST(P.MAXFE, NVL(B.MAXFE,P.MAXFE))-P.FE)*24*60+1),2) IND_X_MINUTO,
  ROUND((NVL(B.CANTIDAD,0)+P.CANTIDAD)/((GREATEST(P.MAXFE, NVL(B.MAXFE,P.MAXFE))-P.FE)*24*60+1),2)*60 IND_X_HORA,
  ROUND((NVL(B.CANTIDAD,0)+P.CANTIDAD)/((GREATEST(P.MAXFE, NVL(B.MAXFE,P.MAXFE))-P.FE)*24*60+1),2)*60*24 IND_X_DIA,
  ROUND(((GREATEST(P.MAXFE, NVL(B.MAXFE,P.MAXFE))-P.FE)*24*60)/(NVL(B.CANTIDAD,0)+P.CANTIDAD),2) MINUTO_X_IND,
  P.FE MINFE, GREATEST(P.MAXFE, NVL(B.MAXFE,P.MAXFE)) MAXFE
FROM (
    SELECT TRUNC(FECHA_PROCESO, 'HH24') FE, COUNT(ID_INDIVIDUO) CANTIDAD, MIN(FECHA_PROCESO) MINFE, MAX(FECHA_PROCESO) MAXFE
    FROM PROCESO GROUP BY TRUNC(FECHA_PROCESO, 'HH24')
  ) P LEFT JOIN
  (
    SELECT TO_CHAR(FECHA_BORRADO, 'YYYYMMDD HH24') FE, COUNT(ID) CANTIDAD, MIN(FECHA_BORRADO) MINFE, MAX(FECHA_BORRADO) MAXFE
    FROM INDIVIDUO_BORRADO GROUP BY TO_CHAR(FECHA_BORRADO, 'YYYYMMDD HH24')
    ORDER BY TO_CHAR(FECHA_BORRADO, 'YYYYMMDD HH24') DESC
  ) B ON B.FE=P.FE
ORDER BY P.FE DESC
;

SELECT IND.TIPO_OPERACION, FECHA_HISTORICO, 
COUNT(*) FROM INDIVIDUO IND
  INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID --AND P.FECHA_HISTORICO>=TO_DATE('20140101', 'YYYYMMDD')
  --WHERE NOT EXISTS (SELECT 1 FROM OPERACION OPER WHERE OPER.ID_INDIVIDUO=IND.ID)
  GROUP BY IND.TIPO_OPERACION
	, FECHA_HISTORICO
ORDER BY FECHA_HISTORICO DESC
;

SELECT P.FECHA_HISTORICO, P.FECHA_PROCESO, IND.* FROM INDIVIDUO IND INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID
WHERE IND.ID LIKE '1477365411387.8092'
ORDER BY P.FECHA_PROCESO DESC;

--UPDATE PROCESO P SET FECHA_HISTORICO=TO_DATE('2009/05/06 08:45', 'YYYY/MM/DD HH24:MI')
--WHERE P.ID_INDIVIDUO='1477365411387.12683';

SELECT * FROM (SELECT IND.ID ID_INDIVIDUO, MAX(IND.TIPO_OPERACION), MAX(PROC.FECHA_HISTORICO) /*TO_DATE('2009/02/10 18:30', 'YYYY/MM/DD HH24:MI')*/ FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
--INNER JOIN INDIVIDUO_INDICADORES II ON II.ID=IND.ID  
INNER JOIN INDICADOR_INDIVIDUO II ON II.ID_INDIVIDUO=IND.ID 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=TO_DATE('2016/11/11 23:59', 'YYYY/MM/DD HH24:MI')) 
AND IND.TIPO_INDIVIDUO='INDICADORES' AND IND.TIPO_OPERACION='BUY'
AND (IND.ID LIKE '%.%0' OR IND.ID LIKE '%.%1' OR IND.ID LIKE '%.%2' OR IND.ID LIKE '%.%3' OR IND.ID LIKE '%.%4') AND IND.CREATION_DATE >= TO_DATE('2016/01/01', 'YYYY/MM/DD')
GROUP BY IND.ID 
ORDER BY FECHA_HISTORICO ASC, FECHA_APERTURA ASC, IND.ID DESC) 
WHERE ROWNUM<100;

SELECT * FROM PROCESO 
WHERE ID_INDIVIDUO IN ('1477365411387.9459')
--WHERE ID_INDIVIDUO LIKE '%.%0'
--ORDER BY FECHA_HISTORICO DESC
ORDER BY ID_INDIVIDUO DESC
; 

SELECT 'FILTRO_ADICIONAL_' || ROWNUM || '=AND IND.ID LIKE ''' || (IDS) || '.%'''
FROM (
  SELECT DISTINCT (SUBSTR(ID_INDIVIDUO,0,13)) IDS
  FROM PROCESO P
  WHERE P.FECHA_HISTORICO<(SELECT MAX(DH.FECHA) FROM DATOHISTORICO DH)  
  )
WHERE ROWNUM<9
--FILTRO_ADICIONAL_1=AND IND.ID LIKE '1329398391890.%'
;

SELECT COUNT(DISTINCT ID_INDIVIDUO) FROM FOREX.OPERACION;
SELECT COUNT(ID_INDIVIDUO) FROM FOREX.PROCESO;
SELECT COUNT(*) FROM FOREX.OPERACION;
SELECT OPER.* FROM OPERACION OPER 
 --INNER JOIN PROCESO P ON P.ID_INDIVIDUO=OPER.ID_INDIVIDUO
 WHERE OPER.TAKE_PROFIT=0
--ORDER BY P.FECHA_PROCESO DESC
;

--UPDATE PROCESO SET FECHA_HISTORICO=(SELECT MAX(FECHA_APERTURA) FROM OPERACION WHERE ID_INDIVIDUO='1338171613583.42')
--WHERE ID_INDIVIDUO='1338171613583.42';

--DELETE FROM OPERACION WHERE ID_INDIVIDUO='1454889600000.241' AND FECHA_APERTURA>=TO_DATE('2015/01/26 00:00', 'YYYY/MM/DD HH24:MI');

SELECT * FROM PROCESO WHERE ID_INDIVIDUO=
'1453119773449.111'
;

SELECT * FROM PROCESO P
WHERE P.FECHA_HISTORICO<(SELECT MAX(OPER.FECHA_APERTURA) FROM OPERACION OPER
  WHERE OPER.ID_INDIVIDUO=P.ID_INDIVIDUO);

SELECT * FROM PROCESO_REPETIDOS;

INSERT INTO PROCESO (ID_INDIVIDUO, FECHA_HISTORICO, FECHA_PROCESO)
  SELECT OPER.ID_INDIVIDUO, MAX (OPER.FECHA_CIERRE) FECHA_HISTORICO, SYSDATE FECHA_PROCESO
  FROM OPERACION OPER WHERE ID_INDIVIDUO NOT IN (
    SELECT ID_INDIVIDUO FROM PROCESO P
  ) GROUP BY OPER.ID_INDIVIDUO;
  
SELECT COUNT(*) FROM (SELECT IND.*
FROM INDIVIDUO IND 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
AND IND.CREATION_DATE IS NOT NULL AND IND.CREATION_DATE BETWEEN TO_DATE('2012/06/07', 'YYYY/MM/DD') AND TO_DATE('2012/08/06', 'YYYY/MM/DD')
ORDER BY IND.ID DESC) 
--WHERE ROWNUM<1000
;

SELECT COUNT(*) FROM OPERACION WHERE FECHA>=TO_DATE('2015/02/07 15:50', 'YYYY/MM/DD HH24:MI');

SELECT COUNT(*) FROM PROCESO WHERE FECHA_PROCESO>=TO_DATE('2015/02/07 15:50', 'YYYY/MM/DD HH24:MI');

--DELETE FROM PROCESO WHERE ID_INDIVIDUO='1452862634234.1';

--UPDATE PROCESO SET FECHA_HISTORICO=TO_DATE('2015.01.26 04:25', 'YYYY/MM/DD HH24:MI') WHERE ID_INDIVIDUO='1454889600000.241';

SELECT * FROM PROCESO P 
INNER JOIN (SELECT OPER.ID_INDIVIDUO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA FROM OPERACION OPER GROUP BY OPER.ID_INDIVIDUO) OP
  ON P.ID_INDIVIDUO=OP.ID_INDIVIDUO
WHERE P.FECHA_HISTORICO>TO_DATE('20160101', 'YYYYMMDD')
AND OP.FECHA_APERTURA<TO_DATE('20160101', 'YYYYMMDD')
AND ROWNUM<2
;

SELECT TRUNC(SYSDATE, 'YYYY') TANYO, TRUNC(SYSDATE, 'MM') TMES, TRUNC(SYSDATE, 'HH24') THORA FROM DUAL;