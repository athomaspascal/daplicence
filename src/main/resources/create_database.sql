create table DIVISION
(
  ID            INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_25F4E63E_7414_49F5_94AB_415490C60B9A)
    primary key,
  NAME_DIVISION VARCHAR(50) not null
    constraint DIVISION_NAME_DIVISION_UINDEX
    unique,
  DESCRIPTION   VARCHAR(150)
);

create table PRODUCT
(
  ID    BIGINT not null
    primary key,
  ADMIN BOOLEAN,
  NAME  VARCHAR(255)
);

create table TEAM
(
  NOM_TEAM        VARCHAR(50),
  ID              BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_D63D3972_523D_4DEE_90A1_F0CCCF3DA54C)
    primary key,
  USERDIVISION_ID BIGINT,
  constraint TEAM_DIVISION_ID_FK
  foreign key (USERDIVISION_ID) references DIVISION
);

create table USER
(
  ID             BIGINT       not null
    primary key,
  NOM            VARCHAR(255) not null,
  ACTIVE         BOOLEAN,
  DATECREATION   TIMESTAMP,
  EMAIL          VARCHAR(255) not null,
  GENDER         INTEGER,
  MATRICULE      INTEGER      not null,
  PASSWORD       VARCHAR(100) not null,
  MAINPRODUCT_ID BIGINT,
  TEAMID_ID      BIGINT,
  constraint FK8GA285XNNMH27TUXWDWHQMMNP
  foreign key (TEAMID_ID) references TEAM,
  constraint FKEPSVYLIC4X63C14ISTDNV0D2P
  foreign key (TEAMID_ID) references TEAM,
  constraint FKOYT5LTRM670TRXVXFGS7FJTVB
  foreign key (TEAMID_ID) references TEAM,
  constraint USER_TEAM_ID_FK
  foreign key (TEAMID_ID) references TEAM
);

create table USER_PRODUCT
(
  USER_ID     BIGINT not null,
  PRODUCTS_ID BIGINT not null,
  primary key (USER_ID, PRODUCTS_ID),
  constraint FKBT8RTEPMVTSBHBJCS6ADUI5BY
  foreign key (USER_ID) references USER,
  constraint FKIO1UHULOD679XJAYF81CNVNCO
  foreign key (PRODUCTS_ID) references PRODUCT
);

t