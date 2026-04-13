-- Script para corregir el nombre de la tabla sucursal a sucursales
-- Ejecuta esto en tu base de datos PostgreSQL

-- OPCIÓN 1: Si la tabla se llama 'sucursal' y quieres mantener el modelo Java
-- Renombrar la tabla para que coincida con @Table(name = "sucursales")
ALTER TABLE sucursal RENAME TO sucursales;

-- OPCIÓN 2: Verificar qué tablas existen actualmente
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
  AND table_name LIKE '%sucursal%';

-- Después de renombrar, verificar que la tabla existe
SELECT * FROM sucursales LIMIT 5;
