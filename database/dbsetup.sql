CREATE DATABASE keepmetest;
USE keepmetest;
CREATE TABLE snapshot (
id bigint not null primary key,
user_id bigint default 0,
expire_time datetime not null,
url varchar(1024) not null,
snapshot_binary mediumblob not null,
status int not null,
key user_id (user_id),
key expire_time (expire_time)
);
commit;

