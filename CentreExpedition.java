/**
 * Description : Classe Automate implementant un automate permettant a validation d'adresses postale
 * Auteurs     : Anoir Boujja, Yujia Ding & Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CentreExpedition {
    private final static int N_MAX_DRONES_TYPE_UN = 10;
    private final static int N_MAX_DRONES_TYPE_DEUX = 5;

    private ArbreAdresses arbreAdresses; // L'automate
    private ArrayList<ArrayList<Queue<Drone>>> flotte;
    private ArrayList<Requete> requetesInvalides;
    private Queue<ArrayList<Requete>> requetesValides;

    private int nRequetesTraitees;
    private int nColisTransportesTypeUn; // Nombre de colis transportés par un drone de type 1
    private int nColisTransportesTypeDeux; // Nombre de colis transportés par un drone de type 2

    /**
     * Constructeur de centre d'expedition
     */
    public CentreExpedition() {
        arbreAdresses = new ArbreAdresses();
        requetesValides = new LinkedList<>();
        requetesInvalides = new ArrayList<>();

        flotte = new ArrayList<>(N_MAX_DRONES_TYPE_DEUX + N_MAX_DRONES_TYPE_UN);
        flotte.add(new ArrayList<>());
        flotte.add(new ArrayList<>());
        flotte.get(0).add(new LinkedList<>());
        flotte.get(0).add(new LinkedList<>());
        flotte.get(1).add(new LinkedList<>());
        flotte.get(1).add(new LinkedList<>());
        for (int i = 0; i < N_MAX_DRONES_TYPE_DEUX; i++)
            flotte.get(0).get(0).add(new Drone(2));
        for (int j = 0; j < N_MAX_DRONES_TYPE_UN; j++)
            flotte.get(0).get(1).add(new Drone(1));

        nRequetesTraitees = 0;
        nColisTransportesTypeUn = 0;
        nColisTransportesTypeDeux = 0;
    }


    /**
     * Lit le fichier des codes postaux et cree l'automate qui validera
     * les Requetes futures.
     *
     * @param fichier le nom du fichiers des codes postaux
     */
    public void creerArbreAdresses(String fichier) {
        lireFichier(fichier);
    }


    /**
     * Lit un fichier de Requetes et les valide avant d'assigner les
     * colis aux drones et d'envoyer ces drones vers d'autres stations.
     *
     * @param fichier un fichier de Requetes
     */
    public void traiterLesRequetes(String fichier) {
        lireFichier(fichier);
        assignerLesColis();
        equilibrerFlotte();
    }


    /**
     * Permet d’assigner des colis à un drone en respectant les
     * limitations physiques du drone et en s’assurant que tous
     * les colis d’un drone vont vers le même quartier.
     */
    public void assignerLesColis() {

        for (Iterator<ArrayList<Requete>> it = requetesValides.iterator(); it.hasNext();) {

            ArrayList<Requete> groupe = it.next();

            if (flotte.get(0).get(0).size() + flotte.get(0).get(1).size() > 0) { // s'il y a des drone disponibles

                if (getPoidsTotal(groupe) > 1000 && flotte.get(0).get(0).size() > 0) { // s'il y a des drones de type 2 disponibles

                    flotte.get(0).get(0).element().setStatut(false);
                    flotte.get(0).get(0).element().setGroupeColis(groupe);
                    flotte.get(0).get(0).element().setSource(groupe.get(0).getSource());
                    flotte.get(0).get(0).element().setDestination(groupe.get(0).getDestination());
                    flotte.get(0).get(0).element().setPoidsColis(getPoidsTotal(groupe));

                    flotte.get(1).get(0).add(flotte.get(0).get(0).element());
                    flotte.get(0).get(0).remove();

                    nColisTransportesTypeDeux += groupe.size();

                } else if (flotte.get(0).get(1).size() > 0) { // s'il y a des drones de type 1 disponibles

                    flotte.get(0).get(1).element().setStatut(false);
                    flotte.get(0).get(1).element().setGroupeColis(groupe);
                    flotte.get(0).get(1).element().setSource(groupe.get(0).getSource());
                    flotte.get(0).get(1).element().setDestination(groupe.get(0).getDestination());
                    flotte.get(0).get(1).element().setPoidsColis(getPoidsTotal(groupe));

                    flotte.get(1).get(1).add(flotte.get(0).get(1).element());
                    flotte.get(0).get(1).remove();

                    nColisTransportesTypeUn += groupe.size();
                }
                it.remove();
                nRequetesTraitees++;
            }
        }
    }


    /**
     * Permet d’équilibrer le nombre de quadricoptères présents dans
     * chaque quartier de la ville.
     */
    public void equilibrerFlotte() {

    }


    /**
     * Permet d'afficher les statisques suivantes :
     * nombre de requêtes traitées,
     * requêtes invalides,
     * colis transportés par un drone de type 1,
     * colis transportés par un drone de type 2,
     * drones dans chaque quartier
     */
    public void imprimerStatistiques() {

        System.out.println("\n" + "Depuis le lancement du programme, " + "\n");
        System.out.println(nRequetesTraitees + " requêtes traitées." + "\n");
        System.out.println(requetesInvalides.size() + " requêtes invalides." + "\n");
        System.out.println(nColisTransportesTypeUn + " colis transportés par un/des drone(s) de type 1." + "\n");
        System.out.println(nColisTransportesTypeDeux + " colis transportés par un/des drone(s) de type 2." + "\n");

        System.out.println("Nombre de drones dans chaque quartier : " + "\n");

//        for (String quartier : quartiers){
//            int nDrones = 0;
//            for (Drone drone : flotte){
//
//                if (quartier.equals(drone.getDestination()))
//                    nDrones++;
//            }
//            if (nDrones > 0)
//                System.out.println(quartier + " : " + nDrones);
//        }
    }


    /**
     * Permet de lire le fichier de codes postaux ou de Requetes
     * et remplie la liste (codePostaux ou Requetes) correspondante.
     *
     * @param fichier un nom de fichier
     */
    private void lireFichier(String fichier) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "/src/autres/" + fichier;
        Path path = Paths.get(s);

        if (!Files.exists(path) || !Files.isRegularFile(path) || !Files.isReadable(path) || !Files.isExecutable(path))
            System.out.println("Nom de fichier érroné ou fichier introuvable!\n");
        else {
            try {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {

                    String[] split = line.split(" ");

                    if (split.length > 1) { // S'il y a plrs mots par ligne, c'est qu'on lit un fichier de requetes

                        Requete requete = new Requete(split[0], split[1], Integer.parseInt(split[2]));

                        if (arbreAdresses.estValide(split[0]) &&
                                arbreAdresses.estValide(split[1]) &&
                                poidsValide(Integer.parseInt(split[2]))) { // Si la requete est valide

                            if (!peutCreerGroupe(requete)) { // Si la requete ne forme pas un nouveau groupe

                                ArrayList<Requete> nouveauGroupe = new ArrayList<>();
                                nouveauGroupe.add(requete);
                                requetesValides.add(nouveauGroupe);
                            }
                        } else
                            requetesInvalides.add(requete);
                    } else // S'il y a un seul mot par ligne, c'est qu'on lit un fichier de codes postaux
                        arbreAdresses.insert(line);
                }
            } catch (IOException e) {
            }
        }
    }


    /**
     * Permet la creation de groupes de livraison
     *
     * @param requete la requete a evaluer
     * @return boolean
     */
    private boolean peutCreerGroupe(Requete requete) {

        if (requetesValides.isEmpty())
            return false;
        for (ArrayList<Requete> groupe : requetesValides) {
            for (Requete req : groupe) {

                if (req.getSource().equals(requete.getSource()) &&
                        req.getDestination().equals(requete.getDestination()) &&
                        poidsValide(getPoidsTotal(groupe) + requete.getPoids())) {

                    groupe.add(requete);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Valide le poids d'une requete
     *
     * @param poids le poids da la requete a valider
     * @return boolean
     */
    private boolean poidsValide(int poids) {
        if (poids <= 5000) return true;
        else return false;
    }


    /**
     * Calcule le poids total d'un groupe de requetes
     *
     * @param groupe
     * @return int
     */
    public int getPoidsTotal(ArrayList<Requete> groupe) {
        int poidsTotal = 0;
        for (Requete requete : groupe)
            poidsTotal += requete.getPoids();
        return poidsTotal;
    }
}
