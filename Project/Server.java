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
		
			System.out.println("This is a Network Discovery Tool\n");
			System.out.println("Server is available at Port " +port + " looking for a Client to get a Hostname (Not IP Address) do the following: ")
			System.out.println("")
			while(true) {
				// accept and send
				Socket socket = serverSocket.accept();
				System.out.println("\nConnected to a Client\n");
				new JobCreatorServerThread(socket).start();
			}
			
    	} catch (UnknownHostException ex) {
    		System.out.println("Server not found: " + ex.getMessage());
    	} catch (IOException ex) {
    		System.out.println("I/O error: " + ex.getMessage());
    	}		
	}
}