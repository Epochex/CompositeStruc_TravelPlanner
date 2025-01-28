-- 切换到数据库
USE agp;

-- Création des tables de base
CREATE TABLE Ile (
                     ILE_id INT AUTO_INCREMENT PRIMARY KEY,
                     ILE_nom VARCHAR(100) NOT NULL
);

CREATE TABLE Plage (
                       PLG_id INT AUTO_INCREMENT PRIMARY KEY,
                       PLG_nom VARCHAR(255) NOT NULL
);

CREATE TABLE Transport (
                           TRP_id INT AUTO_INCREMENT PRIMARY KEY,
                           TRP_nom VARCHAR(255) NOT NULL,
                           TRP_capacite INT NOT NULL
);

CREATE TABLE Lieu (
                      LIE_id INT AUTO_INCREMENT PRIMARY KEY,
                      LIE_nom VARCHAR(255) NOT NULL,
                      LIE_latitude DECIMAL(10, 8) NOT NULL,
                      LIE_longitude DECIMAL(11, 8) NOT NULL,
                      LIE_type ENUM('plage', 'hotel', 'site_touristique', 'arret') NOT NULL,
                      ILE_id INT NOT NULL,
                      FOREIGN KEY (ILE_id) REFERENCES Ile(ILE_id)
);

CREATE TABLE Hotel (
                       HOT_id INT PRIMARY KEY,
                       HOT_description TEXT,
                       HOT_etoiles INT CHECK (HOT_etoiles BETWEEN 1 AND 5),
                       PLG_id INT NOT NULL,
                       FOREIGN KEY (HOT_id) REFERENCES Lieu(LIE_id),
                       FOREIGN KEY (PLG_id) REFERENCES Plage(PLG_id)
);

CREATE TABLE SiteTouristique (
                                 SIT_id INT PRIMARY KEY,
                                 SIT_duree INT CHECK (SIT_duree > 0),
                                 SIT_type ENUM('site_historique', 'site_activite') NOT NULL,
                                 FOREIGN KEY (SIT_id) REFERENCES Lieu(LIE_id)
);

CREATE TABLE Arret (
                       ARR_id INT PRIMARY KEY,
                       ARR_nom VARCHAR(255) NOT NULL,
                       ARR_latitude DECIMAL(10, 8) NOT NULL,
                       ARR_longitude DECIMAL(11, 8) NOT NULL,
                       TRP_id INT NOT NULL,
                       ILE_id INT NOT NULL,
                       FOREIGN KEY (TRP_id) REFERENCES Transport(TRP_id),
                       FOREIGN KEY (ILE_id) REFERENCES Ile(ILE_id),
                       FOREIGN KEY (ARR_id) REFERENCES Lieu(LIE_id)
);

-- Insertion des données dans Ile
INSERT INTO Ile (ILE_id, ILE_nom) VALUES
                                      (1, 'Mahé'),
                                      (2, 'Silhouette'),
                                      (3, 'Praslin');

-- Insertion des données dans Plage
INSERT INTO Plage (PLG_id, PLG_nom) VALUES
                                        (1, 'Hillside Beach'),
                                        (2, 'Chellos Beach'),
                                        (3, 'Savoy Beach'),
                                        (4, 'Garden\'s Beach'),
(5, 'Turtle\'s Beach'),
                                        (6, 'Hilton Beach'),
                                        (7, 'Seashell\'s Beach'),
(8, 'Paradize\'s Beach'),
                                        (9, 'Collibri\'s Beach');

-- Insertion des données dans Transport
INSERT INTO Transport (TRP_id, TRP_nom, TRP_capacite) VALUES
(1, 'Bus 1', 25),
(2, 'Bus 2', 25),
(3, 'Bus 3', 25),
(4, 'Bus 4', 25),
(5, 'Ferry 1', 150),
(6, 'Ferry 2', 150),
(7, 'Ferry 3', 150);

