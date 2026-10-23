-- =====================================================================
-- SCRIPT COMPLET SIRH : AUTHENTIFICATION, GESTION RH & RECRUTEMENT AVEC NEGOCIATION
-- =====================================================================

-- 1. Suppression des tables dans l'ordre des dépendances
DROP TABLE IF EXISTS negociation_candidature CASCADE;
DROP TABLE IF EXISTS candidature CASCADE;
DROP TABLE IF EXISTS offre_emploi CASCADE;
DROP TABLE IF EXISTS education_candidat CASCADE;
DROP TABLE IF EXISTS approbation_recrutement CASCADE;
DROP TABLE IF EXISTS demande_recrutement CASCADE;
DROP TABLE IF EXISTS contract CASCADE;
DROP TABLE IF EXISTS employe CASCADE;
DROP TABLE IF EXISTS poste CASCADE;
DROP TABLE IF EXISTS utilisateur CASCADE;
DROP TABLE IF EXISTS departement CASCADE;
DROP TABLE IF EXISTS role CASCADE;

-- 2. Suppression des types énumérés
DROP TYPE IF EXISTS niveau_etude_enum CASCADE;
DROP TYPE IF EXISTS candidature_statut_enum CASCADE;
DROP TYPE IF EXISTS offre_statut_enum CASCADE;
DROP TYPE IF EXISTS statut_etape_enum CASCADE;
DROP TYPE IF EXISTS etape_approbation_enum CASCADE;
DROP TYPE IF EXISTS demande_statut_enum CASCADE;
DROP TYPE IF EXISTS contract_statut_enum CASCADE;
DROP TYPE IF EXISTS type_contract_enum CASCADE;
DROP TYPE IF EXISTS role_code_enum CASCADE;

-- =====================================================================
-- 3. Types énumérés
-- =====================================================================

CREATE TYPE role_code_enum AS ENUM (
    'CANDIDAT', 'EMPLOYE', 'MANAGER'
);

CREATE TYPE type_contract_enum AS ENUM (
    'CDI', 'CDD', 'ESSAI'
);

CREATE TYPE contract_statut_enum AS ENUM (
    'ACTIF', 'TERMINE', 'ROMPU'
);

CREATE TYPE demande_statut_enum AS ENUM (
    'EN_ATTENTE_RH',
    'EN_ATTENTE_FINANCE',
    'EN_ATTENTE_DIRECTION',
    'APPROUVEE',
    'REFUSEE'
);

CREATE TYPE etape_approbation_enum AS ENUM (
    'RH', 'FINANCE', 'DIRECTION'
);

CREATE TYPE statut_etape_enum AS ENUM (
    'EN_ATTENTE', 'VALIDEE', 'REFUSEE'
);

-- Nouveaux Enums Recrutement
CREATE TYPE offre_statut_enum AS ENUM (
    'BROUILLON', 'PUBLIEE', 'FERMEE', 'ANNULEE'
);

CREATE TYPE candidature_statut_enum AS ENUM (
    'SOUMISE', 'EN_COURS_NEGOCIATION', 'ACCEPTEE', 'REFUSEE', 'RETIREE'
);

CREATE TYPE niveau_etude_enum AS ENUM (
    'BACCALAUREAT', 'LICENCE', 'MASTER', 'DOCTORAT', 'AUTRE'
);

-- =====================================================================
-- 4. Tables principales de base
-- =====================================================================

-- Rôles
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    code role_code_enum UNIQUE NOT NULL,
    libelle VARCHAR(100) NOT NULL
);

-- Départements
CREATE TABLE departement (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) UNIQUE NOT NULL,
    libelle VARCHAR(150) NOT NULL
);

