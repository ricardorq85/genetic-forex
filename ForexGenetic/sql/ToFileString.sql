ALTER SESSION SET NLS_NUMERIC_CHARACTERS = '.,';

SELECT 'EstrategiaId='||II.ID||',VigenciaLower=2001.01.01 00:00'||
    ',VigenciaHigher=2100.01.01 00:00'||
    ',Active=true,Pair='||II.MONEDA||',Operation='||II.TIPO_OPERACION||',TakeProfit='||II.TAKE_PROFIT
  ||',StopLoss='||II.STOP_LOSS
  ||',Lote='||II.LOTE||','||
'OPEN'||'MA'||'LOWER='||II.OPEN_INFERIOR_MA*100||','||'OPEN'||'MA'||'HIGHER='||II.OPEN_SUPERIOR_MA*100||','||
'OPEN'||'MACD'||'LOWER='||II.OPEN_INFERIOR_MACD*100||','||'OPEN'||'MACD'||'HIGHER='||II.OPEN_SUPERIOR_MACD*100||','||
'OPEN'||'MACOMPARE'||'LOWER='||II.OPEN_INFERIOR_COMPARE*100||','||'OPEN'||'MACOMPARE'||'HIGHER='||II.OPEN_SUPERIOR_COMPARE*100||','||
'OPEN'||'SAR'||'LOWER='||II.OPEN_INFERIOR_SAR*100||','||'OPEN'||'SAR'||'HIGHER='||II.OPEN_SUPERIOR_SAR*100||','||
'OPEN'||'ADX'||'LOWER='||II.OPEN_INFERIOR_ADX*100||','||'OPEN'||'ADX'||'HIGHER='||II.OPEN_SUPERIOR_ADX*100||','||
'OPEN'||'RSI'||'LOWER='||II.OPEN_INFERIOR_RSI*100||','||'OPEN'||'RSI'||'HIGHER='||II.OPEN_SUPERIOR_RSI*100||','||
'OPEN'||'BOLLINGER'||'LOWER='||II.OPEN_INFERIOR_BOLLINGER*100||','||'OPEN'||'BOLLINGER'||'HIGHER='||II.OPEN_SUPERIOR_BOLLINGER*100||','||
'OPEN'||'MOMENTUM'||'LOWER='||II.OPEN_INFERIOR_MOMENTUM*100||','||'OPEN'||'MOMENTUM'||'HIGHER='||II.OPEN_SUPERIOR_MOMENTUM*100||','||
'OPEN'||'ICHITREND'||'LOWER='||II.OPEN_INFERIOR_ICHITREND*100||','||'OPEN'||'ICHITREND'||'HIGHER='||II.OPEN_SUPERIOR_ICHITREND*100||','||
'OPEN'||'ICHISIGNAL'||'LOWER='||II.OPEN_INFERIOR_ICHISIGNAL*100||','||'OPEN'||'ICHISIGNAL'||'HIGHER='||II.OPEN_SUPERIOR_ICHISIGNAL*100||','||
'OPEN'||'MA1200'||'LOWER='||II.OPEN_INFERIOR_MA1200*100||','||'OPEN'||'MA1200'||'HIGHER='||II.OPEN_SUPERIOR_MA1200*100||','||
'OPEN'||'MACD20X'||'LOWER='||II.OPEN_INFERIOR_MACD20X*100||','||'OPEN'||'MACD20X'||'HIGHER='||II.OPEN_SUPERIOR_MACD20X*100||','||
'OPEN'||'MACOMPARE1200'||'LOWER='||II.OPEN_INFERIOR_COMPARE1200*100||','||'OPEN'||'MACOMPARE1200'||'HIGHER='||II.OPEN_SUPERIOR_COMPARE1200*100||','||
'OPEN'||'SAR1200'||'LOWER='||II.OPEN_INFERIOR_SAR1200*100||','||'OPEN'||'SAR1200'||'HIGHER='||II.OPEN_SUPERIOR_SAR1200*100||','||
'OPEN'||'ADX168'||'LOWER='||II.OPEN_INFERIOR_ADX168*100||','||'OPEN'||'ADX168'||'HIGHER='||II.OPEN_SUPERIOR_ADX168*100||','||
'OPEN'||'RSI84'||'LOWER='||II.OPEN_INFERIOR_RSI84*100||','||'OPEN'||'RSI84'||'HIGHER='||II.OPEN_SUPERIOR_RSI84*100||','||
'OPEN'||'BOLLINGER240'||'LOWER='||II.OPEN_INFERIOR_BOLLINGER240*100||','||'OPEN'||'BOLLINGER240'||'HIGHER='||II.OPEN_SUPERIOR_BOLLINGER240*100||','||
'OPEN'||'MOMENTUM1200'||'LOWER='||II.OPEN_INFERIOR_MOMENTUM1200*100||','||'OPEN'||'MOMENTUM1200'||'HIGHER='||II.OPEN_SUPERIOR_MOMENTUM1200*100||','||
'OPEN'||'ICHITREND6'||'LOWER='||II.OPEN_INFERIOR_ICHITREND6*100||','||'OPEN'||'ICHITREND6'||'HIGHER='||II.OPEN_SUPERIOR_ICHITREND6*100||','||
'OPEN'||'ICHISIGNAL6'||'LOWER='||II.OPEN_INFERIOR_ICHISIGNAL6*100||','||'OPEN'||'ICHISIGNAL6'||'HIGHER='||II.OPEN_SUPERIOR_ICHISIGNAL6*100||','||

