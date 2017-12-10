create or replace FUNCTION WEEK_MINUTES(
              in_end_dt IN DATE DEFAULT SYSDATE ,
                in_start_dt IN DATE DEFAULT SYSDATE )
            RETURN NUMBER DETERMINISTIC
          IS
            d          NUMBER; 
            END_DATE     DATE := GREATEST (in_start_dt, in_end_dt); 
            return_val NUMBER;  
            START_DATE   DATE := LEAST (in_start_dt, in_end_dt); 
          BEGIN          
						WITH ALL_DATES AS	(SELECT (END_DATE-START_DATE)*24 HORAS_TOTALES FROM DUAL),
							WEEKEND_DATES AS 
							(
								SELECT START_DATE, END_DATE, TO_CHAR(START_DATE+LEVEL-1, 'HH24') HORAS,
								START_DATE+LEVEL-1 WEEK_DAY
								FROM DUAL
								CONNECT BY START_DATE+LEVEL-1 <= END_DATE
								)
								SELECT ROUND((AD.HORAS_TOTALES-HORAS)*60) 
								INTO return_val
								FROM ALL_DATES AD,
								(
									SELECT COUNT(*)*24 HORAS
									FROM WEEKEND_DATES WHERE TO_CHAR(WEEK_DAY, 'DY', 'nls_date_language=AMERICAN') IN ('SUN', 'SAT')
								)
						;
          IF in_start_dt > in_end_dt THEN
            return_val  := -return_val;
          END IF;
          RETURN return_val;
        END WEEK_MINUTES ;