package thread;

public class afficheurMultiThread {

    static class Action extends Thread {
        private String message;
        public static Object mutex = new Object();

        public Action(String message) {
            this.message = message;
        }

        public void run() {
            /*
            mutex l'entiereté de le methode run pour chaque thread pour éviter le melange de message
            */
            synchronized (mutex) {
                for (int i = 0; i < message.length(); i++) {
                    System.out.print(message.charAt(i));
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Action("Thread 1: Bonjour !");
        Thread t2 = new Action("Thread 2: Salut !");
        Thread t3 = new Action("Thread 3: Coucou !");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


