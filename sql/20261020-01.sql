
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'SIRH'
  AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS SIRH;

CREATE DATABASE SIRH;

\c SIRH


DROP TYPE IF EXISTS role_code_enum CASCADE;
DROP TYPE IF EXISTS type_contract_enum CASCADE;
DROP TYPE IF EXISTS contract_statut_enum CASCADE;
DROP TYPE IF EXISTS demande_statut_enum CASCADE;
DROP TYPE IF EXISTS etape_approbation_enum CASCADE;
DROP TYPE IF EXISTS statut_etape_enum CASCADE;




CREATE TYPE role_code_enum AS ENUM ('ADMIN', 'MANAGER', 'EMPLOYE');

CREATE TYPE type_contract_enum AS ENUM ('CDI', 'CDD', 'STAGE');

CREATE TYPE contract_statut_enum AS ENUM ('ACTIF', 'TERMINE', 'ROMPU');

CREATE TYPE demande_statut_enum AS ENUM (
    'EN_ATTENTE_RH', 
    'EN_ATTENTE_FINANCE', 
    'EN_ATTENTE_DIRECTION', 
    'APPROUVEE', 
    'REFUSEE'
);

CREATE TYPE etape_approbation_enum AS ENUM ('RH', 'FINANCE', 'DIRECTION');

CREATE TYPE statut_etape_enum AS ENUM ('EN_ATTENTE', 'VALIDER');



-- Table des utilisateurs (Identité / Authentification)
CREATE TABLE utilisateur (
    id_user BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_passe VARCHAR(255) NOT NULL,
    is_employe BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des départements
CREATE TABLE departement (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) UNIQUE NOT NULL,
    libelle VARCHAR(150) NOT NULL
);

-- Table des rôles système
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    code role_code_enum UNIQUE NOT NULL
);

-- Table des employés
CREATE TABLE employe (
    id_employe BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES utilisateur(id_user) ON DELETE CASCADE,
    departement_id BIGINT NOT NULL REFERENCES departement(id),
    role_id BIGINT NOT NULL REFERENCES role(id),
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL
);

-- Table des postes
CREATE TABLE poste (
    id BIGSERIAL PRIMARY KEY,
    departement_id BIGINT NOT NULL REFERENCES departement(id) ON DELETE CASCADE,
    titre VARCHAR(150) NOT NULL,
    description TEXT
);

-- Table des contrats de travail
CREATE TABLE contract (
    id BIGSERIAL PRIMARY KEY,
    employe_id BIGINT NOT NULL REFERENCES employe(id_employe) ON DELETE CASCADE,
    poste_id BIGINT NOT NULL REFERENCES poste(id),
    type_contract type_contract_enum NOT NULL,
    salaire NUMERIC(12, 2) NOT NULL CHECK (salaire >= 0),
    date_debut DATE NOT NULL,
    date_fin DATE,
    statut contract_statut_enum NOT NULL DEFAULT 'ACTIF',
    CONSTRAINT chk_dates_contrat CHECK (date_fin IS NULL OR date_fin >= date_debut)
);

-- Table des demandes de recrutement
CREATE TABLE demande_recrutement (
    id BIGSERIAL PRIMARY KEY,
    departement_id BIGINT NOT NULL REFERENCES departement(id),
    demandeur_id BIGINT NOT NULL REFERENCES employe(id_employe),
    poste_id BIGINT NOT NULL REFERENCES poste(id),
    nombre_poste INT NOT NULL DEFAULT 1 CHECK (nombre_poste > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut demande_statut_enum NOT NULL DEFAULT 'EN_ATTENTE_RH'
);

-- Table d'historique et de suivi des approbations
CREATE TABLE approbation_recrutement (
    id BIGSERIAL PRIMARY KEY,
    demande_recrutement_id BIGINT NOT NULL REFERENCES demande_recrutement(id) ON DELETE CASCADE,
    etape etape_approbation_enum NOT NULL,
    valideur_id BIGINT REFERENCES employe(id_employe),
    statut_etape statut_etape_enum NOT NULL DEFAULT 'EN_ATTENTE',
    commentaire TEXT,
    valider_at TIMESTAMP
);


-- Règle métier : Un employé ne peut avoir QU'UN SEUL contrat actif (date_fin IS NULL et statut = 'ACTIF')
CREATE UNIQUE INDEX idx_unique_active_contract 
ON contract (employe_id) 
WHERE date_fin IS NULL AND statut = 'ACTIF';

-- Index de performance pour les jointures fréquentes
CREATE INDEX idx_employe_dept ON employe(departement_id);
CREATE INDEX idx_demande_dept ON demande_recrutement(departement_id);
CREATE INDEX idx_contract_employe ON contract(employe_id);