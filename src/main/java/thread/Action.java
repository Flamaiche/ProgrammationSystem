package thread;

import java.lang.Thread;
import java.util.ArrayList;

public class Action extends Thread {
    public static final int n = 1_000_000;
    public static final Object mutex = new Object();
    public static int x = 0;

    /*
    ce cas est meilleur dans ce cas si car il permet ce rapidement faire le changement de valeur sur x, qui ici dans ce
    cas est le but, mais ouvrir et fermer le verrou a chaque iteration est couteux en temps
    */
    public void run() {
        for(int i=0; i<n; i++) {
            synchronized(mutex) {
                x++;
            }
        }
    }

    /*
    ce cas en meilleur dans les cas ou il faut juste faire sur une section, cela evite de ouvrir et fermer le verrou
    (synchronized block) a chaque iteration
    */
    /*
    public void run() {
        synchronized(mutex) {
            for(int i=0; i<n; i++) {
                x++;
            }
        }
    }*/
}

class TestThread {
    private static int nbThreads = 10;
    private static ArrayList<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {
        addThread(nbThreads);
        launchAllThreads();
        System.out.println(Action.x);
    }

    public static void addThread(int nb) {
        for(int i=0; i<nb; i++) {
            threads.add(new Action());
        }
    }

    public static void startThread(int i) {
        threads.get(i).start();
    }

    public static void joinThread(int i) {
        try {
            threads.get(i).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void launchAllThreads() {
        for(int i=0; i<threads.size(); i++) {
            startThread(i);
        }
        for(int i=0; i<threads.size(); i++) {
            joinThread(i);
        }
    }
}
