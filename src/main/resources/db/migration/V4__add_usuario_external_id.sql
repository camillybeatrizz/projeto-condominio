alter table usuario
    add column external_id varchar(255);

alter table usuario
    add constraint uk_usuario_external_id unique (external_id);
