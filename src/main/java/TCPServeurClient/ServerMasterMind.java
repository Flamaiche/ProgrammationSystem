package TCPServeurClient;

import java.io.*;
import java.net.*;
import java.util.Random;

class ServerMasterMind extends Thread {
    final static int port = 5000;
    private Socket s;
    private char[] secret;
    private Random rand;
    private final int LONGUEUR_Reponse = 6;
    private final int MAX_Value = 10;
    private final char[] symbole = {'X', '-'}; // existe ailleurs / absent

    private static String caracterReponse = Client.CARACTERE_REPONSE;

    ServerMasterMind(Socket s) {
        this.s = s;
        rand = new Random();
        secret = new char[LONGUEUR_Reponse];
    }

    public void generateSecret() {
        for (int i = 0; i < LONGUEUR_Reponse; i++) {
            secret[i] = (char) ('0' + rand.nextInt(MAX_Value));
        }
    }

    private char[] formatReponse(String reponse) {
        char[] reponseChar = new char[LONGUEUR_Reponse];

        // Remplir reponseChar avec ce que l'utilisateur a envoyé
        for (int i = 0; i < LONGUEUR_Reponse; i++) {
            if (i < reponse.length()) {
                reponseChar[i] = reponse.charAt(i);
            } else {
                reponseChar[i] = symbole[1];
            }
        }
        return reponseChar;
    }

    public String traitementReponse(String reponse) {
        char[] reponseChar = formatReponse(reponse);

        char[] resultat = new char[LONGUEUR_Reponse];
        boolean[] secretUsed = new boolean[LONGUEUR_Reponse];
        boolean win = true;

        // Pré-remplir avec '-'
        for (int i = 0; i < LONGUEUR_Reponse; i++) {
            resultat[i] = symbole[1];
        }

        // Marquer les positions exactes
        for (int i = 0; i < LONGUEUR_Reponse; i++) {
            if (reponseChar[i] == secret[i]) {
                resultat[i] = secret[i];
                secretUsed[i] = true;
            } else {
                win = false;
            }
        }

        // Marquer les chiffres existant ailleurs
        for (int i = 0; i < LONGUEUR_Reponse; i++) {
            if (resultat[i] != symbole[1]) continue; // déjà exact
            boolean found = false;
            for (int j = 0; j < LONGUEUR_Reponse; j++) {
                if (!secretUsed[j] && reponseChar[i] == secret[j]) {
                    found = true;
                    secretUsed[j] = true;
                    break;
                }
            }
            if (found) resultat[i] = symbole[0]; // chiffre présent ailleurs
        }

        return win ? null : new String(resultat);
    }

    public void run() {
        generateSecret();
        System.out.print("secret : "); for (char s : secret) System.out.print(s);
        System.out.println();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            out.println("Essayez de deviner le code de " + LONGUEUR_Reponse + " chiffres !");

            String line;
            String reponse;

            while (true) {
                out.println("Votre proposition ?" + caracterReponse);
                line = in.readLine();

                reponse = traitementReponse(line);

                if (reponse == null) {
                    out.println("Vous avez gagné !");
                    break;
                } else {
                    out.println(reponse);
                }
            }

            System.out.println("Fin de la communication Serveur");
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket passiveSocket = new ServerSocket(port);
            while (true) {
                Socket activeSocket = passiveSocket.accept();
                new ServerMasterMind(activeSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
