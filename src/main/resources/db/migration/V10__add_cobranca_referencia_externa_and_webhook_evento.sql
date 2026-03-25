alter table cobranca
    add column referencia_externa varchar(255);

alter table cobranca
    add constraint uk_cobranca_referencia_externa unique (referencia_externa);

create table webhook_evento_processado (
    id uuid primary key,
    provedor varchar(255) not null,
    evento_externo_id varchar(255) not null,
    tipo_evento varchar(255) not null,
    payload_resumo varchar(1000),
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint uk_webhook_evento_externo_id unique (evento_externo_id)
);
