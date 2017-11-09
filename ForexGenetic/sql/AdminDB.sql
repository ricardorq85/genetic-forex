SELECT GROUP#, STATUS, MEMBER FROM V$LOGFILE;

select name, file#, status, enabled from v$datafile;

select distinct error , file# from v$recover_file;

select checkoint_change#,file# from v$datafile_header;

select checkpoint_change# from v$database;

select open_mode from v$database;

 select file#,status from v$backup;  
 
  recover database until cancel using backup controlfile;