CREATE SCHEMA IF NOT EXISTS demo;

CREATE TABLE demo.item (
    id uuid NOT NULL,
    data varchar(4096) NOT NULL,
    sequence_number BIGINT NOT NULL,
    CONSTRAINT item_pkey PRIMARY KEY (id)
);
