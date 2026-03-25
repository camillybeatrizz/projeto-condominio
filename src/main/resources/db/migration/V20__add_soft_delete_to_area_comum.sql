ALTER TABLE area_comum
    ADD COLUMN deleted_at TIMESTAMP NULL;

ALTER TABLE area_comum
    ADD COLUMN deleted_by VARCHAR(255) NULL;
