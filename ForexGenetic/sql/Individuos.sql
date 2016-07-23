SELECT * FROM INDIVIDUO 
WHERE CREATION_DATE IS NOT NULL
ORDER BY CREATION_DATE DESC;
SELECT COUNT(*) FROM INDIVIDUO;
SELECT  TIPO_OPERACION, COUNT(*) FROM INDIVIDUO GROUP BY TIPO_OPERACION;
SELECT COUNT(*) FROM INDICADOR_INDIVIDUO;
SELECT COUNT(*) FROM INDIVIDUO_BORRADO;

SELECT * FROM INDIVIDUO 
WHERE ID = '1462763219878.287';

SELECT II.* FROM FOREX.INDICADOR_INDIVIDUO II
--WHERE II.ID_INDIVIDUO in('1461512685034.63','1461512685034.69')
ORDER BY TIPO DESC, ID_INDICADOR, ID_INDIVIDUO
;

SELECT II.* FROM FOREX.INDICADOR_INDIVIDUO II
WHERE II.ID_INDIVIDUO LIKE '1462031621723.723'
ORDER BY II.ID_INDIVIDUO DESC, TIPO DESC, ID_INDICADOR, INTERVALO_INFERIOR, INTERVALO_SUPERIOR
;

SELECT * FROM INDIVIDUO IND
INNER JOIN INDICADOR_INDIVIDUO II ON II.ID_INDIVIDUO=IND.ID
WHERE IND.TIPO_OPERACION='BUY' 
AND II.ID_INDICADOR='MA1200' AND II.INTERVALO_INFERIOR IS NOT NULL
;

CREATE OR REPLACE VIEW INDICADORES_COLUMNAS AS
SELECT IND.*, 
  II_MA.INTERVALO_INFERIOR INFERIOR_MA, II_MA.INTERVALO_SUPERIOR SUPERIOR_MA,
  II_MACD.INTERVALO_INFERIOR INFERIOR_MACD, II_MACD.INTERVALO_SUPERIOR SUPERIOR_MACD,
  II_COMPARE.INTERVALO_INFERIOR INFERIOR_COMPARE, II_COMPARE.INTERVALO_SUPERIOR SUPERIOR_COMPARE,
  II_ADX.INTERVALO_INFERIOR INFERIOR_ADX, II_ADX.INTERVALO_SUPERIOR SUPERIOR_ADX,
  II_BOLLINGER.INTERVALO_INFERIOR INFERIOR_BOLLINGER, II_BOLLINGER.INTERVALO_SUPERIOR SUPERIOR_BOLLINGER,
  II_ICHISIGNAL.INTERVALO_INFERIOR INFERIOR_ICHISIGNAL, II_ICHISIGNAL.INTERVALO_SUPERIOR SUPERIOR_ICHISIGNAL,
  II_ICHITREND.INTERVALO_INFERIOR INFERIOR_ICHITREND, II_ICHITREND.INTERVALO_SUPERIOR SUPERIOR_ICHITREND,
  II_MOMENTUM.INTERVALO_INFERIOR INFERIOR_MOMENTUM, II_MOMENTUM.INTERVALO_SUPERIOR SUPERIOR_MOMENTUM,
  II_RSI.INTERVALO_INFERIOR INFERIOR_RSI, II_RSI.INTERVALO_SUPERIOR SUPERIOR_RSI,
  II_SAR.INTERVALO_INFERIOR INFERIOR_SAR, II_SAR.INTERVALO_SUPERIOR SUPERIOR_SAR
