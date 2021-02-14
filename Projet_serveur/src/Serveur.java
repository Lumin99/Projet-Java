import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Serveur extends Thread {
	Socket connexion;
	ServerSocket sSocket;
	int numero;
	
	public void transfert(InputStream in, OutputStream out, boolean closeOnExit) throws IOException
    {
        byte buf[] = new byte[1024];
        
        int n;
        while((n=in.read(buf))!=-1)
            out.write(buf,0,n);
        
        if (closeOnExit)
        {
            in.close();
            out.close();
        }
    }
	
	public Serveur(int num,Socket cli,ServerSocket serv) {

				sSocket = serv;
				connexion = cli;
				numero = num;
				this.start();

	}
	
	public void run(){
		try {
			String rep;
			Socket client = this.connexion;
			IOCommandes IO = new IOCommandes(client);
			String demande, chemin;
			Liste_fichier ls_fic = new Liste_fichier("Fichiers",true);
			
			
			
			//Envoie des fichiers serveurs dans fichier clients
			
			//
			
			IO.ecrireReseau("Bienvenue sur le serveur !");
			do {
				ls_fic.lister();
	    		rep = IO.lireReseau();
	    		if(rep==null)
	    		{
	    			rep = "Continue";
	    		}
	    		else
	    		{
		    		String decoupeur[]=rep.split(":",2);
		    		demande = decoupeur[0];
		    		chemin = decoupeur[1];
					System.out.println("La requête est :"+demande);
		    		IO.ecrireEcran(demande+" "+chemin);
		    		switch(demande){
		    		case "NEED" :	SEND(IO, client, chemin);
		    			break;
		    		case "DELETE" :	DELETE(IO, chemin);
	    				break;
		    		case "ADD" :	ADD(IO, client, chemin);
	    				break;
		    		case "QUIT" :	QUIT(IO);
		    			break;
		    		case"ADDDIR" : ADDDIR(chemin);
		    			break;
		    			
					default :	IO.ecrireEcran("Erreur !");
		    		}
	    		}

			}while(rep.equalsIgnoreCase("QUIT") == false);
			
			IO.ecrireReseau("Au revoir !");
			
			client.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void QUIT(IOCommandes IO) {
		
		//RÃ©colte des donnÃ©es/ Reprise des fichiers et Suppression des fichiers dans le fichier clients (2*)
	}
	
	public void SEND(IOCommandes IO, Socket client, String chemin) throws IOException{
		Socket client_trans;
		ServerSocket ssock_trans = new ServerSocket(8081,10);
		IO.ecrireReseau("Ready");
		client_trans =ssock_trans.accept();
		transfert(new FileInputStream(chemin), client_trans.getOutputStream(),true);
		client_trans.close();
		ssock_trans.close();
	}
	
	public void DELETE(IOCommandes IO, String chemin) {
		IO.ecrireEcran("Une demande de suppression est effectuÃ©e "+chemin);
		File asupr = new File(chemin);
		Boolean etat;
		etat =deleteDirectory(asupr);
		System.out.println("Le résultat est : "+etat);
	}
	
	public void ADD(IOCommandes IO, Socket client, String chemin) throws IOException{
		Socket client_trans;
		ServerSocket ssock_trans = new ServerSocket(8080,10);
		IO.ecrireReseau("Ready");
		client_trans =ssock_trans.accept();
		transfert(client_trans.getInputStream(),new FileOutputStream(chemin),true);
		client_trans.close();
		ssock_trans.close();
		System.out.println("J'ai fini d'écrire");
	}
	
	public void ADDDIR(String chemin)
	{
		File doss = new File(chemin);
		doss.mkdir();
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[]allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}

}