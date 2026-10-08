CREATE TABLE circle_devices (
                                id UUID PRIMARY KEY,
                                circle_id UUID NOT NULL,
                                device_id UUID NOT NULL,
                                added_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                CONSTRAINT uk_circle_device
                                    UNIQUE (circle_id, device_id)
);

CREATE INDEX idx_circle_devices_circle_id
    ON circle_devices(circle_id);

CREATE INDEX idx_circle_devices_device_id
    ON circle_devices(device_id);