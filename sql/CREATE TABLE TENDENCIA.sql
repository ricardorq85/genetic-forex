
  CREATE TABLE "FOREX"."TENDENCIA" 
   (	"FECHA_BASE" DATE NOT NULL ENABLE, 
	"PRECIO_BASE" NUMBER(10,5) NOT NULL ENABLE, 
	"ID_INDIVIDUO" VARCHAR2(50 CHAR) NOT NULL ENABLE, 
	"FECHA_TENDENCIA" DATE NOT NULL ENABLE, 
	"PIPS" NUMBER(10,5) NOT NULL ENABLE, 
	"PRECIO_CALCULADO" NUMBER(10,5) NOT NULL ENABLE, 
	"TIPO_TENDENCIA" VARCHAR2(50 BYTE), 
	 CONSTRAINT "TENDENCIA_PK" PRIMARY KEY ("FECHA_BASE", "ID_INDIVIDUO", "FECHA_TENDENCIA")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE, 
	 CONSTRAINT "TENDENCIA_INDIVIDUO_FK1" FOREIGN KEY ("ID_INDIVIDUO")
	  REFERENCES "FOREX"."INDIVIDUO" ("ID") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;

  CREATE INDEX "FOREX"."INDEX1" ON "FOREX"."TENDENCIA" ("FECHA_TENDENCIA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
  CREATE UNIQUE INDEX "FOREX"."TENDENCIA_PK" ON "FOREX"."TENDENCIA" ("FECHA_BASE", "ID_INDIVIDUO", "FECHA_TENDENCIA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;