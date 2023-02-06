DROP TABLE IF EXISTS "user";
CREATE TABLE "user"
(
    id      UUID         NOT NULL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    version LONG         NOT NULL
);