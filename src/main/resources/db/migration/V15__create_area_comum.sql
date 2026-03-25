create table area_comum (
    id uuid primary key,
    nome varchar(120) not null,
    descricao varchar(255),
    capacidade integer not null,
    condominio_id uuid not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint fk_area_comum_condominio foreign key (condominio_id) references condominio (id)
);

alter table area_comum
    add constraint uk_area_comum_condominio_nome unique (condominio_id, nome);
