package TCPServeurClient.profClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MasterMindClient {
    public static void main(String[] args)
    {
        try
        {
            Socket activeSocket=new Socket("localhost",6000);
            BufferedReader in=new BufferedReader(
                    new InputStreamReader(activeSocket.getInputStream()));
            PrintWriter out=new PrintWriter(activeSocket.getOutputStream());
            BufferedReader clavier=new BufferedReader(new InputStreamReader(System.in));
            String ligne;

            if((ligne=in.readLine())!=null)  System.out.println(ligne); // premiere ligne
            if((ligne=in.readLine())!=null)  System.out.println(ligne); // deuxième ligne
            ligne=clavier.readLine();
            out.println(ligne);out.flush(); // envoyer le nombre ....
            if((ligne=in.readLine())!=null)  System.out.println(ligne); // réponse du serveu
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
