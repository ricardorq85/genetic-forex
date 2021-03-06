alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_MA	 to 	OPEN_INFERIOR_MA;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_MA	 to 	OPEN_SUPERIOR_MA;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_MACD	 to 	OPEN_INFERIOR_MACD;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_MACD	 to 	OPEN_SUPERIOR_MACD;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_COMPARE	 to 	OPEN_INFERIOR_COMPARE;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_COMPARE	 to 	OPEN_SUPERIOR_COMPARE;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_ADX	 to 	OPEN_INFERIOR_ADX;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_ADX	 to 	OPEN_SUPERIOR_ADX;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_BOLLINGER	 to 	OPEN_INFERIOR_BOLLINGER;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_BOLLINGER	 to 	OPEN_SUPERIOR_BOLLINGER;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_ICHISIGNAL	 to 	OPEN_INFERIOR_ICHISIGNAL;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_ICHISIGNAL	 to 	OPEN_SUPERIOR_ICHISIGNAL;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_ICHITREND	 to 	OPEN_INFERIOR_ICHITREND;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_ICHITREND	 to 	OPEN_SUPERIOR_ICHITREND;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_MOMENTUM	 to 	OPEN_INFERIOR_MOMENTUM;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_MOMENTUM	 to 	OPEN_SUPERIOR_MOMENTUM;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_RSI	 to 	OPEN_INFERIOR_RSI;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_RSI	 to 	OPEN_SUPERIOR_RSI;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_SAR	 to 	OPEN_INFERIOR_SAR;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_SAR	 to 	OPEN_SUPERIOR_SAR;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_MA1200	 to 	OPEN_INFERIOR_MA1200;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_MA1200	 to 	OPEN_SUPERIOR_MA1200;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_MACD20X	 to 	OPEN_INFERIOR_MACD20X;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_MACD20X	 to 	OPEN_SUPERIOR_MACD20X;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_COMPARE1200	 to 	OPEN_INFERIOR_COMPARE1200;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_COMPARE1200	 to 	OPEN_SUPERIOR_COMPARE1200;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_ADX168	 to 	OPEN_INFERIOR_ADX168;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_ADX168	 to 	OPEN_SUPERIOR_ADX168;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_BOLLINGER240	 to 	OPEN_INFERIOR_BOLLINGER240;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_BOLLINGER240	 to 	OPEN_SUPERIOR_BOLLINGER240;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_ICHISIGNAL6	 to 	OPEN_INFERIOR_ICHISIGNAL6;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_ICHISIGNAL6	 to 	OPEN_SUPERIOR_ICHISIGNAL6;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_ICHITREND6	 to 	OPEN_INFERIOR_ICHITREND6;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_ICHITREND6	 to 	OPEN_SUPERIOR_ICHITREND6;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_MOMENTUM1200	 to 	OPEN_INFERIOR_MOMENTUM1200;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_MOMENTUM1200	 to 	OPEN_SUPERIOR_MOMENTUM1200;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_RSI84	 to 	OPEN_INFERIOR_RSI84;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_RSI84	 to 	OPEN_SUPERIOR_RSI84;
alter table INDIVIDUO_INDICADORES rename column 	INFERIOR_SAR1200	 to 	OPEN_INFERIOR_SAR1200;
alter table INDIVIDUO_INDICADORES rename column 	SUPERIOR_SAR1200	 to 	OPEN_SUPERIOR_SAR1200;

ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_MA NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_MA NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_MACD NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_MACD NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_COMPARE NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_COMPARE NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_ADX NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_ADX NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_BOLLINGER NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_BOLLINGER NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_ICHISIGNAL NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_ICHISIGNAL NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_ICHITREND NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_ICHITREND NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_MOMENTUM NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_MOMENTUM NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_RSI NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_RSI NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_SAR NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_SAR NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_MA1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_MA1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_MACD20X NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_MACD20X NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_COMPARE1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_COMPARE1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_ADX168 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_ADX168 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_BOLLINGER240 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_BOLLINGER240 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_ICHISIGNAL6 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_ICHISIGNAL6 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_ICHITREND6 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_ICHITREND6 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_MOMENTUM1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_MOMENTUM1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_RSI84 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_RSI84 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_INFERIOR_SAR1200 NUMBER(10, 5) );
ALTER TABLE INDIVIDUO_INDICADORES ADD (	CLOSE_SUPERIOR_SAR1200 NUMBER(10, 5) );

ALTER TABLE INDIVIDUO_INDICADORES ADD (	ESTADO VARCHAR2(20 CHAR) );

UPDATE INDIVIDUO_INDICADORES SET ESTADO='OPEN';
