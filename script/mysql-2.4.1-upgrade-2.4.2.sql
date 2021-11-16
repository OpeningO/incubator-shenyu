-- update plugin_handle id to bigint
ALTER TABLE plugin_handle MODIFY COLUMN id BIGINT unsigned NOT NULL AUTO_INCREMENT;
-- update shenyu_dict id to bigint
ALTER TABLE shenyu_dict MODIFY COLUMN id BIGINT unsigned NOT NULL AUTO_INCREMENT;