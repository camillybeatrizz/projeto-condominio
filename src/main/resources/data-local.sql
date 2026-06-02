insert into endereco (id, logradouro, numero, complemento, bairro, cidade, estado, cep, created_at, updated_at)
values ('650e8400-e29b-41d4-a716-446655440001', 'Rua das Palmeiras', '100', 'Portaria A', 'Jardim Aurora', 'Joao Pessoa', 'PB', '58000-000', current_timestamp, current_timestamp);

insert into usuario (id, nome, email, telefone, ativo, created_at, updated_at, external_id, asaas_customer_id)
values
  ('550e8400-e29b-41d4-a716-446655440000', 'Joao da Silva', 'joao@kondo.com', '(83) 99999-0001', true, current_timestamp, current_timestamp, 'mock-token', 'cus_demo_joao'),
  ('550e8400-e29b-41d4-a716-446655440001', 'Maria Oliveira', 'maria@kondo.com', '(83) 99999-0002', true, current_timestamp, current_timestamp, 'mock-maria', 'cus_demo_maria');

insert into condominio (id, nome, cnpj, telefone, endereco_id, created_at, updated_at, deleted_at, deleted_by)
values ('660e8400-e29b-41d4-a716-446655440001', 'Residencial Aurora', '12.345.678/0001-90', '(83) 3333-0000', '650e8400-e29b-41d4-a716-446655440001', current_timestamp, current_timestamp, null, null);

insert into bloco (id, nome, condominio_id, deleted_at, deleted_by)
values
  ('bb0e8400-e29b-41d4-a716-446655440006', 'Bloco A', '660e8400-e29b-41d4-a716-446655440001', null, null),
  ('bb0e8400-e29b-41d4-a716-446655440007', 'Bloco B', '660e8400-e29b-41d4-a716-446655440001', null, null);

insert into unidade (id, numero, andar, tipo, bloco_id, morador_id, deleted_at, deleted_by)
values
  ('770e8400-e29b-41d4-a716-446655440002', '101', '1', 'APARTAMENTO', 'bb0e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440000', null, null),
  ('770e8400-e29b-41d4-a716-446655440003', '102', '1', 'APARTAMENTO', 'bb0e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440001', null, null),
  ('770e8400-e29b-41d4-a716-446655440004', '201', '2', 'APARTAMENTO', 'bb0e8400-e29b-41d4-a716-446655440007', null, null, null);

insert into acesso (id, usuario_id, perfil, condominio_id, unidade_id)
values
  ('cc0e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440000', 'SINDICO', '660e8400-e29b-41d4-a716-446655440001', null),
  ('cc0e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440000', 'MORADOR', '660e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440002'),
  ('cc0e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440000', 'ADMIN', null, null);

insert into area_comum (id, nome, descricao, capacidade, condominio_id, created_at, updated_at, deleted_at, deleted_by)
values
  ('aa0e8400-e29b-41d4-a716-446655440001', 'Piscina', 'Area de lazer principal', 24, '660e8400-e29b-41d4-a716-446655440001', current_timestamp, current_timestamp, null, null),
  ('aa0e8400-e29b-41d4-a716-446655440002', 'Salao de Festas', 'Espaco para eventos dos moradores', 80, '660e8400-e29b-41d4-a716-446655440001', current_timestamp, current_timestamp, null, null);

insert into cobranca (id, valor, vencimento, status, competencia, unidade_id, created_at, updated_at, referencia_externa, url_pagamento_externo, pix_qr_code_base64, pix_copia_cola, pix_expiracao, deleted_at, deleted_by)
values
  ('aa0e8400-e29b-41d4-a716-446655440005', 450.00, date '2026-06-10', 'ABERTA', '2026-06', '770e8400-e29b-41d4-a716-446655440002', current_timestamp, current_timestamp, 'pay_demo_aberta_101', 'https://sandbox.asaas.com/i/pay_demo_aberta_101', 'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+/p9sAAAAASUVORK5CYII=', '00020101021226890014br.gov.bcb.pix2567pix-demo-kondo-101', timestamp '2026-06-10 23:59:59', null, null),
  ('aa0e8400-e29b-41d4-a716-446655440006', 450.00, date '2026-05-10', 'PAGA', '2026-05', '770e8400-e29b-41d4-a716-446655440002', current_timestamp, current_timestamp, 'pay_demo_paga_101', 'https://sandbox.asaas.com/i/pay_demo_paga_101', null, null, null, null, null),
  ('aa0e8400-e29b-41d4-a716-446655440007', 450.00, date '2026-04-10', 'VENCIDA', '2026-04', '770e8400-e29b-41d4-a716-446655440003', current_timestamp, current_timestamp, 'pay_demo_vencida_102', 'https://sandbox.asaas.com/i/pay_demo_vencida_102', null, null, null, null, null);

insert into pagamento (id, valor, data_pagamento, forma, transaction_id, cobranca_id, created_at, updated_at, deleted_at, deleted_by)
values ('220e8400-e29b-41d4-a716-446655440012', 450.00, date '2026-05-08', 'PIX', 'PIX-DEMO-20260508-001', 'aa0e8400-e29b-41d4-a716-446655440006', current_timestamp, current_timestamp, null, null);

insert into chamado (id, descricao, status, data_abertura, unidade_id, deleted_at, deleted_by)
values
  ('dd0e8400-e29b-41d4-a716-446655440008', 'Vazamento identificado na area de servico da unidade.', 'ABERTO', date '2026-06-01', '770e8400-e29b-41d4-a716-446655440002', null, null),
  ('dd0e8400-e29b-41d4-a716-446655440009', 'Lampada do corredor do bloco A queimada.', 'ANDAMENTO', date '2026-05-28', '770e8400-e29b-41d4-a716-446655440003', null, null),
  ('dd0e8400-e29b-41d4-a716-446655440010', 'Solicitacao de revisao do interfone.', 'CONCLUIDO', date '2026-05-20', '770e8400-e29b-41d4-a716-446655440002', null, null);
