-- Sample Staff Management data used while developing/testing the API.

INSERT INTO staff_member(
    id,
    first_name,
    surname,
    email_address
)
VALUES(
    '0001',
    'Sade',
    'Joseph',
    'sade.joseph@example.com'
);

INSERT INTO staff_member(
    id,
    first_name,
    surname,
    email_address
)
VALUES(
    '0002',
    'Alex',
    'Smith',
    'alex.smith@example.com'
);

INSERT INTO staff_member(
    id,
    first_name,
    surname,
    email_address
)
VALUES(
    '0003',
    'Jordan',
    'Brown',
    'jordan.brown@example.com'
);

-- PENDING annual leave request for staff member 0001.
-- 0 = PENDING, 1 = APPROVED, 2 = REJECTED , 3 = CANCELLED
-- Leave type: 0 = ANNUAL
INSERT INTO leave_request(
    id,
    staff_id,
    start_date,
    end_date,
    reason,
    leave_type,
    leave_status,
    description_of_status
)
VALUES(
    '1001',
    '0001',
    '2026-10-12',
    '2026-10-16',
    'Annual leave',
    0,
    0,
    'Awaiting approval'
);


-- APPROVED annual leave request for staff member 0002.
INSERT INTO leave_request(
    id,
    staff_id,
    start_date,
    end_date,
    reason,
    leave_type,
    leave_status,
    description_of_status
)
VALUES(
    '1002',
    '0002',
    '2026-09-21',
    '2026-09-23',
    'Annual leave',
    0,
    1,
    'Leave request approved'
);


-- Leave allowances

INSERT INTO leave_allowance(
    id,
    staff_id,
    first_name,
    surname,
    manager_id,
    yearly_entitlement,
    remaining_balance
)
VALUES(
    '2001',
    '0001',
    'Sade',
    'Joseph',
    '0003',
    25,
    20
);

INSERT INTO leave_allowance(
    id,
    staff_id,
    first_name,
    surname,
    manager_id,
    yearly_entitlement,
    remaining_balance
)
VALUES(
    '2002',
    '0002',
    'Alex',
    'Smith',
    '0003',
    25,
    22
);