CREATE TABLE circles (
                         id UUID PRIMARY KEY,
                         owner_id UUID NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_circles_owner_id
    ON circles(owner_id);


CREATE TABLE circle_members (
                                id UUID PRIMARY KEY,
                                circle_id UUID NOT NULL,
                                user_id UUID NOT NULL,
                                role VARCHAR(20) NOT NULL,
                                can_send_messages BOOLEAN NOT NULL DEFAULT TRUE,
                                joined_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                CONSTRAINT uk_circle_member
                                    UNIQUE (circle_id, user_id)
);

CREATE INDEX idx_circle_members_circle_id
    ON circle_members(circle_id);

CREATE INDEX idx_circle_members_user_id
    ON circle_members(user_id);


CREATE TABLE circle_invitations (
                                    id UUID PRIMARY KEY,
                                    circle_id UUID NOT NULL,
                                    invited_user_id UUID NOT NULL,
                                    invited_by UUID NOT NULL,
                                    status VARCHAR(20) NOT NULL,
                                    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_circle_invitations_circle_id
    ON circle_invitations(circle_id);

CREATE INDEX idx_circle_invitations_invited_user
    ON circle_invitations(invited_user_id);