-- Utilisateurs
CREATE TABLE utilisateur (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL REFERENCES role(id),
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Employés
CREATE TABLE employe (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT UNIQUE NOT NULL
        REFERENCES utilisateur(id) ON DELETE CASCADE,
    departement_id BIGINT NOT NULL REFERENCES departement(id),
    matricule VARCHAR(30) UNIQUE NOT NULL,
    date_embauche DATE NOT NULL
);

-- Postes (Correction du bug de frappe 'NOT NULLa')
CREATE TABLE poste (
    id BIGSERIAL PRIMARY KEY,
    departement_id BIGINT NOT NULL
        REFERENCES departement(id) ON DELETE CASCADE,
    titre VARCHAR(150) NOT NULL,
    description TEXT,
    UNIQUE (departement_id, titre)
);

-- Contrats de travail
CREATE TABLE contract (
    id BIGSERIAL PRIMARY KEY,
    employe_id BIGINT NOT NULL
        REFERENCES employe(id) ON DELETE CASCADE,
    poste_id BIGINT NOT NULL REFERENCES poste(id),
    type_contract type_contract_enum NOT NULL,
    salaire NUMERIC(12, 2) NOT NULL CHECK (salaire >= 0),
    date_debut DATE NOT NULL,
    date_fin DATE,
    statut contract_statut_enum NOT NULL DEFAULT 'ACTIF',
    CONSTRAINT chk_dates_contrat
        CHECK (date_fin IS NULL OR date_fin >= date_debut)
);

-- Demandes de recrutement
CREATE TABLE demande_recrutement (
    id BIGSERIAL PRIMARY KEY,
    departement_id BIGINT NOT NULL REFERENCES departement(id),
    demandeur_id BIGINT NOT NULL REFERENCES employe(id),
    poste_id BIGINT NOT NULL REFERENCES poste(id),
    nombre_poste INT NOT NULL DEFAULT 1 CHECK (nombre_poste > 0),
    statut demande_statut_enum NOT NULL DEFAULT 'EN_ATTENTE_RH',
    salaire INT NOT NULL DEFAULT 1 CHECK (salaire > 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
ALTER TABLE demande_recrutement
ADD COLUMN salaire NUMERIC(12,2) NOT NULL DEFAULT 0
CHECK (salaire >= 0);

ALTER TABLE demande_recrutement
ADD COLUMN date_limite DATE NOT NULL;

ALTER TABLE demande_recrutement
ALTER COLUMN salaire TYPE INTEGER
USING salaire::INTEGER;
-- Approbations des demandes
CREATE TABLE approbation_recrutement (
    id BIGSERIAL PRIMARY KEY,
    demande_recrutement_id BIGINT NOT NULL
        REFERENCES demande_recrutement(id) ON DELETE CASCADE,
    etape etape_approbation_enum NOT NULL,
    valideur_id BIGINT REFERENCES employe(id),
    statut_etape statut_etape_enum NOT NULL DEFAULT 'EN_ATTENTE',
    commentaire TEXT,
    valider_at TIMESTAMPTZ,
    UNIQUE (demande_recrutement_id, etape)
);

-- =====================================================================
-- 5. NOUVELLES TABLES : CANDIDATS, OFFRES & NEGOCIATION
-- =====================================================================

-- Parcours académique des candidats
CREATE TABLE education_candidat (
    id BIGSERIAL PRIMARY KEY,
    candidat_id BIGINT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    diplome VARCHAR(150) NOT NULL,
    niveau niveau_etude_enum NOT NULL,
    etablissement VARCHAR(200) NOT NULL,
    domaine_etude VARCHAR(150),
    date_debut DATE NOT NULL,
    date_fin DATE,
    description TEXT,
    CONSTRAINT chk_dates_education CHECK (date_fin IS NULL OR date_fin >= date_debut)
);

-- Offres d'emploi publiques
CREATE TABLE offre_emploi (
    id BIGSERIAL PRIMARY KEY,
    demande_recrutement_id BIGINT NOT NULL REFERENCES demande_recrutement(id) ON DELETE CASCADE,
    titre VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    profil_recherche TEXT,
    salaire_propose_min NUMERIC(12, 2) CHECK (salaire_propose_min >= 0),
    salaire_propose_max NUMERIC(12, 2) CHECK (salaire_propose_max >= salaire_propose_min),
    heures_par_semaine_proposees INT DEFAULT 40 CHECK (heures_par_semaine_proposees > 0),
    statut offre_statut_enum NOT NULL DEFAULT 'BROUILLON',
    date_publication TIMESTAMPTZ,
    date_expiration TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Candidatures soumises par les candidats
CREATE TABLE candidature (
    id BIGSERIAL PRIMARY KEY,
    offre_id BIGINT NOT NULL REFERENCES offre_emploi(id) ON DELETE CASCADE,
    candidat_id BIGINT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    cv_url VARCHAR(255),
    lettre_motivation TEXT,
    statut candidature_statut_enum NOT NULL DEFAULT 'SOUMISE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (offre_id, candidat_id) -- Un candidat ne postule qu'une seule fois à la même offre
);

-- Historique des négociations (Contre-propositions Candidat <-> RH)
CREATE TABLE negociation_candidature (
    id BIGSERIAL PRIMARY KEY,
    candidature_id BIGINT NOT NULL REFERENCES candidature(id) ON DELETE CASCADE,
    auteur_id BIGINT NOT NULL REFERENCES utilisateur(id), -- Peut être l'utilisateur Candidat ou l'Employé RH
    salaire_propose NUMERIC(12, 2) NOT NULL CHECK (salaire_propose >= 0),
    heures_par_semaine INT NOT NULL CHECK (heures_par_semaine > 0),
    commentaire TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =====================================================================
-- 6. Index d'optimisation
-- =====================================================================

CREATE UNIQUE INDEX idx_unique_active_contract ON contract (employe_id) WHERE date_fin IS NULL AND statut = 'ACTIF';
CREATE INDEX idx_utilisateur_email ON utilisateur (LOWER(email));
CREATE INDEX idx_employe_dept ON employe (departement_id);
CREATE INDEX idx_demande_dept ON demande_recrutement (departement_id);
CREATE INDEX idx_contract_employe ON contract (employe_id);

-- Index pour les nouvelles tables
CREATE INDEX idx_education_candidat ON education_candidat (candidat_id);
CREATE INDEX idx_offre_statut ON offre_emploi (statut);
CREATE INDEX idx_candidature_offre ON candidature (offre_id);
CREATE INDEX idx_candidature_candidat ON candidature (candidat_id);
CREATE INDEX idx_negociation_candidature ON negociation_candidature (candidature_id);