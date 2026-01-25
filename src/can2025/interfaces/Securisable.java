package can2025.interfaces;

import can2025.models.ZoneSecurisee;

public interface Securisable {
    

    boolean autoriser(ZoneSecurisee zone);
    

    boolean refuser(ZoneSecurisee zone);
    

    boolean verifierAcces(ZoneSecurisee zone);
}
