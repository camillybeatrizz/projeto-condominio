alter table bloco
    add constraint uk_bloco_condominio_nome unique (condominio_id, nome);
