ALTER SESSION SET NLS_NUMERIC_CHARACTERS = '.,';

SELECT 'EstrategiaId='||IND.ID||',VigenciaLower=2000.01.01 00:00,VigenciaHigher=2020.01.01 00:00,'||
  'Active=true,Pair=USDCAD,Operation=Sell,TakeProfit='||IND.TAKE_PROFIT
  ||',StopLoss='||IND.STOP_LOSS
  ||',Lote='||IND.LOTE||','||
II_OPEN_MA.TIPO||II_OPEN_MA.ID_INDICADOR||'LOWER='||II_OPEN_MA.INTERVALO_INFERIOR*100||','||II_OPEN_MA.TIPO||II_OPEN_MA.ID_INDICADOR||'HIGHER='||II_OPEN_MA.INTERVALO_SUPERIOR*100||','||
II_OPEN_MACD.TIPO||II_OPEN_MACD.ID_INDICADOR||'LOWER='||II_OPEN_MACD.INTERVALO_INFERIOR*100||','||II_OPEN_MACD.TIPO||II_OPEN_MACD.ID_INDICADOR||'HIGHER='||II_OPEN_MACD.INTERVALO_SUPERIOR*100||','||
II_OPEN_COMPARE.TIPO||'MACOMPARE'||'LOWER='||II_OPEN_COMPARE.INTERVALO_INFERIOR*100||','||II_OPEN_COMPARE.TIPO||'MACOMPARE'||'HIGHER='||II_OPEN_COMPARE.INTERVALO_SUPERIOR*100||','||
II_OPEN_SAR.TIPO||II_OPEN_SAR.ID_INDICADOR||'LOWER='||II_OPEN_SAR.INTERVALO_INFERIOR*100||','||II_OPEN_SAR.TIPO||II_OPEN_SAR.ID_INDICADOR||'HIGHER='||II_OPEN_SAR.INTERVALO_SUPERIOR*100||','||
II_OPEN_ADX.TIPO||II_OPEN_ADX.ID_INDICADOR||'LOWER='||II_OPEN_ADX.INTERVALO_INFERIOR*100||','||II_OPEN_ADX.TIPO||II_OPEN_ADX.ID_INDICADOR||'HIGHER='||II_OPEN_ADX.INTERVALO_SUPERIOR*100||','||
II_OPEN_RSI.TIPO||II_OPEN_RSI.ID_INDICADOR||'LOWER='||II_OPEN_RSI.INTERVALO_INFERIOR*100||','||II_OPEN_RSI.TIPO||II_OPEN_RSI.ID_INDICADOR||'HIGHER='||II_OPEN_RSI.INTERVALO_SUPERIOR*100||','||
II_OPEN_BOLLINGER.TIPO||II_OPEN_BOLLINGER.ID_INDICADOR||'LOWER='||II_OPEN_BOLLINGER.INTERVALO_INFERIOR*100||','||II_OPEN_BOLLINGER.TIPO||II_OPEN_BOLLINGER.ID_INDICADOR||'HIGHER='||II_OPEN_BOLLINGER.INTERVALO_SUPERIOR*100||','||
II_OPEN_MOMENTUM.TIPO||II_OPEN_MOMENTUM.ID_INDICADOR||'LOWER='||II_OPEN_MOMENTUM.INTERVALO_INFERIOR*100||','||II_OPEN_MOMENTUM.TIPO||II_OPEN_MOMENTUM.ID_INDICADOR||'HIGHER='||II_OPEN_MOMENTUM.INTERVALO_SUPERIOR*100||','||
II_OPEN_ICHITREND.TIPO||'ICHITREND'||'LOWER='||II_OPEN_ICHITREND.INTERVALO_INFERIOR*100||','||II_OPEN_ICHITREND.TIPO||'ICHITREND'||'HIGHER='||II_OPEN_ICHITREND.INTERVALO_SUPERIOR*100||','||
II_OPEN_ICHISIGNAL.TIPO||'ICHISIGNAL'||'LOWER='||II_OPEN_ICHISIGNAL.INTERVALO_INFERIOR*100||','||II_OPEN_ICHISIGNAL.TIPO||'ICHISIGNAL'||'HIGHER='||II_OPEN_ICHISIGNAL.INTERVALO_SUPERIOR*100||','||
II_CLOSE_MA.TIPO||II_CLOSE_MA.ID_INDICADOR||'LOWER='||II_CLOSE_MA.INTERVALO_INFERIOR*100||','||II_CLOSE_MA.TIPO||II_CLOSE_MA.ID_INDICADOR||'HIGHER='||II_CLOSE_MA.INTERVALO_SUPERIOR*100||','||
II_CLOSE_MACD.TIPO||II_CLOSE_MACD.ID_INDICADOR||'LOWER='||II_CLOSE_MACD.INTERVALO_INFERIOR*100||','||II_CLOSE_MACD.TIPO||II_CLOSE_MACD.ID_INDICADOR||'HIGHER='||II_CLOSE_MACD.INTERVALO_SUPERIOR*100||','||
II_CLOSE_COMPARE.TIPO||'MACOMPARE'||'LOWER='||II_CLOSE_COMPARE.INTERVALO_INFERIOR*100||','||II_CLOSE_COMPARE.TIPO||'MACOMPARE'||'HIGHER='||II_CLOSE_COMPARE.INTERVALO_SUPERIOR*100||','||
II_CLOSE_SAR.TIPO||II_CLOSE_SAR.ID_INDICADOR||'LOWER='||II_CLOSE_SAR.INTERVALO_INFERIOR*100||','||II_CLOSE_SAR.TIPO||II_CLOSE_SAR.ID_INDICADOR||'HIGHER='||II_CLOSE_SAR.INTERVALO_SUPERIOR*100||','||
II_CLOSE_ADX.TIPO||II_CLOSE_ADX.ID_INDICADOR||'LOWER='||II_CLOSE_ADX.INTERVALO_INFERIOR*100||','||II_CLOSE_ADX.TIPO||II_CLOSE_ADX.ID_INDICADOR||'HIGHER='||II_CLOSE_ADX.INTERVALO_SUPERIOR*100||','||
II_CLOSE_RSI.TIPO||II_CLOSE_RSI.ID_INDICADOR||'LOWER='||II_CLOSE_RSI.INTERVALO_INFERIOR*100||','||II_CLOSE_RSI.TIPO||II_CLOSE_RSI.ID_INDICADOR||'HIGHER='||II_CLOSE_RSI.INTERVALO_SUPERIOR*100||','||
II_CLOSE_BOLLINGER.TIPO||II_CLOSE_BOLLINGER.ID_INDICADOR||'LOWER='||II_CLOSE_BOLLINGER.INTERVALO_INFERIOR*100||','||II_CLOSE_BOLLINGER.TIPO||II_CLOSE_BOLLINGER.ID_INDICADOR||'HIGHER='||II_CLOSE_BOLLINGER.INTERVALO_SUPERIOR*100||','||
II_CLOSE_MOMENTUM.TIPO||II_CLOSE_MOMENTUM.ID_INDICADOR||'LOWER='||II_CLOSE_MOMENTUM.INTERVALO_INFERIOR*100||','||II_CLOSE_MOMENTUM.TIPO||II_CLOSE_MOMENTUM.ID_INDICADOR||'HIGHER='||II_CLOSE_MOMENTUM.INTERVALO_SUPERIOR*100||','||
II_CLOSE_ICHITREND.TIPO||'ICHITREND'||'LOWER='||II_CLOSE_ICHITREND.INTERVALO_INFERIOR*100||','||II_CLOSE_ICHITREND.TIPO||'ICHITREND'||'HIGHER='||II_CLOSE_ICHITREND.INTERVALO_SUPERIOR*100||','||
II_CLOSE_ICHISIGNAL.TIPO||'ICHISIGNAL'||'LOWER='||II_CLOSE_ICHISIGNAL.INTERVALO_INFERIOR*100||','||II_CLOSE_ICHISIGNAL.TIPO||'ICHISIGNAL'||'HIGHER='||II_CLOSE_ICHISIGNAL.INTERVALO_SUPERIOR*100 
||',' FILESTRING

