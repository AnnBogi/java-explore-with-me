create table if not exists public.endpoint_hit
(
    id        bigint generated by default as identity
        primary key,
    app       varchar(255),
    ip        varchar(20),
    timestamp timestamp,
    uri       varchar(2048)
);