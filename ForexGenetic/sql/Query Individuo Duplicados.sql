
  CREATE OR REPLACE FORCE VIEW "FOREX"."INDIVIDUOS_REPETIDOS" ("ID_INDIVIDUO1", "ID_INDIVIDUO2") AS 
  SELECT MAX(IND1.ID) ID_INDIVIDUO1, IND2.ID ID_INDIVIDUO2
    FROM INDIVIDUO IND1 INNER JOIN INDIVIDUO IND2 ON IND1.ID <> IND2.ID AND IND1.ID>IND2.ID        
        AND IND1.TIPO_OPERACION=IND2.TIPO_OPERACION
        AND (IND1.TAKE_PROFIT/IND2.TAKE_PROFIT) BETWEEN 0.9 AND 1.1
        AND (IND1.STOP_LOSS/IND2.STOP_LOSS) BETWEEN 0.9 AND 1.1
      INNER JOIN INDIVIDUO_INDICADORES II1 ON II1.ID=IND1.ID 
      INNER JOIN INDIVIDUO_INDICADORES II2 ON II2.ID=IND2.ID

  WHERE NVL(II1.OPEN_INFERIOR_MA,0)=NVL(II2.OPEN_INFERIOR_MA,0)
    AND NVL(II1.OPEN_SUPERIOR_MA,0)=NVL(II2.OPEN_SUPERIOR_MA,0)

    AND NVL(II1.OPEN_INFERIOR_MACD,0)=NVL(II2.OPEN_INFERIOR_MACD,0)
    AND NVL(II1.OPEN_SUPERIOR_MACD,0)=NVL(II2.OPEN_SUPERIOR_MACD,0)

    AND NVL(II1.OPEN_INFERIOR_COMPARE,0)=NVL(II2.OPEN_INFERIOR_COMPARE,0)
    AND NVL(II1.OPEN_SUPERIOR_COMPARE,0)=NVL(II2.OPEN_SUPERIOR_COMPARE,0)

    AND NVL(II1.OPEN_INFERIOR_SAR,0)=NVL(II2.OPEN_INFERIOR_SAR,0)
    AND NVL(II1.OPEN_SUPERIOR_SAR,0)=NVL(II2.OPEN_SUPERIOR_SAR,0)
    
    AND NVL(II1.OPEN_INFERIOR_ADX,0)=NVL(II2.OPEN_INFERIOR_ADX,0)
    AND NVL(II1.OPEN_SUPERIOR_ADX,0)=NVL(II2.OPEN_SUPERIOR_ADX,0)

    AND NVL(II1.OPEN_INFERIOR_RSI,0)=NVL(II2.OPEN_INFERIOR_RSI,0)
    AND NVL(II1.OPEN_SUPERIOR_RSI,0)=NVL(II2.OPEN_SUPERIOR_RSI,0)

    AND NVL(II1.OPEN_INFERIOR_BOLLINGER,0)=NVL(II2.OPEN_INFERIOR_BOLLINGER,0)
    AND NVL(II1.OPEN_SUPERIOR_BOLLINGER,0)=NVL(II2.OPEN_SUPERIOR_BOLLINGER,0)

    AND NVL(II1.OPEN_INFERIOR_MOMENTUM,0)=NVL(II2.OPEN_INFERIOR_MOMENTUM,0)
    AND NVL(II1.OPEN_SUPERIOR_MOMENTUM,0)=NVL(II2.OPEN_SUPERIOR_MOMENTUM,0)

    AND NVL(II1.OPEN_INFERIOR_ICHITREND,0)=NVL(II2.OPEN_INFERIOR_ICHITREND,0)
    AND NVL(II1.OPEN_SUPERIOR_ICHITREND,0)=NVL(II2.OPEN_SUPERIOR_ICHITREND,0)

    AND NVL(II1.OPEN_INFERIOR_ICHISIGNAL,0)=NVL(II2.OPEN_INFERIOR_ICHISIGNAL,0)
    AND NVL(II1.OPEN_SUPERIOR_ICHISIGNAL,0)=NVL(II2.OPEN_SUPERIOR_ICHISIGNAL,0)

    AND NVL(II1.OPEN_INFERIOR_MA1200,0)=NVL(II2.OPEN_INFERIOR_MA1200,0)
    AND NVL(II1.OPEN_SUPERIOR_MA1200,0)=NVL(II2.OPEN_SUPERIOR_MA1200,0)

    AND NVL(II1.OPEN_INFERIOR_MACD20X,0)=NVL(II2.OPEN_INFERIOR_MACD20X,0)
    AND NVL(II1.OPEN_SUPERIOR_MACD20X,0)=NVL(II2.OPEN_SUPERIOR_MACD20X,0)

    AND NVL(II1.OPEN_INFERIOR_COMPARE1200,0)=NVL(II2.OPEN_INFERIOR_COMPARE1200,0)
    AND NVL(II1.OPEN_SUPERIOR_COMPARE1200,0)=NVL(II2.OPEN_SUPERIOR_COMPARE1200,0)

    AND NVL(II1.OPEN_INFERIOR_SAR1200,0)=NVL(II2.OPEN_INFERIOR_SAR1200,0)
    AND NVL(II1.OPEN_SUPERIOR_SAR1200,0)=NVL(II2.OPEN_SUPERIOR_SAR1200,0)
    
    AND NVL(II1.OPEN_INFERIOR_ADX168,0)=NVL(II2.OPEN_INFERIOR_ADX168,0)
    AND NVL(II1.OPEN_SUPERIOR_ADX168,0)=NVL(II2.OPEN_SUPERIOR_ADX168,0)

    AND NVL(II1.OPEN_INFERIOR_RSI84,0)=NVL(II2.OPEN_INFERIOR_RSI84,0)
    AND NVL(II1.OPEN_SUPERIOR_RSI84,0)=NVL(II2.OPEN_SUPERIOR_RSI84,0)

    AND NVL(II1.OPEN_INFERIOR_BOLLINGER240,0)=NVL(II2.OPEN_INFERIOR_BOLLINGER240,0)
    AND NVL(II1.OPEN_SUPERIOR_BOLLINGER240,0)=NVL(II2.OPEN_SUPERIOR_BOLLINGER240,0)

    AND NVL(II1.OPEN_INFERIOR_MOMENTUM1200,0)=NVL(II2.OPEN_INFERIOR_MOMENTUM1200,0)
    AND NVL(II1.OPEN_SUPERIOR_MOMENTUM1200,0)=NVL(II2.OPEN_SUPERIOR_MOMENTUM1200,0)

    AND NVL(II1.OPEN_INFERIOR_ICHITREND6,0)=NVL(II2.OPEN_INFERIOR_ICHITREND6,0)
    AND NVL(II1.OPEN_SUPERIOR_ICHITREND6,0)=NVL(II2.OPEN_SUPERIOR_ICHITREND6,0)

    AND NVL(II1.OPEN_INFERIOR_ICHISIGNAL6,0)=NVL(II2.OPEN_INFERIOR_ICHISIGNAL6,0)
    AND NVL(II1.OPEN_SUPERIOR_ICHISIGNAL6,0)=NVL(II2.OPEN_SUPERIOR_ICHISIGNAL6,0)
    
    AND NVL(II1.CLOSE_INFERIOR_MA,0)=NVL(II2.CLOSE_INFERIOR_MA,0)
    AND NVL(II1.CLOSE_SUPERIOR_MA,0)=NVL(II2.CLOSE_SUPERIOR_MA,0)

    AND NVL(II1.CLOSE_INFERIOR_MACD,0)=NVL(II2.CLOSE_INFERIOR_MACD,0)
    AND NVL(II1.CLOSE_SUPERIOR_MACD,0)=NVL(II2.CLOSE_SUPERIOR_MACD,0)

    AND NVL(II1.CLOSE_INFERIOR_COMPARE,0)=NVL(II2.CLOSE_INFERIOR_COMPARE,0)
    AND NVL(II1.CLOSE_SUPERIOR_COMPARE,0)=NVL(II2.CLOSE_SUPERIOR_COMPARE,0)

    AND NVL(II1.CLOSE_INFERIOR_SAR,0)=NVL(II2.CLOSE_INFERIOR_SAR,0)
    AND NVL(II1.CLOSE_SUPERIOR_SAR,0)=NVL(II2.CLOSE_SUPERIOR_SAR,0)
    
    AND NVL(II1.CLOSE_INFERIOR_ADX,0)=NVL(II2.CLOSE_INFERIOR_ADX,0)
    AND NVL(II1.CLOSE_SUPERIOR_ADX,0)=NVL(II2.CLOSE_SUPERIOR_ADX,0)

    AND NVL(II1.CLOSE_INFERIOR_RSI,0)=NVL(II2.CLOSE_INFERIOR_RSI,0)
    AND NVL(II1.CLOSE_SUPERIOR_RSI,0)=NVL(II2.CLOSE_SUPERIOR_RSI,0)

    AND NVL(II1.CLOSE_INFERIOR_BOLLINGER,0)=NVL(II2.CLOSE_INFERIOR_BOLLINGER,0)
    AND NVL(II1.CLOSE_SUPERIOR_BOLLINGER,0)=NVL(II2.CLOSE_SUPERIOR_BOLLINGER,0)

    AND NVL(II1.CLOSE_INFERIOR_MOMENTUM,0)=NVL(II2.CLOSE_INFERIOR_MOMENTUM,0)
    AND NVL(II1.CLOSE_SUPERIOR_MOMENTUM,0)=NVL(II2.CLOSE_SUPERIOR_MOMENTUM,0)

    AND NVL(II1.CLOSE_INFERIOR_ICHITREND,0)=NVL(II2.CLOSE_INFERIOR_ICHITREND,0)
    AND NVL(II1.CLOSE_SUPERIOR_ICHITREND,0)=NVL(II2.CLOSE_SUPERIOR_ICHITREND,0)

    AND NVL(II1.CLOSE_INFERIOR_ICHISIGNAL,0)=NVL(II2.CLOSE_INFERIOR_ICHISIGNAL,0)
    AND NVL(II1.CLOSE_SUPERIOR_ICHISIGNAL,0)=NVL(II2.CLOSE_SUPERIOR_ICHISIGNAL,0)

    AND NVL(II1.CLOSE_INFERIOR_MA1200,0)=NVL(II2.CLOSE_INFERIOR_MA1200,0)
    AND NVL(II1.CLOSE_SUPERIOR_MA1200,0)=NVL(II2.CLOSE_SUPERIOR_MA1200,0)

    AND NVL(II1.CLOSE_INFERIOR_MACD20X,0)=NVL(II2.CLOSE_INFERIOR_MACD20X,0)
    AND NVL(II1.CLOSE_SUPERIOR_MACD20X,0)=NVL(II2.CLOSE_SUPERIOR_MACD20X,0)

    AND NVL(II1.CLOSE_INFERIOR_COMPARE1200,0)=NVL(II2.CLOSE_INFERIOR_COMPARE1200,0)
    AND NVL(II1.CLOSE_SUPERIOR_COMPARE1200,0)=NVL(II2.CLOSE_SUPERIOR_COMPARE1200,0)

    AND NVL(II1.CLOSE_INFERIOR_SAR1200,0)=NVL(II2.CLOSE_INFERIOR_SAR1200,0)
    AND NVL(II1.CLOSE_SUPERIOR_SAR1200,0)=NVL(II2.CLOSE_SUPERIOR_SAR1200,0)
    
    AND NVL(II1.CLOSE_INFERIOR_ADX168,0)=NVL(II2.CLOSE_INFERIOR_ADX168,0)
    AND NVL(II1.CLOSE_SUPERIOR_ADX168,0)=NVL(II2.CLOSE_SUPERIOR_ADX168,0)

    AND NVL(II1.CLOSE_INFERIOR_RSI84,0)=NVL(II2.CLOSE_INFERIOR_RSI84,0)
    AND NVL(II1.CLOSE_SUPERIOR_RSI84,0)=NVL(II2.CLOSE_SUPERIOR_RSI84,0)

    AND NVL(II1.CLOSE_INFERIOR_BOLLINGER240,0)=NVL(II2.CLOSE_INFERIOR_BOLLINGER240,0)
    AND NVL(II1.CLOSE_SUPERIOR_BOLLINGER240,0)=NVL(II2.CLOSE_SUPERIOR_BOLLINGER240,0)

    AND NVL(II1.CLOSE_INFERIOR_MOMENTUM1200,0)=NVL(II2.CLOSE_INFERIOR_MOMENTUM1200,0)
    AND NVL(II1.CLOSE_SUPERIOR_MOMENTUM1200,0)=NVL(II2.CLOSE_SUPERIOR_MOMENTUM1200,0)

    AND NVL(II1.CLOSE_INFERIOR_ICHITREND6,0)=NVL(II2.CLOSE_INFERIOR_ICHITREND6,0)
    AND NVL(II1.CLOSE_SUPERIOR_ICHITREND6,0)=NVL(II2.CLOSE_SUPERIOR_ICHITREND6,0)

    AND NVL(II1.CLOSE_INFERIOR_ICHISIGNAL6,0)=NVL(II2.CLOSE_INFERIOR_ICHISIGNAL6,0)
    AND NVL(II1.CLOSE_SUPERIOR_ICHISIGNAL6,0)=NVL(II2.CLOSE_SUPERIOR_ICHISIGNAL6,0)
    
  GROUP BY IND2.ID
;
