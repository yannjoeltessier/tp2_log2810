/**
 * Description : Classe Main contenant la methode principale du programme
 * Auteurs     : Anoir Boujja, Yujia Ding, Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 *
 *
 * */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Creation des objets principaux
        Automate automate = new Automate();

        // Choix du menu principal
        int choixGeneral = 0;

        while (choixGeneral == 0) // Lecture du choix principal
        {
            System.out.println(
                    "\nMENU\n" +
                            "(1) Créer l’automate.\n" +
                            "(2) Traiter des requêtes.\n" +
                            "(3) Afficher les statistiques.\n" +
                            "(4) Quitter.\n");

            Scanner scan1 = new Scanner(System.in);
            choixGeneral = verifierEntreeUsager(scan1);

            if (choixGeneral == 1) // Si l'usager choisi de creer l'automate
            {
                System.out.println("Choix #1");

                System.out.println("Nom du fichier des codes postaux : ");
                Scanner scan3 = new Scanner(System.in);
                String fichierCodesPostaux = "CodesPostaux.txt"; //scan3.next();

                automate.creerArbreAdresses(fichierCodesPostaux);

                choixGeneral = 0;
            }
            else if (choixGeneral == 2) // Si l'usgager choisi de traiter les requetes
            {
                System.out.println("Choix #2");

                System.out.println("Nom du fichier des requêtes : ");
                Scanner scan4 = new Scanner(System.in);
                String fichierRequetes = "requetes1.txt"; //scan4.next();

                automate.traiterLesRequetes(fichierRequetes);

                choixGeneral = 0;
            }
            else if (choixGeneral == 3) // Si l'usager choisi d'afficher les statistiques
            {
                System.out.println("Choix #3");
                choixGeneral = 0;
            }
            else if (choixGeneral == 4) return; // Si l'usager choisi Quitter
        }
    }

    public static int verifierEntreeUsager(Scanner scan) {
        if (scan.hasNextInt()){
            int a = scan.nextInt();
            if (a < 5 && a > 0) return a;
        }
        System.out.println("Entrée invalide. Veuillez entrer une entrée valide");
        return 0;
    }
}
