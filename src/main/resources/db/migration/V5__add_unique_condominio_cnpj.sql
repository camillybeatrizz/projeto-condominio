alter table condominio
    add constraint uk_condominio_cnpj unique (cnpj);
