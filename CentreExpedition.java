/**
 * Description : Classe Automate implementant un automate permettant a validation d'adresses postale
 * Auteurs     : Anoir Boujja, Yujia Ding & Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CentreExpedition {
    private Flotte flotte;
    private ArbreAdresses arbreAdresses; // L'automate
    private ArrayList<Requete> requetesInvalides;
    private Queue<Queue<Requete>> requetesValides;

    private int nRequetesTraitees;
    private int nColisTransportesTypeUn; // Nombre de colis transportés par un drone de type 1
    private int nColisTransportesTypeDeux; // Nombre de colis transportés par un drone de type 2

    /**
     * Constructeur de centre d'expedition
     */
    public CentreExpedition() {
        flotte = new Flotte();
        arbreAdresses = new ArbreAdresses();
        requetesValides = new LinkedList<>();
        requetesInvalides = new ArrayList<>();

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

        for (Iterator<Queue<Requete>> i = requetesValides.iterator(); i.hasNext(); ) {

            Queue<Requete> groupe = i.next();

            // s'il y a des drone disponibles
            if (flotte.getTypeDeuxDispo().size() + flotte.getTypeUnDispo().size() > 0) {

                // s'il y a des drones de type 2 disponibles
                if (getPoidsTotal(groupe) > 1000 && flotte.getTypeDeuxDispo().size() > 0) {

                    flotte.getTypeDeuxDispo().get(0).setStatut(false);
                    flotte.getTypeDeuxDispo().get(0).setGroupeColis(groupe);
                    flotte.getTypeDeuxDispo().get(0).setSource(groupe.peek().getSource());
                    flotte.getTypeDeuxDispo().get(0).setDestination(groupe.peek().getDestination());
                    flotte.getTypeDeuxDispo().get(0).setPoidsColis(getPoidsTotal(groupe));

                    flotte.getTypeDeuxIndispo().add(flotte.getTypeDeuxDispo().get(0));
                    flotte.getTypeDeuxDispo().remove(0);

                    nColisTransportesTypeDeux += groupe.size();

                    i.remove();
                    nRequetesTraitees++;

                    // s'il y a des drones de type 1 disponibles
                } else if (getPoidsTotal(groupe) <= 1000 && flotte.getTypeUnDispo().size() > 0) {

                    flotte.getTypeUnDispo().get(0).setStatut(false);
                    flotte.getTypeUnDispo().get(0).setGroupeColis(groupe);
                    flotte.getTypeUnDispo().get(0).setSource(groupe.peek().getSource());
                    flotte.getTypeUnDispo().get(0).setDestination(groupe.peek().getDestination());
                    flotte.getTypeUnDispo().get(0).setPoidsColis(getPoidsTotal(groupe));

                    flotte.getTypeUnIndispo().add(flotte.getTypeUnDispo().get(0));
                    flotte.getTypeUnDispo().remove(0);

                    nColisTransportesTypeUn += groupe.size();

                    i.remove();
                    nRequetesTraitees++;
                }
            }
        }
    }


    /**
     * Permet d’équilibrer le nombre de quadricoptères présents dans
     * chaque quartier de la ville.
     */
    public void equilibrerFlotte() {

        // On parcours nos drones indisponibles et on verifie s'ils ont
        // termine la livraison de tous leurs colis. Si oui, leur statut devient disponible.
        // Si non, on suppose qu'ils ont effectue une livraison durant ce cycle alors
        // nous leur enlevons un colis de leur groupe.
        for (Iterator<Drone> i = flotte.getTypeDeuxIndispo().iterator(); i.hasNext(); ) { // drones indispo de type 2

            Drone droneType2 = i.next();

            if (droneType2.getGroupeColis().size() == 0) {

                droneType2.setStatut(true);
                droneType2.setPoidsColis(0);
                flotte.getTypeDeuxDispo().add(droneType2);
                i.remove();
            } else
                droneType2.getGroupeColis().remove();
        }
        for (Iterator<Drone> j = flotte.getTypeUnIndispo().iterator(); j.hasNext(); ) { // drones indispo de type 1

            Drone droneType1 = j.next();

            if (droneType1.getGroupeColis().size() == 0) {

                droneType1.setStatut(true);
                droneType1.setPoidsColis(0);
                flotte.getTypeUnDispo().add(droneType1);
                j.remove();
            } else
                droneType1.getGroupeColis().remove();
        }

        // On verifie ici s'il y a des requetes dans notre file d'attente
        // et si nous avons encore des drones disponibles. Si oui, nous les
        // envoyons chercher de nouveaux colis en leur ajoutant un faux colis pour
        // qu'ils deviennent indisponibles pendant un cycle pour qu'ils aient le
        // temps de recuperer le vrai colis.
        if (!requetesValides.isEmpty() &&
                (!flotte.getTypeDeuxDispo().isEmpty() ||
                        !flotte.getTypeUnDispo().isEmpty())) {

            for (Queue<Requete> requete : requetesValides) {

                if (getPoidsTotal(requete) > 1000) {

                    for (Iterator<Drone> i = flotte.getTypeDeuxDispo().iterator(); i.hasNext(); ) {

                        Drone droneType2 = i.next();

                        if (!requete.peek().getSource().equals(droneType2.getSource())) {

                            droneType2.setSource(requetesValides.element().peek().getSource());
                            droneType2.setDestination(requetesValides.element().peek().getSource());
                            droneType2.setPoidsColis(0);
                            droneType2.setGroupeColis(new LinkedList<>());
                            flotte.getTypeDeuxIndispo().add(droneType2);
                            i.remove();
                        }
                    }
                } else {
                    for (Iterator<Drone> j = flotte.getTypeUnDispo().iterator(); j.hasNext(); ) {

                        Drone droneType1 = j.next();

                        if (!requete.peek().getSource().equals(droneType1.getSource())) {

                            droneType1.setSource(requetesValides.element().peek().getSource());
                            droneType1.setDestination(requetesValides.element().peek().getSource());
                            droneType1.setPoidsColis(0);
                            droneType1.setGroupeColis(new LinkedList<>());
                            flotte.getTypeUnIndispo().add(droneType1);
                            j.remove();
                        }
                    }
                }
            }
        }
        // Les drones qui n'ont pas de colis assigne, nous les envoyons aleatoirement
        // vers des quartiers libres.
        for (Iterator<Drone> i = flotte.getTypeDeuxDispo().iterator(); i.hasNext(); ) {

            Drone droneType2 = i.next();

            if (droneType2.getDestination() == "") {

                Random generator = new Random(System.nanoTime());
                int randomInt = generator.nextInt(arbreAdresses.getAdresses().size() + 1);
                droneType2.setDestination(arbreAdresses.getAdresses().get(randomInt));
            }
        }
        for (Iterator<Drone> j = flotte.getTypeUnDispo().iterator(); j.hasNext(); ) {

            Drone droneType1 = j.next();

            if (droneType1.getDestination() == "") {

                Random generator = new Random(System.nanoTime());
                int randomInt = generator.nextInt(arbreAdresses.getAdresses().size() + 1);
                droneType1.setDestination(arbreAdresses.getAdresses().get(randomInt));
            }
        }
    }


    /**
     * Permet d'afficher les statisques suivantes :
     * - nombre de requêtes traitées,
     * - requêtes invalides,
     * - colis transportés par un drone de type 1,
     * - colis transportés par un drone de type 2,
     * - nombre de drones dans chaque quartier
     */
    public void imprimerStatistiques() {

        System.out.println("\n" + "Depuis le lancement du programme, " + "\n");
        System.out.println(nRequetesTraitees + " requêtes traitées." + "\n");
        System.out.println(requetesInvalides.size() + " requêtes invalides." + "\n");
        System.out.println(nColisTransportesTypeUn + " colis transportés par un/des drone(s) de type 1." + "\n");
        System.out.println(nColisTransportesTypeDeux + " colis transportés par un/des drone(s) de type 2." + "\n");

        System.out.println("Nombre de drones dans chaque quartier (caché si 0) : " + "\n");

        for (String quartier : arbreAdresses.getAdresses()) {

            int nDrones = 0;

            for (Drone drone : flotte.getTypeUnDispo())
                if (quartier.equals(drone.getDestination()))
                    nDrones++;

            for (Drone drone : flotte.getTypeDeuxDispo())
                if (quartier.equals(drone.getDestination()))
                    nDrones++;

            for (Drone drone : flotte.getTypeUnIndispo())
                if (quartier.equals(drone.getDestination()))
                    nDrones++;

            for (Drone drone : flotte.getTypeDeuxIndispo())
                if (quartier.equals(drone.getDestination()))
                    nDrones++;

            if (nDrones > 0)
                System.out.println(quartier + " : " + nDrones);
        }
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

                                Queue<Requete> nouveauGroupe = new LinkedList<>();
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
        for (Queue<Requete> groupe : requetesValides) {
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
    public int getPoidsTotal(Queue<Requete> groupe) {
        int poidsTotal = 0;
        for (Requete requete : groupe)
            poidsTotal += requete.getPoids();
        return poidsTotal;
    }
}