FROM INDIVIDUO IND
  INNER JOIN INDICADOR_INDIVIDUO II_MA ON IND.ID=II_MA.ID_INDIVIDUO AND II_MA.ID_INDICADOR='MA' AND II_MA.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_MACD ON IND.ID=II_MACD.ID_INDIVIDUO AND II_MACD.ID_INDICADOR='MACD' AND II_MACD.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_COMPARE ON IND.ID=II_COMPARE.ID_INDIVIDUO AND II_COMPARE.ID_INDICADOR='COMPARE_MA' AND II_COMPARE.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_ADX ON IND.ID=II_ADX.ID_INDIVIDUO AND II_ADX.ID_INDICADOR='ADX' AND II_ADX.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_BOLLINGER ON IND.ID=II_BOLLINGER.ID_INDIVIDUO AND II_BOLLINGER.ID_INDICADOR='BOLLINGER' AND II_BOLLINGER.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_ICHISIGNAL ON IND.ID=II_ICHISIGNAL.ID_INDIVIDUO AND II_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' AND II_ICHISIGNAL.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_ICHITREND ON IND.ID=II_ICHITREND.ID_INDIVIDUO AND II_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' AND II_ICHITREND.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_MOMENTUM ON IND.ID=II_MOMENTUM.ID_INDIVIDUO AND II_MOMENTUM.ID_INDICADOR='MOMENTUM' AND II_MOMENTUM.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_RSI ON IND.ID=II_RSI.ID_INDIVIDUO AND II_RSI.ID_INDICADOR='RSI' AND II_RSI.TIPO='OPEN'
  INNER JOIN INDICADOR_INDIVIDUO II_SAR ON IND.ID=II_SAR.ID_INDIVIDUO AND II_SAR.ID_INDICADOR='SAR' AND II_SAR.TIPO='OPEN'
WHERE IND.ID = '1453135607883.90'
;

SELECT II.*
FROM INDICADOR_INDIVIDUO II 
WHERE II.ID_INDICADOR='MA'
  AND II.INTERVALO_INFERIOR BETWEEN (-0.00886-0.00010) AND (-0.00886+0.00010)
  AND II.INTERVALO_SUPERIOR BETWEEN (-0.00027-0.00010) AND (-0.00027+0.00010)
--ORDER BY OPER.FECHA_APERTURA ASC
  --AND ROWNUM<10
;

SELECT * FROM INDIVIDUO ORDER BY TAKE_PROFIT DESC;
SELECT * FROM INDIVIDUO ORDER BY STOP_LOSS DESC;
SELECT * FROM INDIVIDUO 
  WHERE LOTE>0 AND INITIAL_BALANCE>0
ORDER BY (TAKE_PROFIT+STOP_LOSS)/2 DESC;

SELECT * FROM FOREX.INDIVIDUO WHERE ID LIKE
'1334463374425.351';

SELECT * FROM FOREX.INDIVIDUO 
WHERE PARENT_ID_1='1414339217357.97' OR PARENT_ID_2='1414339217357.97'
;

SELECT COUNT(*) FROM INDICADOR_INDIVIDUO II
WHERE II.TIPO='OPEN' AND II.INTERVALO_INFERIOR IS NOT NULL
AND II.ID_INDIVIDUO='1424050827038.111';

SELECT * FROM FOREX.INDIVIDUO WHERE CREATION_DATE>TRUNC(SYSDATE);

SELECT AVG(CONT), MIN(CONT), MAX(CONT) FROM (
  SELECT COUNT(*) CONT
  FROM FOREX.INDICADOR_INDIVIDUO II
  WHERE II.TIPO='OPEN' AND INTERVALO_INFERIOR IS NOT NULL
  GROUP BY II.ID_INDIVIDUO
)
;

SELECT *  FROM FOREX.INDIVIDUO_BORRADO 
WHERE ID='1334463374425.351'
;

SELECT COUNT(*) FROM FOREX.INDIVIDUO_BORRADO WHERE CAUSA_BORRADO='DUPLICADO_APERTURA';
SELECT * FROM FOREX.INDIVIDUO_BORRADO WHERE TIPO_BORRADO='ANTIGUO';

SELECT II.*--, II.INTERVALO_INFERIOR*100, II.INTERVALO_SUPERIOR*100 
FROM FOREX.INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO IN ('1342007251051.69929')
--AND TIPO='OPEN'
ORDER BY II.TIPO DESC, ID_INDICADOR ASC;

SELECT * FROM (
SELECT II.*--, II.INTERVALO_INFERIOR*100, II.INTERVALO_SUPERIOR*100 
FROM FOREX.INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO IN ('1342007251051.69929')
--AND TIPO='OPEN'
UNION
SELECT II.*--, II.INTERVALO_INFERIOR*100, II.INTERVALO_SUPERIOR*100 
FROM INDICADOR_INDIVIDUO_BORRADO II WHERE II.ID_INDIVIDUO IN ('1342007251051.68839')
--AND TIPO='OPEN'
) ORDER BY TIPO DESC, ID_INDICADOR ASC;
;

