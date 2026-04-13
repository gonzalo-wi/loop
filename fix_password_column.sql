-- Script para ajustar el tamaño de la columna password en la tabla users
-- BCrypt genera hashes de 60 caracteres, asegurémonos de que la columna sea suficientemente grande

-- Verificar el tamaño actual de la columna password
SELECT column_name, data_type, character_maximum_length
FROM information_schema.columns
WHERE table_name = 'users' AND column_name = 'password';

-- Si la columna es más pequeña que 60 caracteres, ejecuta esto:
ALTER TABLE users ALTER COLUMN password TYPE VARCHAR(60);

-- Verificar que el cambio se aplicó
SELECT column_name, data_type, character_maximum_length
FROM information_schema.columns
WHERE table_name = 'users' AND column_name = 'password';
