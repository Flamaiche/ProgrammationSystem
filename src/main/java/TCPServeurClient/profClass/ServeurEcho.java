package TCPServeurClient.profClass;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurEcho extends Thread {
    final static int port=5000;
    static int num = 1;
    Socket s;
    ServeurEcho(Socket s)  {
        this.s=s;
    }
    public void run() {
        int numComm=0;
        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out=new PrintWriter(s.getOutputStream());
            String ligne;
            numComm = ServeurEcho.num++;
            /*out.println("Serveur "+numComm+" d’echo");
            out.flush();*/
            while((ligne=in.readLine())!=null)  {
                System.out.println(ligne);
                /*out.println("from server "+numComm+" : "+ligne);
                out.flush();*/
            }
            System.out.println("Fin de communication Serveur "+numComm);
            s.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        try
        {
            ServerSocket passiveSocket=new ServerSocket(port);
            while(true)
            {
                Socket activeSocket=passiveSocket.accept();
                Thread t=new ServeurEcho(activeSocket);
                t.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
