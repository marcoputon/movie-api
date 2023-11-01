create table AWARD (
  ID_AWARD      int          not null AUTO_INCREMENT,
  NUM_YEAR      int          not null               ,
  DES_TITLE     varchar(800) not null               ,
  DES_STUDIOS   varchar(800) not null               ,
  DES_PRODUCERS varchar(800) not null               ,
  DES_WINNER    varchar(800)                        ,
  PRIMARY KEY (ID_AWARD)
);