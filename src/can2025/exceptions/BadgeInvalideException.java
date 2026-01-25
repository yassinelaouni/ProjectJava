package can2025.exceptions;

public class BadgeInvalideException extends Exception {
    
    public int numeroBadge;
    public String raison;
    
    public BadgeInvalideException(String message) {
        super(message);
    }
    
    public BadgeInvalideException(int numeroBadge, String raison) {
        super("Badge no" + numeroBadge + " invalide: " + raison);
        this.numeroBadge = numeroBadge;
        this.raison = raison;
    }

}
