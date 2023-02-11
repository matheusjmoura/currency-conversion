DROP TABLE IF EXISTS "exchange";
CREATE TABLE "exchange"
(
    id               UUID           NOT NULL PRIMARY KEY,
    user_id          UUID           NOT NULL,
    origin_currency  VARCHAR(3)     NOT NULL,
    origin_value     DECIMAL(18, 6) NOT NULL,
    destiny_currency VARCHAR(3)     NOT NULL,
    tax_rate         DECIMAL(18, 6) NOT NULL,
    date_time        TIMESTAMP      NOT NULL,
    version          LONG           NOT NULL,
    CONSTRAINT fk_user_exchange FOREIGN KEY (user_id) REFERENCES "user" (id)
);