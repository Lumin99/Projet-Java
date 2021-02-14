import java.io.File;
import java.io.IOException;

public class main {

	public static void main(String[] args) {
		File init_dos =new File("Fichiers");
		if(init_dos.exists()==false)
		{
			init_dos.mkdir();
		}
		
		Client client = new Client();
		System.out.println("Je passe avant le start");

			client.start();

		int i =1;
		while(i==1)
		{
			
		}
	}

}
