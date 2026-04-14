-- Migration script: Add missing columns and tables
-- Date: 2026-04-14
-- Description: Adds state column to orders and creates packing_slips table

-- 1. Add state column to orders table
ALTER TABLE orders 
ADD COLUMN IF NOT EXISTS state VARCHAR(50) NOT NULL DEFAULT 'PENDING';

COMMENT ON COLUMN orders.state IS 'Estado de la orden: PENDING, COMPLETED';

-- 2. Create packing_slips table if not exists
CREATE TABLE IF NOT EXISTS packing_slips (
    id BIGSERIAL PRIMARY KEY,
    slip_number VARCHAR(255) NOT NULL UNIQUE,
    control_id BIGINT NOT NULL,
    order_id BIGINT,  -- NULL cuando no hay orden asociada
    delivery_id BIGINT NOT NULL,
    supervisor_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_packing_slip_control FOREIGN KEY (control_id) REFERENCES controls(id),
    CONSTRAINT fk_packing_slip_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

COMMENT ON TABLE packing_slips IS 'Packing slips (remitos) for deliveries';
COMMENT ON COLUMN packing_slips.status IS 'Status: ACTIVE, EXPIRED, CANCELLED';

-- Si la tabla ya existe, hacer que order_id sea nullable
ALTER TABLE packing_slips ALTER COLUMN order_id DROP NOT NULL;

-- 3. Add delivery_date column to controls table if not exists
ALTER TABLE controls
ADD COLUMN IF NOT EXISTS delivery_date DATE;

COMMENT ON COLUMN controls.delivery_date IS 'Delivery date for OUT controls';

-- 4. Add packing_slip_code column to sucursals table if not exists  
ALTER TABLE sucursals
ADD COLUMN IF NOT EXISTS packing_slip_code VARCHAR(50);

COMMENT ON COLUMN sucursals.packing_slip_code IS 'Code used to generate packing slip numbers';
