drop table if exists users;
drop sequence if exists seq_users;
create sequence seq_users start with 1 increment by 1;
create table users (id bigint not null check(id > 0), name varchar(255), primary key (id));
