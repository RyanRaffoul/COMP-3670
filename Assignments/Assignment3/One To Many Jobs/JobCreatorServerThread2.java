//Assignment 3: Job Creator Server Thread for One-To-Many Questions
//Donovan Longo
//This class connects to a Job Seeker and communicates with it to complete ICMP/UDP flood attacks and terminate the connection

import java.io.*;
import java.util.*;
import java.net.*;
import org.icmp4j.*;

public class JobCreatorServerThread2 extends Thread {
private Socket socket; // used for socket
	
	// constructor
	public JobCreatorServerThread2(Socket socket)
	{
		this.socket = socket;
	}
	 
	// method for communication with Job Seeker
	public void run()
	{
		try {
			//input
			InputStream input = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			//output
			OutputStream output = socket.getOutputStream();
			PrintWriter pr = new PrintWriter(output,true);
			
			String option = ""; // used to get input on if to do another equation
			String cont = ""; // used to get input from user on if to continue
			// used for IP Address, port, and result
			String ipH = "";
			int givenPort = 0;
			String result = "";
			
			String start = ""; // used to check if connection accepted
			Scanner sc = new Scanner(System.in); // used for user input
			
			start = br.readLine(); // read on if to start
			
			// if accepted
			if(start.equals("yes")) {
				do {
					// check for option (question 1 or 2)
					int getJob = 0;
					System.out.println("Would you like execute ICMP flood attack (enter 0) or execute UDP flood attack (enter 1)?");
					getJob = sc.nextInt();
					
					// send option to Job Seeker
					pr.println(getJob);
					
					if(getJob == 0) {
						// get the IP Address or host name from the user and send to Job Seeker
						System.out.print("Enter the IP Address or Host Name: ");
						ipH = sc.next();
						pr.println(ipH);
						
						result = br.readLine(); // get result from Job Seeker
	
						// output result
						System.out.println("\nCompleted Successfully\nJob Seeker reports: " +result + "\n");
						
					}else {
						// get the IP Address or Host Name
						System.out.print("Enter the IP Address or Host Name: ");
						ipH = sc.next();
						pr.println(ipH); // send IP Address to Job Seeker
						
						System.out.print("Enter a Port Number between 1 and 49190: ");
						givenPort = sc.nextInt();
						pr.println(givenPort);
						
						
						
						// get the result and output
						result = br.readLine();
						System.out.println("\nCompleted Successfully\nJob Seeker reports: " +result + "\n");
					}
					
					// check for another
					option = br.readLine();

					// if another
					if(option.equals("yes")) {
						// get from user if to continue
						System.out.println("Job Seeker wants another Job");
						System.out.println("Would you like to continue with this Job Seeker? (yes/no)");
						cont = sc.next();
						
						pr.println(cont);
					}else {
						System.out.println("Job Seeker terminated the connection");
					}			
					
				}while(option.equals("yes") && cont.equals("yes")); // check if another iteration is wanted
				
			}else {
				System.out.println("Job Seeker has rejected the check IP Address/Host Name job or check the status of a given Port at a specific IP Address/Host Name job and terminated the connection");
			}
			
			socket.close(); // close connection
			
		}catch(IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
	}
}
