UPDATE TENDENCIA_PARA_OPERAR SET ACTIVA=0 WHERE PERIODO='EXTREMO_SINFILTRAR' AND ACTIVA=1;
commit;
exit;
