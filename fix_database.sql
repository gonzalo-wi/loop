-- Script para corregir restricciones NOT NULL en la tabla users
-- Ejecuta este script en tu base de datos PostgreSQL

-- ====================================================================
-- AJUSTAR COLUMNAS PARA QUE COINCIDAN CON EL MODELO JAVA
-- ====================================================================

-- Permitir NULL en last_name (apellido es opcional)
ALTER TABLE users ALTER COLUMN last_name DROP NOT NULL;

-- Permitir NULL en username (no requerido para usuarios móviles)
ALTER TABLE users ALTER COLUMN username DROP NOT NULL;

-- Permitir NULL en password (no requerido para usuarios móviles)
ALTER TABLE users ALTER COLUMN password DROP NOT NULL;

-- Permitir NULL en pin (no requerido para usuarios web)
ALTER TABLE users ALTER COLUMN pin DROP NOT NULL;

-- Permitir NULL en delivery_id (solo para repartidores)
ALTER TABLE users ALTER COLUMN delivery_id DROP NOT NULL;

-- ====================================================================
-- VERIFICACIÓN
-- ====================================================================

-- Verificar que los cambios se aplicaron correctamente
SELECT 
    column_name, 
    is_nullable,
    data_type
FROM information_schema.columns 
WHERE table_name = 'users' 
ORDER BY ordinal_position;

-- ====================================================================
-- RESULTADO ESPERADO:
-- ====================================================================
-- name          -> NO  (obligatorio)
-- last_name     -> YES (opcional)
-- username      -> YES (opcional - solo para web)
-- password      -> YES (opcional - solo para web)
-- pin           -> YES (opcional - solo para móviles)
-- sucursal_id   -> NO  (obligatorio)
-- role          -> NO  (obligatorio)
-- delivery_id   -> YES (opcional - solo para repartidores)
-- active        -> NO  (obligatorio, default TRUE)
-- created_at    -> YES (se auto-completa)
-- updated_at    -> YES (se auto-completa)

