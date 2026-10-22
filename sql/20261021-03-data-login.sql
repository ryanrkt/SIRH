-- =====================================================================
-- JEU DE DONNÉES DE TEST : Authentification et Redirection par Rôle/Département

-- TOUT LES MOT DE PASSE SONT :password
-- =====================================================================

-- 1. Insertion des Rôles
INSERT INTO role (code, libelle) VALUES
('CANDIDAT', 'Candidat'),
('EMPLOYE', 'Employé'),
('MANAGER', 'Manager de département');

-- 2. Insertion des Départements (correspondant aux codes du CustomLoginSuccessHandler)
INSERT INTO departement (code, libelle) VALUES
('RH', 'Ressources Humaines'),
('FINANCE', 'Finance et Comptabilité'),
('DIRECTION', 'Direction Générale'),
('IT', 'Technologies de l''Information');

-- 3. Insertion des Utilisateurs
INSERT INTO utilisateur (role_id, email, mot_passe, nom, prenom, date_naissance, actif) VALUES
-- 01. Manager RH (Doit être redirigé vers /rh/dashboard)
((SELECT id FROM role WHERE code = 'MANAGER'), 'manager.rh@sirh.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'RAKOTO', 'Hery', '1985-05-12', TRUE),

-- 02. Manager Finance (Doit être redirigé vers /finance/dashboard)
((SELECT id FROM role WHERE code = 'MANAGER'), 'manager.finance@sirh.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ANDRIANA', 'Lova', '1990-08-22', TRUE),

-- 03. Manager Direction (Doit être redirigé vers /direction/dashboard/)
((SELECT id FROM role WHERE code = 'MANAGER'), 'manager.dir@sirh.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'RASOA', 'Miarana', '1978-02-15', TRUE),

-- 04. Manager IT - Autre département (Doit être redirigé vers /dept-IT/demandes-recrutement)
((SELECT id FROM role WHERE code = 'MANAGER'), 'manager.it@sirh.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'RABE', 'Tiana', '1988-11-04', TRUE),

-- 05. Employé simple (Doit être redirigé vers /offres-emploi)
((SELECT id FROM role WHERE code = 'EMPLOYE'), 'employe@sirh.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'RANAIVO', 'Fara', '1995-07-30', TRUE),

-- 06. Candidat externe (Doit être redirigé vers /offres-emploi)
((SELECT id FROM role WHERE code = 'CANDIDAT'), 'candidat@sirh.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'RANDRIA', 'Koto', '1998-12-05', TRUE);

-- 4. Insertion des Profils Employés (UNIQUEMENT pour les managers et l'employé)
INSERT INTO employe (utilisateur_id, departement_id, matricule, date_embauche) VALUES
-- Employé lié au manager RH
((SELECT id FROM utilisateur WHERE email = 'manager.rh@sirh.mg'), 
 (SELECT id FROM departement WHERE code = 'RH'), 'MAT-RH-001', '2020-01-15'),

-- Employé lié au manager Finance
((SELECT id FROM utilisateur WHERE email = 'manager.finance@sirh.mg'), 
 (SELECT id FROM departement WHERE code = 'FINANCE'), 'MAT-FIN-001', '2019-06-01'),

-- Employé lié au manager Direction
((SELECT id FROM utilisateur WHERE email = 'manager.dir@sirh.mg'), 
 (SELECT id FROM departement WHERE code = 'DIRECTION'), 'MAT-DIR-001', '2015-03-10'),

-- Employé lié au manager IT
((SELECT id FROM utilisateur WHERE email = 'manager.it@sirh.mg'), 
 (SELECT id FROM departement WHERE code = 'IT'), 'MAT-IT-001', '2021-09-01'),

-- Employé simple (rattaché au département IT)
((SELECT id FROM utilisateur WHERE email = 'employe@sirh.mg'), 
 (SELECT id FROM departement WHERE code = 'IT'), 'MAT-IT-002', '2023-01-10');