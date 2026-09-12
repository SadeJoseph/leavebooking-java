-- Staff Management bounded context

-- Stores the persistence representation of a staff member.
CREATE TABLE staff_member (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    surname VARCHAR NOT NULL,
    email_address VARCHAR NOT NULL
);

-- Stores leave requests submitted by staff members.
CREATE TABLE leave_request (
    id VARCHAR(36) PRIMARY KEY,
    staff_id VARCHAR(36) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR NOT NULL,
    leave_type INT NOT NULL,
    leave_status INT NOT NULL,
    description_of_status VARCHAR NOT NULL
);


-- Stores each staff member's annual leave allowance.
CREATE TABLE leave_allowance (
    id VARCHAR(36) PRIMARY KEY,
    staff_id VARCHAR(36) NOT NULL,
    first_name VARCHAR NOT NULL,
    surname VARCHAR NOT NULL,
    manager_id VARCHAR(36) NOT NULL,
    yearly_entitlement INT NOT NULL,
    remaining_balance INT NOT NULL
);