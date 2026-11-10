USE cabinet_medical;

-- Table Patients
CREATE TABLE IF NOT EXISTS Patients (
    id_patient INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    adresse TEXT,
    telephone VARCHAR(20)
);

-- Table Medecins
CREATE TABLE IF NOT EXISTS Medecins (
    id_medecin INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    specialite VARCHAR(100),
    telephone VARCHAR(20)
);

-- Table RendezVous
CREATE TABLE IF NOT EXISTS RendezVous (
    id_rdv INT PRIMARY KEY AUTO_INCREMENT,
    date_rdv DATETIME NOT NULL,
    id_patient INT,
    id_medecin INT,
    FOREIGN KEY (id_patient) REFERENCES Patients(id_patient) ON DELETE CASCADE,
    FOREIGN KEY (id_medecin) REFERENCES Medecins(id_medecin) ON DELETE CASCADE
);

-- Table Traitements
CREATE TABLE IF NOT EXISTS Traitements (
    id_traitement INT PRIMARY KEY AUTO_INCREMENT,
    description TEXT,
    medicaments TEXT,
    duree VARCHAR(50),
    id_patient INT,
    id_medecin INT,
    FOREIGN KEY (id_patient) REFERENCES Patients(id_patient) ON DELETE CASCADE,
    FOREIGN KEY (id_medecin) REFERENCES Medecins(id_medecin) ON DELETE CASCADE
);

-- Ajouter des données de test
INSERT INTO Medecins (nom, specialite, telephone) VALUES
('Dr. Martin', 'Généraliste', '0123456789'),
('Dr. Bernard', 'Cardiologue', '0987654321'),
('Dr. Petit', 'Dermatologue', '0234567890');

INSERT INTO Patients (nom, prenom, date_naissance, telephone) VALUES
('Dupont', 'Jean', '1980-05-15', '0112233445'),
('Durand', 'Marie', '1992-08-22', '0223344556'),
('Leroy', 'Pierre', '1975-11-30', '0334455667');

SELECT '✅ Toutes les tables créées avec données de test!' as Message;