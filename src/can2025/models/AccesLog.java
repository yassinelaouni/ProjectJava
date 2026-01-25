package can2025.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AccesLog {
    
    private static int compteurLog = 0;
    
    private int idLog;
    private LocalDateTime dateHeure;
    private Badge badge;
    private ZoneSecurisee zone;
    private ResultatAcces resultat;
    private String details;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    

    public AccesLog(Badge badge, ZoneSecurisee zone, ResultatAcces resultat) {
        this.idLog = ++compteurLog;
        this.dateHeure = LocalDateTime.now();
        this.badge = badge;
        this.zone = zone;
        this.resultat = resultat;
    }
    
    public AccesLog(Badge badge, ZoneSecurisee zone, ResultatAcces resultat, String details) {
        this(badge, zone, resultat);
        this.details = details;
    }
    

    public AccesLog(LocalDateTime dateHeure, Badge badge, ZoneSecurisee zone, ResultatAcces resultat) {
        this.idLog = ++compteurLog;
        this.dateHeure = dateHeure;
        this.badge = badge;
        this.zone = zone;
        this.resultat = resultat;
    }
    
    public int getIdLog() {
        return idLog;
    }
    
    public LocalDateTime getDateHeure() {
        return dateHeure;
    }
    
    public Badge getBadge() {
        return badge;
    }
    
    public ZoneSecurisee getZone() {
        return zone;
    }
    
    public ResultatAcces getResultat() {
        return resultat;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    

    public boolean estAutorise() {
        return resultat.estAutorise();
    }
    

    public UtilisateurCAN getUtilisateur() { 
        return badge != null ? badge.getUtilisateur() : null;
    }
    

    public static void resetCompteur() {
        compteurLog = 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] ", dateHeure.format(FORMATTER)));
        
        if (badge != null && badge.getUtilisateur() != null) {
            sb.append(String.format("Badge N°%d (%s) ", 
                    badge.getNumeroBadge(), 
                    badge.getUtilisateur().getNom()));
        } else {
            sb.append("Badge inconnu ");
        }
        
        sb.append(String.format("-> Zone [%s] : %s",
                zone != null ? zone.getIdZone() : "?",
                resultat.getLibelle()));
        
        if (details != null && !details.isEmpty()) {
            sb.append(" (").append(details).append(")");
        }
        
        return sb.toString();
    }
    

    public String toShortString() {
        return String.format("%s | %s | %s | %s",
                dateHeure.format(FORMATTER),
                badge != null ? "Badge " + badge.getNumeroBadge() : "N/A",
                zone != null ? zone.getIdZone() : "N/A",
                resultat.estAutorise() ? "OK" : "REFUS");
    }
}
