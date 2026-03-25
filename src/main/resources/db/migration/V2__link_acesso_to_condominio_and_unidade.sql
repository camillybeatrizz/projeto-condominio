alter table acesso
    add column condominio_id uuid;

alter table acesso
    add column unidade_id uuid;

alter table acesso
    add constraint fk_acesso_condominio
        foreign key (condominio_id) references condominio(id);

alter table acesso
    add constraint fk_acesso_unidade
        foreign key (unidade_id) references unidade(id);
