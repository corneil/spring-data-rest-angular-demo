-- Users
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (1, 'ben', '1917-3-4', 'ben.kenobi@temple.jedi', 'Obi-Wan Kenobi')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (2, 'quigon', '1895-3-4', 'qui-gon.jinn@temple.jedi', 'Qui-Gon Jinn')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (3, 'padme', '1931-9-3', 'padme.amidala@naboo.rim', 'Padme Amidala')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (4, 'anakin', '1936-6-6', 'anakin.skywalker@tatooine.rim', 'Anakin Skywalker')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (5, 'yoda', '1190-2-1', 'yoda@council.jedi', 'Yoda')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (6, 'palpatine', '1886-4-9', 'senator.palpatine@naboo.galaxy', 'Senator Palpatine')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (7, 'jango', '1937-3-18', 'jango.fett@mandalore.galaxy', 'Jango Fett')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (8, 'emperor', '1886-4-9', 'emperor@empire.galaxy', 'Emperor Palpatine')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (9, 'vader', '1936-6-6', 'darth.vader@empire.galaxy', 'Darth Vader')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (10, 'leia', '1959-11-11', 'leia.organa@alderaan.galaxy', 'Leia Organa')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (11, 'luke', '1959-11-11', 'luke.skywalker@tatooine.galaxy', 'Luke Skywalker')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (12, 'han', '1947-7-3', 'han.solo@corellia.galaxy', 'Han Solo')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (13, 'chewie', '1903-8-3', 'chewbacca@kashyyk.galaxy', 'Chewbacca')
insert into sd.users(id, user_id, date_of_birth, email_address, full_name) values (14, 'boba', '1957-8-1', 'boba.fett@mandalore.galaxy', 'Boba Fett')




-- Groups
insert into sd.groups(id, group_name, description, group_owner) values (1, 'naboo', 'Naboo Royal House', 3)
insert into sd.groups(id, group_name, description, group_owner) values (2, 'jedi-knights', 'Jedi Knights', 2)
insert into sd.groups(id, group_name, description, group_owner) values (3, 'jedi-masters', 'Jedi Masters', 1)
insert into sd.groups(id, group_name, description, group_owner) values (4, 'republic', 'Old Republic', 6)
insert into sd.groups(id, group_name, description, group_owner) values (5, 'sith', 'Sith', 8)
insert into sd.groups(id, group_name, description, group_owner) values (6, 'empire', 'Galactic Empire', 8)
insert into sd.groups(id, group_name, description, group_owner) values (7, 'rebels', 'Rebel Alliance', 10)
insert into sd.groups(id, group_name, description, group_owner) values (8, 'mandalorians', 'Mandalorians', 7)
insert into sd.groups(id, group_name, description, group_owner) values (9, 'resistance', 'Resistance', 10)

-- Group Members
insert into sd.group_members(member, member_ofgroup, enabled) values (3, 1, 1) -- padme, naboo
insert into sd.group_members(member, member_ofgroup, enabled) values (6, 1, 1) -- palpatine, naboo
insert into sd.group_members(member, member_ofgroup, enabled) values (6, 4, 1) -- palpatine, republic

insert into sd.group_members(member, member_ofgroup, enabled) values (2, 2, 1) -- quigon, jedi-knights
insert into sd.group_members(member, member_ofgroup, enabled) values (1, 2, 1) -- ben, jedi-knights
insert into sd.group_members(member, member_ofgroup, enabled) values (2, 3, 1) -- quigon, jedi-masters
insert into sd.group_members(member, member_ofgroup, enabled) values (5, 2, 1) -- yoda, jedi-knights
insert into sd.group_members(member, member_ofgroup, enabled) values (5, 3, 1) -- yoda, jedi-masters

