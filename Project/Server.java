import java.io.*;
import java.util.*;
import java.net.*;

public class JobCreatorServer
{
	public static void main(String[] args)
	{
		// port number, can change
		int port = 8080;
		
		try(ServerSocket serverSocket = new ServerSocket(port)){
		
			// outputs to display what the Server does
			System.out.println("This is a Network Discovery Tool\n");
			System.out.println("Server is available at Port " +port + " looking for a Client to get a Hostname (Not IP Address) do the following: ");
			System.out.println("Do a WHOIS Lookup for Hostnames that qualify");
			System.out.println("Find all IP Addresses for this Hostname");
			System.out.println("For all IP Addresses found: Check if the IP is reachable and find all Open Ports\n");
		
			while(true) {
				// accept and send to the Server Thread
				Socket socket = serverSocket.accept();
				System.out.println("\nConnected to a Client\n");
				new ServerThread(socket).start();
			}
			
    	} catch (UnknownHostException ex) {
    		System.out.println("Server not found: " + ex.getMessage());
    	} catch (IOException ex) {
    		System.out.println("I/O error: " + ex.getMessage());
    	}		
	}
}