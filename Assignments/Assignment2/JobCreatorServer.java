// Assignment 2: Job Creator Server
// Ryan Raffoul
// JobCreatorServer.java
// This class simply connects to a Job Seeker and sends to the Job Creator thread

// libraries used
import java.io.*;
import java.util.*;
import java.net.*;

// JobCreatorServer: class to establish connection and send to thread
public class JobCreatorServer
{
	public static void main(String[] args)
	{
		// port number, can change
		int port = 8080;
		
		// accept a connection from a job seeker and send to thread to start
		try(ServerSocket serverSocket = new ServerSocket(port)){
			System.out.println("The Job Creator is available at Port " +port + " looking for a Job Seeker to add 2 numbers");
		
			while(true) {
				// accept and send
				Socket socket = serverSocket.accept();
				System.out.println("\nConnected to a Job Seeker\n");
				new JobCreatorServerThread(socket).start();
			}
			
    	} catch (UnknownHostException ex) {
    		System.out.println("Server not found: " + ex.getMessage());
    	} catch (IOException ex) {
    		System.out.println("I/O error: " + ex.getMessage());
    	}		
	}
}
