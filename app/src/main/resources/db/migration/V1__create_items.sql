CREATE TABLE IF NOT EXISTS items (
    id         INT8         NOT NULL DEFAULT unique_rowid() PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    quantity   INT4         NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_items_quantity ON items (quantity);
