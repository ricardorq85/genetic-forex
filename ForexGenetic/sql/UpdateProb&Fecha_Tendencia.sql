UPDATE TENDENCIA SET PROBABILIDAD=PROBABILIDAD_POSITIVOS,FECHA=SYSDATE
WHERE PROBABILIDAD_POSITIVOS>=PROBABILIDAD_NEGATIVOS AND FECHA IS NULL;
COMMIT;

UPDATE TENDENCIA SET PROBABILIDAD=PROBABILIDAD_NEGATIVOS,FECHA=SYSDATE
WHERE PROBABILIDAD_NEGATIVOS>PROBABILIDAD_POSITIVOS  AND FECHA IS NULL;
COMMIT;