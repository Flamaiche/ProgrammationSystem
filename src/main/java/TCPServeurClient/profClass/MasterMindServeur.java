package TCPServeurClient.profClass;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MasterMindServeur extends Thread {
    final static int port=6000;
    final static int MAX_TENTATIVES = 3;
    final static int MAX_CASES = 10;
    static int num = 1;
    Socket s;
    public class CodeMystere {
        final static int MAX = 10;
        final static char NON_EXISTE = '-';
        final static char MAL_PLACE = 'X';
        private String codeMystere;
        private int nbCases;
        private int nbTentatives = 0;
        public CodeMystere(int nbCases) {
            this.nbCases = nbCases;
            this.nbTentatives = 0;
            this.codeMystere="";
            Random rand=new Random();
            for(int i=0;i<this.nbCases;i++)  this.codeMystere+=(char)('0'+rand.nextInt(MAX));
            // this.codeMystere="123456";  // A décommenter pour les tests
        }
        public int getNbTentatives () {
            return this.nbTentatives;
        }
        public void setNbTentatives(int nbTentatives) {
            this.nbTentatives = nbTentatives;
        }
        public String getCodeMystere () {
            return this.codeMystere;
        }
        public String toString () {
            return this.codeMystere+"("+this.nbTentatives+")";
        }
        public String tentatives (String proposition) {
            this.nbTentatives++;
            if(proposition.length()>0 && proposition.endsWith("\r"))
                proposition=proposition.substring(0,proposition.length()-1);
            if(proposition.length()!=this.nbCases)
                return "Erreur format : Votre proposition doit comporter exactement "+this.nbCases+" chiffres.";
            else {
                String reponse = "";
                boolean[] used=new boolean[this.nbCases]; // ne pas utiliser deux fois le même chiffre pour afficher un X
                for(int i=0;i<this.nbCases;i++)
                    used[i]=this.codeMystere.charAt(i)==proposition.charAt(i);
                for(int i=0;i<this.nbCases;i++) {
                    if (this.codeMystere.charAt(i) == proposition.charAt(i)) {
                        reponse += this.codeMystere.charAt(i);
                    } else {
                        int j = 0;
                        while (j < this.nbCases && (this.codeMystere.charAt(j) != proposition.charAt(i) || used[j]))
                            j++;
                        if (j == this.nbCases)
                            reponse += NON_EXISTE;
                        else {
                            reponse += MAL_PLACE;
                            used[j] = true;
                        }
                    }
                }
                return reponse+"("+this.nbTentatives+")";
            }
        }
    }


    MasterMindServeur(Socket s)  {
        this.s=s;
    }
    public void run() {
        int numComm=0;
        int nbCases=0;
        boolean trouve=false;
        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out=new PrintWriter(s.getOutputStream());

            numComm = MasterMindServeur.num++;
            out.println("Master Mind Serveur num "+numComm);out.flush();
            out.println("Donner le nombre de cases : ");out.flush();
            try {
                nbCases = Integer.parseInt(in.readLine());
                if (nbCases<1 || nbCases>MAX_CASES) {
                    out.println("Nombre invalide\n");out.flush();
                    s.close();
                }
            }
            catch(Exception e) {
                out.println("Votre nombre n’est pas un entier !"); out.flush();
                s.close();
            }
            CodeMystere mystere = new CodeMystere(nbCases);
            do {
              out.println("Quel est votre proposition : (quit  pour abondonner)");out.flush();
              String proposition=in.readLine();
              if (proposition.equalsIgnoreCase("quit")) break;
              if (proposition!=null) {
                  if (proposition.compareTo(mystere.getCodeMystere())==0) {
                      mystere.setNbTentatives(mystere.getNbTentatives()+1);
                      trouve=true;
                      out.println("Bravo ! "+mystere );out.flush();
                      break; // nombre trouvé !!
                  }
                  String resultat = mystere.tentatives(proposition);
                  if (mystere.getNbTentatives()<MAX_TENTATIVES) {
                      out.println("Résultat : "+resultat);out.flush();
                  } else {
                      out.println("Perdu ! la réponse : "+mystere.getCodeMystere());out.flush();
                      break;
                  }
              }
            } while (true);
            out.print("Vous avez "+(trouve?"gagné ":"perdu "));
            out.println("avec "+mystere.getNbTentatives()+" tentative(s)");out.flush();
            if(!trouve)
              out.println("\tLa solution : "+mystere);out.flush();
            System.out.println("\t Fin Communication "+numComm);
            s.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        System.out.println("Start Serveur Master Mind ...");
        try
        {
            ServerSocket passiveSocket=new ServerSocket(port);
            while(true)
            {
                Socket activeSocket=passiveSocket.accept();
                Thread t=new MasterMindServeur(activeSocket);
                t.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
