package TCPServeurClient;

import java.io.*;
import java.net.*;

class ClientBasic {
    // envoie au server sa reponse sans attendre ce quil lui demande
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost",5000); // localhost = nom du serveur
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
            String ligne;

            ligne = in.readLine();
            System.out.println("Le serveur a parlé : "+ligne);

            out.println("Hello");
            out.flush();

            ligne = in.readLine();
            System.out.println("Le serveur a répondu : "+ligne);

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}