package can2025.models;


public enum CategorieUtilisateur {
    SPECTATEUR("Spectateur", 1),
    STAFF("Staff", 2),
    VIP("VIP", 3);
    
    
    private final String libelle;
    private final int niveauAccesBase;
    
    CategorieUtilisateur(String libelle, int niveauAccesBase) {
        this.libelle = libelle;
        this.niveauAccesBase = niveauAccesBase;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public int getNiveauAccesBase() {
        return niveauAccesBase;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
