CREATE TABLE IF NOT EXISTS statistics (
    id        int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app       varchar(100) NOT NULL,
    uri       text         NOT NULL,
    ip        varchar(40)  NOT NULL,
    timestamp timestamp    NOT NULL
);