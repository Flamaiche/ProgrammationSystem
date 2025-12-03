package TCPServeurClient.profClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurCalcul extends Thread {
    final static int port = 5000;
    final static int MAX_TENTATIVES = 4;
    static int num = 1;
    static Random rand=new Random();
    Socket s;

    public class Operation {
        private int a,b,result;
        char operation;
        Operation () {
            this.a = ServeurCalcul.rand.nextInt(100);
            this.b = ServeurCalcul.rand.nextInt(100);
            switch(ServeurCalcul.rand.nextInt(4)) {
                case 0:
                    this.operation='+';
                    this.result = a+b;
                    break;
                case 1:
                    this.operation='-';
                    this.result = a-b;
                    break;
                case 2:
                    this.operation='x';
                    this.result = a*b;
                    break;
                case 3:
                    this.operation='/';
                    if (this.b==0)this.b++;
                    this.result = a/b;
                    break;
                default:
                    this.operation=' '; // initialisation bidon pour supprimer un warning
                    this.result=0;
                    break;
            }
        }
        public int getResult() {
            return this.result;
        }
        public String toString() {
            return a+" "+operation+" "+b+" = ";
        }
    }

    ServeurCalcul(Socket s) {
        this.s = s;
    }

    public void run() {
        int numComm = 0;
        int nbTentatives = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
            String reponse;
            Operation operation;
            numComm = ServeurCalcul.num++;
            out.println("Serveur " + numComm + " Calculn, Max tentatives : "+MAX_TENTATIVES);//out.flush();
            operation = new Operation();
            while (nbTentatives<MAX_TENTATIVES) {
                out.println ("Test num "+nbTentatives+ " : "+operation);out.flush();
                reponse = in.readLine();
                // vérification de la réponse ...
                try {
                  int resultat = Integer.parseInt(reponse);
                  if (resultat== operation.getResult()) out.println("Bonne réponse !");
                  else {
                     out.println("Mauvaise réponse, la réponse est "+operation.getResult());
                  }
                }
                catch(Exception e) {
                    out.println("Votre nombre n’est pas un entier ! La bonne réponse est "+operation.getResult());
                }
                out.flush();
                operation = new Operation();
                nbTentatives++;
                // out.println ("Test num "+nbTentatives+ " : "+operation);out.flush();
            }
            out.println("\nFin de communication " + numComm);
            out.println("\t - Nombre de tentatives " + nbTentatives);
            out.println("\t ***TODO*** : Pourcentage de bonnes réponses ?");
            out.println("\t ***TODO*** : Ne pas faire tous les tests ? \n");
            out.flush();
            System.out.println("Fin de communication Serveur " + numComm);
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
                Thread t = new ServeurCalcul(activeSocket);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
