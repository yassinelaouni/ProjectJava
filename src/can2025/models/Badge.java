package can2025.models;

import can2025.interfaces.Accreditable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Badge implements Accreditable {
    
    private static int compteurBadge = 0;
    
    private int numeroBadge;
    private UtilisateurCAN utilisateur;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutBadge statut;
    private LocalDate dateCreation;
    private String raisonBlocage;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public Badge(UtilisateurCAN utilisateur, LocalDate dateDebut, LocalDate dateFin) {
        this.numeroBadge = ++compteurBadge;
        this.utilisateur = utilisateur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = StatutBadge.ACTIF;
        this.dateCreation = LocalDate.now();
    }
    
    public Badge(UtilisateurCAN utilisateur, int dureeJours) {
        this(utilisateur, LocalDate.now(), LocalDate.now().plusDays(dureeJours));
    }
    
    @Override
    public boolean genererBadge() { 
        if (utilisateur != null && dateDebut != null && dateFin != null) {
            this.statut = StatutBadge.ACTIF;
            utilisateur.setBadge(this);
            return true;
        }
        return false;
    }
    
    @Override
    public void invaliderBadge() {
        this.statut = StatutBadge.ANNULE;
        if (utilisateur != null) {
            utilisateur.setBadge(null);
        }
    }
    
    @Override
    public boolean estBadgeValide() {
        if (statut == StatutBadge.BLOQUE || statut == StatutBadge.ANNULE) {
            return false;
        }
        
        LocalDate aujourdhui = LocalDate.now();
        
        // Vérifier si le badge est expiré
        if (dateFin != null && aujourdhui.isAfter(dateFin)) {
            this.statut = StatutBadge.EXPIRE;
            return false;
        }
        
        // Vérifier si le badge n'est pas encore valide
        if (dateDebut != null && aujourdhui.isBefore(dateDebut)) {
            return false;
        }
        
        return statut == StatutBadge.ACTIF;
    }
    
    public void bloquer(String raison) {
        this.statut = StatutBadge.BLOQUE;
        this.raisonBlocage = raison;
    }
    
    public void debloquer() {
        if (statut == StatutBadge.BLOQUE) {
            // Vérifier si le badge n'est pas expiré
            if (dateFin != null && LocalDate.now().isAfter(dateFin)) {
                this.statut = StatutBadge.EXPIRE;
            } else {
                this.statut = StatutBadge.ACTIF;
            }
            this.raisonBlocage = null;
        }
    }
    
    public boolean estBloque() {
        return statut == StatutBadge.BLOQUE;
    }
    
    public boolean estExpire() {
        if (dateFin != null && LocalDate.now().isAfter(dateFin)) {
            this.statut = StatutBadge.EXPIRE;
            return true;
        }
        return statut == StatutBadge.EXPIRE;
    }
    
    public int getNumeroBadge() {
        return numeroBadge;
    }
    
    public UtilisateurCAN getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(UtilisateurCAN utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public StatutBadge getStatut() {
        return statut;
    }
    
    public void setStatut(StatutBadge statut) {
        this.statut = statut;
    }
    
    public LocalDate getDateCreation() {
        return dateCreation;
    }
    
    public String getRaisonBlocage() {
        return raisonBlocage;
    }
    
    public static void resetCompteur() {
        compteurBadge = 0;
    }
    

    public static int getCompteurActuel() {
        return compteurBadge;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Badge N°%d | Utilisateur: %s | Statut: %s",
                numeroBadge,
                utilisateur != null ? utilisateur.getNom() : "Non assigne",
                statut.getLibelle()));
        
        sb.append(String.format(" | Validite: %s - %s",
                dateDebut != null ? dateDebut.format(FORMATTER) : "N/A",
                dateFin != null ? dateFin.format(FORMATTER) : "N/A"));
        
        if (raisonBlocage != null && !raisonBlocage.isEmpty()) {
            sb.append(" | Raison blocage: ").append(raisonBlocage);
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Badge badge = (Badge) obj;
        return numeroBadge == badge.numeroBadge;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(numeroBadge);
    }
}
