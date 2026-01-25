package can2025.models;

public class Staff extends UtilisateurCAN {
    
    private String fonction;
    private String departement;
    private boolean estSecurite;
    
    public Staff(String nom, CategorieUtilisateur categorie) {
        super(nom, categorie);
        this.estSecurite = (categorie == CategorieUtilisateur.STAFF);
    }
    
    public Staff(String nom, CategorieUtilisateur categorie, String fonction, String departement) {
        super(nom, categorie);
        this.fonction = fonction;
        this.departement = departement;
        this.estSecurite = (categorie == CategorieUtilisateur.STAFF);
    }
    
    @Override
    public int getNiveauAcces() {
        if (estSecurite) {
            return 3;
        }
        return categorie.getNiveauAccesBase();
    }
    
    @Override
    public String getTypeDescription() {
        return "STAFF";
    }
    
    public boolean peutGererBadges() {
        return estSecurite;
    }
    
    public String getFonction() {
        return fonction;
    }
    
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
    
    public String getDepartement() {
        return departement;
    }
    
    public void setDepartement(String departement) {
        this.departement = departement;
    }
    
    public boolean isEstSecurite() {
        return estSecurite;
    }
    
    @Override
    public String toString() {
        String base = super.toString();
        StringBuilder sb = new StringBuilder(base);
        if (fonction != null) {
            sb.append(" | Fonction: ").append(fonction);
        }
        if (departement != null) {
            sb.append(" | DDepartement: ").append(departement);
        }
        if (estSecurite) {
            sb.append(" | [SECURITE]");
        }
        return sb.toString();
    }
}
