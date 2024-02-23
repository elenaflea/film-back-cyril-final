INSERT INTO genre (id, titre) VALUES (1,'Science-Fiction');
INSERT INTO genre (id, titre) VALUES (2,'Animation');
INSERT INTO genre (id, titre) VALUES (3,'Fantastique');
INSERT INTO genre (id, titre) VALUES (4,'Drame');
INSERT INTO genre (id, titre) VALUES (5,'Comédie');


INSERT INTO participant(id,prenom, nom) VALUES (1, 'Georges', 'Lucas');
INSERT INTO participant(id,prenom, nom) VALUES (2, 'Steven', 'Spielberg');
INSERT INTO participant(id,prenom, nom) VALUES (3, 'Harrison', 'Ford');
INSERT INTO participant(id,prenom, nom) VALUES (4, 'Jennifer', 'Lawrence');

INSERT INTO film(titre, genre_id, realisateur_id, duree, annee) VALUES('E.T', 1, 2, 120, 1988);
INSERT INTO film(titre, genre_id, realisateur_id, duree, annee) VALUES('Star Wars', 3, 1, 110, 1978);
INSERT INTO film(titre, genre_id, realisateur_id, duree, annee) VALUES('Le seigneur des anneaux', 3, 3, 110, 1978);
