SELECT
    ii.id_individuo,
    NULL id_individuo_padre
FROM
    forex.indicador_individuo ii
WHERE
    ii.tipo = 'CLOSE'
    AND ii.intervalo_inferior IS NOT NULL
    --AND ii.id_individuo = ?
GROUP BY
    ii.id_individuo
HAVING COUNT(*) > 0
       AND COUNT(*) < 2;