create table endereco (
    id uuid primary key,
    logradouro varchar(255),
    numero varchar(255),
    complemento varchar(255),
    bairro varchar(255),
    cidade varchar(255),
    estado varchar(255),
    cep varchar(255),
    created_at timestamp(6),
    updated_at timestamp(6)
);

create table usuario (
    id uuid primary key,
    nome varchar(255),
    email varchar(255) unique,
    senha varchar(255),
    telefone varchar(255),
    ativo boolean,
    created_at timestamp(6),
    updated_at timestamp(6)
);

create table fornecedor (
    id uuid primary key,
    nome varchar(255),
    cnpj varchar(255),
    telefone varchar(255),
    created_at timestamp(6),
    updated_at timestamp(6)
);

create table condominio (
    id uuid primary key,
    nome varchar(255),
    cnpj varchar(255),
    telefone varchar(255),
    endereco_id uuid,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint fk_condominio_endereco foreign key (endereco_id) references endereco(id)
);

create table bloco (
    id uuid primary key,
    nome varchar(255),
    condominio_id uuid,
    constraint fk_bloco_condominio foreign key (condominio_id) references condominio(id)
);

create table unidade (
    id uuid primary key,
    numero varchar(255),
    andar varchar(255),
    tipo varchar(255),
    bloco_id uuid,
    constraint fk_unidade_bloco foreign key (bloco_id) references bloco(id)
);

create table acesso (
    id uuid primary key,
    usuario_id uuid,
    perfil varchar(255),
    constraint fk_acesso_usuario foreign key (usuario_id) references usuario(id)
);

create table conta_bancaria (
    id uuid primary key,
    banco varchar(255),
    agencia varchar(255),
    conta varchar(255),
    tipo varchar(255),
    condominio_id uuid,
    constraint fk_conta_bancaria_condominio foreign key (condominio_id) references condominio(id)
);

create table contrato (
    id uuid primary key,
    descricao varchar(255),
    valor numeric(19,2),
    data_inicio date,
    data_fim date,
    fornecedor_id uuid,
    condominio_id uuid,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint fk_contrato_fornecedor foreign key (fornecedor_id) references fornecedor(id),
    constraint fk_contrato_condominio foreign key (condominio_id) references condominio(id)
);

create table despesa (
    id uuid primary key,
    descricao varchar(255),
    valor numeric(19,2),
    data date,
    categoria varchar(255),
    condominio_id uuid,
    constraint fk_despesa_condominio foreign key (condominio_id) references condominio(id)
);

create table chamado (
    id uuid primary key,
    descricao varchar(255),
    status varchar(255),
    data_abertura date,
    unidade_id uuid,
    constraint fk_chamado_unidade foreign key (unidade_id) references unidade(id)
);

create table cobranca (
    id uuid primary key,
    valor numeric(19,2),
    vencimento date,
    status varchar(255),
    competencia varchar(255),
    unidade_id uuid,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint fk_cobranca_unidade foreign key (unidade_id) references unidade(id)
);

create table pagamento (
    id uuid primary key,
    valor numeric(19,2),
    data_pagamento date,
    forma varchar(255),
    transaction_id varchar(255) unique,
    cobranca_id uuid,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint fk_pagamento_cobranca foreign key (cobranca_id) references cobranca(id)
);
