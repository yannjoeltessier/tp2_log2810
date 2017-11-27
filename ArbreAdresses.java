/**
 * Description : Classe Automate qui implemente l'arbre des codes postaux
 *               et permet ainsi la validation d'autres codes postaux.
 * Auteurs     : Anoir Boujja, Yujia Ding, Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

import java.util.HashMap;
import java.util.Map;

public class ArbreAdresses {
    private class Node {
        Map<Character, Node> fils;
        boolean finDuMot;

        public Node() {
            fils = new HashMap<>();
            finDuMot = false;
        }
    }

    private final Node racine;

    public ArbreAdresses() {
        racine = new Node();
    }

    /**
     * Insere un code postal dans l'arbre
     **/
    public void insert(String codePostal) {
        Node courant = racine;
        for (int i = 0; i < codePostal.length(); i++) {
            char ch = codePostal.charAt(i);
            Node node = courant.fils.get(ch);
            if (node == null) {
                node = new Node();
                courant.fils.put(ch, node);
            }
            courant = node;
        }
        // Determine le codePostal comme valide
        courant.finDuMot = true;
    }

    /**
     * Permet de voir si un code postal est valide
     **/
    public boolean estValide(String codePostal) {
        Node courant = racine;
        for (int i = 0; i < codePostal.length(); i++) {
            char ch = codePostal.charAt(i);
            Node node = courant.fils.get(ch);
            //Si le noeud n'existe pas pour un certain caractere
            if (node == null) {
                return false;
            }
            courant = node;
        }
        //Vrai si le codePostal est valide;
        return courant.finDuMot;
    }
}

