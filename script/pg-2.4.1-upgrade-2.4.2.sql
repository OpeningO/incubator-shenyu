-- update plugin_handle
ALTER TABLE plugin_handle ALTER COLUMN id TYPE bigint USING(id::bigint);

CREATE TABLE plugin_handle_seq
(
    id bigserial NOT NULL
);
INSERT INTO plugin_handle_seq VALUE('10000')
ALTER TABLE plugin_handle ALTER COLUMN id SET DEFAULT nextval('plugin_handle_seq_id_seq'::regclass);