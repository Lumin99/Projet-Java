import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
	IOCommandes IO;
	Socket sock;
	boolean actif;
	
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
	
	public Client() {
		//Liste_fichier fichier= new Liste_fichier("Fichiers",true);
		//fichier.lister();
		try {
			sock = new Socket("192.168.1.119", 8000);
			IO = new IOCommandes(sock);
			
			dialogue();
			sock.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		int exec =1;
		while(exec ==1)
		{
			System.out.println("Je passe dans le thread");
			Liste_fichier ls_fic = new Liste_fichier("Fichiers",true);
		
			ls_fic.lister(this);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	public void dialogue() throws IOException{
		String rep;
		actif = true;
		//Envoie des fichiers serveurs dans fichier clients
		
		//
		this.start();
		
		IO.ecrireEcran(IO.lireReseau()); //Bienvenue
		do {
    		IO.ecrireEcran("Tapez 'QUIT' si vous voulez quitter");
    		rep = IO.lireEcran();
    		
    		switch(rep){
    		case "QUIT" :	QUIT();
    			break;
    		default :	IO.ecrireEcran("Cette commande ne peut être traité !");
    		}
    		
		}while(rep.equalsIgnoreCase("QUIT") == false);
		
		IO.ecrireEcran(IO.lireReseau()); //Au revoir
    }
	
	public void QUIT() {
		actif = false;
		IO.ecrireReseau("QUIT:0");
		//Récolte des données/ Reprise des fichiers et Suppression des fichiers dans le fichier clients (2*)
	}
	
	public void NEED(String chemin) throws IOException, InterruptedException{
		IO.ecrireReseau("NEED:"+chemin);
		IO.lireReseau();
		sleep(500);
		Socket sock_trans = new Socket("192.168.1.119", 8081);
		transfert(sock_trans.getInputStream(),new FileOutputStream(chemin),true);
		sock_trans.close();
	}
	
	public void DELETE(String chemin) throws IOException{
		IO.ecrireReseau("DELETE:"+chemin);
	}
	
	public void ADD(String chemin) throws IOException, InterruptedException{
		File fic = new File(chemin);
		if(fic.isDirectory()==true)
		{
			System.out.println("J'ajoute ");
			IO.ecrireReseau("ADDDIR:"+chemin);
		}
		else
		{
			IO.ecrireReseau("ADD:"+chemin);
			IO.lireReseau();
			sleep(500);
			Socket sock_trans = new Socket("192.168.1.119", 8080);
			transfert(new FileInputStream(chemin), sock_trans.getOutputStream(),true);
			sock_trans.close();
			System.out.println("J'ai fini d'�crire");
		}

	}
	
}