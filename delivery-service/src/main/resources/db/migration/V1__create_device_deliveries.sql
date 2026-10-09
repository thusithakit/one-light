CREATE TABLE device_deliveries (
                                   id UUID PRIMARY KEY,
                                   message_id VARCHAR(100) NOT NULL,
                                   device_id VARCHAR(100) NOT NULL,
                                   status VARCHAR(20) NOT NULL,
                                   failure_reason VARCHAR(1000),
                                   created_at TIMESTAMPTZ NOT NULL,
                                   published_at TIMESTAMPTZ,

                                   CONSTRAINT uk_device_deliveries_message_id UNIQUE (message_id),

                                   CONSTRAINT chk_device_deliveries_status
                                       CHECK (status IN ('PENDING', 'PUBLISHED', 'FAILED'))
);

CREATE INDEX idx_device_deliveries_device_id
    ON device_deliveries (device_id);

CREATE INDEX idx_device_deliveries_status
    ON device_deliveries (status);