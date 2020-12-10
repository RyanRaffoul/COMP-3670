import java.io.*;
import java.util.*;
import java.net.*;

public class ServerThread extends Thread
{ 
	private Socket socket; // used for socket
	
	// default constructor
	public JobCreatorServerThread(Socket socket)
	{
		this.socket = socket;
	}
	 
	public void run()
	{
		try {
			// used for input
			InputStream input = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			// used for output
			OutputStream output = socket.getOutputStream();
			PrintWriter pr = new PrintWriter(output,true);
			
			String option = ""; // used to get input on if to do another Host Name discovery
			String cont = ""; // used to get input from user on if to continue
			
			String start = ""; // used to check if connection accepted
			Scanner sc = new Scanner(System.in); // used for user input
			
			start = br.readLine(); // read on if to start
			
			// if accepted
			if(start.equals("yes")) {
				do {
					
					// check for another
					option = br.readLine();

					// if another
					if(option.equals("yes")) {
						// get from user if to continue
						System.out.println("Client wants another equation");
						System.out.println("Would you like to continue with this Client? (yes/no)");
						cont = sc.next();
						
						pr.println(cont);
					}else {
						System.out.println("Client terminated the connection");
					}			
					
				}while(option.equals("yes") && cont.equals("yes")); // check if another iteration is wanted
				
			}else {
				System.out.println("Client has rejected the Host Name Discovery Job and terminated the connection");
			}
			
			socket.close(); // close connection
			
		}catch(IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
	}
	
	public void WhoIsLookup()
	{
		
	}
	
	public void getIPAddress()
	{
		
	}
	
	public boolean checkIfReachable()
	{
		
	}
	public void findOpenPorts()
	{
		
	}
}