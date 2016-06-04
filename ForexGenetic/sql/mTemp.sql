SET echo off;
SET feedback off;
SET term off;
SET pagesize 0;
SET linesize 2000;
SET newpage 0;
SET space 0;

SPOOL D:/mTemp.txt;
ALTER SESSION SET NLS_NUMERIC_CHARACTERS = '.,';
SELECT * FROM DUAL;
SPOOL OFF;