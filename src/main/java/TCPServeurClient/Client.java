package TCPServeurClient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    public static void main(String[] args) {
        // client qui boucle et reponds dans le terminal (reponds apres que le serveur ai commencé)
        try {
            Socket s = new Socket("localhost", 5000); // localhost = serveur
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream()); // auto-flush
            Scanner sc = new Scanner(System.in);

            String ligneServeur;

            while ((ligneServeur = in.readLine()) != null) { // tant que le serveur n'a pas fermé
                System.out.println("Serveur : " + ligneServeur);

                // répondre après chaque message serveur (terminal)
                System.out.print("> ");
                String reponse = sc.nextLine();
                out.println(reponse);
                out.flush();
            }

            System.out.println("Le serveur a fermé la connexion.");
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
