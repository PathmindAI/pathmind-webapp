
CREATE SCHEMA PATHMIND;

CREATE TABLE PATHMIND.User(
                 id bigint auto_increment,
                 name varchar(100) not null,
                 email varchar(100) not null,
                 password varchar(100) not null,
                 primary key (id));


CREATE TABLE PATHMIND.Project(
                 id bigint auto_increment,
                 user_id bigint unsigned not null,
                 name varchar(100) not null,
                 date_created date,
                 primary key (id),
                 FOREIGN KEY (user_id) REFERENCES PATHMIND.User(id));


CREATE TABLE PATHMIND.Experiment(
                id bigint auto_increment,
                project_id bigint unsigned not null,
                name varchar(255),
                date date,
                run_type tinyint,
                score int,
                primary key (id),
                FOREIGN KEY (project_id) REFERENCES PATHMIND.Project(id));

CREATE TABLE PATHMIND.Run(
                id bigint auto_increment,
                experiment_id bigint unsigned not null,
                name varchar(255),
                date date,
                FOREIGN KEY (experiment_id) REFERENCES PATHMIND.Experiment(id));
