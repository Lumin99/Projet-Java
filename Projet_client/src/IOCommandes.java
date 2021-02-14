
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

public class IOCommandes {
	private BufferedReader lectureEcran;
	private PrintStream ecritureEcran;
	private BufferedReader lectureReseau;
	private PrintStream ecritureReseau;
	
	public IOCommandes() {
		lectureEcran = new BufferedReader(new InputStreamReader(System.in));
		ecritureEcran =	new PrintStream(System.out);
	}
	
	public IOCommandes(Socket so) throws IOException{
    	lectureEcran = new BufferedReader(new InputStreamReader(System.in));
    	ecritureEcran =	new PrintStream(System.out);
    	lectureReseau = new BufferedReader(new InputStreamReader(so.getInputStream()));
		ecritureReseau = new PrintStream(so.getOutputStream());
    }
    
    public void ecrireReseau(String texte){
    	ecritureReseau.println(texte);
    }
    
    public String lireReseau() throws IOException{
    	return lectureReseau.readLine();
    }
    
    public void ecrireEcran(String texte){
    	ecritureEcran.println(texte);
    }
    
    public String lireEcran() throws IOException{
    	return lectureEcran.readLine();
    }
}
