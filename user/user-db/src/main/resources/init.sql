insert into groups(group_name) values('管理员组');
insert into groups(group_name) values('销售部');
insert into groups(group_name) values('市场部');
insert into groups(group_name) values('财务部');

insert into users(username, password, enabled) values('管理员', '111111', 1);
insert into user_profile(username, email) values('管理员', 'taoma9@163.com');
insert into authorities(authority, username) values('ROLE_ADMIN', '管理员');