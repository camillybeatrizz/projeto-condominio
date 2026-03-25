alter table conta_bancaria
    add constraint uk_conta_bancaria_condominio_agencia_conta unique (condominio_id, agencia, conta);