-- Insertion des données dans Lieu
INSERT INTO Lieu (LIE_id, LIE_nom, LIE_latitude, LIE_longitude, LIE_type, ILE_id) VALUES
(1, 'Ros Sodyer Rock Pool', -4.78093258, 55.49221045, 'site_touristique', 1),
(2, 'Anse Forbans Beach', -4.77474792, 55.52427935, 'site_touristique', 1),
(3, 'Mont Sebert viewpoint', -4.67887615, 55.50361046, 'site_touristique', 1),
(4, 'Aussichtspunkt', -4.67146619, 55.50148626, 'site_touristique', 1),
(5, 'Mont (Mount) Sebert trail', -4.67403700, 55.49902825, 'site_touristique', 1),
(6, 'Cascade Waterfall', -4.67724111, 55.49313667, 'site_touristique', 1),
(7, 'Jardin botanique', -4.62924787, 55.45650348, 'site_touristique', 1),
(8, 'Dans Gallas Trail', -4.62399155, 55.42730613, 'site_touristique', 1),
(9, 'Beau Vallon Beach', -4.61536035, 55.42276382, 'site_touristique', 1),
(10, 'View point Anse Major', -4.61784017, 55.39679683, 'site_touristique', 1),
(11, 'Anse Major', -4.62591855, 55.38515602, 'site_touristique', 1),
(12, 'Mare Aux Cochons Trail', -4.62857393, 55.40690888, 'site_touristique', 1),
(13, 'Sauzier Waterfall', -4.65737884, 55.41514089, 'site_touristique', 1),
(14, 'Morne Blanc View Point', -4.65678010, 55.43378609, 'site_touristique', 1),
(15, 'Anse Boileau Beach', -4.71116035, 55.48367635, 'site_touristique', 1),
(16, 'Spiaggia di Baie Lazare', -4.75651577, 55.48116113, 'site_touristique', 1),
(17, 'Ros (Rocks) Maravi', -4.76902717, 55.48729389, 'site_touristique', 1),
(18, 'Dan Sours Baie Lazare', -4.75963228, 55.50840662, 'site_touristique', 1),
(19, 'Pointe Grand Barbe', -4.51042060, 55.22746440, 'site_touristique', 2),
(20, 'Pointe Ramasse Tout', -4.48919945, 55.25572455, 'site_touristique', 2),
(21, 'Jardin Marron Trail', -4.48576433, 55.23644233, 'site_touristique', 2),
(22, 'Muffin Rock', -4.46677144, 55.23893441, 'site_touristique', 2),
(23, 'Anse Modon', -4.46627593, 55.22082990, 'site_touristique', 2),
(24, 'Pointe Consolation', -4.36041050, 55.76038617, 'site_touristique', 3),
(25, 'Taboga Seychelles', -4.34051471, 55.76142427, 'site_touristique', 3),
(26, 'Vallé de Mai Trail', -4.33131142, 55.73938354, 'site_touristique', 3),
(27, 'Ansé Lazario', -4.29345959, 55.70147049, 'site_touristique', 3),
(28, 'Point Millers Anse', -4.29465960, 55.68088878, 'site_touristique', 3),
(29, 'Sunset View Point', -4.33740208, 55.72056312, 'site_touristique', 3),
(30, 'Kot Man-Ya Exotic Flower Garden', -4.73844299, 55.50680470, 'site_touristique', 1),
(31, 'Old Tea Factory', -4.66220317, 55.43790854, 'site_touristique', 1),
(32, 'Ancient Marina', -4.48431632, 55.25228669, 'site_touristique', 2),
(33, 'Zugangz Tor', -4.30667162, 55.70557810, 'site_touristique', 3),
(34, 'Hillside Retreat', -4.74695178, 55.47781735, 'hotel', 1),
(35, 'Villa Chellos', -4.71152340, 55.48339726, 'hotel', 1),
(36, 'Savoy Resort', -4.61224064, 55.43028637, 'hotel', 1),
(37, 'Garden\'s Villa', -4.66254347, 55.41449393, 'hotel', 1),
                                        (38, 'Turtle\'s Villa', -4.48526620, 55.25327862, 'hotel', 2),
(39, 'Hilton Resort', -4.48103369, 55.24803149, 'hotel', 2),
(40, 'Seashell\'s Villa', -4.32387647, 55.70431743, 'hotel', 3),
                                        (41, 'Paradize Hotel', -4.31232639, 55.73897763, 'hotel', 3),
                                        (42, 'Collibri GuestHotel', -4.34888747, 55.76720959, 'hotel', 3);

-- Insertion des données dans SiteTouristique
INSERT INTO SiteTouristique (SIT_id, SIT_duree, SIT_type) VALUES
                                                              (1, 90, 'site_activite'),
                                                              (2, 90, 'site_activite'),
                                                              (3, 90, 'site_activite'),
                                                              (4, 90, 'site_activite'),
                                                              (5, 0, 'site_activite'),
                                                              (6, 0, 'site_activite'),
                                                              (7, 90, 'site_activite'),
                                                              (8, 0, 'site_activite'),
                                                              (9, 90, 'site_activite'),
                                                              (10, 90, 'site_activite'),
                                                              (11, 90, 'site_activite'),
                                                              (12, 0, 'site_activite'),
                                                              (13, 90, 'site_activite'),
                                                              (14, 90, 'site_activite'),
                                                              (15, 90, 'site_activite'),
                                                              (16, 0, 'site_activite'),
                                                              (17, 90, 'site_activite'),
                                                              (18, 90, 'site_activite'),
                                                              (19, 90, 'site_activite'),
                                                              (20, 90, 'site_activite'),
                                                              (21, 0, 'site_activite'),
                                                              (22, 90, 'site_activite'),
                                                              (23, 90, 'site_activite'),
                                                              (24, 90, 'site_historique'),
                                                              (25, 90, 'site_historique'),
                                                              (26, 0, 'site_historique'),
                                                              (27, 90, 'site_historique'),
                                                              (28, 90, 'site_historique'),
                                                              (29, 90, 'site_historique'),
                                                              (30, 120, 'site_historique'),
                                                              (31, 120, 'site_historique'),
                                                              (32, 120, 'site_historique'),
                                                              (33, 120, 'site_historique');
