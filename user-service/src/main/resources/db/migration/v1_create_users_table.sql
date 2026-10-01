CREATE TABLE users (
   id UUID PRIMARY KEY,
   auth_user_id UUID NOT NULL UNIQUE,

   name VARCHAR(100) NOT NULL,

   avatar_url VARCHAR(500),

   timezone VARCHAR(100) NOT NULL,

   created_at TIMESTAMP WITH TIME ZONE NOT NULL,
   updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_users_auth_user_id
    ON users(auth_user_id);