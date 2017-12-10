update INDICADOR_INDIVIDUO 
set intervalo_inferior=0,
intervalo_superior=0.0020
where id_individuo='1453680000000.11'
AND id_indicador='MA'
and tipo='OPEN';

update INDICADOR_INDIVIDUO 
set intervalo_inferior=-0.0010,
intervalo_superior=0
where id_individuo='1453680000000.11'
AND id_indicador='MA1200'
and tipo='OPEN';
