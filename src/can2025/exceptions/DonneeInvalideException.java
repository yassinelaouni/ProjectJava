package can2025.exceptions;

public class DonneeInvalideException extends Exception {
    
    public String champ;
    public String valeur;
    
    public DonneeInvalideException(String message) {
        super(message);
    }
    
    public DonneeInvalideException(String champ, String valeur, String raison) {
        super("Donnee invalide pour le champ '" + champ + "' (valeur: " + valeur + "): " + raison);
        this.champ = champ;
        this.valeur = valeur;
    }
}
