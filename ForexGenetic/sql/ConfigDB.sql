--Before Import
-- Validar rutas del oracle home

-- Crear usuario FOREX con SYS as SYSDBA
@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\CreateUserFOREX.sql"
@"C:\Users\Angela\Documents\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\CreateUserFOREX.sql"

-- Alter tablespace USERS con SYS as SYSDBA
@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\ALTER TABLESPACE USERS.sql"

-- Create tablespace TS_FOREX2
@"d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\CREATE TABLESPACE TS_FOREX2.sql"

--Ejecutar import sin conectarse a la BD
d:\ricardorq85\JavaProjects\Git\genetic-forex\ForexGenetic\sql\Import.bat

