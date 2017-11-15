import java.util.*;

public class Main {


    public static void main(String[] args) {

            // Creation des objets principaux


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

                if (choixGeneral == 1) // Si l'usager choisi Drones
                {
                    System.out.println("Choix 1");

                    Automate automate = new Automate();

                    System.out.println("Nom du fichier : ");
                    Scanner scan3 = new Scanner(System.in);
                    String fichier = scan3.next();
                    automate.creerArbreAdresses(fichier);

                    choixGeneral=0;
                }
                else if (choixGeneral == 2) // Si l'usgager choisi Desserts
                {
                    System.out.println("Choix 2");
                    choixGeneral=0;
                }
                else if (choixGeneral == 3) // Si l'usager choisi Quitter
                {
                    System.out.println("Choix 3");
                    choixGeneral=0;
                }
                else if (choixGeneral == 4) return;
                else if ((choixGeneral > 4 || choixGeneral < 1) && scan1.hasNextInt()  ) {
                    choixGeneral = 0;
                    System.out.println("Entrée invalide. Veuillez entrer une entrée valide");
                }
            }
        }

        public static int verifierEntreeUsager(Scanner scan){
            if(scan.hasNextInt()) return scan.nextInt();
            System.out.println("Entrée invalide. Veuillez entrer une entrée valide");
            return 0;
        }
    }
