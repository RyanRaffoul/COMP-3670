//Assignment 3: Job Creator Server for One-To-Many Connections
//Donovan Longo & Ryan Raffoul
//This class connects to a Job Seeker communicates with it to complete ICMP/UDP flood attacks and terminate the connection
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class JobCreatorServer2 {
	public static void main(String[] args)
	{
		// port number
		int port = 8080;
		
		// accept a connection from job seekers, create new thread for each seeker
		try(ServerSocket serverSocket = new ServerSocket(port)){
			System.out.println("The Job Creator is available at Port " +port + " looking for a Job Seeker to execute UDP flood attack or execute ICMP flood attack");
		
			while(true) {
				// accept and send
				Socket socket = serverSocket.accept();
				System.out.println("\nConnected to a Job Seeker\n");
				new JobCreatorServerThread2(socket).start();
			}
			
 	} catch (UnknownHostException ex) {
 		System.out.println("Server not found: " + ex.getMessage());
 	} catch (IOException ex) {
 		System.out.println("I/O error: " + ex.getMessage());
 	}		
	}
}
