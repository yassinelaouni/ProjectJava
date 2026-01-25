package can2025.models;

public enum ResultatAcces {
    OK("Acces autorise"),
    REFUS_NIVEAU("Acces refuse - Niveau insuffisant"),
    REFUS_BADGE_INVALIDE("Acces refuse - Badge invalide"),
    REFUS_BADGE_BLOQUE("Acces refuse - Badge bloque"),
    REFUS_BADGE_EXPIRE("Acces refuse - Badge expire"),
    REFUS_ZONE_INACTIVE("Acces refuse - Zone inactive"),
    REFUS_SANS_BADGE("Acces refuse - Pas de badge");
    
    private final String libelle;
    
    ResultatAcces(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public boolean estAutorise() {
        return this == OK;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
