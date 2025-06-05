INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('George', 'Orwell', '1903-06-25', '1950-01-21', 'Inglese', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('J.K.', 'Rowling', '1965-07-31', NULL, 'Britannica', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('Robert', 'Kirkman', '1978-11-30', NULL, 'Americana', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('George R.R.', 'Martin', '1948-09-20', NULL, 'Americana', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('Antoine de Saint-Exupéry', 'Tonio', '1900-07-29', '1944-07-31', 'Francese', NULL);
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazionalita, fotografia_id) VALUES ('Elisabetta', 'Dami', '1958-05-05', NULL, 'Italiana', NULL);

INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('1984', 1949);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('Harry Potter', 1997);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('The Walking Dead', 2003);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('Game of Thrones', 1996);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('Il piccolo principe', 1943);
INSERT INTO libro (titolo, anno_pubblicazione) VALUES ('Geronimo Stilton', 1997);

INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = '1984'), (SELECT id FROM autore WHERE nome = 'George' AND cognome = 'Orwell'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'Harry Potter'), (SELECT id FROM autore WHERE nome = 'J.K.' AND cognome = 'Rowling'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'The Walking Dead'), (SELECT id FROM autore WHERE nome = 'Robert' AND cognome = 'Kirkman'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'Game of Thrones'), (SELECT id FROM autore WHERE nome = 'George R.R.' AND cognome = 'Martin'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = '1984'), (SELECT id FROM autore WHERE nome = 'George R.R.' AND cognome = 'Martin'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'Il piccolo principe'), (SELECT id FROM autore WHERE nome = 'Antoine de Saint-Exupéry' AND cognome = 'Tonio'));
INSERT INTO libro_autori (libro_id, autori_id) VALUES ((SELECT id FROM libro WHERE titolo = 'Geronimo Stilton'), (SELECT id FROM autore WHERE nome = 'Elisabetta' AND cognome = 'Dami'));


INSERT INTO utente  (nome, cognome, email) VALUES ('Mario', 'Verdi', 'mario.verdi@example.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Luca', 'Blu', 'luca.blu@example.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Silvia', 'Nera', 'silvia.nera@example.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Alessio', 'Pecilli', 'a@gmail.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Simone', 'Morolli', 's@gmail.com');
INSERT INTO utente  (nome, cognome, email) VALUES ('Giulia', 'Biba', 'g@gmail.com');


INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Capolavoro', 5, 'Incredibile e profondo', (SELECT id FROM utente WHERE nome = 'Mario' AND cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = '1984'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Magico', 5, 'Harry è unico', (SELECT id FROM utente WHERE nome = 'Mario' AND cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Harry Potter'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Brutale', 4, 'Una storia cruda e avvincente', (SELECT id FROM utente WHERE nome = 'Luca' AND cognome = 'Blu'), (SELECT id FROM libro WHERE titolo = 'The Walking Dead'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Epico', 5, 'Intrighi e battaglie leggendarie', (SELECT id FROM utente WHERE nome = 'Silvia' AND cognome = 'Nera'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Meh', 3, 'Meglio la serie', (SELECT id FROM utente WHERE nome = 'Mario' AND cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Distopico e potente', 4, 'Un mondo che fa riflettere', (SELECT id FROM utente WHERE nome = 'Simone' AND cognome = 'Morolli'), (SELECT id FROM libro WHERE titolo = '1984'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Avvincente', 5, 'Letto tutto d’un fiato', (SELECT id FROM utente WHERE nome = 'Simone' AND cognome = 'Morolli'), (SELECT id FROM libro WHERE titolo = 'The Walking Dead'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Fiabesco', 4, 'Mi ha fatto sognare', (SELECT id FROM utente WHERE nome = 'Simone' AND cognome = 'Morolli'), (SELECT id FROM libro WHERE titolo = 'Harry Potter'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Confuso ma epico', 3, 'Troppi nomi, ma bella trama', (SELECT id FROM utente WHERE nome = 'Simone' AND cognome = 'Morolli'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Non il mio genere', 2, 'Troppa violenza per i miei gusti', (SELECT id FROM utente WHERE nome = 'Simone' AND cognome = 'Morolli'), (SELECT id FROM libro WHERE titolo = 'The Walking Dead'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Angosciante', 4, 'Una visione cupa del futuro', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = '1984'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Intrigante', 5, 'Mi ha colpita nel profondo', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = '1984'));

INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Fantastico', 5, 'Magia pura dalla prima pagina', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = 'Harry Potter'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Bellissimo', 4, 'Harry è troppo forte', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = 'Harry Potter'));

INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Crudo', 3, 'Mi ha messo ansia ma mi è piaciuto', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = 'The Walking Dead'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Poco realistico', 2, 'Troppo splatter per me', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = 'The Walking Dead'));

INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Avvincente', 5, 'Non riuscivo a smettere di leggerlo', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));
INSERT INTO recensione (titolo, voto, testo, autore_id, libro_id) VALUES ('Troppi personaggi', 3, 'All’inizio confuso, poi bello', (SELECT id FROM utente WHERE nome = 'Giulia' AND cognome = 'Biba'), (SELECT id FROM libro WHERE titolo = 'Game of Thrones'));

INSERT INTO credentials(id, utente_id, password, role, username) VALUES (nextval('credentials_seq'),(SELECT u.id FROM utente as u WHERE u.email='a@gmail.com'),'$2a$10$Nz4769bR1Iutd8perNFRPuB9xf5CbEMqRd02hg8twA.6jqE1Gq1Iy', 'ADMIN','ale');
INSERT INTO credentials(id, utente_id, password, role, username) VALUES (nextval('credentials_seq'),(SELECT u.id FROM utente as u WHERE u.email='g@gmail.com'),'$2a$10$Nz4769bR1Iutd8perNFRPuB9xf5CbEMqRd02hg8twA.6jqE1Gq1Iy', 'ADMIN','biba');

INSERT INTO credentials(id, utente_id, password, role, username) VALUES (nextval('credentials_seq'),(SELECT u.id FROM utente as u WHERE u.email='s@gmail.com'),'$2a$10$Nz4769bR1Iutd8perNFRPuB9xf5CbEMqRd02hg8twA.6jqE1Gq1Iy', 'DEFAULT','simo');

INSERT INTO immagine (nome_file, path, libro_id) VALUES ('1984_1.jpg', '/images/books/1984_1.jpg', 1);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('game_of_thrones_1.jpg', '/images/books/game_of_thrones_1.jpg', 4);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('game_of_thrones_2.jpg', '/images/books/game_of_thrones_2.jpg', 4);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('game_of_thrones_3.jpg', '/images/books/game_of_thrones_3.jpg', 4);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('game_of_thrones_4.jpg', '/images/books/game_of_thrones_4.jpg', 4);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('game_of_thrones_5.jpg', '/images/books/game_of_thrones_5.jpg', 4);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('harry_potter_1.jpg', '/images/books/harry_potter_1.jpg', 2);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('the_walking_dead_1.jpg', '/images/books/the_walking_dead_1.jpg', 3);

INSERT INTO immagine (nome_file, path, autore_id) VALUES ('george_orwell.jpg', '/images/authors/george_orwell.jpg', 1);
INSERT INTO immagine (nome_file, path, autore_id) VALUES ('george_rr_martin.jpg', '/images/authors/george_rr_martin.jpg', 2);
INSERT INTO immagine (nome_file, path, autore_id) VALUES ('jk_rowling.jpg', '/images/authors/jk_rowling.jpg', 3);
INSERT INTO immagine (nome_file, path, autore_id) VALUES ('robert_kirkman.jpg', '/images/authors/robert_kirkman.jpg', 4);


INSERT INTO immagine (nome_file, path, libro_id) VALUES ('harry_potter_2.jpg', '/images/books/harry_potter_2.jpg', 2);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('harry_potter_3.jpg', '/images/books/harry_potter_3.jpg', 2);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('harry_potter_4.jpg', '/images/books/harry_potter_4.jpg', 2);

INSERT INTO immagine (nome_file, path, libro_id) VALUES ('the_walking_dead_2.jpg', '/images/books/the_walking_dead_2.jpg', 3);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('the_walking_dead_3.jpg', '/images/books/the_walking_dead_3.jpg', 3);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('the_walking_dead_4.jpg', '/images/books/the_walking_dead_4.jpg', 3);

INSERT INTO immagine (nome_file, path, libro_id) VALUES ('1984_2.jpg', '/images/books/1984_2.jpg', 1);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('1984_3.jpg', '/images/books/1984_3.jpg', 1);
UPDATE autore SET fotografia_id = 9 WHERE id = 1; 
UPDATE autore SET fotografia_id = 11 WHERE id = 2; 
UPDATE autore SET fotografia_id = 12 WHERE id = 3; 
UPDATE autore SET fotografia_id = 10 WHERE id = 4; 
INSERT INTO immagine (nome_file, path, autore_id) VALUES ('antoine_de_saint_exupery.jpg', '/images/authors/antoine_de_saint_exupery.jpg', 5);
INSERT INTO immagine (nome_file, path, autore_id) VALUES ('elisabetta_dami.jpg', '/images/authors/elisabetta_dami.jpg', 6);
UPDATE autore SET fotografia_id = 21 WHERE id = 5; 
UPDATE autore SET fotografia_id = 22 WHERE id = 6; 
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('il_piccolo_principe_1.jpg', '/images/books/il_piccolo_principe_1.jpg', 5);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('il_piccolo_principe_2.jpg', '/images/books/il_piccolo_principe_2.jpg', 5);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('il_piccolo_principe_3.jpg', '/images/books/il_piccolo_principe_3.jpg', 5);

INSERT INTO immagine (nome_file, path, libro_id) VALUES ('geronimo_stilton_1.jpg', '/images/books/geronimo_stilton_1.jpg', 6);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('geronimo_stilton_2.jpg', '/images/books/geronimo_stilton_2.jpg', 6);
INSERT INTO immagine (nome_file, path, libro_id) VALUES ('geronimo_stilton_3.jpg', '/images/books/geronimo_stilton_3.jpg', 6);



