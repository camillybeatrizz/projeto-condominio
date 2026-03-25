alter table unidade
    add constraint uk_unidade_bloco_numero unique (bloco_id, numero);