SELECT COUNT(*) FROM INDIVIDUO_BORRADO;
SELECT * FROM INDIVIDUO_BORRADO ORDER BY ID DESC;
SELECT * FROM INDICADOR_INDIVIDUO_BORRADO;

SELECT * FROM FOREX.INDIVIDUO WHERE CREATION_DATE IS NOT NULL ORDER BY ID DESC;

SELECT * FROM FOREX.INDICADOR;

SELECT * FROM FOREX.INDICADOR WHERE ID='ICHIMOKU_SIGNAL' AND TIPO='CLOSE';

SELECT * FROM FOREX.INDICADOR_INDIVIDUO
ORDER BY ID_INDIVIDUO,TIPO ASC;

SELECT * FROM FOREX.INDICADOR_INDIVIDUO
WHERE ID_INDICADOR='ICHIMOKU_SIGNAL' AND TIPO='CLOSE' AND ID_INDIVIDUO='1327755218940.22316'
;

SELECT COUNT(*) FROM INDICADOR_INDIVIDUO;

SELECT * FROM (SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) /*TO_DATE('2009/02/10 18:30', 'YYYY/MM/DD HH24:MI')*/ FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA 
FROM INDIVIDUO IND 
INNER JOIN INDICADOR_INDIVIDUO II ON II.ID_INDIVIDUO=IND.ID 
LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO 
LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL 
WHERE 
IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO)) 
/*<FILTRO_ADICIONAL>*/
  AND IND.CREATION_DATE IS NULL AND IND.PARENT_ID_1 IS NOT NULL AND IND.PARENT_ID_2 IS NULL
GROUP BY IND.ID 
ORDER BY IND.ID DESC) 
WHERE ROWNUM<1000
;

SELECT * FROM INDIVIDUO IND
--  INNER JOIN INDICADOR_INDIVIDUO II ON II.ID_INDIVIDUO=IND.ID
  WHERE /*IND.CREATION_DATE IS NOT NULL AND*/ IND.CREATION_DATE BETWEEN TO_DATE('2011/12/01', 'YYYY/MM/DD') AND TO_DATE('2012/02/07', 'YYYY/MM/DD')
;

SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, 
                 IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, IND_MAXIMOS.FECHA_HISTORICO,
                 IND3.ID_INDICADOR, IND3.INTERVALO_INFERIOR, IND3.INTERVALO_SUPERIOR, IND3.TIPO,
                 OPER.FECHA_APERTURA, OPER.SPREAD, OPER.OPEN_PRICE
                  FROM INDIVIDUO IND2
                   INNER JOIN (
                     SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA
                     FROM INDIVIDUO IND 
                       LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO
                       LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL
                     WHERE 
                     IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO))    
                     GROUP BY IND.ID) IND_MAXIMOS ON IND2.ID=IND_MAXIMOS.ID_INDIVIDUO
                   INNER JOIN INDICADOR_INDIVIDUO IND3 ON IND2.ID=IND3.ID_INDIVIDUO
                   LEFT JOIN OPERACION OPER ON IND2.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_APERTURA=IND_MAXIMOS.FECHA_APERTURA
                  WHERE IND2.ID=:1
                  ORDER BY IND2.ID DESC;

SELECT count(*) FROM INDIVIDUO IND 
  WHERE IND.STOP_LOSS<=100 
  AND EXISTS (
      SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO=IND.ID AND II.TIPO='OPEN'
    )
  --AND ROWNUM<100
  ;
  
SELECT COUNT(*) FROM INDIVIDUO IND
  INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID AND P.FECHA_HISTORICO>=TO_DATE('20140101', 'YYYYMMDD')
  WHERE NOT EXISTS (SELECT 1 FROM OPERACION OPER WHERE OPER.ID_INDIVIDUO=IND.ID)
;

SELECT II.* FROM INDIVIDUO_BORRADO IND
INNER JOIN INDICADOR_INDIVIDUO_BORRADO II ON IND.ID=II.ID_INDIVIDUO
WHERE TIPO_BORRADO='SIN_OPERACIONES'
ORDER BY II.ID_INDIVIDUO DESC, II.ID_INDICADOR ASC, II.TIPO DESC
;

SELECT * FROM INDIVIDUO_BORRADO IND
WHERE IND.ID='1462763219878.287';

SELECT COUNT(*) FROM INDIVIDUO_BORRADO IND
WHERE TIPO_BORRADO='SIN_OPERACIONES';
