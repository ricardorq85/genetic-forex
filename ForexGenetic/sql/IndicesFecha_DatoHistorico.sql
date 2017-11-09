CREATE INDEX FECHA_ANYO_IDX ON DATOHISTORICO (TO_CHAR(FECHA, 'YYYY'));
CREATE INDEX FECHA_SEMANA_IDX ON DATOHISTORICO (TO_CHAR(FECHA, 'YYYYWW'));
CREATE INDEX FECHA_DIA_IDX ON DATOHISTORICO (TO_CHAR(FECHA, 'YYYYMMDD'));
CREATE INDEX FECHA_HORA_IDX ON DATOHISTORICO (TO_CHAR(FECHA, 'YYYYMMDD HH24'));
CREATE INDEX FECHA_MINUTO_IDX ON DATOHISTORICO (TO_CHAR(FECHA, 'YYYYMMDD HH24:MI'));
