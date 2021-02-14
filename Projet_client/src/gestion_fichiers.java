import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class gestion_fichiers {
	
	public gestion_fichiers()
	{
		
	}
	
	public void suppresion(File fichier)
	{
		fichier.delete();
	}
	
	public void crea_dossier()
	{
		
	}
	
	public void gestion_sync_interne(String[][] ancienne,String[][] nouvelle,String chemin,Client client_req) throws IOException //Pour gérer la sync (nvx fichiers, suppression,...)
, InterruptedException
	{
		Liste_fichier ls_fic = new Liste_fichier();
		if(ls_fic.comparer(ancienne, nouvelle)==false) // Si ils sont pas égaux
		{
			if(ancienne.length>nouvelle.length) //Il y a une suppression
			{
				for(int i =0;i<ancienne.length;i++)
				{
					int occu =0;
					for(int u=0;u<nouvelle.length;u++)
					{
						if(ancienne[i][1].equals(nouvelle[u][1])==true)
						{
							occu++;
						}
					}
					if(occu == 0)
					{
						client_req.DELETE(chemin+"\\"+ancienne[i][1]);
					}

				}
			} //Fin de si la suppr
			else if(ancienne.length<nouvelle.length) //Ajout d'un fichier
			{
				for(int i=0;i<nouvelle.length;i++)
				{
					int occu =0;
					for(int u=0;u<ancienne.length;u++)
					{
						if(nouvelle[i][1].equals(ancienne[u][1]))
						{
							occu++;
						}
						if(occu==0)
						{
							//commande Ajouter au serv
							client_req.ADD(chemin+"\\"+nouvelle[i][1]);
						}
					}
				}
			}
			else if(ancienne.length==nouvelle.length) // A voir si on le fait
			{
				for(int i=0;i<ancienne.length;i++) //Comparer ancienne vers nouvelle
				{
					int occu=0;
					for(int u=0;u<ancienne.length;u++)
					{
						if(ancienne[i][1].equals(nouvelle[u][1])==true)
						{
							occu++;
						}
						
					}
					if(occu==0)
					{
						client_req.DELETE(chemin+"\\"+ancienne[i][1]);
					}
					
				}
				for(int i=0;i<ancienne.length;i++) //Comparer nouvelle vers ancienne
				{
					int occu=0;
					for(int u=0;u<ancienne.length;u++)
					{
						if(nouvelle[i][1].equals(ancienne[u][1]))
						{
							occu++;
						}
					}
					if(occu==0)
					{
						client_req.ADD(chemin+"\\"+nouvelle[i][1]);
					}
				}
			}
		}
		
	}
	
	public void gestion_sync_externe(String[][] client,String chemin,Client client_req) //Pour gérer la sync avec le serveur
	{
		Liste_fichier ls_fic = new Liste_fichier();
		try {
			client_req.NEED(chemin+"\\"+".serv_sync.txt");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File Fichier_syncServ = new File(chemin+"\\"+".serv_sync.txt");
	/*	if(Fichier_syncServ.exists())
		{
			
		} */
		String[][] serveur = ls_fic.recup_liste(Fichier_syncServ);
		if(ls_fic.comparer(client, serveur)==false) // S'il ne sont pas égaux
		{
			if(client.length<serveur.length) // Dans le cas ou il manque un fichier chez le client
			{
				for(int i=0;i<serveur.length;i++)
				{
					int occu=0;
					for(int u=0;u<client.length;u++)
					{
						if(serveur[i][1].equals(client[u][1])==true)
						{
								occu++;
						}
					}
					if(occu==0)
					{
						System.out.println("Valeur du tab = "+serveur[i][0]);
						//Commande pour demander un fichier (NEED)
						if(serveur[i][0].equals("d"))
						{
							File doss = new File(chemin+"\\"+serveur[i][1]);
							System.out.println("Je crée un dossier");
							doss.mkdir();
						}
						else
						{
							try {
								client_req.NEED(chemin+"\\"+serveur[i][1]);
							} catch (IOException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
			}//Fin de NEED
			else if(client.length==serveur.length)
			{
				for(int i=0;i<serveur.length;i++)
				{
					int occu=0;
					for(int u=0;u<client.length;u++)
					{
						if(serveur[i][1].equals(client[u][1])==true)
						{
							if(serveur[i][2].equals(client[u][2])==true)
							{
								occu++;
							}

						}
					}
					if(occu==0)
					{
						System.out.println("Valeur du tab = "+serveur[i][0]);
						//Commande pour demander un fichier (NEED)
						if(serveur[i][0].equals("d"))
						{
							File doss = new File(chemin+"\\"+serveur[i][1]);
							System.out.println("Je crée un dossier");
							doss.mkdir();
						}
						else
						{
							try {
								File asupr = new File(chemin+"\\"+serveur[i][1]);
								client_req.NEED(chemin+"\\"+serveur[i][1]);
							} catch (IOException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
			}
			
			else if(client.length>serveur.length) //Dans le cas ou le client doit suppr des fichiers
			{
				for(int i=0;i<client.length;i++)
				{
					int occu=0;
					for(int u=0;u<serveur.length;u++)
					{
						if(client[i][1].equals(serveur[u][1])==true)
						{
							occu++;
						}
					}
					if(occu ==0)
					{
						File asupr = new File(chemin+"\\"+client[i][1]);
						deleteDirectory(asupr);
					}
				}
			} //Fin Suppr
		}
	}
	
	public void run()
	{
		int exec =1;
		while(exec==1)
		{

		}
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
