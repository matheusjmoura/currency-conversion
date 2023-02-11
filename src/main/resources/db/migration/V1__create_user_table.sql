DROP TABLE IF EXISTS "user";
CREATE TABLE "user"
(
    id      UUID         NOT NULL,
    name    VARCHAR(255) NOT NULL,
    version LONG         NOT NULL,
    PRIMARY KEY (id)
);