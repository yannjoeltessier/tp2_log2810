/**
 * Description : Classe Drone implemantant les deux categories de drone
 * Auteurs     : Anoir Boujja, Yujia Ding, Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

import java.util.Queue;

public class Drone {
    private int type; // type du drone, 1 pour categorie 1 et 2 pour categorie 2
    private boolean statut; // le statut du drone, vrai si le drone est disponible, faux sinon
    private Queue<Requete> groupeColis;
    private String source; // quartier actuel du drone
    private String destination; // quartier de destination apres livraison
    private int poidsColis; // poids du colis assigne

    /**
     * Constructeurs
     */
    public Drone(int type) {
        this.type = type;
        statut = true;
        groupeColis = null;
        source = "";
        destination = "";
        poidsColis = 0;
    }

    public Drone(int type, Queue<Requete> groupeColis) {
        this.type = type;
        this.statut = groupeColis.size() > 1;
        this.groupeColis = groupeColis;
        this.source = groupeColis.peek().getSource();
        this.destination = groupeColis.peek().getDestination();
        for (Requete requete : groupeColis)
            this.poidsColis += requete.getPoids();
    }

    /**
     * Getters et setters
     */
    public int getType() {
        return type;
    }

    public boolean getStatut() {
        return statut;
    }

    public Queue<Requete> getGroupeColis() {
        return groupeColis;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getPoidsColis() {
        return poidsColis;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public void setGroupeColis(Queue<Requete> groupeColis) {
        this.groupeColis = groupeColis;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPoidsColis(int poidsColis) {
        this.poidsColis = poidsColis;
    }
}
