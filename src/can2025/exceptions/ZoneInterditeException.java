package can2025.exceptions;

public class ZoneInterditeException extends Exception {
    
    public String idZone;
    
    public ZoneInterditeException(String message) {
        super(message);
    }
    
    public ZoneInterditeException(String idZone, boolean inexistante) {
        super(inexistante ? "Zone " + idZone + " inexistante dans le systeme" 
                         : "Zone " + idZone + " est interdite d'acces");
        this.idZone = idZone;
    }
}
