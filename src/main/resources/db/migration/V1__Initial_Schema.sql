CREATE TABLE user(
                 id bigint auto_increment,
                 name varchar(100) not null,
                 email varchar(100) not null,
                 password varchar(100) not null,
                 primary key (id));
