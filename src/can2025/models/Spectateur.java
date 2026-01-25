package can2025.models;

public class Spectateur extends UtilisateurCAN {
    
    private String numeroPlace;
    private String tribune;
    
    public Spectateur(String nom) {
        super(nom, CategorieUtilisateur.SPECTATEUR);
    }
    
    public Spectateur(String nom, String numeroPlace, String tribune) {
        super(nom, CategorieUtilisateur.SPECTATEUR);
        this.numeroPlace = numeroPlace;
        this.tribune = tribune;
    }
    
    @Override
    public int getNiveauAcces() {
        return 1; 
    }
    
    @Override
    public String getTypeDescription() {
        return "SPECTATEUR";
    }
    
    public String getNumeroPlace() {
        return numeroPlace;
    }
    
    public void setNumeroPlace(String numeroPlace) {
        this.numeroPlace = numeroPlace;
    }
    
    public String getTribune() {
        return tribune;
    }
    
    public void setTribune(String tribune) {
        this.tribune = tribune;
    }
    
    @Override
    public String toString() {
        String base = super.toString();
        if (numeroPlace != null && tribune != null) {
            return base + " | Place: " + numeroPlace + " | Tribune: " + tribune;
        }
        return base;
    }
}
