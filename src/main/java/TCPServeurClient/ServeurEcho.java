package TCPServeurClient;

import java.io.*;
import java.net.*;

class ServeurEcho extends Thread {
    final static int port = 5000;
    Socket s;

    ServeurEcho(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
            String line;

            out.println("server echo actif");
            out.flush();
            while((line=in.readLine())!=null) { // attendre la ligne
                out.println(line);
                out.flush(); // renvoyer la ligne lue
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
                ServeurEcho serveurEcho = new ServeurEcho(activeSocket);
                serveurEcho.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

