CREATE TABLE pairing_sessions (
    id UUID PRIMARY KEY,

    device_id UUID NOT NULL,

    code VARCHAR(6) NOT NULL UNIQUE,

    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    used BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_pairing_sessions_code
    ON pairing_sessions(code);

CREATE INDEX idx_pairing_sessions_device_id
    ON pairing_sessions(device_id);