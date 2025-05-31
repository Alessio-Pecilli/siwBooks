INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('George', 'Orwell', '1903-06-25', '1950-01-21', 'Inglese', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('J.K.', 'Rowling', '1965-07-31', NULL, 'Britannica', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('Robert', 'Kirkman', '1978-11-30', NULL, 'Americana', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('George R.R.', 'Martin', '1948-09-20', NULL, 'Americana', NULL);

INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('1984', 1949);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('Harry Potter', 1997);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('The Walking Dead', 2003);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('Game of Thrones', 1996);

INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = '1984'), (SELECT id FROM autore WHERE nome = 'George' AND cognome = 'Orwell'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'Harry Potter'), (SELECT id FROM autore WHERE nome = 'J.K.' AND cognome = 'Rowling'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'The Walking Dead'), (SELECT id FROM autore WHERE nome = 'Robert' AND cognome = 'Kirkman'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'Game of Thrones'), (SELECT id FROM autore WHERE nome = 'George R.R.' AND cognome = 'Martin'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = '1984'), (SELECT id FROM autore WHERE nome = 'George R.R.' AND cognome = 'Martin'));

INSERT INTO utente  (nome, cognome, email) VALUES ('Mario', 'Verdi', 'mario.verdi@example.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Luca', 'Blu', 'luca.blu@example.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Silvia', 'Nera', 'silvia.nera@example.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Alessio', 'Pecilli', 'alessiopecilli2003@gmail.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Simone', 'Morolli', 'simone.morolli@gmail.com');


INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Capolavoro', 5, 'Incredibile e profondo', (SELECT id FROM utente WHERE nome = 'Mario' AND cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = '1984'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Magico', 5, 'Harry è unico', (SELECT id FROM utente WHERE nome = 'Mario' AND cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Harry Potter'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Brutale', 4, 'Una storia cruda e avvincente', (SELECT id FROM utente WHERE nome = 'Luca' AND cognome = 'Blu'), (SELECT id FROM libro WHERE titolo = 'The Walking Dead'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Epico', 5, 'Intrighi e battaglie leggendarie', (SELECT id FROM utente WHERE nome = 'Silvia' AND cognome = 'Nera'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Meh', 3, 'Meglio la serie', (SELECT id FROM utente WHERE nome = 'Mario' AND cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));

INSERT INTO credentials(id, utente_id, password, role, username) VALUES (nextval('credentials_seq'),(SELECT u.id FROM utente as u WHERE u.email='alessiopecilli2003@gmail.com'),'$2a$10$Nz4769bR1Iutd8perNFRPuB9xf5CbEMqRd02hg8twA.6jqE1Gq1Iy', 'ADMIN','ale');

INSERT INTO credentials(id, utente_id, password, role, username) VALUES (nextval('credentials_seq'),(SELECT u.id FROM utente as u WHERE u.email='simone.morolli@gmail.com'),'$2a$10$Nz4769bR1Iutd8perNFRPuB9xf5CbEMqRd02hg8twA.6jqE1Gq1Iy', 'DEFAULT','simo');
