package can2025.ui;

import can2025.exceptions.*;
import can2025.models.*;
import can2025.services.SecurityService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    
    private SecurityService service;
    private Scanner scanner;
    private boolean running;
    
    private static final String SEPARATOR = "================================================================";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ConsoleUI(SecurityService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void lancer() {
        afficherBanniere();
        
        while (running) {
            afficherMenuPrincipal();
            System.out.print("Votre choix: ");
            int choix = scanner.nextInt();
            scanner.nextLine(); 
            
            try {
                traiterChoix(choix);
            } catch (Exception e) {
                System.out.println("\n[ERREUR] " + e.getMessage());
            }
            
            if (running) {
                System.out.print("\nAppuyez sur Entree pour continuer...");
                scanner.nextLine();
            }
        }
        
        System.out.println("\nAu revoir et bonne CAN 2025!");
        scanner.close();
    }
    
    private void afficherBanniere() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("    SYSTEME DE GESTION DE SECURITE - CAN 2025");
        System.out.println(SEPARATOR);
        System.out.println("    Bienvenue dans le systeme d'accreditation");
        System.out.println("    et de controle d'acces de la CAN 2025");
        System.out.println(SEPARATOR + "\n");
    }
    
    private void afficherMenuPrincipal() {
        System.out.println("\n+------------------------------------+");
        System.out.println("|           MENU PRINCIPAL           |");
        System.out.println("+------------------------------------+");
        System.out.println("|  1. Ajouter un utilisateur         |");
        System.out.println("|  2. Ajouter une zone securisee     |");
        System.out.println("|  3. Generer un badge               |");
        System.out.println("|  4. Verifier un acces              |");
        System.out.println("|  5. Bloquer / Debloquer un badge   |");
        System.out.println("|  6. Afficher historique des acces  |");
        System.out.println("|  7. Afficher les rapports          |");
        System.out.println("|  8. Lister les utilisateurs        |");
        System.out.println("|  9. Lister les zones               |");
        System.out.println("| 10. Lister les badges              |");
        System.out.println("|  0. Quitter                        |");
        System.out.println("+------------------------------------+");
    }
    
    private void traiterChoix(int choix) throws Exception {
        switch (choix) {
            case 1: ajouterUtilisateur(); break;
            case 2: ajouterZone(); break;
            case 3: genererBadge(); break;
            case 4: verifierAcces(); break;
            case 5: gererBadge(); break;
            case 6: afficherHistorique(); break;
            case 7: afficherRapports(); break;
            case 8: listerUtilisateurs(); break;
            case 9: listerZones(); break;
            case 10: listerBadges(); break;
            case 0: running = false; break;
            default: System.out.println("[ERREUR] Choix invalide!");
        }
    }
    
    private void ajouterUtilisateur() throws DonneeInvalideException {
        System.out.println("\n+---------------------------------+");
        System.out.println("|    AJOUTER UN UTILISATEUR       |");
        System.out.println("+---------------------------------+");
        
        System.out.println("\nType d'utilisateur:");
        System.out.println("  1. Spectateur");
        System.out.println("  2. Staff General");
        System.out.println("  3. VIP");
        
        System.out.print("Choisissez le type: ");
        int type = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Nom de l'utilisateur: ");
        String nom = scanner.nextLine().trim();
        
        switch (type) {
            case 1:
                service.creerSpectateur(nom);
                break;
            case 2:
                service.creerStaff(nom, CategorieUtilisateur.STAFF);
                break;
            case 3:
                service.creerVIP(nom);
                break;
            default:
                throw new DonneeInvalideException("Type invalide");
        }
        
        System.out.println("\n[OK] Utilisateur cree avec succes!");
    }
    
    private void ajouterZone() throws DonneeInvalideException {
        System.out.println("\n+---------------------------------+");
        System.out.println("|    AJOUTER UNE ZONE SECURISEE   |");
        System.out.println("+---------------------------------+");
        
        System.out.print("ID de la zone (ex: Z01): ");
        String idZone = scanner.nextLine().trim();
        
        System.out.print("Nom de la zone: ");
        String nomZone = scanner.nextLine().trim();
        
        System.out.println("\nNiveaux de securite:");
        System.out.println("  1 - Spectateur");
        System.out.println("  2 - Staff");
        System.out.println("  3 - VIP");
        
        System.out.print("Niveau requis (1-3): ");
        int niveau = scanner.nextInt();
        scanner.nextLine();
        
        service.creerZone(idZone, nomZone, niveau);
        
        System.out.println("\n[OK] Zone creee avec succes!");
    }
    
    private void genererBadge() throws DonneeInvalideException, BadgeInvalideException {
        System.out.println("\n+---------------------------------+");
        System.out.println("|       GENERER UN BADGE          |");
        System.out.println("+---------------------------------+");
        
        // Afficher la liste des utilisateurs sans badge
        List<UtilisateurCAN> sansBadge = service.getUtilisateurs().stream()
                .filter(u -> !u.hasBadge())
                .toList();
        
        if (sansBadge.isEmpty()) {
            System.out.println("\n[INFO] Tous les utilisateurs ont deja un badge!");
            return;
        }
        
        System.out.println("\nUtilisateurs sans badge:");
        for (UtilisateurCAN u : sansBadge) {
            System.out.println("  ID " + u.getId() + ": " + u.getNom() + " (" + u.getCategorie() + ")");
        }
        
        System.out.print("\nID de l'utilisateur: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("\nType de validite:");
        System.out.println("  1. Duree en jours");
        System.out.println("  2. Dates personnalisees");
        
        System.out.print("Choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        if (choix == 1) {
            System.out.print("Duree en jours: ");
            int duree = scanner.nextInt();
            scanner.nextLine();
            service.genererBadge(userId, duree);
            System.out.println("\n[OK] Badge genere avec duree de " + duree + " jours");
        } else {
            System.out.print("Date de debut (jj/mm/aaaa): ");
            String debutStr = scanner.nextLine().trim();
            LocalDate dateDebut = parseDate(debutStr);
            
            System.out.print("Date de fin (jj/mm/aaaa): ");
            String finStr = scanner.nextLine().trim();
            LocalDate dateFin = parseDate(finStr);
            
            service.genererBadge(userId, dateDebut, dateFin);
            System.out.println("\n[OK] Badge genere avec succes!");
        }
    }
    
    private void verifierAcces() {
        System.out.println("\n+---------------------------------+");
        System.out.println("|       VERIFIER UN ACCES         |");
        System.out.println("+---------------------------------+");
        
        System.out.print("Numero du badge: ");
        int numBadge = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("ID de la zone: ");
        String idZone = scanner.nextLine().trim();
        
        AccesLog log = service.tenterAcces(numBadge, idZone);
        
        if (log.estAutorise()) {
            System.out.println("\n[OK] ACCES AUTORISE");
        } else {
            System.out.println("\n[NO] ACCES REFUSE");
        }
        System.out.println("   Resultat: " + log.getResultat().getLibelle());
    }
    
    private void gererBadge() throws BadgeInvalideException {
        System.out.println("\n+---------------------------------+");
        System.out.println("|   BLOQUER / DEBLOQUER UN BADGE  |");
        System.out.println("+---------------------------------+");
        
        System.out.println("\n  1. Bloquer un badge");
        System.out.println("  2. Debloquer un badge");
        
        System.out.print("Choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Numero du badge: ");
        int numBadge = scanner.nextInt();
        scanner.nextLine();
        
        if (choix == 1) {
            System.out.print("Raison du blocage: ");
            String raison = scanner.nextLine().trim();
            service.bloquerBadge(numBadge, raison);
            System.out.println("\n[OK] Badge No " + numBadge + " bloque avec succes!");
        } else if (choix == 2) {
            service.debloquerBadge(numBadge);
            System.out.println("\n[OK] Badge No " + numBadge + " debloque avec succes!");
        } else {
            System.out.println("[ERREUR] Choix invalide!");
        }
    }
    
    private void afficherHistorique() {
        System.out.println("\n+---------------------------------+");
        System.out.println("|    HISTORIQUE DES ACCES         |");
        System.out.println("+---------------------------------+");
        
        List<AccesLog> logs = service.getHistoriqueAccesTrieParDate();
        
        if (logs.isEmpty()) {
            System.out.println("\n[INFO] Aucun acces enregistre.");
            return;
        }
        
        System.out.println("\n" + SEPARATOR);
        for (AccesLog log : logs) {
            String icon = log.estAutorise() ? "[OK]" : "[NO]";
            System.out.println(icon + " " + log.getUtilisateur() + " -> " + 
                    log.getZone() + " - " + log.getResultat().getLibelle() + 
                    " (" + log.getDateHeure() + ")");
        }
        System.out.println(SEPARATOR);
        System.out.println("Total: " + logs.size() + " acces");
    }
    
    private void afficherRapports() {
        System.out.println(service.genererRapport());
    }
    
    private void listerUtilisateurs() {
        System.out.println("\n+---------------------------------+");
        System.out.println("|    LISTE DES UTILISATEURS       |");
        System.out.println("+---------------------------------+");
        
        System.out.println("\n  1. Tri par niveau d'acces");
        System.out.println("  2. Tri par nom");
        
        System.out.print("Choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        List<UtilisateurCAN> liste;
        
        if (choix == 2) {
            liste = service.getUtilisateurs().stream()
                    .sorted((u1, u2) -> u1.getNom().compareTo(u2.getNom()))
                    .toList();
        } else {
            liste = service.getUtilisateursTriesParNiveauAcces();
        }
        
        if (liste.isEmpty()) {
            System.out.println("\n[INFO] Aucun utilisateur enregistre.");
            return;
        }
        
        System.out.println("\n" + SEPARATOR);
        for (UtilisateurCAN u : liste) {
            System.out.println("ID " + u.getId() + ": " + u.getNom() + 
                    " (" + u.getTypeDescription() + ", Niveau " + u.getNiveauAcces() + ")");
        }
        System.out.println(SEPARATOR);
        System.out.println("Total: " + liste.size() + " utilisateurs");
    }
    
    private void listerZones() {
        System.out.println("\n+---------------------------------+");
        System.out.println("|    LISTE DES ZONES              |");
        System.out.println("+---------------------------------+");
        
        List<ZoneSecurisee> zones = service.getZones();
        
        if (zones.isEmpty()) {
            System.out.println("\n[INFO] Aucune zone enregistree.");
            return;
        }
        
        System.out.println("\n" + SEPARATOR);
        for (ZoneSecurisee z : zones) {
            System.out.println(z.getIdZone() + " - " + z.getNomZone() + 
                    " (Niveau " + z.getNiveauRequis() + ")");
        }
        System.out.println(SEPARATOR);
        System.out.println("Total: " + zones.size() + " zones");
    }
    
    private void listerBadges() {
        System.out.println("\n+---------------------------------+");
        System.out.println("|    LISTE DES BADGES             |");
        System.out.println("+---------------------------------+");
        
        List<Badge> badges = service.getBadges();
        
        if (badges.isEmpty()) {
            System.out.println("\n[INFO] Aucun badge enregistre.");
            return;
        }
        
        System.out.println("\n" + SEPARATOR);
        for (Badge b : badges) {
            String statut;
            if (b.estBloque()) {
                statut = "[BLOQUE]";
            } else if (b.estExpire()) {
                statut = "[EXPIRE]";
            } else if (b.estBadgeValide()) {
                statut = "[VALIDE]";
            } else {
                statut = "[INVALIDE]";
            }
            
            System.out.println("Badge No " + b.getNumeroBadge() + ": " + 
                    b.getUtilisateur().getNom() + " " + statut + 
                    " (Du " + b.getDateDebut() + " au " + b.getDateFin() + ")");
        }
        System.out.println(SEPARATOR);
        
        long actifs = badges.stream().filter(Badge::estBadgeValide).count();
        long bloques = badges.stream().filter(Badge::estBloque).count();
        long expires = badges.stream().filter(Badge::estExpire).count();
        
        System.out.println("Total: " + badges.size() + " badges");
        System.out.println("  - Actifs: " + actifs);
        System.out.println("  - Bloques: " + bloques);
        System.out.println("  - Expires: " + expires);
    }
    

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez jj/mm/aaaa");
        }
    }
    
}