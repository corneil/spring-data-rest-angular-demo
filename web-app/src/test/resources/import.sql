-- Users
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (1, 'leia', '1959-11-11', 'leia.organa@alderaan.galaxy', 'Leia Organa')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (2, 'luke', '1959-11-11', 'luke.skywalker@tatooine.galaxy', 'Luke Skywalker')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (3, 'han', '1947-7-3', 'han.solo@corellia.galaxy', 'Han Solo')

-- Groups
insert into sd.user_groups(id, group_name, description, group_owner) values (1, 'jedi-knights', 'Jedi Knights', 2)
insert into sd.user_groups(id, group_name, description, group_owner) values (2, 'rebels', 'Rebel Alliance', 1)

-- Group Members
insert into sd.group_members(user_a, group_a, enabled) values (1, 2, 1) -- leia, rebels
insert into sd.group_members(user_a, group_a, enabled) values (2, 1, 1) -- luke, jedi
insert into sd.group_members(user_a, group_a, enabled) values (3, 2, 1) -- han, rebels
insert into sd.group_members(user_a, group_a, enabled) values (2, 2, 1) -- luke, rebels
