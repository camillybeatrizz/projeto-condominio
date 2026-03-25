ALTER TABLE usuario
    ADD COLUMN asaas_customer_id VARCHAR(255);

ALTER TABLE usuario
    ADD CONSTRAINT uk_usuario_asaas_customer_id UNIQUE (asaas_customer_id);
