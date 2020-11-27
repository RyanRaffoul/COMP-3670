// Assignment 3: Job Seeker Client
// Ryan Raffoul
// JobSeekerClient.java
// This class simply connects to a Job Creator and communicates with it to complete the task and terminate the connection

package com.Comp3670Assignment3;

// libraries used
import java.io.*;
import java.util.*;
import java.net.*;

// JobSeekerClient: Job Seeker to communicate with Job Creator
public class JobSeekerClient
{
	public static void main(String[] args)
	{
		// ip and port number, could change, used to run on local machine
		String ip = "127.0.0.1";
		int port = 8080;
		
		// used for socket
		try(Socket socket = new Socket(ip,port)){
			System.out.println("Job Seeker is available looking for a Job Creator to send a IP Address/Host Name to check if it is connected to the Network or check the status of a given Port at a specific IP Address/Host Name");
			
			// used for output
			OutputStream output = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(output,true);
			
			// used for input
			InputStream input = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			String cont = "yes"; // used to check if Job Creator has terminated the connection
			String start = ""; // used to check if connection is accepted
	
			Scanner sc = new Scanner(System.in); // used to get input
			
			// check if job is wanted
			System.out.println("Do you want to accept a job from this Job Creator? (yes/no)");
			start = sc.next();
			pw.println(start); // send Job is wanted to Job Creator
			
			String result = "";
			String option = "yes";
			
			// if wanted
			if(start.equals("yes")) {
				do {	
					
					// get the Job option
					int getProgram = Integer.parseInt(br.readLine());
					// 0 is Question 1
					if(getProgram == 0) {
						// get the IP Address/Host Name to check
						String ipH = "";
						ipH = br.readLine();
						
						// output IP Address/Host Name
						System.out.println("\nChecking if " +ipH + " is Connected to the Network\n");
						
						int timeout = 2000; // set timeout
						
						// check if the IP Address/Host Name is reachable
						try {
							InetAddress[] address = InetAddress.getAllByName(ipH);
							for(InetAddress a: address) {
								if(a.isReachable(timeout)) {
									result = "Yes";
								}else {
									result = "No";
								}
							}						
						}catch(UnknownHostException ex) {
							result = "Error";
						}
						
						// send result to Job Creator
						System.out.println("Sending Result " +result + " to Job Creator");
						pw.println(result);
					}else {
						// this is Question 2
						// IP Address/Host Name, Port Number, and timeout
						String checkIP = "";
						int getPort = 0;
						int checkTimeout = 2000;
						
						// get the IP Address/Host Name from Job Creator
						checkIP = br.readLine();
						getPort = Integer.parseInt(br.readLine());
						
						// method call
						boolean portResult = checkPortStatus(checkIP,getPort,checkTimeout);
						
						// output and send result to Job Creator
						System.out.println("Checking the status of Port Number " +getPort + " at IP Address/Host Name " +checkIP);
						System.out.println("Sending the result to Job Creator");
						pw.println(portResult);
					}
					
					// ask if another is wanted
					System.out.println("\nWant to do another Job? (yes/no)");
					option = sc.next();
					
					// send option to Job Creator
					pw.println(option);
					
					// check if Creator terminated
					if((cont=br.readLine()) != null) {
						if(cont.equals("no")) {
							System.out.println("Job Creator terminated the Connection");
						}
					}				
					
				}while(option.equals("yes") && cont.equals("yes")); // check if another iteration is wanted
				
			}else {
				pw.println(start); // if rejected
			}

			socket.close(); // close connection
			
    		} catch (UnknownHostException ex) {
    			System.out.println("Server not found: " + ex.getMessage());
    		} catch (IOException ex) {
    			System.out.println("I/O error: " + ex.getMessage());
    		}		
	}
	
	// checkPortStatus: method to check if a given Port is available at the IP Address/Host Name
	// Input: the IP Address/Host Name, port number, and timeout
	// Output: return true if open or false if closed or filtered
	private static boolean checkPortStatus(String address, int port, int timeout)
	{
		// try to connect and see
		try {
			try(Socket s = new Socket()){
				s.connect(new InetSocketAddress(address,port),timeout);
			}
			return true;
		}catch(IOException e) {
			return false;
		}
	}
}

