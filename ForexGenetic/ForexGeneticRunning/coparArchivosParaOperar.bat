:copy

FOR %%G IN (d:\MyWS\*.*) DO copy "%%G" d:\MyWSForCopying && move "%%G" d:\MyWSForMoving

TIMEOUT /T 5 /NOBREAK

goto :copy

pause