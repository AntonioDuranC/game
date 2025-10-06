CREATE TABLE public.games (
      id serial PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      category VARCHAR(255) NOT NULL,
      description TEXT,
      price NUMERIC(10,2) NOT NULL,
      active BOOLEAN NOT NULL DEFAULT TRUE,
      release_date DATE
);

CREATE TABLE public.game_stock (
    id SERIAL PRIMARY KEY,
    game_id INT NOT NULL REFERENCES public.games(id) ON DELETE CASCADE,
    total_stock INT NOT NULL DEFAULT 0,
    reserved_stock INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    order_date TIMESTAMP DEFAULT NOW(),
    total NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE public.order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
    quantity INT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    CONSTRAINT uq_order_game UNIQUE (order_id, game_id)
);

CREATE TABLE public.payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    -- Monto original enviado en la moneda especificada
    amount NUMERIC(18,2) NOT NULL,
    -- Moneda del pago: 'PEN' (soles) o 'USD' (dólares)
    currency VARCHAR(10) NOT NULL,
    -- Monto convertido a soles (si la moneda es USD)
    converted_amount NUMERIC(18,2),
    -- Tipo de cambio usado (campo 'venta' del API)
    exchange_rate NUMERIC(10,4),
    -- Fecha del tipo de cambio usado
    exchange_date TIMESTAMP,
    -- Fecha del pago
    payment_date TIMESTAMP DEFAULT NOW(),
    -- Metodo de pago (tarjeta, efectivo, etc.)
    method VARCHAR(50) NOT NULL,
    -- Estado del pago (PENDING, PAID, CANCELLED, REFUNDED)
    status VARCHAR(20) NOT NULL
);
