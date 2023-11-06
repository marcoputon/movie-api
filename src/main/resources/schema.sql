create table AWARD (
    ID_AWARD        INT             NOT NULL    AUTO_INCREMENT,
    NUM_YEAR        INT             NOT NULL                  ,
    DES_TITLE       VARCHAR(800)    NOT NULL                  ,
    DES_STUDIOS     VARCHAR(800)    NOT NULL                  ,
    DES_PRODUCERS   VARCHAR(800)    NOT NULL                  ,
    DES_WINNER      VARCHAR(800)                              ,
    PRIMARY KEY     (ID_AWARD)
);


create table PRODUCER (
    ID_PRODUCER     INT             NOT NULL    AUTO_INCREMENT,
    DES_PRODUCER    VARCHAR(800)    NOT NULL                  ,
    ID_AWARD        INT             NOT NULL                  ,
    PRIMARY KEY     (ID_PRODUCER)                             ,
    FOREIGN KEY     (ID_AWARD)      REFERENCES  AWARD(ID_AWARD)
);