FROM    TMP_TOFILESTRING TMP, INDICADOR_INDIVIDUO II_OPEN_MA, INDICADOR_INDIVIDUO II_CLOSE_MA,
        INDICADOR_INDIVIDUO II_OPEN_MACD, INDICADOR_INDIVIDUO II_CLOSE_MACD,
        INDICADOR_INDIVIDUO II_OPEN_COMPARE, INDICADOR_INDIVIDUO II_CLOSE_COMPARE,
        INDICADOR_INDIVIDUO II_OPEN_SAR, INDICADOR_INDIVIDUO II_CLOSE_SAR,
        INDICADOR_INDIVIDUO II_OPEN_ADX, INDICADOR_INDIVIDUO II_CLOSE_ADX,
        INDICADOR_INDIVIDUO II_OPEN_RSI, INDICADOR_INDIVIDUO II_CLOSE_RSI,
        INDICADOR_INDIVIDUO II_OPEN_BOLLINGER, INDICADOR_INDIVIDUO II_CLOSE_BOLLINGER,
        INDICADOR_INDIVIDUO II_OPEN_MOMENTUM, INDICADOR_INDIVIDUO II_CLOSE_MOMENTUM,
        INDICADOR_INDIVIDUO II_OPEN_ICHITREND, INDICADOR_INDIVIDUO II_CLOSE_ICHITREND,
        INDICADOR_INDIVIDUO II_OPEN_ICHISIGNAL, INDICADOR_INDIVIDUO II_CLOSE_ICHISIGNAL,
        INDIVIDUO IND
      WHERE IND.ID=II_OPEN_MA.ID_INDIVIDUO AND IND.ID=II_CLOSE_MA.ID_INDIVIDUO 
      AND IND.ID=II_OPEN_MACD.ID_INDIVIDUO AND IND.ID=II_CLOSE_MACD.ID_INDIVIDUO
      AND IND.ID=II_OPEN_COMPARE.ID_INDIVIDUO AND IND.ID=II_CLOSE_COMPARE.ID_INDIVIDUO
      AND IND.ID=II_OPEN_SAR.ID_INDIVIDUO AND IND.ID=II_CLOSE_SAR.ID_INDIVIDUO
      AND IND.ID=II_OPEN_ADX.ID_INDIVIDUO AND IND.ID=II_CLOSE_ADX.ID_INDIVIDUO
      AND IND.ID=II_OPEN_RSI.ID_INDIVIDUO AND IND.ID=II_CLOSE_RSI.ID_INDIVIDUO
      AND IND.ID=II_OPEN_BOLLINGER.ID_INDIVIDUO AND IND.ID=II_CLOSE_BOLLINGER.ID_INDIVIDUO
      AND IND.ID=II_OPEN_MOMENTUM.ID_INDIVIDUO AND IND.ID=II_CLOSE_MOMENTUM.ID_INDIVIDUO
      AND IND.ID=II_OPEN_ICHITREND.ID_INDIVIDUO AND IND.ID=II_CLOSE_ICHITREND.ID_INDIVIDUO
      AND IND.ID=II_OPEN_ICHISIGNAL.ID_INDIVIDUO AND IND.ID=II_CLOSE_ICHISIGNAL.ID_INDIVIDUO
      
      AND II_OPEN_MA.TIPO='OPEN' AND II_OPEN_MA.ID_INDICADOR='MA' 
      AND II_OPEN_MACD.TIPO='OPEN' AND II_OPEN_MACD.ID_INDICADOR='MACD' 
      AND II_OPEN_COMPARE.TIPO='OPEN' AND II_OPEN_COMPARE.ID_INDICADOR='COMPARE_MA' 
      AND II_OPEN_SAR.TIPO='OPEN' AND II_OPEN_SAR.ID_INDICADOR='SAR' 
      AND II_OPEN_ADX.TIPO='OPEN' AND II_OPEN_ADX.ID_INDICADOR='ADX' 
      AND II_OPEN_RSI.TIPO='OPEN' AND II_OPEN_RSI.ID_INDICADOR='RSI' 
      AND II_OPEN_BOLLINGER.TIPO='OPEN' AND II_OPEN_BOLLINGER.ID_INDICADOR='BOLLINGER' 
      AND II_OPEN_MOMENTUM.TIPO='OPEN' AND II_OPEN_MOMENTUM.ID_INDICADOR='MOMENTUM' 
      AND II_OPEN_ICHITREND.TIPO='OPEN' AND II_OPEN_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' 
      AND II_OPEN_ICHISIGNAL.TIPO='OPEN' AND II_OPEN_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' 
      
      AND II_CLOSE_MA.TIPO='CLOSE' AND II_CLOSE_MA.ID_INDICADOR='MA' 
      AND II_CLOSE_MACD.TIPO='CLOSE' AND II_CLOSE_MACD.ID_INDICADOR='MACD'
      AND II_CLOSE_COMPARE.TIPO='CLOSE' AND II_CLOSE_COMPARE.ID_INDICADOR='COMPARE_MA'
      AND II_CLOSE_SAR.TIPO='CLOSE' AND II_CLOSE_SAR.ID_INDICADOR='SAR' 
      AND II_CLOSE_ADX.TIPO='CLOSE' AND II_CLOSE_ADX.ID_INDICADOR='ADX' 
      AND II_CLOSE_RSI.TIPO='CLOSE' AND II_CLOSE_RSI.ID_INDICADOR='RSI' 
      AND II_CLOSE_BOLLINGER.TIPO='CLOSE' AND II_CLOSE_BOLLINGER.ID_INDICADOR='BOLLINGER' 
      AND II_CLOSE_MOMENTUM.TIPO='CLOSE' AND II_CLOSE_MOMENTUM.ID_INDICADOR='MOMENTUM' 
      AND II_CLOSE_ICHITREND.TIPO='CLOSE' AND II_CLOSE_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' 
      AND II_CLOSE_ICHISIGNAL.TIPO='CLOSE' AND II_CLOSE_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' 
            
      AND IND.ID = TMP.ID_INDIVIDUO
      --AND TMP.CRITERIO_ORDER1>=10000
      ORDER BY TMP.CRITERIO_ORDER1 DESC
;

ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.';
