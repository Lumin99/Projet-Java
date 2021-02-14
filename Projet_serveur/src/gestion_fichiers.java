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
	
	public void gestion_sync_interne(String[][] ancienne,String[][] nouvelle,String chemin) //Pour gérer la sync (nvx fichiers, suppression,...)
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
						//Commande au serveur pour la suppr
						System.out.println("Supression");
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
							System.out.println("Il y a un ajout");
							//commande Ajouter au serv
						}
					}
				}
			}
			else if(ancienne.length==nouvelle.length) // A voir si on le fait
			{
				
			}
		}
		
	}
	
	public void gestion_sync_externe(String[][] client,String[][] serveur,String chemin) //Pour gérer la sync avec le serveur
	{
		Liste_fichier ls_fic = new Liste_fichier();
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
						//Commande pour demander un fichier (NEED)
					}
				}
			}//Fin de NEED
			
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
						asupr.delete(); //Pour supprimer le fichier
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

}
