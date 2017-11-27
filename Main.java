/**
 * Description : Classe Main contenant la methode principale du programme
 * Auteurs     : Anoir Boujja, Yujia Ding, Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CentreExpedition ce = new CentreExpedition();
        menu(ce);

//        ce.creerArbreAdresses("CodesPostaux.txt");
//        ce.traiterLesRequetes("requetes4.txt");
//        ce.traiterLesRequetes("requetes8.txt");
//        ce.imprimerStatistiques();
    }


    /**
     * Verifie l'entree de l'usager
     * @param scan l'entree
     * @return la valeur entree si elle est valide, 0 sinon
     * */
    public static int verifierEntreeUsager(Scanner scan) {
        if (scan.hasNextInt()) {
            int a = scan.nextInt();
            if (a < 5 && a > 0) return a;
        }
        System.out.println("Entrée invalide. Veuillez entrer une entrée valide");
        return 0;
    }


    /**
     * Affiche le menu en boucle a l'usager
     * @param ce le centre d'expedition
     * */
    public static void menu(CentreExpedition ce) {

        // Choix du menu principal
        int choixGeneral = 0;

        /******************* Lecture du choix principal **************************/
        while (choixGeneral == 0) {
            System.out.println(
                    "\nMENU\n" +
                            "(1) Créer l’automate.\n" +
                            "(2) Traiter des requêtes.\n" +
                            "(3) Afficher les statistiques.\n" +
                            "(4) Quitter.\n");

            Scanner scan1 = new Scanner(System.in);
            choixGeneral = verifierEntreeUsager(scan1);

            /************************ Si l'usager choisi de creer l'automate ***********************/
            if (choixGeneral == 1) {
                System.out.println("Choix #1");

                System.out.println("Nom du fichier des codes postaux : ");
                Scanner scan3 = new Scanner(System.in);
                String fichierCodesPostaux = scan3.next();

                ce.creerArbreAdresses(fichierCodesPostaux);

                choixGeneral = 0;
            }
            /************************ Si l'usgager choisi de traiter les Requetes ***********************/
            else if (choixGeneral == 2) {
                System.out.println("Choix #2");

                System.out.println("Nom du fichier des requêtes : ");
                Scanner scan4 = new Scanner(System.in);
                String fichierRequetes = scan4.next();

                ce.traiterLesRequetes(fichierRequetes);

                choixGeneral = 0;
            }
            /************************ Si l'usgager choisi d'afficher les statistiques ***********************/
            else if (choixGeneral == 3) {
                System.out.println("Choix #3");

                ce.imprimerStatistiques();

                choixGeneral = 0;
            }
            /************************ Si l'usgager choisi de quitter ***********************/
            else if (choixGeneral == 4) return;
        }
    }
}
