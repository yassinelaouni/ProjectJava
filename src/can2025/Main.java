package can2025;

import can2025.exceptions.*;
import can2025.models.*;
import can2025.services.SecurityService;
import can2025.ui.ConsoleUI;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class Main {
    
    private static SecurityService service;
    
    public static void main(String[] args) {
        System.out.println("\n" + "================================================================");
        System.out.println("      SYSTEME DE GESTION DE SECURITE ET D'ACCREDITATION - CAN 2025 ");
        System.out.println("==================================================================");
        
        service = new SecurityService();
        
        System.out.println("\n  EXECUTION DES SCENARIOS DE TEST");
        System.out.println("------------------------------------------------------------------");
        
        try {
            // 1. Créer les zones
            creerZonesTest();
            
            // 2. Créer les utilisateurs
            creerUtilisateursTest();
            
            // 3. Générer les badges
            genererBadgesTest();
            
            // 4. Simuler les tentatives d'accès
            simulerAccesTest();
            
            // 5. Bloquer des badges et tester
            testerBlocageBadges();
            
            // 6. Provoquer les erreurs
            provoquerErreurs();
            
            // 7. Afficher le rapport final
            afficherRapportFinal();
            
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Demander si l'utilisateur veut lancer l'interface interactive
        System.out.println("\n" + "===============================================================");
        System.out.println("Tests termines. Voulez-vous lancer l'interface interactive? (O/N)");
        
        Scanner scanner = new Scanner(System.in);
        String reponse = scanner.nextLine().trim().toUpperCase();
        
        if (reponse.equals("O") || reponse.equals("OUI")) {
            ConsoleUI ui = new ConsoleUI(service);
            ui.lancer();
        }
    }
    
    private static void creerZonesTest() throws DonneeInvalideException {
        System.out.println("\n[INFO] CREATION DES ZONES SECURISEES (3 niveaux)");
        System.out.println("-".repeat(50));
        // Zones de niveau 1 (Spectateur)
        service.creerZone("Z01", "Tribune Sud", 1);
        service.creerZone("Z02", "Tribune Nord", 1);
        // Zones de niveau 2 (Staff)
        service.creerZone("Z03", "Zone Presse", 2);
        service.creerZone("Z04", "Zone Technique", 2);
        // Zones de niveau 3 (VIP)
        service.creerZone("Z05", "Vestiaires", 3);
        service.creerZone("Z06", "Salle de controle", 3);
        service.creerZone("Z07", "Lounge VIP", 3);
        // Plus de niveau 4 ou 5
        for (ZoneSecurisee zone : service.getZones()) {
            System.out.println("  [OK] " + zone);
        }
        System.out.println("\n  Total: " + service.getZones().size() + " zones creees");
    }
    

    private static void creerUtilisateursTest() throws DonneeInvalideException {
        System.out.println("\n  CREATION DES UTILISATEURS (20 utilisateurs)");
        System.out.println("----------------------------------------------------------");
        
        // 8 Spectateurs
        service.creerSpectateur("Ahmed Ben Ali");
        service.creerSpectateur("Fatima Ouattara");
        service.creerSpectateur("Mohamed Diallo");
        service.creerSpectateur("Aminata Traore");
        service.creerSpectateur("Omar Sy");
        service.creerSpectateur("Aya Nakamura");
        service.creerSpectateur("Koffi Annan");
        service.creerSpectateur("Mariama Balde");
        
        // 8 Staff
        service.creerStaff("Jean Dupont", CategorieUtilisateur.STAFF);
        service.creerStaff("Marie Martin", CategorieUtilisateur.STAFF);
        service.creerStaff("Ibrahima Keita", CategorieUtilisateur.STAFF);
        service.creerStaff("Aissatou Camara", CategorieUtilisateur.STAFF);
        service.creerStaff("Paul Ndong", CategorieUtilisateur.STAFF);
        service.creerStaff("Sophie Mbeki", CategorieUtilisateur.STAFF);
        service.creerStaff("Dr. Moussa Fall", CategorieUtilisateur.STAFF);
        service.creerStaff("Dr. Fatoumata Ba", CategorieUtilisateur.STAFF);
        
        // 4 VIP
        service.creerVIP("Patrice Motsepe");       
        service.creerVIP("Samuel Eto'o");          
        service.creerVIP("Gianni Infantino");       
        service.creerVIP("Macky Sall");              

        System.out.println("\n  Total: " + service.getUtilisateurs().size() + " utilisateurs crees");
        
        // Afficher quelques utilisateurs en exemple
        System.out.println("\n  Exemples d'utilisateurs:");
        List<UtilisateurCAN> utilisateurs = service.getUtilisateursTriesParNiveauAcces();
        boolean printedSpectateur = false, printedVIP = false, printedStaff = false;
        int printed = 0;

        for (UtilisateurCAN u : utilisateurs) {
            String type = u.getTypeDescription();
            if (!printedSpectateur && type.equalsIgnoreCase("Spectateur")) {
                System.out.println("    - " + type + ": " + u.getNom() + " (Niveau " + u.getNiveauAcces() + ")");
                printedSpectateur = true;
                printed++;
            } else if (!printedVIP && type.equalsIgnoreCase("VIP")) {
                System.out.println("    - " + type + ": " + u.getNom() + " (Niveau " + u.getNiveauAcces() + ")");
                printedVIP = true;
                printed++;
            } else if (!printedStaff && type.equalsIgnoreCase("Staff")) {
                System.out.println("    - " + type + ": " + u.getNom() + " (Niveau " + u.getNiveauAcces() + ")");
                printedStaff = true;
                printed++;
            }
            if (printed >= 3) break;
        }

        // Afficher les 2 suivants, peu importe le type
        int extraPrinted = 0;
        for (UtilisateurCAN u : utilisateurs) {
            String type = u.getTypeDescription();
            if ((type.equalsIgnoreCase("Spectateur") && printedSpectateur) ||
                (type.equalsIgnoreCase("VIP") && printedVIP) ||
                (type.equalsIgnoreCase("Staff") && printedStaff)) {
                continue; // Already printed one of each
            }
            System.out.println("    - " + type + ": " + u.getNom() + " (Niveau " + u.getNiveauAcces() + ")");
            extraPrinted++;
            if (extraPrinted >= 2) break;
        }
    }

    private static void genererBadgesTest() throws DonneeInvalideException, BadgeInvalideException {
        System.out.println("\n  GENERATION DES BADGES");
        System.out.println("-".repeat(50));
        
        LocalDate aujourdhui = LocalDate.now();
        // Badges valides
        LocalDate debutValide = aujourdhui;
        LocalDate finValide = aujourdhui.plusDays(60);
        // Badges expirés
        LocalDate passeDebut = aujourdhui.minusDays(60);
        LocalDate passeFin = aujourdhui.minusDays(10);
        
        int badgesValides = 0;
        int badgesExpires = 0;
        
        List<UtilisateurCAN> utilisateurs = service.getUtilisateurs();
        
        for (int i = 0; i < utilisateurs.size(); i++) {
            UtilisateurCAN u = utilisateurs.get(i);
            
            if (i % 5 == 0) {
                // Badge expiré
                service.genererBadge(u.getId(), passeDebut, passeFin);
                badgesExpires++;
            } else {
                // Badge valide
                service.genererBadge(u.getId(), debutValide, finValide);
                badgesValides++;
            }
        }
        
        System.out.println("  Badges valides generes: " + badgesValides);
        System.out.println("  Badges expires generes: " + badgesExpires);
        System.out.println("\n  Total: " + service.getBadges().size() + " badges generes");
        
        System.out.println("\n  Exemples de badges:");
        List<Badge> badges = service.getBadges();
        for (int i = 0; i < Math.min(5, badges.size()); i++) {
            Badge b = badges.get(i);
            String statut = b.estBadgeValide() ? "[OK] Valide" : "[EXPIRED] Expire";
            System.out.println("    - Badge No" + b.getNumeroBadge() + ": " + 
                    b.getUtilisateur().getNom() + " [" + statut + "]");
        }
    }
    
    private static void simulerAccesTest() {
        System.out.println("\n  SIMULATION QUELQUES TENTATIVES D'ACCES ");
        System.out.println("----------------------------------------------------------");
        
        int accesOK = 0;
        int accesRefus = 0;
        
        List<Badge> badges = service.getBadges();
        List<ZoneSecurisee> zones = service.getZones();
        
        System.out.println("\n  Tentatives d'acces:");
        
        // Tentatives variées
        for (int i = 0; i < badges.size(); i++) {
            Badge badge = badges.get(i);
            
            // Chaque utilisateur tente d'accéder à plusieurs zones
            for (int j = 0; j < zones.size(); j++) {
                if ((i * zones.size() + j) >= 30) break;
                
                ZoneSecurisee zone = zones.get(j);
                AccesLog log = service.tenterAcces(badge.getNumeroBadge(), zone.getIdZone());
                
                String icon = log.estAutorise() ? "[OK]" : "[NO]";
                System.out.println("    " + icon + " Badge No" + badge.getNumeroBadge() + 
                        " (" + badge.getUtilisateur().getNom() + ") -> " + 
                        zone.getNomZone() + ": " + log.getResultat().getLibelle());
                
                if (log.estAutorise()) {
                    accesOK++;
                } else {
                    accesRefus++;
                }
            }
        }
        
        System.out.println("\n  Resume des acces:");
        System.out.println("    - Acces autorises: " + accesOK);
        System.out.println("    - Acces refuses: " + accesRefus);
        System.out.println("    - Total: " + service.getLogsAcces().size() + " tentatives");
        System.out.printf("    - Taux de refus: %.2f%%\n", service.getTauxRefus());
    }
    
    private static void testerBlocageBadges() throws BadgeInvalideException {
        System.out.println("\n  TEST DU BLOCAGE DE BADGES");
        System.out.println("----------------------------------------------------------");
        
        List<Badge> badges = service.getBadges();
        
        // Bloquer 3 badges
        int[] badgesABloquer = {2, 5, 8};
        
        for (int numBadge : badgesABloquer) {
            if (numBadge <= badges.size()) {
                service.bloquerBadge(numBadge, "Comportement suspect - Test de securite");
                System.out.println("  Badge No" + numBadge + " bloque");
            }
        }
        
        // Tenter des acces avec les badges bloques
        System.out.println("\n  Test d'acces avec badges bloques:");
        
        for (int numBadge : badgesABloquer) {
            if (numBadge <= badges.size()) {
                AccesLog log = service.tenterAcces(numBadge, "Z01");
                System.out.println("    Badge No" + numBadge + " -> " + 
                        log.getResultat().getLibelle());
            }
        }
        
        System.out.println("\n  Badges bloques actuellement: " + service.getBadgesBloques().size());
        System.out.println("----------------------------------------------------------");
    }
    
    private static void provoquerErreurs() {
        System.out.println("\n   TEST DES ERREURS (4 types d'exceptions)");
        System.out.println("----------------------------------------------------------");
        
        int erreursProvoquees = 0;
        
        // 1. BadgeInvalideException - Badge introuvable
        System.out.println("\n  1. Test BadgeInvalideException (badge introuvable):");
        try {
            service.rechercherBadgeParNumero(9999);
        } catch (BadgeInvalideException e) {
            System.out.println("    Exception capturee: " + e.getMessage());
            erreursProvoquees++;
        }
        
        // 2. ZoneInterditeException - Zone inexistante
        System.out.println("\n  2. Test ZoneInterditeException (zone inexistante):");
        try {
            service.rechercherZoneParId("ZONE_NULL");
        } catch (ZoneInterditeException e) {
            System.out.println("    Exception capturee: " + e.getMessage());
            erreursProvoquees++;
        }
        
        // 3. AccesRefuseException - Niveau insuffisant
        System.out.println("\n  3. Test AccesRefuseException (niveau insuffisant):");
        try {
            Badge badgeSpectateur = service.getBadges().stream()
                    .filter(b -> b.getUtilisateur() instanceof Spectateur && b.estBadgeValide())
                    .findFirst()
                    .orElse(null);
            
            if (badgeSpectateur != null) {
                service.verifierEtEnregistrerAcces(badgeSpectateur.getNumeroBadge(), "Z07");
            }
        } catch (AccesRefuseException e) {
            System.out.println("    Exception capturee: " + e.getMessage());
            erreursProvoquees++;
        } catch (Exception e) {
            System.out.println("    Autre exception: " + e.getMessage());
        }
        
        // 4. DonneeInvalideException - Données invalides
        System.out.println("\n  4. Test DonneeInvalideException (nom vide):");
        try {
            service.creerSpectateur("");
        } catch (DonneeInvalideException e) {
            System.out.println("    Exception capturee: " + e.getMessage());
            erreursProvoquees++;
        }
    }
    
    private static void afficherRapportFinal() {
        System.out.println("\n" + "===============================================================");
        System.out.println("                    RAPPORT FINAL");
        System.out.println("=========================================================================");
        
        System.out.println(service.genererRapport());
        
    }
}