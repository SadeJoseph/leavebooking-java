-- Staff Management bounded context

-- Stores the persistence representation of a staff member.
CREATE TABLE staff_member (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    surname VARCHAR NOT NULL,
    email_address VARCHAR NOT NULL,
    department VARCHAR NOT NULL
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

-- used to store local domain events.
CREATE TABLE event_store (
    id INT AUTO_INCREMENT PRIMARY KEY,
    occurred_on DATE NOT NULL,
    event_body VARCHAR(65000) NOT NULL,
    event_type VARCHAR(255) NOT NULL, -- class names exceeded 50 char in testin g
    status VARCHAR(20) NOT NULL,
    retry_count INT DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS event_publication (
    id UUID NOT NULL PRIMARY KEY,
    listener_id VARCHAR(512) NOT NULL,
    event_type VARCHAR(512) NOT NULL,
    serialized_event VARCHAR(4000) NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) DEFAULT 'PUBLISHED' NOT NULL,
    completion_attempts INT DEFAULT 0 NOT NULL,
    last_resubmission_date TIMESTAMP WITH TIME ZONE
);