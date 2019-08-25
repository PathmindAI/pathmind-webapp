CREATE TABLE User(
                 id bigint auto_increment,
                 name varchar(100) not null,
                 email varchar(100) not null,
                 password varchar(100) not null,
                 primary key (id));


CREATE TABLE Project(
                 id bigint auto_increment,
                 name varchar(100) not null,
                 date_created date,
                 primary key (id));


CREATE TABLE Experiment(
                id bigint auto_increment,
                project_id bigint unsigned not null,
                date date,
                run_type tinyint,
                score int,
                primary key (id),
                FOREIGN KEY (project_id) REFERENCES Project(id));
