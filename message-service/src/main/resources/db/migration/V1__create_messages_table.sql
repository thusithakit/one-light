CREATE TABLE messages (
                          id UUID PRIMARY KEY,

                          device_id UUID NOT NULL,

                          sender_user_id UUID NOT NULL,

                          content VARCHAR(500) NOT NULL,

                          status VARCHAR(20) NOT NULL,

                          created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                          delivered_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_messages_device_id
    ON messages(device_id);

CREATE INDEX idx_messages_sender_user_id
    ON messages(sender_user_id);

CREATE INDEX idx_messages_created_at
    ON messages(created_at);