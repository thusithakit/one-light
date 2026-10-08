CREATE TABLE devices (
    id UUID PRIMARY KEY,

    owner_id UUID,

    device_identifier VARCHAR(100) NOT NULL UNIQUE,

    name VARCHAR(100) NOT NULL,

    status VARCHAR(20) NOT NULL,

    firmware_version VARCHAR(50),

    last_seen_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_devices_owner_id
    ON devices(owner_id);

CREATE INDEX idx_devices_identifier
    ON devices(device_identifier);