import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Liste_fichier {
	private String chemin_init="";
	private Boolean sous_dossier = false;
	public int nbr_fichier;
	public int nbr_dossier;

	public Liste_fichier() //Constructeur
	{

	}
	
	
	public Liste_fichier(String chemin, Boolean sous_dos) //Constructeur
	{
		super();
		this.chemin_init = chemin;
		this.sous_dossier = sous_dos;
	}
	
	public void lister(Client client) //Pour générer la liste du répertoire dans un fichier texte
	{
		try {
			this.listerRepertoire(this.chemin_init,client);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[][] recup_liste(File fichier) //Pour récupérer la liste à partir d'un fichier texte
	{
		return creer_liste(fichier);
	}
	
	
	private void listerRepertoire(String chemin,Client client_req) throws FileNotFoundException, NoSuchAlgorithmException
	{
		File fichier = new File(chemin);
		File doss = new File("Fichiers/.doss.txt");
		gestion_fichiers gsfic = new gestion_fichiers();
		BufferedWriter bw;
		File[] liste_fichiers = fichier.listFiles();
		String chemin_fichier_sync = chemin+"\\.sync.txt";
		String[][] ancienne_liste =null;

		File fichier_sync = new File (chemin_fichier_sync);
		if(fichier_sync.exists())
		{
			ancienne_liste = creer_liste(fichier_sync); //On récupere l'ancienne liste avant de l'écraser
			//System.out.println("BABABABABABA "+ancienne_liste[0][1]);
		}


		if(liste_fichiers != null)
		{
			ecrire_fichier(fichier_sync,Integer.toString(liste_fichiers.length),0);
			for(int i =0;i< liste_fichiers.length;i++)
			{
				if(liste_fichiers[i].isDirectory()==true)
				{
					//Voir pour mettre à true
					nbr_dossier++;
					System.out.println("Nom de dossier : "+liste_fichiers[i].getName());
					ecrire_fichier(fichier_sync,"d:"+liste_fichiers[i].getName()+":D",1);
				}
				else
				{
					this.nbr_fichier++;
					System.out.println("Nom de fichier : "+liste_fichiers[i].getName());
					if((liste_fichiers[i].getName().equals(".sync.txt")==true) || (liste_fichiers[i].getName().equals(".serv_sync.txt")==true))
					{
						System.out.println("Je suis un fichier de sync");
						ecrire_fichier(fichier_sync,"f:"+liste_fichiers[i].getName()+":S",1);
					}
					else
					{
						ecrire_fichier(fichier_sync,"f:"+liste_fichiers[i].getName()+":"+this.generer_hash(chemin+"\\"+liste_fichiers[i].getName()),1);
					}
					//ecrire_fichier(fichier_sync,"f:"+liste_fichiers[i].getName()+":"+this.generer_hash(chemin+"\\"+liste_fichiers[i].getName()),1);
				}
				if(liste_fichiers[i].isDirectory() == true && this.sous_dossier == true)
				{
					this.listerRepertoire(liste_fichiers[i].getPath(),client_req);
				}
			}
		}
		//Pour la sync
		if(fichier_sync.exists())
		{
			String[][] nouvelle_liste = creer_liste(fichier_sync);
			try {
				gsfic.gestion_sync_interne(ancienne_liste,nouvelle_liste,chemin,client_req);

				gsfic.gestion_sync_externe(nouvelle_liste,chemin,client_req);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}

	private void ecrire_fichier(File fichier, String contenu,int etat) { //Pour écrire dans un fichier
		try {
			if(etat==1)
			{
				FileWriter fw = new FileWriter(fichier.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(contenu);
				bw.newLine();
				bw.close();
			}
			else
			{
				FileWriter fw = new FileWriter(fichier.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(contenu);
				bw.newLine();
				bw.close();
			}
			

		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private String[][] creer_liste(File fichier) //Pour récupérer une liste à partir d'un fichier
	{
		BufferedReader buf_lecture =null;
		String ligne;
		int nbr_fichier;
		try {
			buf_lecture = new BufferedReader(new FileReader(fichier));
			ligne = buf_lecture.readLine();
			if(ligne==null)
			{
				nbr_fichier=0;
			}
			else
			{
				nbr_fichier=Integer.parseInt(ligne);
			}

			String[] tableau_ligne = new String[nbr_fichier]; //Reste à faire la mise en tableau multidim
			//System.out.println("Est-ce que je plante?");
			if(nbr_fichier ==1)
			{
				String[][] liste_infos = new String[1][3];
				String decoupeur[]= buf_lecture.readLine().split(":",3);
				liste_infos[0][0] = decoupeur[0]; //Récupérer fichier ou dossier
				liste_infos[0][1]=decoupeur[1]; //Récupérer le nom
				liste_infos[0][2] = decoupeur[2];
				//System.out.println("Decoupeur type : "+decoupeur[0]);
				//System.out.println("Decoupeur nom : "+decoupeur[1]);
				buf_lecture.close();
				return liste_infos;
			}
			else
			{
				for(int i =0;i<nbr_fichier;i++) //Pour mettre les lignes du fichier dans un tableau
				{
					tableau_ligne[i] = buf_lecture.readLine();
				}
				
				String[][] liste_infos = new String[nbr_fichier][3];
				for(int i=0;i<tableau_ligne.length;i++) // Pour récupérer les diverses informations dans un tableau multidim
				{
					//System.out.println("Je passe dans la découpeuse : "+(nbr_fichier-i));
					String decoupeur[]= tableau_ligne[i].split(":",0);
					liste_infos[i][0] = decoupeur[0]; //Récupérer fichier ou dossier
					liste_infos[i][1]=decoupeur[1]; //Récupérer le nom
					liste_infos[i][2]=decoupeur[2];
					//System.out.println("Decoupeur type : "+decoupeur[0]);
					//System.out.println("Decoupeur nom : "+decoupeur[1]);
				}
				buf_lecture.close();
				return liste_infos;
				
			}	
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean comparer(String[][] tabA,String[][]tabB)
	{
		if(tabA==null)
		{
			return true;
		}
		else
		{
			if(tabA.length != tabB.length)
			{
				return false;
			}
			else
			{
				for(int i=0;i<tabA.length;i++)
				{
					for(int u=0;u<3;u++)
					{
						System.out.println("Tab "+tabA.length );
						if(tabA[i][u].equals(tabB[i][u])==false)
						{
							System.out.println("Non égaux");
							return false;
						}

					}
				}
				System.out.println("Ils sont égaux");
				return true;
			}
		}

	}
	public String generer_hash(String chemin) throws NoSuchAlgorithmException
	{
    	MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream fis = new FileInputStream(chemin)) {
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
	}
		
	}

