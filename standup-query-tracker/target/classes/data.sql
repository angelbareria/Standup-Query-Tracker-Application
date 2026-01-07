
INSERT IGNORE INTO users (id, name, email, password, role, created_at) VALUES
(1, 'John Doe', 'john@company.com', 'password123', 'DEVELOPER', NOW()),
(2, 'Jane Smith', 'jane@company.com', 'password123', 'DEVELOPER', NOW()),
(3, 'Bob Manager', 'bob@company.com', 'password123', 'MANAGER', NOW()),
(4, 'Alice Developer', 'alice@company.com', 'password123', 'DEVELOPER', NOW());

-- Insert sample queries (only if they don't exist)
INSERT IGNORE INTO queries (id, title, description, type, priority, status, raised_by_id, assigned_to_id, created_date, last_updated, sla_deadline) VALUES
(1, 'API Authentication Issue', 'Unable to authenticate with OAuth2. Getting 401 errors consistently.', 'BLOCKER', 'HIGH', 'OPEN', 1, 2, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR)),
(2, 'Database Design Question', 'Should we use NoSQL or SQL for the new feature? Need architectural guidance.', 'QUERY', 'MEDIUM', 'IN_PROGRESS', 2, 3, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 72 HOUR)),
(3, 'Waiting on Design Files', 'Need mockups from design team to proceed with UI implementation.', 'DEPENDENCY', 'HIGH', 'OPEN', 1, NULL, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 48 HOUR)),
(4, 'CI/CD Pipeline Status', 'What is the status of the Jenkins pipeline setup?', 'FOLLOWUP', 'LOW', 'RESOLVED', 4, 3, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 72 HOUR)),
(5, 'Performance Bottleneck', 'API response time is 5+ seconds. Need to investigate and optimize.', 'BLOCKER', 'HIGH', 'IN_PROGRESS', 2, 4, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR));

-- Update resolved query
UPDATE queries
SET resolved_date = DATE_SUB(NOW(), INTERVAL 1 DAY),
    resolution_time_hours = 24
WHERE id = 4;