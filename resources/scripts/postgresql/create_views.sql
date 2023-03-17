CREATE OR REPLACE VIEW v_users AS
SELECT u.*,
       REPLACE(
               CONCAT(
                       CONCAT(u.first_name, ' '),
                       CONCAT(u.middle_name, ' '),
                       u.last_name
                   ),
               '  ',
               ' '
           )   AS full_name,
       CASE
           WHEN u.person_id IS NULL THEN null
           ELSE json_build_object(
                   'id', p.id,
                   'first_name', p.first_name,
                   'middle_name', p.middle_name,
                   'last_name', p.last_name,
                   'full_name', REPLACE(
                           CONCAT(
                                   CONCAT(p.first_name, ' '),
                                   CONCAT(p.middle_name, ' '),
                                   p.last_name
                               ),
                           '  ',
                           ' '
                       )
               )
           END AS person
FROM users u
         LEFT JOIN persons p
                   ON u.person_id = p.id;
--;;
CREATE OR REPLACE VIEW v_persons AS
SELECT p.*,
       REPLACE(
               CONCAT(
                       CONCAT(p.first_name, ' '),
                       CONCAT(p.middle_name, ' '),
                       p.last_name
                   ),
               '  ',
               ' '
           )   AS full_name,
       CASE
           WHEN u.person_id IS NULL THEN null
           ELSE json_build_object(
                   'id', u.id,
                   'email', u.email,
                   'person_id', u.person_id,
                   'first_name', u.first_name,
                   'middle_name', u.middle_name,
                   'last_name', u.last_name,
                   'full_name', REPLACE(
                           CONCAT(
                                   CONCAT(u.first_name, ' '),
                                   CONCAT(u.middle_name, ' '),
                                   u.last_name
                               ),
                           '  ',
                           ' '
                       )
               )
           END AS "user"
FROM persons p
         LEFT JOIN users u
                   ON p.id = u.person_id;
--;;
