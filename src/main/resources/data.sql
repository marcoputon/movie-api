INSERT INTO AWARD (NUM_YEAR, DES_TITLE, DES_STUDIOS, DES_PRODUCERS, DES_WINNER)
    SELECT
        *
    FROM
        CSVREAD('classpath:movielist.csv', NULL, 'fieldSeparator=;');