package TCPServeurClient.profClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientEcho {
    public static void main(String[] args) {
        try {
            Socket activeSocket=new Socket("localhost",7007);
            BufferedReader in=new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
            PrintWriter out=new PrintWriter(activeSocket.getOutputStream());
            BufferedReader clavier=new BufferedReader(new InputStreamReader(System.in));
            String ligne;
            if((ligne=in.readLine())!=null)  System.out.println(ligne);
            while((ligne=clavier.readLine())!=null) {  // lecture au clavier  ^D pour arrêter
                out.println(ligne); // vers le serveur
                out.flush();
                ligne=in.readLine(); // provient du serveur
                System.out.println(ligne); // affichage à l'écran
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
