import java.io.File;

public class main {

	public static void main(String[] args) 
	{
		File init_dos =new File("Fichiers");
		if(init_dos.exists()==false)
		{
			init_dos.mkdir();
		}
		ConnexionManager gesco = new ConnexionManager();

	}

}
