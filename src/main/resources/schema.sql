-- Staff Management bounded context

-- Stores the persistence representation of a staff member.
CREATE TABLE staff_member (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    surname VARCHAR NOT NULL,
    email_address VARCHAR NOT NULL
);