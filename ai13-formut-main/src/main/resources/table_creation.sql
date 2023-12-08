CREATE TYPE activity as ENUM ('enabled', 'disabled');

CREATE TABLE Usr(
                    id int primary key,
                    mail varchar unique not null,
                    society varchar not null,
                    password varchar not null,
                    phone_number varchar(10) not null,
                    creation date not null,
                    name varchar not null,
                    status activity not null,
                    is_admin varchar not null
);

CREATE TABLE Form(
                     id int primary key,
                     subject varchar unique not null,
                     writer int not null,
                     foreign key(writer) references usr,
                     version int not null,
                     status activity not null
);

CREATE TABLE Journey(
                        id int primary key,
                        score int,
                        duration int,
                        intern_id int not null,
                        foreign key (intern_id) references usr,
                        form_id int not null,
                        foreign key (form_id) references form
);

CREATE TABLE Question(
                         id int primary key,
                         wording varchar unique not null,
                         form_id int not null,
                         foreign key (form_id) references form,
                         status activity not null,
                         good_answer_id int
);

CREATE TABLE Answer(
                       id int primary key,
                       content varchar not null,
                       question_id int not null,
                       foreign key (question_id) references question,
                       status activity not null
);

ALTER TABLE Question ADD CONSTRAINT good_answer foreign key (good_answer_id) references answer;

CREATE TABLE Journey_Answer(
                               id int primary key,
                               journey_id int not null,
                               foreign key (journey_id) references journey,
                               answer_id int not null,
                               foreign key (answer_id) references answer
);