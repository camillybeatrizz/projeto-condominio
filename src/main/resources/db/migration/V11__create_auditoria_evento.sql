create table auditoria_evento (
    id uuid primary key,
    tipo_evento varchar(255) not null,
    entidade varchar(255) not null,
    entidade_id varchar(255),
    ator varchar(255) not null,
    detalhe varchar(1000) not null,
    created_at timestamp(6),
    updated_at timestamp(6)
);
