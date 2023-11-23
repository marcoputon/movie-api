create table AWARD (
    ID_AWARD        INT             NOT NULL    AUTO_INCREMENT,
    NUM_YEAR        INT             NOT NULL                  ,
    DES_TITLE       VARCHAR(800)    NOT NULL                  ,
    DES_STUDIOS     VARCHAR(800)    NOT NULL                  ,
    DES_PRODUCERS   VARCHAR(800)    NOT NULL                  ,
    DES_WINNER      VARCHAR(800)                              ,
    PRIMARY KEY     (ID_AWARD)
);


create table AWARD_WINNING_PRODUCER (
    ID_AWARD_WINNING_PRODUCER   INT             NOT NULL    AUTO_INCREMENT,
    DES_PRODUCER                VARCHAR(800)    NOT NULL                  ,
    NUM_YEAR                    INT             NOT NULL                  ,
    ID_AWARD                    INT             NOT NULL                  ,
    PRIMARY KEY (ID_AWARD_WINNING_PRODUCER)
);


create table PRODUCER_INTERVAL (
    ID_PRODUCER_INTERVAL     INT            NOT NULL    AUTO_INCREMENT,
    DES_PRODUCER             VARCHAR(800)   NOT NULL                  ,
    NUM_YEAR_START           INT            NOT NULL                  ,
    NUM_YEAR_END             INT            NOT NULL                  ,
    NUM_INTERVAL             INT            NOT NULL                  ,
    PRIMARY KEY (ID_PRODUCER_INTERVAL)
);