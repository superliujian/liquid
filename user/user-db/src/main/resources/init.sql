insert into groups(group_name) values('管理员组');
insert into groups(group_name) values('销售部');
insert into groups(group_name) values('市场部');
insert into groups(group_name) values('财务部');
insert into groups(group_name) values('操作部');
insert group_authorities(group_id, authority) values(1, 'ROLE_ADMIN');
insert group_authorities(group_id, authority) values(2, 'ROLE_SALES');
insert group_authorities(group_id, authority) values(3, 'ROLE_MARKETING');
insert group_authorities(group_id, authority) values(4, 'ROLE_COMMERCE');
insert group_authorities(group_id, authority) values(5, 'ROLE_OPERATING');

insert into users(username, password, enabled) values('管理员', '$2a$10$t77KuUV.wZ56qj4LGk68iu6PzVupH4olLsErkni81v87HgXsPOIES', 1);
insert into user_profile(username, first_name, last_name, email) values('管理员', '', '', 'taoma9@163.com');
insert into group_members(username, group_id) values('管理员', 1);
