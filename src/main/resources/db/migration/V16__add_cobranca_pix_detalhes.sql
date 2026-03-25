ALTER TABLE cobranca
    ADD COLUMN pix_qr_code_base64 CLOB;

ALTER TABLE cobranca
    ADD COLUMN pix_copia_cola VARCHAR(4000);

ALTER TABLE cobranca
    ADD COLUMN pix_expiracao TIMESTAMP;
