alter table fornecedor
    add constraint uk_fornecedor_cnpj unique (cnpj);
