insert into groups(id, group_name) values(1, '管理员组');
insert into groups(id, group_name) values(2, '销售部');
insert into groups(id, group_name) values(3, '市场部');
insert into groups(id, group_name) values(4, '财务部');
insert into groups(id, group_name) values(5, '操作部');
insert into groups(id, group_name) values(6, '箱管部');
insert into groups(id, group_name) values(7, '现场部');
insert group_authorities(group_id, authority) values(1, 'ROLE_ADMIN');
insert group_authorities(group_id, authority) values(2, 'ROLE_SALES');
insert group_authorities(group_id, authority) values(3, 'ROLE_MARKETING');
insert group_authorities(group_id, authority) values(4, 'ROLE_COMMERCE');
insert group_authorities(group_id, authority) values(5, 'ROLE_OPERATING');
insert group_authorities(group_id, authority) values(6, 'ROLE_CONTAINER');
insert group_authorities(group_id, authority) values(7, 'ROLE_FIELD');

insert into users(username, password, enabled) values('管理员', '$2a$10$t77KuUV.wZ56qj4LGk68iu6PzVupH4olLsErkni81v87HgXsPOIES', 1);
insert into user_profile(username, first_name, last_name, email) values('管理员', '', '', 'taoma9@163.com');
insert into group_members(username, group_id) values('管理员', 1);
