CREATE OR REPLACE VIEW FOREX.DATOHIST_INDICADORINDIVIDUO AS
  SELECT IND.ID ID_INDIVIDUO, IND.TAKE_PROFIT, IND.STOP_LOSS, DH1_OPEN.FECHA OPEN_FECHA, DH1_CLOSE.FECHA CLOSE_FECHA
  ,CALCULAR_PIPS(DH1_OPEN.OPEN,DH1_OPEN.CLOSE,DH1_OPEN.HIGH,DH1_OPEN.LOW, DH1_CLOSE.OPEN,DH1_CLOSE.CLOSE,DH1_CLOSE.HIGH,DH1_CLOSE.LOW,DH1_OPEN.SPREAD) PIPS
  ,PROCESS_OPEN_PRICE_DEPENDENT(II_OPEN_MA.INTERVALO_INFERIOR, II_OPEN_MA.INTERVALO_SUPERIOR, DH1_OPEN.AVERAGE, DH1_OPEN.FECHA) OPEN_MA
  ,PROCESS_OPEN(II_OPEN_MACD.INTERVALO_INFERIOR, II_OPEN_MACD.INTERVALO_SUPERIOR, DH1_OPEN.MACD_VALUE, DH1_OPEN.MACD_SIGNAL) OPEN_MACD
  ,PROCESS_OPEN(II_OPEN_COMPARE.INTERVALO_INFERIOR, II_OPEN_COMPARE.INTERVALO_SUPERIOR, DH1_OPEN.COMPARE_VALUE, DH1_OPEN.AVERAGE_COMPARE) OPEN_COMPARE
  ,PROCESS_OPEN_PRICE_DEPENDENT(II_OPEN_SAR.INTERVALO_INFERIOR, II_OPEN_SAR.INTERVALO_SUPERIOR, DH1_OPEN.SAR, DH1_OPEN.FECHA) OPEN_SAR
  ,PROCESS_OPEN(II_OPEN_ADX.INTERVALO_INFERIOR, II_OPEN_ADX.INTERVALO_SUPERIOR, DH1_OPEN.ADX_VALUE*DH1_OPEN.ADX_PLUS, DH1_OPEN.ADX_VALUE*DH1_OPEN.ADX_MINUS) OPEN_ADX
  ,PROCESS_OPEN(II_OPEN_RSI.INTERVALO_INFERIOR, II_OPEN_RSI.INTERVALO_SUPERIOR, DH1_OPEN.RSI, DH1_OPEN.RSI) OPEN_RSI
  ,PROCESS_OPEN(II_OPEN_BOLLINGER.INTERVALO_INFERIOR, II_OPEN_BOLLINGER.INTERVALO_SUPERIOR, DH1_OPEN.BOLLINGER_UPPER, DH1_OPEN.BOLLINGER_LOWER) OPEN_BOLLINGER
  ,PROCESS_OPEN(II_OPEN_MOMENTUM.INTERVALO_INFERIOR, II_OPEN_MOMENTUM.INTERVALO_SUPERIOR, DH1_OPEN.MOMENTUM, 0) OPEN_MOMENTUM
  ,PROCESS_OPEN_PRICE_DEPENDENT(II_OPEN_ICHITREND.INTERVALO_INFERIOR, II_OPEN_ICHITREND.INTERVALO_SUPERIOR, DH1_OPEN.ICHIMOKUSENKOUSPANA-DH1_OPEN.ICHIMOKUSENKOUSPANB, DH1_OPEN.FECHA) OPEN_ICHITREND
  ,PROCESS_OPEN(II_OPEN_ICHISIGNAL.INTERVALO_INFERIOR, II_OPEN_ICHISIGNAL.INTERVALO_SUPERIOR, DH1_OPEN.ICHIMOKUCHINKOUSPAN*DH1_OPEN.ICHIMOKUTENKANSEN, DH1_OPEN.ICHIMOKUCHINKOUSPAN*DH1_OPEN.ICHIMOKUKIJUNSEN) OPEN_ICHISIGNAL
  ,PROCESS_CLOSE_PIPS(CALCULAR_PIPS(DH1_OPEN.OPEN,DH1_OPEN.CLOSE,DH1_OPEN.HIGH,DH1_OPEN.LOW, DH1_CLOSE.OPEN,DH1_CLOSE.CLOSE,DH1_CLOSE.HIGH,DH1_CLOSE.LOW,DH1_OPEN.SPREAD), IND.TAKE_PROFIT, IND.STOP_LOSS) CLOSE_BY_PIPS
  ,PROCESS_CLOSE_PRICE_DEPENDENT(II_CLOSE_MA.INTERVALO_INFERIOR, II_CLOSE_MA.INTERVALO_SUPERIOR, DH1_CLOSE.AVERAGE, DH1_CLOSE.FECHA) CLOSE_MA
  ,PROCESS_CLOSE(II_CLOSE_MACD.INTERVALO_INFERIOR, II_CLOSE_MACD.INTERVALO_SUPERIOR, DH1_CLOSE.MACD_VALUE, DH1_CLOSE.MACD_SIGNAL) CLOSE_MACD
  ,PROCESS_CLOSE(II_CLOSE_COMPARE.INTERVALO_INFERIOR, II_CLOSE_COMPARE.INTERVALO_SUPERIOR, DH1_CLOSE.COMPARE_VALUE, DH1_CLOSE.AVERAGE_COMPARE) CLOSE_COMPARE
  ,PROCESS_CLOSE_PRICE_DEPENDENT(II_CLOSE_SAR.INTERVALO_INFERIOR, II_CLOSE_SAR.INTERVALO_SUPERIOR, DH1_CLOSE.SAR, DH1_CLOSE.FECHA) CLOSE_SAR
  ,PROCESS_CLOSE(II_CLOSE_ADX.INTERVALO_INFERIOR, II_CLOSE_ADX.INTERVALO_SUPERIOR, DH1_CLOSE.ADX_VALUE*DH1_CLOSE.ADX_PLUS, DH1_CLOSE.ADX_VALUE*DH1_CLOSE.ADX_MINUS) CLOSE_ADX
  ,PROCESS_CLOSE(II_CLOSE_RSI.INTERVALO_INFERIOR, II_CLOSE_RSI.INTERVALO_SUPERIOR, DH1_CLOSE.RSI, 0) CLOSE_RSI
  ,PROCESS_CLOSE(II_CLOSE_BOLLINGER.INTERVALO_INFERIOR, II_CLOSE_BOLLINGER.INTERVALO_SUPERIOR, DH1_CLOSE.BOLLINGER_UPPER, DH1_CLOSE.BOLLINGER_LOWER) CLOSE_BOLLINGER
  ,PROCESS_CLOSE(II_CLOSE_MOMENTUM.INTERVALO_INFERIOR, II_CLOSE_MOMENTUM.INTERVALO_SUPERIOR, DH1_CLOSE.MOMENTUM, 0) CLOSE_MOMENTUM
  ,PROCESS_CLOSE_PRICE_DEPENDENT(II_CLOSE_ICHITREND.INTERVALO_INFERIOR, II_CLOSE_ICHITREND.INTERVALO_SUPERIOR, DH1_CLOSE.ICHIMOKUSENKOUSPANA-DH1_CLOSE.ICHIMOKUSENKOUSPANB, DH1_CLOSE.FECHA) CLOSE_ICHITREND
  ,PROCESS_CLOSE(II_CLOSE_ICHISIGNAL.INTERVALO_INFERIOR, II_CLOSE_ICHISIGNAL.INTERVALO_SUPERIOR, DH1_CLOSE.ICHIMOKUCHINKOUSPAN*DH1_CLOSE.ICHIMOKUTENKANSEN, DH1_CLOSE.ICHIMOKUCHINKOUSPAN*DH1_CLOSE.ICHIMOKUKIJUNSEN) CLOSE_ICHISIGNAL
    FROM DATOHISTORICO DH1_OPEN, DATOHISTORICO DH1_CLOSE, INDIVIDUO IND, 
      INDICADOR_INDIVIDUO II_OPEN_MA, INDICADOR_INDIVIDUO II_CLOSE_MA,
      INDICADOR_INDIVIDUO II_OPEN_MACD, INDICADOR_INDIVIDUO II_CLOSE_MACD,
      INDICADOR_INDIVIDUO II_OPEN_COMPARE, INDICADOR_INDIVIDUO II_CLOSE_COMPARE,
      INDICADOR_INDIVIDUO II_OPEN_SAR, INDICADOR_INDIVIDUO II_CLOSE_SAR,
      INDICADOR_INDIVIDUO II_OPEN_ADX, INDICADOR_INDIVIDUO II_CLOSE_ADX,
      INDICADOR_INDIVIDUO II_OPEN_RSI, INDICADOR_INDIVIDUO II_CLOSE_RSI,
      INDICADOR_INDIVIDUO II_OPEN_BOLLINGER, INDICADOR_INDIVIDUO II_CLOSE_BOLLINGER,
      INDICADOR_INDIVIDUO II_OPEN_MOMENTUM, INDICADOR_INDIVIDUO II_CLOSE_MOMENTUM,
      INDICADOR_INDIVIDUO II_OPEN_ICHITREND, INDICADOR_INDIVIDUO II_CLOSE_ICHITREND,
      INDICADOR_INDIVIDUO II_OPEN_ICHISIGNAL, INDICADOR_INDIVIDUO II_CLOSE_ICHISIGNAL
      
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
;
/