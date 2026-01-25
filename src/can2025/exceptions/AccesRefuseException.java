package can2025.exceptions;

public class AccesRefuseException extends Exception {
    
    public String utilisateur;
    public String zone;
    public int niveauUtilisateur;
    public int niveauRequis;
    
    public AccesRefuseException(String message) {
        super(message);
    }
    
    public AccesRefuseException(String utilisateur, String zone, int niveauUtilisateur, int niveauRequis) {
        super("Acces refuse pour " + utilisateur + " a la zone " + zone + 
              ". Niveau requis: " + niveauRequis + ", niveau utilisateur: " + niveauUtilisateur);
        this.utilisateur = utilisateur;
        this.zone = zone;
        this.niveauUtilisateur = niveauUtilisateur;
        this.niveauRequis = niveauRequis;
    }

}
