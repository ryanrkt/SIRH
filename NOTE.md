# Conception d'un logiciel de gestion RH
Departement - Direction - Section - Service

**Fonctionnement**:
Besoin - Recrutement - Embauche - Vie du salarie - Evaluation - Rupture

## Bloc 1: Expression du besoin en personnel
Un manager 

## Bloc 2: Recrutement
Transformer une demande validee en offre d'emploi<br>
Publier l'offre et recevoir plusieurs candidatures.<br>
Preselectionner,organiser les entretiens et les tests.<br>
Conserver les evaluations et la decision finale.<br>
Objets: Offres Emploi, Candidat, Candidature,Entretien, Evaluation Candidat <br> 
**NB: La tracabilite est la plus importante.**

## Bloc 3: Selection et embauche
Le candidat retenu recoit une proposition d'embauche.<br>
Apres acceptation: creation du dossier salarie et du matricule.<br>
Creer le contract, le poste et l'affectation.
Point de conception: CANDIDAT != Employe.
Une personne peut avoir plusieurs candidatures avant de devenir salariee <br>
Montant Salaire,Date et heure de travail, Lieu de Travail,Poste affectation <br>
**Voir sur  Internet un contract de travail ,  une fois un contract accepte GENERER PDF CONTRAcT de Travail**

## Bloc 4: Contract et onboarding
Un salarie peut avoir plusieurs contract successifs: CE,CDD,renouvellement,CDI<br>
- CE Contract Essaie : 6 mois maxrenouvellable une fois
- CDD  : 2 ans max renouvellable une fois
- CDI : Duree Indeterminer<br>

Ne pas ecraser l'historique en stockant seulement,employe.type_contract.
Onboarding: contracvt signe,badge,compte,materiel,formation,inscription,paie.<br>
Utiliser une checklist et un workflow d'integration.<br>
Object: Contract,Affectation,Onbording,OnbordingTache,Document<br>
**Facultite:  possession de compte bancaire**




