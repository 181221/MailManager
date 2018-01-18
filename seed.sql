DROP TABLE if exists bruker;
DROP TABLE if exists email;
DROP TABLE if exists lytter;

CREATE TABLE bruker (
	id integer primary key,
	name text UNIQUE not null
);

CREATE TABLE email (
	id integer primary key, 
	username text not null, 
	passord text not null,
	salt text not null,
	mailtype integer not null,
	bruker_id integer,
	FOREIGN KEY(bruker_id) REFERENCES bruker(id)
);

CREATE TABLE lytter(
	id integer primary key,
	sokeliste text not null,
	beskrivelse text not null,
	mappe_fra text not null,
	mappe_til text not null,
	email_id integer,
	FOREIGN KEY(email_id) REFERENCES email(id)
); 


INSERT INTO bruker (name) VALUES ("peder");
INSERT INTO bruker (name) VALUES ("gunnar");
INSERT INTO email (username, passord, salt, mailtype, bruker_id) VALUES ("peder@gmail.com", "asasas","1234", -1, 1);
INSERT INTO email (username, passord, salt, mailtype, bruker_id) VALUES ("gunnar@gmail.com", "asasas", "1234", -1, 2);
INSERT INTO lytter (sokeliste, beskrivelse, mappe_fra, mappe_til, email_id) VALUES ("Kvitteringer ordre bekreftelse", "lytter for beskrivelse", "INBOX", "tester", 1);
INSERT INTO lytter (sokeliste, beskrivelse, mappe_fra, mappe_til, email_id) VALUES ("Kvitteringer ordre bekreftelse", "lytter for gunnar", "INBOX", "tester", 2);
