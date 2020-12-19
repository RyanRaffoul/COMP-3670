// COMP-3670 Final Project Network Discovery Tool Server Thread
// ServerThread.java
// This class gets a hostname and does what was explained in Server.java output

// libraries used
import java.io.*;
import java.util.*;
import java.net.*;

// ServerThread: class to commmunicate with the client to complete task
// ServerThread has a socket to connect to the Client
public class ServerThread extends Thread
{ 
	private Socket socket; // used for socket
	
	// default constructor
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}
	
	// run: method to complete all the jobs explained in Server.java output
	// run recieves the socket and returns nothing
	public void run()
	{
		try {
			String getHostname = ""; // used to get hostname from the client
			String extension = ""; // used to check the hostname extension
			boolean checkExtensionFormat = false; // used to check if one of the valid extensions
			
			ArrayList<String> lookupInfo; // used to get WHOIS lookup info
			int lookupSize = 0; // used to get number of info
			
			ArrayList<String> ipAddresses; // used to get all IP Addresses
			String getIP = ""; // used to loop through all IPs
			int ipAddressesSize = 0; // used to get number of IP Addresses for Host name
			
			boolean isReachable = false; // used to check if IP is reachable
			String outputReach = ""; // used to output if reachable or if not reachable
			
			ArrayList<Integer> openPorts; // used to get all Open Ports for IP Addresses
			int portSize = 0; // used to get number of Ports that are open for IP Addresses
			int pn = 0; // used to loop through port numbers
			String portOutput = ""; // used to output Port is open
			
			String option = ""; // used to get input on if to do another Host Name discovery
			String cont = ""; // used to get input from user on if to continue
			String start = ""; // used to check if connection accepted			
			
			// used for input
			InputStream input = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			// used for output
			OutputStream output = socket.getOutputStream();
			PrintWriter pr = new PrintWriter(output,true);
			
			Scanner sc = new Scanner(System.in); // used for user input
			
			start = br.readLine(); // read on if to start
			
			// if accepted
			if(start.equals("yes")) {
				do {
					// get hostname from client and output
					getHostname = br.readLine();
					System.out.println("Received " +getHostname + " from the Client");
					System.out.println("Getting and Sending Data now");
					
					// get extension and check for one of the valid extensions
					extension = getExtension(getHostname);
					checkExtensionFormat = checkExtension(extension);
					
					// if valid extension
					if(checkExtensionFormat) {
						// call WHOIS Lookup method and send size and data to the Client
						lookupInfo = WhoIsLookup(getHostname);
						lookupSize = lookupInfo.size();
						pr.println(lookupSize);
						for(int i = 0; i < lookupSize; ++i) {
							pr.println(lookupInfo.get(i));
						}
					}else {
						// Send cannot do WHOIS Lookup to Client because of invalid extension
						String notWhoIs = "Did not do a WHOIS lookup because of incompatible extension";
						pr.println(lookupSize);
						pr.println(notWhoIs);
					}
					
					// call getIPAddresses to get all IP Addresses for Host names then get size and output to Client
					ipAddresses = getIPAddresses(getHostname);
					ipAddressesSize = ipAddresses.size();
					pr.println(ipAddressesSize);
					
					// if no IP Addresses found
					if(ipAddressesSize == 0) {
						pr.println("No IP Addresses found");
					}else{
						// first print all IP Addresses to the Client
						for(int i = 0; i < ipAddressesSize; ++i) {
							pr.println(ipAddresses.get(i));
						}
						
						// loop through all IP Addresses to check if reachable (ping request) and find all open ports if reachable
						for(int i = 0; i < ipAddressesSize; ++i) {
							getIP = ipAddresses.get(i); // get IP Address
							
							isReachable = checkIfReachable(getIP); // method call to check if reachable
							// if reachable
							if(isReachable) {
								// output to client IP Address is reachable
								outputReach = "IP Address " +getIP + " is Reachable";
								pr.println(outputReach);
								
								openPorts = findOpenPorts(getIP); // method call to get open ports
								portSize = openPorts.size(); // check size
								pr.println(portSize); // print size to client
								// if no open ports output this to client
								if(portSize == 0) {
									pr.println("No Open Ports for IP Address " +getIP);
								}else {
									// loop through port array list size and print open ports to the client
									for(int n = 0; n < portSize; ++n) {
										pn = openPorts.get(n);
										portOutput = " Port # " +pn + " is Open";
										pr.println(portOutput);
									}
								}
							// if not reachable
							}else {
								outputReach = "IP Address " +getIP + " is not Reachable";
								pr.println(outputReach);
							}
						}
					}
					System.out.println("Sent all Data to the Client"); // when done
					// check for another
					option = br.readLine();

					// if another
					if(option.equals("yes")) {
						// get from user if to continue
						System.out.println("Client wants the Server to do another Network Discovery Job");
						System.out.println("Would you like to continue with this Client? (yes/no)");
						cont = sc.next();
						
						pr.println(cont);
					}else {
						System.out.println("Client terminated the connection");
					}			
					
				}while(option.equals("yes") && cont.equals("yes")); // check if another iteration is wanted
				
			}else {
				System.out.println("Client has terminated the connection");
			}
			
			socket.close(); // close connection
			
		}catch(IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}
	}
	
	// WhoIsLookup: connect to the WhoIsLookup Server and get all the information about the Hostname
	// Receives a hostname and Returns all the information in a Array List
	// WHOISLOOKUP Server is a server that can fetch a bunch of interesting information about any hostname
	public ArrayList<String> WhoIsLookup(String hostname)
	{
		ArrayList<String> a = new ArrayList<String>(); // used to store data
		String hostname1 = "whois.internic.net"; // WHOIS server
		int port = 43; // port to listen
		
		// connect to server
		try(Socket s = new Socket(hostname1,port)){
			// used for input
			OutputStream o = s.getOutputStream();
			PrintWriter p = new PrintWriter(o, true);
			p.println(hostname); // send hostname to server
			
			// used for output
			InputStream i = s.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(i));
			
			// add all information to array list and only store first 20 lines as output can be very long
			String lines = "";
			int count = 0;
			while((lines = r.readLine()) != null) {
				a.add(lines);
				++count;
				if(count == 20) {
					break;
				}
			}
		}catch(UnknownHostException e) {
			System.out.println("Server not found: " + e.getMessage());
		}catch(IOException e) {
			System.out.println("I/O error: " + e.getMessage());
		}
		return a;
		
	}
	
	// getIPAddresses: get all the IP addresses for a host name
	// Recieves a host name and Returns all the information in a ArrayList
	public ArrayList<String> getIPAddresses(String hostname)
	{
		ArrayList<String> ipAdds = new ArrayList<String>(); // used to store data
		try {
			// get all IP Addresses and add to the ArrayList
			InetAddress[] hosts = InetAddress.getAllByName(hostname);
			for(InetAddress host: hosts) {
				ipAdds.add(host.getHostAddress());
			}
		}catch(UnknownHostException e) {
			System.out.println("Server not found: " +e.getMessage());
		}
		return ipAdds;
	}
	
	// checkIfReachable: check if an IP Address is reachable by sending a ping request
	// Recieves a IP Address and Returns true if it is reachable or false if it is not
	public boolean checkIfReachable(String ip)
	{
		try {
			// store ip in InetAddress then send ping request
			InetAddress inet = InetAddress.getByName(ip);
			// send ping request and check if reachable
			if(inet.isReachable(5000)) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			System.out.println("IP NOT FOUND");
			return false;
		}
	}
	
	// findOpenPorts: find all the open ports for an IP Address and store in ArrayList
	// Receieves an IP Address and Returns all the open ports in an ArrayList
	public ArrayList<Integer> findOpenPorts(String ip)
	{
		ArrayList<Integer> a = new ArrayList<Integer>(); // used to store open ports
		
		// loop through all the possible port numbers
		for(int port = 1; port <= 65535; ++port) {
			try {
				// check if can connect using port and add those that are open
				Socket so = new Socket();
				socket.connect(new InetSocketAddress(ip,port),1000);
				socket.close();
				a.add(port);
			}catch(Exception e) {
			}
		}
		return a;
	}
	
    // getExtension: get the Hostname extension
    // Receives a hostname and Returns the extension
    public String getExtension(String hostname) 
    {
    	String result = ""; // used to store extension
    	
    	// used to check if an .extension is present and get last occurence
        if(hostname.lastIndexOf(".") != -1 && hostname.lastIndexOf(".") != 0) {
        	// store and return
        	result = hostname.substring(hostname.lastIndexOf(".")+1);
        	return result;
        }else {
        	return result; // if no extension
        }
    }
    
    // checkExtension: get an extension and check if one of the valid extensions
    // Receieves a extension and Returns if it is one of the valid extensions
    public boolean checkExtension(String e)
    {
    	String[] extensions = {"com", "net", "edu"}; // all the valid extensions for a WHOISLOOKUP
    	int n = 3; // array size
    	boolean check = false; // used to check if one of the valid extensions
    	
    	// loop through and check if one of the valid hostname extensions.
    	for(int i = 0; i < n; ++i ) {
    		if(e.equalsIgnoreCase(extensions[i])) {
    			check = true;
    			break;
    		}
    	}
    	return check;
    }
}
