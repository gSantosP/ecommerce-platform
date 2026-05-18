-- Users (Auth domain)
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- Products (Catalog domain)
CREATE TABLE products (
    id UUID PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_active ON products(active);

-- Orders (Order domain)
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);

CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12, 2) NOT NULL
);

CREATE INDEX idx_order_items_order ON order_items(order_id);

-- Seed: admin user (password: admin123)
-- BCrypt hash for "admin123"
INSERT INTO users (id, email, password_hash, name, role) VALUES
    ('00000000-0000-0000-0000-000000000001', 'admin@ecomm.com', '$2b$10$ZihUXbKV637S/SPLUCCGCep2TPPyEMjAxZRIrsg08NorNzwj1H/Ia', 'Admin', 'ADMIN');

-- Seed: sample products
INSERT INTO products (id, sku, name, description, price, stock_quantity) VALUES
    ('11111111-1111-1111-1111-111111111111', 'NB-001', 'Notebook Dell XPS 13', 'Intel i7, 16GB RAM, 512GB SSD', 8500.00, 10),
    ('22222222-2222-2222-2222-222222222222', 'KB-001', 'Teclado Mecânico Keychron K2', 'Wireless, switches brown', 850.00, 25),
    ('33333333-3333-3333-3333-333333333333', 'MS-001', 'Mouse Logitech MX Master 3', 'Bluetooth, 4000 DPI', 650.00, 30),
    ('44444444-4444-4444-4444-444444444444', 'MN-001', 'Monitor LG UltraWide 34', '34 polegadas, 4K, IPS', 3200.00, 8),
    ('55555555-5555-5555-5555-555555555555', 'HD-001', 'Headphone Sony WH-1000XM5', 'Cancelamento de ruído', 2500.00, 15);
