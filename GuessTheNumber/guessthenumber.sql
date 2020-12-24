DROP DATABASE IF EXISTS guessthenumber;
CREATE DATABASE guessthenumber;

USE GuessTheNumber;

CREATE TABLE numbers(
id INT PRIMARY KEY AUTO_INCREMENT,
number INT NOT NULL default 0,
finished BOOLEAN DEFAULT false
);

CREATE TABLE rounds(
roundid int primary key auto_increment,
id int,
exact int not null default 0,
partial int not null default 0,
foreign key (id) references numbers(id)
);
select * from numbers;