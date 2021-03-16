DROP TABLE t_test IF EXISTS;
DROP TABLE t_spread_event IF EXISTS;
DROP TABLE t_spread_amount IF EXISTS;

CREATE TABLE t_test
(
    no         int(10) NOT NULL IDENTITY PRIMARY KEY,
    name       varchar(100) NOT NULL,
    created_at timestamp    NOT NULL,
    updated_at timestamp NULL DEFAULT NULL
);

CREATE TABLE t_spread_event
(
    no              int(10) NOT NULL IDENTITY PRIMARY KEY,
    state           tinyint(4) NOT NULL DEFAULT '0',
    user_id         int(11) NOT NULL,
    room_id         varchar(100) NOT NULL DEFAULT '',
    token           varchar(100) NOT NULL DEFAULT '',
    total_amount    int(11) NOT NULL,
    receiver_count  int(11) NOT NULL,
    expired_seconds int(11) NOT NULL,
    created_at      timestamp    NOT NULL,
    updated_at      timestamp NULL DEFAULT NULL
);

CREATE TABLE t_spread_amount
(
    no            int(10) NOT NULL IDENTITY PRIMARY KEY,
    event_id      int(11) NOT NULL,
    state         int(11) NOT NULL,
    amount        int(11) NOT NULL,
    receiver_id   int(11) NOT NULL,
    received_date timestamp NULL DEFAULT NULL,
    created_at    timestamp NOT NULL,
    updated_at    timestamp NULL DEFAULT NULL
);