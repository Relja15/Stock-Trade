INSERT INTO user_profile (user_id)
SELECT id
FROM users
WHERE id NOT IN (SELECT user_id FROM user_profile);