import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnexionManager {

	public ConnexionManager() {
		int exec =1;
		int nbThread=1;
	
		ServerSocket sSocket =null;
		Socket client =null;
		try {
			sSocket = new ServerSocket(8000,1);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(exec==1)
		{
			try {
				client =sSocket.accept();
				System.out.println("Connexion reçu");

				
			}catch(IOException e) {
				e.printStackTrace();
			}
			if(client!=null)
			{
				Serveur s = new Serveur(nbThread, client, sSocket);

				client =null;
				nbThread++;
				System.out.println(exec + " " +nbThread);
			}
			client=null;
		}
	}

}
