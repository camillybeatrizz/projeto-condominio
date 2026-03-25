alter table cobranca
    add constraint uk_cobranca_unidade_competencia unique (unidade_id, competencia);
