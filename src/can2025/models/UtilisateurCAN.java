package can2025.models;

public abstract class UtilisateurCAN {
    
    private static int compteurId = 0;
    
    protected int id;
    protected String nom;
    protected CategorieUtilisateur categorie;
    protected Badge badge;
    
    public UtilisateurCAN(String nom, CategorieUtilisateur categorie) {
        this.id = ++compteurId;
        this.nom = nom;
        this.categorie = categorie;
        this.badge = null;
    }
    
    public abstract int getNiveauAcces();
    

    public abstract String getTypeDescription();
    
    public int getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public CategorieUtilisateur getCategorie() {
        return categorie;
    }
    
    public void setCategorie(CategorieUtilisateur categorie) {
        this.categorie = categorie;
    }
    
    public Badge getBadge() {
        return badge;
    }
    
    public void setBadge(Badge badge) {
        this.badge = badge;
    }
    
    public boolean hasBadge() {
        return badge != null;
    }

    public static void resetCompteur() {
        compteurId = 0;
    }

    public static int getCompteurActuel() {
        return compteurId;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | Categorie: %s | Niveau acces: %d | Badge: %s",
                id, getTypeDescription(), nom, categorie.getLibelle(), getNiveauAcces(),
                badge != null ? "No" + badge.getNumeroBadge() : "Aucun");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UtilisateurCAN that = (UtilisateurCAN) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
