package can2025.models;

public class ZoneSecurisee {
    
    private String idZone;
    private String nomZone;
    private int niveauRequis; 
    private String description;
    private boolean active;
    
    public ZoneSecurisee(String idZone, String nomZone, int niveauRequis) {
        this.idZone = idZone;
        this.nomZone = nomZone;
        this.niveauRequis = Math.max(1, Math.min(3, niveauRequis)); 
        this.active = true;
    }
    
    public ZoneSecurisee(String idZone, String nomZone, int niveauRequis, String description) {
        this(idZone, nomZone, niveauRequis);
        this.description = description;
    }
    

    public boolean peutAcceder(int niveauAcces) {
        return active && niveauAcces >= niveauRequis;
    }
    
    public String getIdZone() {
        return idZone;
    }
    
    public String getNomZone() {
        return nomZone;
    }
    
    public void setNomZone(String nomZone) {
        this.nomZone = nomZone;
    }
    
    public int getNiveauRequis() {
        return niveauRequis;
    }
    
    public void setNiveauRequis(int niveauRequis) {
        this.niveauRequis = Math.max(1, Math.min(3, niveauRequis));
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getNiveauLibelle() {
        switch (niveauRequis) {
            case 1: return "Spectateur";
            case 2: return "Staff";
            case 3: return "VIP";
            default: return "Inconnu";
        }
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s | Niveau requis: %s | %s%s",
                idZone, nomZone, getNiveauLibelle(),
                description != null ? description : "",
                active ? "" : " [INACTIVE]");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ZoneSecurisee that = (ZoneSecurisee) obj;
        return idZone.equals(that.idZone);
    }
    
    @Override
    public int hashCode() {
        return idZone.hashCode();
    }
}
