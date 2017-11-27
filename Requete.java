/**
 * Description : Classe Rquete qui implemente une requete de livraison
 * Auteurs     : Anoir Boujja, Yujia Ding, Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

public class Requete {
    private String source;
    private String destination;
    private int poids; // poids du colis

    public Requete(String source, String destination, int poids) {
        this.source = source;
        this.destination = destination;
        this.poids = poids;
    }

    public int getPoids() {
        return poids;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
}
