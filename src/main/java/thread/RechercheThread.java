package thread;

import java.util.ArrayList;
import java.lang.Thread;
import java.util.Random;

public class RechercheThread extends Thread {
    private static final Object mutex = new Object();
    private static ArrayList<Integer> listNumber;
    private static int min;

    public final int NB_SLOT = 4; // nombre de slot du tableau par Thread

    private ArrayList<RechercheThread> threads;
    private int debut; // indice de debut du thread dans le tableau d'entier

    private RechercheThread(int threadDebut) {
        /*
        constructeur prive pour creer les threads avec leur indice de debut
         */
        debut = threadDebut;
    }

    public RechercheThread(ArrayList<Integer> liste) {
        /*
        constructeur public pour initialiser la liste d'entier a traiter
         */
        threads = new ArrayList<>();
        listNumber = liste;
    }

    public int minList() throws InterruptedException {
        /*
        methode pour lancer les threads et retourner le minimum de la liste
         */
        try {
            min = listNumber.getFirst();
        } catch (NullPointerException e) {
            System.out.println("La listNuumber est vide");
            return 0;
        }

        int nbThreads = listNumber.size() / NB_SLOT + 1;
        for (int i = 0; i < nbThreads; i++) {
            RechercheThread action = new RechercheThread(i * NB_SLOT);
            threads.add(action);
        }
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        return min;
    }

    public void run() {
        /*
        methode run pour chercher le minimum dans la partie du tableau attribuee au thread
         */
        int minLocal = listNumber.getFirst();
        for (int i = debut; i < debut + NB_SLOT; i++) {
            if (i >= listNumber.size()) {
                break;
            }
            if (listNumber.get(i) < minLocal) {
                minLocal = listNumber.get(i);
            }
        }
        synchronized (mutex) {
            if (minLocal < min) {
                min = minLocal;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        ArrayList<Integer> liste = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            liste.add(rand.nextInt(10));
        }

        RechercheThread recherche = new RechercheThread(liste);
        System.out.println(recherche.minList());
    }
}
