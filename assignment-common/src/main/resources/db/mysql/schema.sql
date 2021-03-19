/*
DROP TABLE IF EXISTS t_test;
DROP TABLE IF EXISTS t_spread_event;
DROP TABLE IF EXISTS t_spread_amount;
*/

CREATE TABLE t_spread_event
(
    no              int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    state           tinyint(4) NOT NULL DEFAULT '0',
    user_id         int(11) NOT NULL,
    room_id         varchar(100) NOT NULL DEFAULT '',
    token           varchar(100) NOT NULL DEFAULT '',
    total_amount    int(11) NOT NULL,
    receiver_count  int(11) NOT NULL,
    expired_seconds int(11) NOT NULL,
    created_at      DATETIME    NOT NULL,
    updated_at      DATETIME NULL DEFAULT NULL
);
CREATE INDEX idx_spread_event_token ON t_spread_event (token, room_id);

CREATE TABLE t_spread_amount
(
    no            int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event_id      int(11) NOT NULL,
    state         int(11) NOT NULL,
    amount        int(11) NOT NULL,
    receiver_id   int(11) NOT NULL,
    received_date DATETIME NULL DEFAULT NULL,
    created_at    DATETIME NOT NULL,
    updated_at    DATETIME NULL DEFAULT NULL
);
CREATE INDEX idx_spread_amount_ ON t_spread_amount (event_id);