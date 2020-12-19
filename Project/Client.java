// COMP-3670 Final Project Network Discovery Tool Client
// Client.java
// This class connects to a Server and receives data
// and outputs the information to a text file


import java.io.*;
import java.net.*;
import java.util.*;



public class Client {

	public static void main(String [] args)
	{
		//IP and Port variables to connect to the server
		String ip = "";
		int port = 8080;
		
		//Variables needed to store user inputs
		String start ="";
		String hostName = "";
		String option = "";
		String cont = "";
		String fileName = "";
		String reachable = "";
		int methodSize;
		int portSize;
		
		//Scanner to get user input
		Scanner sc = new Scanner(System.in);
		
		//Get the Ip the of the Server the Client want to connect to
		System.out.println("Enter ip of server you would like to connect to: ");
		ip = sc.next();
		
		
		try(Socket socket = new Socket(ip, port))
		{
			//Variables for reading and writing from/to the server
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			PrintWriter pw = new PrintWriter(output, true);
			
			//Confirm the user would like to connect to the server
			System.out.println("Valid server, would you like to connect? (yes/no)");
			start = sc.next();
			pw.println(start);
			
			//User confirmed the connection
			if(start.equals("yes"))
			{
				do {
				
				//Variable for creating and writing into a text file
				PrintWriter writer = new PrintWriter("DiscoveryData.txt");
				
				//Ask user for hostname and send it to the server
				System.out.println("\nConnected, Enter Hostname for server to:");
				System.out.println("1 - Perform a WHOIS Lookup for Hostnames that qualify");
				System.out.println("2 - Find all IP Addresses for this Hostname");
				System.out.println("3 - For all IP Addresses found: Check if the IP is reachable and find all Open Ports");
				hostName = sc.next();
				pw.println(hostName);
				
				System.out.println("The network data will be stored into the text file: DiscoveryData.txt");
				
				//WhoisCheck
				methodSize = Integer.parseInt(br.readLine());
				
				if(methodSize > 0)
				{
					//Write all the data from the WHOIS lookup into the text file
					writer.println("WHOIS Lookup:");
					for(int i = 0; i < methodSize; ++i) {
						writer.println(br.readLine());
					}
					
					writer.println("-");
					writer.println("-");
					writer.println("-");
				}
				else
				{
					//WHOIS lookup failed
					System.out.println("Error Server Message: " + br.readLine());
					writer.println("Error when attempting WHOIS lookup");
				}
				
				//IPAddress Check
				methodSize = Integer.parseInt(br.readLine());
				if(methodSize > 0)
				{
					//Write all IP Addresses into the text file
					writer.println("All IP Addresses found:");
					for(int i = 0; i < methodSize; ++i) {
						writer.println(br.readLine());
					}
					
					writer.println("-");
					writer.println("-");
					writer.println("-");
					
					//Loop for reading if Ip's are reachable and writes into a text file
					//If they are also reads the open ports and writes them into a text file
					for(int i = 0; i < methodSize; ++i) {
						
						reachable = br.readLine();
						
						if(reachable.contains("is not Reachable"))
						{
							writer.println(reachable);
						}
						
						else
						{
							writer.println(reachable);
							portSize = Integer.parseInt(br.readLine());
							if(portSize > 0)
							{
								for(int n = 0; n < portSize; ++n)
								{
									writer.println(br.readLine());
								}
							}
							
							else
							{
								writer.println(br.readLine());
							}
						}
						
					}
					
					writer.println("-");
					writer.println("-");
					writer.println("-");
					
				}
				else
				{
					System.out.println("Error Server Message: " + br.readLine());
				}
				
				
				
				
				//Close the file writer
				writer.close();
				
				
				//Ask if the user would like to complete another iteration of data collection
				System.out.println("\nWould you like to give the server another HostName? (yes/no)");
				option = sc.next();
				
				//Send to server if connection will continue or terminated
				pw.println(option);
				
				//Check if the Server terminated connection
				if((cont=br.readLine()) != null)
				{
					if(cont.equals("no")) {
						System.out.println("Server has terminated the connection");
					}
				}
				} while(option.equals("yes") && cont.equals("yes"));
			}
			
			//User denied the connection
			else
			{
				pw.println(start);
			}
			
			socket.close();
		
		} 
		
		//To catch an error if the server could not be connected to
		catch (UnknownHostException er) 
		{
			System.out.println("Server could not be found: " + er.getMessage());
		}
		
		//To catch a user input error
		catch (IOException er)
		{
			System.out.println("I/O error: " + er.getMessage());
		} 
		
	} 
	
}