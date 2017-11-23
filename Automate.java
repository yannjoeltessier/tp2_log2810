/**
 * Description : Classe Automate implementant un automate permettant a validation d'adresses postale
 * Auteurs     : Anoir Boujja, Yujia Ding & Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 *
 *
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Automate {
    static final int N_CHARACTERS_IN_CP = 6; // Le nb de caracteres dans un code postal (6)

    protected ArrayList<ArrayList<Character>> codesPostaux = new ArrayList<ArrayList<Character>>(N_CHARACTERS_IN_CP);
    protected ArrayList<ArrayList<String>> requetes = new ArrayList<ArrayList<String>>();

    /**
     * Constructeur de l'automate
     * */
    public Automate() {
        for (int i = 0; i < N_CHARACTERS_IN_CP; i++) {
            ArrayList<Character> characters = new ArrayList<Character>();
            codesPostaux.add(characters);
        }
    }

    /**
     * Implementation d'un noeud
     */
    protected static class Node
    {
        ArrayList<Character> val; // Valeur du noeud
        Node right; // fils droit
        Node left; // fils gauche

        public Node(ArrayList<Character> val)
        {
            this.val = val;
        }

        private static void insert(Node node, ArrayList<Character> elem){
            if (node == null) node = new Node(elem);
            if (node.left != null){
                insert(node.left, elem);
            }
            else node.left = new Node(elem);
        }
    }

    protected Node root = null; // Racine de l'arbre

    /**
     * Lit le fichier des codes postaux et cree l'automate qui validera
     * les requetes futures.
     * @param fichier le nom du fichiers des codes postaux
     * */
    public void creerArbreAdresses(String fichier) {
        lireFichier(fichier);
        for (ArrayList<Character> ch : codesPostaux){
            Node.insert(root, ch);
        }
    }

    /**
     * Permet de lire le fichier de codes postaux ou de requetes
     * et remplie la liste (codePostaux ou requetes) correspondante.
     * @param fichier un nom de fichier
     * */
    private void lireFichier(String fichier) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "/src/autres/" + fichier;
        Path path = Paths.get(s);

        if (!Files.exists(path) || !Files.isRegularFile(path) || !Files.isReadable(path) || !Files.isExecutable(path)) {
            System.out.println("Nom de fichier ou chemin invalide!\n");
        } else {
            try {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    String[] split = line.split(" ");
                    if (split.length > 1) {
                        ArrayList<String> req = new ArrayList<String>();
                        req.add(split[0]);
                        req.add(split[1]);
                        req.add(split[2]);
                        requetes.add(req);
                    } else {
                        for (int i = 0; i < line.length(); i++) {
                            if (!codesPostaux.get(i).contains(line.charAt(i)))
                                codesPostaux.get(i).add(line.charAt(i));
                        }
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Lit un fichier de requetes et les valide avant d'assigner les
     * colis aux drones et d'envoyer ces drones vers d'autres stations.
     * @param reqFile un fichier de requetes
     * */
    public void traiterLesRequetes(String reqFile) {
        lireFichier(reqFile);
    }

    /**
     * Permet d’assigner des colis à un drone en respectant les
     * limitations physiques du drone et en s’assurant que tous
     * les colis d’un drone vont vers le même quartier.
     * */
    public void assignerLesColis() {

    }

    /**
     * Permet d’équilibrer le nombre de quadricoptères présents dans
     * chaque quartier de la ville.
     * */
    public void equilibrerFlotte() {

    }
}
