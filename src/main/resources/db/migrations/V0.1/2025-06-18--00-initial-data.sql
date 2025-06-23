--liquibase formatted sql

--changeset alexandr:2025-06-18--00-insert-default-roles
INSERT INTO roles (name)
VALUES
    ('USER'),
    ('ADMIN')
ON CONFLICT (name) DO NOTHING;
--rollback DELETE FROM roles WHERE name IN ('USER', 'ADMIN');