'CLOSE'||'MA'||'LOWER='||II.CLOSE_INFERIOR_MA*100||','||'CLOSE'||'MA'||'HIGHER='||II.CLOSE_SUPERIOR_MA*100||','||
'CLOSE'||'MACD'||'LOWER='||II.CLOSE_INFERIOR_MACD*100||','||'CLOSE'||'MACD'||'HIGHER='||II.CLOSE_SUPERIOR_MACD*100||','||
'CLOSE'||'MACOMPARE'||'LOWER='||II.CLOSE_INFERIOR_COMPARE*100||','||'CLOSE'||'MACOMPARE'||'HIGHER='||II.CLOSE_SUPERIOR_COMPARE*100||','||
'CLOSE'||'SAR'||'LOWER='||II.CLOSE_INFERIOR_SAR*100||','||'CLOSE'||'SAR'||'HIGHER='||II.CLOSE_SUPERIOR_SAR*100||','||
'CLOSE'||'ADX'||'LOWER='||II.CLOSE_INFERIOR_ADX*100||','||'CLOSE'||'ADX'||'HIGHER='||II.CLOSE_SUPERIOR_ADX*100||','||
'CLOSE'||'RSI'||'LOWER='||II.CLOSE_INFERIOR_RSI*100||','||'CLOSE'||'RSI'||'HIGHER='||II.CLOSE_SUPERIOR_RSI*100||','||
'CLOSE'||'BOLLINGER'||'LOWER='||II.CLOSE_INFERIOR_BOLLINGER*100||','||'CLOSE'||'BOLLINGER'||'HIGHER='||II.CLOSE_SUPERIOR_BOLLINGER*100||','||
'CLOSE'||'MOMENTUM'||'LOWER='||II.CLOSE_INFERIOR_MOMENTUM*100||','||'CLOSE'||'MOMENTUM'||'HIGHER='||II.CLOSE_SUPERIOR_MOMENTUM*100||','||
'CLOSE'||'ICHITREND'||'LOWER='||II.CLOSE_INFERIOR_ICHITREND*100||','||'CLOSE'||'ICHITREND'||'HIGHER='||II.CLOSE_SUPERIOR_ICHITREND*100||','||
'CLOSE'||'ICHISIGNAL'||'LOWER='||II.CLOSE_INFERIOR_ICHISIGNAL*100||','||'CLOSE'||'ICHISIGNAL'||'HIGHER='||II.CLOSE_SUPERIOR_ICHISIGNAL*100||','||
'CLOSE'||'MA1200'||'LOWER='||II.CLOSE_INFERIOR_MA1200*100||','||'CLOSE'||'MA1200'||'HIGHER='||II.CLOSE_SUPERIOR_MA1200*100||','||
'CLOSE'||'MACD20X'||'LOWER='||II.CLOSE_INFERIOR_MACD20X*100||','||'CLOSE'||'MACD20X'||'HIGHER='||II.CLOSE_SUPERIOR_MACD20X*100||','||
'CLOSE'||'MACOMPARE1200'||'LOWER='||II.CLOSE_INFERIOR_COMPARE1200*100||','||'CLOSE'||'MACOMPARE1200'||'HIGHER='||II.CLOSE_SUPERIOR_COMPARE1200*100||','||
'CLOSE'||'SAR1200'||'LOWER='||II.CLOSE_INFERIOR_SAR1200*100||','||'CLOSE'||'SAR1200'||'HIGHER='||II.CLOSE_SUPERIOR_SAR1200*100||','||
'CLOSE'||'ADX168'||'LOWER='||II.CLOSE_INFERIOR_ADX168*100||','||'CLOSE'||'ADX168'||'HIGHER='||II.CLOSE_SUPERIOR_ADX168*100||','||
'CLOSE'||'RSI84'||'LOWER='||II.CLOSE_INFERIOR_RSI84*100||','||'CLOSE'||'RSI84'||'HIGHER='||II.CLOSE_SUPERIOR_RSI84*100||','||
'CLOSE'||'BOLLINGER240'||'LOWER='||II.CLOSE_INFERIOR_BOLLINGER240*100||','||'CLOSE'||'BOLLINGER240'||'HIGHER='||II.CLOSE_SUPERIOR_BOLLINGER240*100||','||
'CLOSE'||'MOMENTUM1200'||'LOWER='||II.CLOSE_INFERIOR_MOMENTUM1200*100||','||'CLOSE'||'MOMENTUM1200'||'HIGHER='||II.CLOSE_SUPERIOR_MOMENTUM1200*100||','||
'CLOSE'||'ICHITREND6'||'LOWER='||II.CLOSE_INFERIOR_ICHITREND6*100||','||'CLOSE'||'ICHITREND6'||'HIGHER='||II.CLOSE_SUPERIOR_ICHITREND6*100||','||
'CLOSE'||'ICHISIGNAL6'||'LOWER='||II.CLOSE_INFERIOR_ICHISIGNAL6*100||','||'CLOSE'||'ICHISIGNAL6'||'HIGHER='||II.CLOSE_SUPERIOR_ICHISIGNAL6*100||','
||',' FILESTRING
FROM    INDIVIDUO_INDICADORES II
      WHERE II.TIPO_INDIVIDUO='INDICADORES'      
      AND II.ID IN ('1452804197914.16')
;
ALTER SESSION SET NLS_NUMERIC_CHARACTERS = ',.';
SPOOL OFF;
