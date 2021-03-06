
SELECT COUNT(*) FROM OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO IN
		(SELECT ID FROM ESTRATEGIA_OPERACION_PERIODO WHERE ID<=147129 
			AND PIPS_TOTALES<-10000 AND PIPS_TOTALES_PARALELAS<-50000
			AND (PIPS_AGRUPADO_MINUTOS IS NULL OR PIPS_AGRUPADO_MINUTOS<-50000)
			AND (PIPS_AGRUPADO_HORAS IS NULL OR PIPS_AGRUPADO_HORAS<-30000)
			AND (PIPS_AGRUPADO_DIAS IS NULL OR PIPS_AGRUPADO_DIAS<-10000)      
      )
--AND ROWNUM<20000
;
*/

DECLARE

BEGIN
  FOR i IN 1..20 LOOP
    DELETE FROM OPERACION_ESTRATEGIA_PERIODO WHERE ESTRATEGIA_PERIODO IN
		(SELECT ID FROM ESTRATEGIA_OPERACION_PERIODO WHERE ID<=147129 
			AND PIPS_TOTALES<-10000 AND PIPS_TOTALES_PARALELAS<-50000
			AND (PIPS_AGRUPADO_MINUTOS IS NULL OR PIPS_AGRUPADO_MINUTOS<-50000)
			AND (PIPS_AGRUPADO_HORAS IS NULL OR PIPS_AGRUPADO_HORAS<-30000)
			AND (PIPS_AGRUPADO_DIAS IS NULL OR PIPS_AGRUPADO_DIAS<-10000)      
      )
      AND ROWNUM<12000;
    --DELETE FROM ESTRATEGIA_OPERACION_PERIODO WHERE ID>=147129
    --AND ROWNUM<100;
    --UPDATE ESTRATEGIA_OPERACION_PERIODO SET FECHA_INICIAL=FECHA_INICIAL-1 WHERE ID>=147130;
	  COMMIT;
  END LOOP;
END;