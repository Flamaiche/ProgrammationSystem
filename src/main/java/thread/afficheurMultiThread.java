package thread;

public class afficheurMultiThread {

    static class Action extends Thread {
        private char first;
        private char last;
        public static Object mutex = new Object();

        public Action(char first, char last) {
            this.first = first;
            this.last = last;
        }

        public void run() {
            /*
            mutex l'entiereté de le methode run pour chaque thread pour éviter le melange de message
            */
            try {
                for (int nbFois = 0; nbFois < 20; nbFois++) {
                    synchronized (mutex) {
                        for (char c = first; c <= last; c++) {
                            System.out.print(c);
                            Thread.sleep(1);
                        }
                        System.out.println();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Action('a', 'z');
        Thread t2 = new Action('A', 'Z');
        Thread t3 = new Action('0', '9');

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


