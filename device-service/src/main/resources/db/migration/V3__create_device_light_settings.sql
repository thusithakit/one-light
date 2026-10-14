CREATE TABLE device_light_settings (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

       device_id UUID NOT NULL UNIQUE,

       "on" BOOLEAN NOT NULL DEFAULT TRUE,

       brightness INTEGER NOT NULL DEFAULT 80,

       color VARCHAR(7) NOT NULL DEFAULT '#FFB464',

       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

       CONSTRAINT fk_device_light_settings_device
           FOREIGN KEY (device_id)
               REFERENCES devices (id)
               ON DELETE CASCADE,

       CONSTRAINT chk_device_light_settings_brightness
           CHECK (brightness BETWEEN 0 AND 100),

       CONSTRAINT chk_device_light_settings_color
           CHECK (color ~ '^#[0-9A-Fa-f]{6}$')
    );