package can2025.models;


public enum StatutBadge {
    ACTIF("Actif"),
    BLOQUE("Bloqué"),
    EXPIRE("Expiré"),
    ANNULE("Annulé");
    
    private final String libelle;
    
    StatutBadge(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
