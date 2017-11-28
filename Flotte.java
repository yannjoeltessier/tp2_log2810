/**
 * Description : Classe Flotte implemantant notre flotte de drones
 * Auteurs     : Anoir Boujja, Yujia Ding, Yann-Joël D. Tessier
 * Date        : 20 Novembre 2017
 */

import java.util.ArrayList;

public class Flotte {
    private final static int N_MAX_DRONES_TYPE_UN = 10;
    private final static int N_MAX_DRONES_TYPE_DEUX = 5;

    private ArrayList<Drone> typeUnDisponibles;
    private ArrayList<Drone> typeDeuxDisponibles;
    private ArrayList<Drone> typeUnIndisponibles;
    private ArrayList<Drone> typeDeuxIndisponibles;

    /**
     * Constructeur
     */
    public Flotte() {
        typeUnDisponibles = new ArrayList<>();
        typeDeuxDisponibles = new ArrayList<>();
        typeUnIndisponibles = new ArrayList<>();
        typeDeuxIndisponibles = new ArrayList<>();

        for (int i = 0; i < N_MAX_DRONES_TYPE_DEUX; i++)
            typeDeuxDisponibles.add(new Drone(2));

        for (int j = 0; j < N_MAX_DRONES_TYPE_UN; j++)
            typeUnDisponibles.add(new Drone(1));
    }

    public ArrayList<Drone> getTypeUnDispo() {
        return typeUnDisponibles;
    }

    public ArrayList<Drone> getTypeDeuxDispo() {
        return typeDeuxDisponibles;
    }

    public ArrayList<Drone> getTypeUnIndispo() {
        return typeUnIndisponibles;
    }

    public ArrayList<Drone> getTypeDeuxIndispo() {
        return typeDeuxIndisponibles;
    }
}
