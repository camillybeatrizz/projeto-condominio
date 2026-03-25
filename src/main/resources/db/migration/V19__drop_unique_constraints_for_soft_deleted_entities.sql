ALTER TABLE condominio DROP CONSTRAINT uk_condominio_cnpj;
ALTER TABLE bloco DROP CONSTRAINT uk_bloco_condominio_nome;
ALTER TABLE unidade DROP CONSTRAINT uk_unidade_bloco_numero;
ALTER TABLE fornecedor DROP CONSTRAINT uk_fornecedor_cnpj;
ALTER TABLE cobranca DROP CONSTRAINT uk_cobranca_unidade_competencia;
