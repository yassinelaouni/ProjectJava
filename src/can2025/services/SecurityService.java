package can2025.services;

import can2025.exceptions.*;
import can2025.models.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class SecurityService {
    
    // Collections principales
    private ArrayList<UtilisateurCAN> utilisateurs;
    private ArrayList<Badge> badges;
    private ArrayList<ZoneSecurisee> zones;
    private ArrayList<AccesLog> logsAcces;
    
    // HashMap pour accès rapide
    private HashMap<Integer, UtilisateurCAN> utilisateursParId;
    private HashMap<Integer, Badge> badgesParNumero;
    private HashMap<String, ZoneSecurisee> zonesParId;
    
    public SecurityService() {
        this.utilisateurs = new ArrayList<>();
        this.badges = new ArrayList<>();
        this.zones = new ArrayList<>();
        this.logsAcces = new ArrayList<>();
        
        this.utilisateursParId = new HashMap<>();
        this.badgesParNumero = new HashMap<>();
        this.zonesParId = new HashMap<>();
    }
    
    // ==================== GESTION DES UTILISATEURS ====================
    private void ajouterUtilisateur(UtilisateurCAN utilisateur) throws DonneeInvalideException {
        if (utilisateur == null) {
            throw new DonneeInvalideException("L'utilisateur ne peut pas etre null");
        }
        if (utilisateur.getNom() == null || utilisateur.getNom().trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom ne peut pas etre vide");
        }
        
        utilisateurs.add(utilisateur);
        utilisateursParId.put(utilisateur.getId(), utilisateur);
    }
    
    public void creerSpectateur(String nom) throws DonneeInvalideException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom ne peut pas etre vide");
        }
        Spectateur spectateur = new Spectateur(nom);
        ajouterUtilisateur(spectateur);
    }
    
    public void creerStaff(String nom, CategorieUtilisateur categorie) throws DonneeInvalideException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom ne peut pas etre vide");
        }
        Staff staff = new Staff(nom, categorie);
        ajouterUtilisateur(staff);
    }
    
    public void creerVIP(String nom) throws DonneeInvalideException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom ne peut pas etre vide");
        }
        VIP vip = new VIP(nom);
        ajouterUtilisateur(vip);
    }
    
    public List<UtilisateurCAN> getUtilisateursTriesParNiveauAcces() {
        return utilisateurs.stream()
                .sorted(Comparator.comparingInt(UtilisateurCAN::getNiveauAcces).reversed())
                .collect(Collectors.toList());
    }
    
    // ==================== GESTION DES ZONES ====================
    
    public void creerZone(String idZone, String nomZone, int niveauRequis) 
            throws DonneeInvalideException {
        if (idZone == null || idZone.trim().isEmpty()) {
            throw new DonneeInvalideException("L'ID de la zone ne peut pas etre vide");
        }
        if (nomZone == null || nomZone.trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom de la zone ne peut pas etre vide");
        }
        if (niveauRequis < 1 || niveauRequis > 3) {
            throw new DonneeInvalideException("Le niveau doit etre entre 1 et 3");
        }
        
        ZoneSecurisee zone = new ZoneSecurisee(idZone, nomZone, niveauRequis);
        zones.add(zone);
        zonesParId.put(idZone, zone);
    }
    
    public ZoneSecurisee rechercherZoneParId(String idZone) throws ZoneInterditeException {
        ZoneSecurisee zone = zonesParId.get(idZone);
        if (zone == null) {
            throw new ZoneInterditeException("Zone introuvable: " + idZone);
        }
        return zone;
    }
    
    // ==================== GESTION DES BADGES ====================
    public void genererBadge(int utilisateurId, LocalDate dateDebut, LocalDate dateFin) 
            throws DonneeInvalideException, BadgeInvalideException {
        
        UtilisateurCAN utilisateur = utilisateursParId.get(utilisateurId);
        if (utilisateur == null) {
            throw new DonneeInvalideException("Utilisateur non trouve");
        }
        
        if (dateDebut == null || dateFin == null) {
            throw new DonneeInvalideException("Les dates sont obligatoires");
        }
        
        if (dateFin.isBefore(dateDebut)) {
            throw new DonneeInvalideException("La date de fin doit etre apres la date de debut");
        }
        
        Badge badge = new Badge(utilisateur, dateDebut, dateFin);
        badges.add(badge);
        badgesParNumero.put(badge.getNumeroBadge(), badge);
    }
    
    public Badge rechercherBadgeParNumero(int numeroBadge) throws BadgeInvalideException {
        Badge badge = badgesParNumero.get(numeroBadge);
        if (badge == null) {
            throw new BadgeInvalideException("Badge introuvable: " + numeroBadge);
        }
        return badge;
    }
    
    public void bloquerBadge(int numeroBadge, String raison) throws BadgeInvalideException {
        Badge badge = rechercherBadgeParNumero(numeroBadge);
        badge.bloquer(raison);
    }
    
    public void debloquerBadge(int numeroBadge) throws BadgeInvalideException {
        Badge badge = rechercherBadgeParNumero(numeroBadge);
        badge.debloquer();
    }
    
    public List<Badge> getBadgesBloques() {
        return badges.stream()
                .filter(Badge::estBloque)
                .collect(Collectors.toList());
    }
    
    // ==================== CONTRÔLE D'ACCÈS ====================
    public void verifierEtEnregistrerAcces(int numeroBadge, String idZone) 
            throws BadgeInvalideException, ZoneInterditeException, AccesRefuseException {
        
        Badge badge = rechercherBadgeParNumero(numeroBadge);
        ZoneSecurisee zone = rechercherZoneParId(idZone);
        
        // Évaluer l'accès
        if (badge.estBloque()) {
            AccesLog log = new AccesLog(badge, zone, ResultatAcces.REFUS_BADGE_BLOQUE);
            logsAcces.add(log);
            throw new AccesRefuseException("Badge bloque");
        }
        
        if (badge.estExpire()) {
            AccesLog log = new AccesLog(badge, zone, ResultatAcces.REFUS_BADGE_EXPIRE);
            logsAcces.add(log);
            throw new AccesRefuseException("Badge expire");
        }
        
        UtilisateurCAN utilisateur = badge.getUtilisateur();
        if (utilisateur.getNiveauAcces() < zone.getNiveauRequis()) {
            AccesLog log = new AccesLog(badge, zone, ResultatAcces.REFUS_NIVEAU);
            logsAcces.add(log);
            throw new AccesRefuseException("Niveau d'acces insuffisant");
        }
        
        // Accès autorisé
        AccesLog log = new AccesLog(badge, zone, ResultatAcces.OK);
        logsAcces.add(log);
    }
    
    public AccesLog tenterAcces(int numeroBadge, String idZone) {
        Badge badge = badgesParNumero.get(numeroBadge);
        ZoneSecurisee zone = zonesParId.get(idZone);
        
        ResultatAcces resultat;
        
        if (badge == null) {
            resultat = ResultatAcces.REFUS_BADGE_INVALIDE;
        } else if (zone == null) {
            resultat = ResultatAcces.REFUS_ZONE_INACTIVE;
        } else if (badge.estBloque()) {
            resultat = ResultatAcces.REFUS_BADGE_BLOQUE;
        } else if (badge.estExpire()) {
            resultat = ResultatAcces.REFUS_BADGE_EXPIRE;
        } else if (badge.getUtilisateur().getNiveauAcces() < zone.getNiveauRequis()) {
            resultat = ResultatAcces.REFUS_NIVEAU;
        } else {
            resultat = ResultatAcces.OK;
        }
        
        AccesLog log = new AccesLog(badge, zone, resultat);
        logsAcces.add(log);
        return log;
    }
    
    // ==================== HISTORIQUE ET RAPPORTS ====================
    public List<AccesLog> getHistoriqueAccesTrieParDate() {
        return logsAcces.stream()
                .sorted(Comparator.comparing(AccesLog::getDateHeure).reversed())
                .collect(Collectors.toList());
    }
    
    public double getTauxRefus() {
        if (logsAcces.isEmpty()) {
            return 0.0;
        }
        long refus = logsAcces.stream()
                .filter(log -> !log.estAutorise())
                .count();
        return (refus * 100.0) / logsAcces.size();
    }
    
    public Map<String, int[]> getStatistiquesParZone() {
        Map<String, int[]> stats = new HashMap<>();
        
        for (ZoneSecurisee zone : zones) {
            int total = 0, autorises = 0, refuses = 0;
            
            for (AccesLog log : logsAcces) {
                if (log.getZone() != null && log.getZone().equals(zone)) {
                    total++;
                    if (log.estAutorise()) {
                        autorises++;
                    } else {
                        refuses++;
                    }
                }
            }
            
            stats.put(zone.getIdZone(), new int[]{total, autorises, refuses});
        }
        
        return stats;
    }
    
    public String genererRapport() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n");
        sb.append("==============================================================\n");
        sb.append("           RAPPORT DE SECURITE - CAN 2025                    \n");
        sb.append("==============================================================\n\n");
        
        // 1. Taux de refus
        sb.append("--------------------------------------------------------------\n");
        sb.append("TAUX DE REFUS GLOBAL                                         \n");
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("Taux de refus : %.2f %%\n\n", getTauxRefus()));
        
        // 2. Badges bloques
        List<Badge> badgesBloques = getBadgesBloques();
        sb.append("--------------------------------------------------------------\n");
        sb.append("BADGES BLOQUES                                               \n");
        sb.append("--------------------------------------------------------------\n");
        if (badgesBloques.isEmpty()) {
            sb.append("Aucun badge bloque\n");
        } else {
            for (Badge badge : badgesBloques) {
                sb.append(String.format("Badge No %d - %s\n", 
                        badge.getNumeroBadge(),
                        badge.getUtilisateur() != null ? badge.getUtilisateur().getNom() : "?"));
                if (badge.getRaisonBlocage() != null) {
                    sb.append(String.format("  Raison: %s\n", badge.getRaisonBlocage()));
                }
            }
        }
        sb.append("\n");
        
        // 3. Zones les plus sensibles
        sb.append("--------------------------------------------------------------\n");
        sb.append("ZONES LES PLUS SENSIBLES (par nombre de refus)               \n");
        sb.append("--------------------------------------------------------------\n");
        Map<String, int[]> statsZones = getStatistiquesParZone();
        List<Map.Entry<String, int[]>> sortedStats = statsZones.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue()[2], e1.getValue()[2]))
                .collect(Collectors.toList());
        
        int count = 0;
        for (Map.Entry<String, int[]> entry : sortedStats) {
            if (count >= 5) break;
            ZoneSecurisee zone = zonesParId.get(entry.getKey());
            int[] vals = entry.getValue();
            if (vals[2] > 0) {
                sb.append(String.format("%s: %d refus / %d tentatives\n", 
                        zone != null ? zone.getNomZone() : "Zone inconnue",
                        vals[2], vals[0]));
                count++;
            }
        }
        
        if (count == 0) {
            sb.append("Aucune statistique disponible\n");
        }
        sb.append("==============================================================\n");
        
        return sb.toString();
    }
    
    // ==================== GETTERS ====================
    
    public ArrayList<UtilisateurCAN> getUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }
    
    public ArrayList<Badge> getBadges() {
        return new ArrayList<>(badges);
    }
    
    public ArrayList<ZoneSecurisee> getZones() {
        return new ArrayList<>(zones);
    }
    
    public ArrayList<AccesLog> getLogsAcces() {
        return new ArrayList<>(logsAcces);
    }
    
}