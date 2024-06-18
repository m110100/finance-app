ALTER TABLE sub_categories ADD CONSTRAINT unique_subcategory_per_user UNIQUE(user_id, name);
ALTER TABLE labels ADD CONSTRAINT unique_label_per_user UNIQUE(user_id, name);

ALTER TABLE sub_categories DROP CONSTRAINT sub_categories_name_key;
ALTER TABLE labels DROP CONSTRAINT labels_name_key;
ALTER TABLE wallets DROP CONSTRAINT wallets_name_key;

SELECT con.conname "constraint",
       concat(nsp.nspname, '.', rel.relname) "table",
       (SELECT array_agg(att.attname)
        FROM pg_attribute att
                 INNER JOIN unnest(con.conkey) unnest(conkey)
                            ON unnest.conkey = att.attnum
        WHERE att.attrelid = con.conrelid) "columns"
FROM pg_constraint con
         INNER JOIN pg_class rel
                    ON rel.oid = con.conrelid
         INNER JOIN pg_namespace nsp
                    ON nsp.oid = rel.relnamespace;