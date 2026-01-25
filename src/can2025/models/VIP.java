package can2025.models;

public class VIP extends UtilisateurCAN {
    
    private String organisation;
    private String titre;

 
    public VIP(String nom) {
        super(nom, CategorieUtilisateur.VIP);
    }
    
 
    public VIP(String nom, String organisation, String titre) {
        super(nom,  CategorieUtilisateur.VIP);
        this.organisation = organisation;
        this.titre = titre;
    }
    
    @Override
    public int getNiveauAcces() {
        return 3;
    }
    
    @Override
    public String getTypeDescription() {
        return "VIP";
    }
    
    public String getOrganisation() {
        return organisation;
    }
    
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    @Override
    public String toString() {
        String base = super.toString();
        StringBuilder sb = new StringBuilder(base);
        if (titre != null) {
            sb.append(" | Titre: ").append(titre);
        }
        if (organisation != null) {
            sb.append(" | Organisation: ").append(organisation);
        }
        return sb.toString();
    }
}
