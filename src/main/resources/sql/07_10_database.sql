create schema if not exists PUBLIC
;

create table DIVISION
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_25F4E63E_7414_49F5_94AB_415490C60B9A)
		primary key,
	NAME_DIVISION VARCHAR(50) not null
		constraint DIVISION_NAME_DIVISION_UINDEX
			unique,
	DESCRIPTION VARCHAR(150)
)
;

create table EMPLOYEES
(
	EMP_NO INTEGER not null
		primary key,
	BIRTH_DATE DATE not null,
	FIRST_NAME VARCHAR(14) not null,
	LAST_NAME VARCHAR(16) not null,
	GENDER ENUM(max) not null,
	HIRE_DATE DATE not null
)
;

create table ENVIRONNEMENT
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_485B9D74_EF00_4B3D_8325_B39B04DF9300),
	ENVIRONNEMENT_NAME VARCHAR(20)
)
;

create unique index ENVIRONNEMENT_ID_UINDEX
	on ENVIRONNEMENT (ID)
;

create unique index PRIMARY_KEY_8
	on ENVIRONNEMENT (ID)
;

alter table ENVIRONNEMENT
	add primary key (ID)
;

create table FORMULAIRE_LIST
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_FAA522A9_2BA6_41D6_8011_068F3364CDAA)
		primary key,
	LIBELLE_FORMULAIRE VARCHAR(50)
)
;

create table FORMULAIRE_PARAMETER
(
	CODE VARCHAR(50) not null,
	LIBELLE VARCHAR(50),
	TYPE_REPRESENTATION VARCHAR(50)
)
;

create unique index CODE_PARAMETER_CODE_PARAMETER_UINDEX
	on FORMULAIRE_PARAMETER (CODE)
;

create unique index PRIMARY_KEY_D1
	on FORMULAIRE_PARAMETER (CODE)
;

alter table FORMULAIRE_PARAMETER
	add primary key (CODE)
;

create table FORMULAIRE_QUESTION
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_1F15F048_8556_4D92_A7BA_F0EBF6EFFE9C)
		primary key,
	ID_FORMULAIRE INTEGER,
	CODE_PARAMETER VARCHAR(50),
	LIBELLE VARCHAR(50),
	DESCRIPTION VARCHAR(50),
	FLAG_MANDATORY INTEGER not null,
	QUESTION_ORDER INTEGER,
	TYPE_FIELD VARCHAR(10),
	LARGEUR INTEGER,
	constraint FORMULAIRE_LIST_CODE_PARAMETER_CODE_PARAMETER_FK
		foreign key (CODE_PARAMETER) references FORMULAIRE_PARAMETER
)
;

create table FORMULAIRE_VALUE
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_7A03FCAB_2B86_4277_ADFB_043232DEFB4D)
		primary key,
	CODE_VALUE VARCHAR(50),
	LIBELLE_VALUE VARCHAR(50),
	FORMULAIREPARAMETER_CODE VARCHAR(10),
	constraint PARAMETER_CODE_PARAMETER_CODE_PARAMETER_FK
		foreign key (FORMULAIREPARAMETER_CODE) references FORMULAIRE_PARAMETER
)
;

create table PRODUCT
(
	ID BIGINT not null
		primary key,
	ADMIN BOOLEAN,
	NAME VARCHAR(255)
)
;

create table TEAM
(
	NOMTEAM VARCHAR(50) not null,
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_A30D8C85_2312_402D_8EDE_8637CDFCE82F)
		primary key,
	USERDIVISION_ID BIGINT,
	TEAMBOSSNAME VARCHAR(50),
	TEAMBOSSEMAIL VARCHAR(50),
	constraint FK18SAUD0G1WGP083F2WCW44T96
		foreign key (USERDIVISION_ID) references DIVISION,
	constraint TEAM_DIVISION_ID_FK
		foreign key (USERDIVISION_ID) references DIVISION
)
;

