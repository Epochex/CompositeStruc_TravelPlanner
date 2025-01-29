use maariaa_agp;
-- Table Ile
CREATE TABLE Ile (
                     ILE_id INT AUTO_INCREMENT PRIMARY KEY,
                     ILE_nom VARCHAR(100) NOT NULL
);

-- Table Lieu avec coordonnées géospatiales
CREATE TABLE Lieu (
                      LIE_id INT AUTO_INCREMENT PRIMARY KEY,
                      LIE_nom VARCHAR(100) NOT NULL,
                      LIE_type ENUM('plage', 'hotel', 'site_touristique', 'site_historique', 'site_activite') NOT NULL,
                      LIE_latitude DECIMAL(10, 8) NOT NULL, -- Coordonnée latitude
                      LIE_longitude DECIMAL(11, 8) NOT NULL, -- Coordonnée longitude
                      ILE_id INT NOT NULL, -- Clé étrangère vers Ile
                      FOREIGN KEY (ILE_id) REFERENCES Ile(ILE_id)
);
CREATE TABLE Hotel (
                       HOT_id INT PRIMARY KEY, -- Même ID que Lieu
                       HOT_description TEXT, -- Description de l'hôtel
                       HOT_etoiles INT CHECK (HOT_etoiles BETWEEN 1 AND 5), -- Nombre d'étoiles (1 à 5)
                       HOT_tarif INT, -- Capacité maximale d'accueil
                       FOREIGN KEY (HOT_id) REFERENCES Lieu(LIE_id)
);

CREATE TABLE SiteTouristique (
                                 SIT_id INT PRIMARY KEY, -- Même ID que Lieu
                                 SIT_description TEXT, -- Description du site
                                 SIT_tarif DECIMAL(10, 2), -- Tarif d'entrée
                                 FOREIGN KEY (SIT_id) REFERENCES Lieu(LIE_id)
);

CREATE TABLE Arret (
                       ARR_id INT PRIMARY KEY, -- Même ID que Lieu
                       ARR_type_transport ENUM('bus', 'bateau') NOT NULL, -- Type de transport
                       FOREIGN KEY (ARR_id) REFERENCES Lieu(LIE_id)
);

CREATE TABLE Plage (
                       PLG_id INT AUTO_INCREMENT PRIMARY KEY,
                       PLG_nom VARCHAR(255) NOT NULL
);

CREATE TABLE Transport (
                           TRP_id INT AUTO_INCREMENT PRIMARY KEY,
                           TRP_nom VARCHAR(255) NOT NULL,       -- Nom du transport
                           TRP_capacite INT NOT NULL,           -- Capacité du transport
                           ILE_id INT NOT NULL,                 -- Référence à l'île associée
                           FOREIGN KEY (ILE_id) REFERENCES Ile(ILE_id) -- Clé étrangère vers l'île
);

CREATE TABLE Bateau (
                        BAT_id INT AUTO_INCREMENT PRIMARY KEY, -- Identifiant unique pour chaque bateau
                        TRP_id INT NOT NULL,                   -- Référence au transport parent
                        FOREIGN KEY (TRP_id) REFERENCES Transport(TRP_id) -- Clé étrangère vers Transport
);

CREATE TABLE Autobus (
                         BUS_id INT AUTO_INCREMENT PRIMARY KEY,  -- Identifiant unique pour chaque autobus
                         TRP_id INT NOT NULL,                    -- Référence au transport parent
                         FOREIGN KEY (TRP_id) REFERENCES Transport(TRP_id) -- Clé étrangère vers Transport
);

CREATE TABLE Arret_Transport (
                                 TRP_id INT NOT NULL,     -- Référence au transport
                                 ARR_id INT NOT NULL,     -- Référence à l'arrêt
                                 PRIMARY KEY (TRP_id, ARR_id), -- Clé primaire composite
                                 FOREIGN KEY (TRP_id) REFERENCES Transport(TRP_id) ON DELETE CASCADE, -- Suppression en cascade
                                 FOREIGN KEY (ARR_id) REFERENCES Arret(ARR_id) ON DELETE CASCADE      -- Suppression en cascade
);

-- Ajouter la colonne SIT_duree
ALTER TABLE SiteTouristique
    ADD SIT_duree INT CHECK (SIT_duree > 0);

-- Ajouter la colonne SIT_type
ALTER TABLE SiteTouristique
    ADD SIT_type ENUM('site_historique', 'site_activite') NOT NULL;

ALTER TABLE Hotel
    ADD PLG_id INT NOT NULL,
ADD CONSTRAINT FK_Hotel_Plage FOREIGN KEY (PLG_id) REFERENCES Plage(PLG_id);