create table CONTACTS
(
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_1EE4ABA3_4A3C_4506_8881_94BD5F584148)
		primary key,
	NAME VARCHAR(50),
	CONTACT_EMAIL VARCHAR(50),
	TEAM_ID BIGINT not null,
	constraint CONTACTS_TEAM_ID_FK
		foreign key (TEAM_ID) references TEAM
)
;

create table FORMULAIRE_RESULTAT
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_B4AF11E4_C25D_43BD_AB0B_7671F9F4BD3B)
		primary key,
	DATE_CREATION DATE,
	TEAM_ID INTEGER,
	FORMULAIRE_ID INTEGER,
	constraint FORMULAIRE_RESULTAT_FORMULAIRE_LIST_ID_FK
		foreign key (FORMULAIRE_ID) references FORMULAIRE_LIST,
	constraint FORMULAIRE_RESULTAT_TEAM_ID_FK
		foreign key (TEAM_ID) references TEAM
)
;

create table FORMULAIRE_REPONSE
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_35C62C39_0C94_4C69_8A8A_CBD59BA49719)
		primary key,
	ID_QUESTION INTEGER,
	REPONSE VARCHAR(2000),
	ID_RESULTAT INTEGER,
	constraint FORMULAIRE_REPONSE_FORMULAIRE_QUESTION_ID_FK
		foreign key (ID_QUESTION) references FORMULAIRE_QUESTION,
	constraint FORMULAIRE_REPONSE_FORMULAIRE_RESULTAT_ID_FK
		foreign key (ID_RESULTAT) references FORMULAIRE_RESULTAT
)
;

create table FORMULAIRE_TEAM
(
	ID_FORMULAIRE INTEGER,
	ID_TEAM INTEGER,
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_139E36F9_38D3_4EE2_95C9_076C34057BCF)
		primary key,
	constraint FORMULAIRE_TEAM_FORMULAIRE_LIST_ID_FK
		foreign key (ID_FORMULAIRE) references FORMULAIRE_LIST,
	constraint FORMULAIRE_TEAM_TEAM_ID_FK
		foreign key (ID_TEAM) references TEAM
)
;

create table USER
(
	ID BIGINT not null
		primary key,
	NOM VARCHAR(255) not null,
	ACTIVE BOOLEAN,
	DATECREATION TIMESTAMP,
	EMAIL VARCHAR(255) not null,
	GENDER INTEGER,
	MATRICULE INTEGER not null,
	PASSWORD VARCHAR(100) not null,
	MAINPRODUCT_ID BIGINT,
	TEAMID_ID BIGINT,
	constraint FK8GA285XNNMH27TUXWDWHQMMNP
		foreign key (TEAMID_ID) references TEAM,
	constraint FKCO3GFS3N2QEVRS4UG842KQKW2
		foreign key (MAINPRODUCT_ID) references PRODUCT,
	constraint FKEPSVYLIC4X63C14ISTDNV0D2P
		foreign key (TEAMID_ID) references TEAM,
	constraint FKOYT5LTRM670TRXVXFGS7FJTVB
		foreign key (TEAMID_ID) references TEAM,
	constraint USER_TEAM_ID_FK
		foreign key (TEAMID_ID) references TEAM
)
;

create table USER_ENVIRONNEMENT
(
	USER_ID BIGINT not null,
	ENVIRONNEMENTS_ID BIGINT not null,
	constraint USER_ENVIRONNEMENT_PK_2
		primary key (USER_ID, ENVIRONNEMENTS_ID),
	constraint USER_ENVIRONNEMENT_ENVIRONNEMENT_ID_FK
		foreign key (ENVIRONNEMENTS_ID) references ENVIRONNEMENT,
	constraint USER_ENVIRONNEMENT_USER_ID_FK
		foreign key (USER_ID) references USER
)
;

create table USER_PRODUCT
(
	USER_ID BIGINT not null,
	PRODUCTS_ID BIGINT not null,
	primary key (USER_ID, PRODUCTS_ID),
	constraint FKBT8RTEPMVTSBHBJCS6ADUI5BY
		foreign key (USER_ID) references USER,
	constraint FKIO1UHULOD679XJAYF81CNVNCO
		foreign key (PRODUCTS_ID) references PRODUCT
)
;

