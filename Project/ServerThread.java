import java.io.*;
import java.util.*;
import java.net.*;

public class ServerThread extends Thread
{ 
	private Socket socket; // used for socket
	
	// default constructor
	public ServerThread(Socket socket)
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
			
			String getHostname = "";
			String extension = "";
			boolean checkExtensionFormat = false;
			
			ArrayList<String> lookupInfo;
			int lookupSize = 0;
			
			int numIpAddresses;
			ArrayList<String> ipAddress;
			String getIP = "";
			int ipAddressSize = 0;
			
			boolean isReachable = false;
			String outputReach = "";
			
			ArrayList<int> openPorts;
			int portSize = 0;
			String portOutput = "";
			
			// if accepted
			if(start.equals("yes")) {
				do {
					
					getHostname = br.readLine();
					extension = getExtension(getHostname);
					checkExtensionFormat = checkExtension(extension);
					
					// WHOIS LOOKUP
					if(checkExtensionFormat) {
						lookupInfo = WhoIsLookup();
						lookupSize = lookupInfo.size();
						for(int i = 0; i < lookupSize; ++i) {
							pr.println(lookupInfo.get(i));
						}
					}else {
						String notWhoIs = "Did not do a WHOIS lookup because of incompatible extension";
						pr.println(lookupSize);
						pr.println(notWhoIs);
					}
					
					ipAddresses = getIPAddresses();
					ipAddressSize = ipAddresses.size();
					pr.println(ipAddressSize);
					
					if(ipAddressSize == 0) {
						pr.println("No IP Addresses found");
					}else{
						// FIRST PRINT ALL THE IP ADDRESSES
						for(int i = 0; i < ipAddressSize; ++i) {
							pr.println(ipAddresses.get(i));
						}
						
						for(int i = 0; i < ipAddressesSize; ++i) {
							getIP = ipAddresses.get(i);
							
							isReachable = checkIfReachable(getIP);
							if(isReachable) {
								outputReach = "IP Address " +getIP + " is Reachable";
								pr.println(outputReach);
								
								openPorts = findOpenPorts();
								portSize = openPorts.size();
								pr.println(portSize);
								if(portSize == 0) {
									pr.println("No Open Ports for IP Address " +getIP);
								}else {
									for(int n = 0; n < portSize; ++n) {
										portOutput = " Port # " +openPorts.get(n) + " is Open";
										pr.println(portOutput);
									}
								}
							}else {
								outputReach = "IP Address " +getIP + " is not Reachable";
								pr.println(outputReach);
							}
							
							
						}
					}
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
	
	public ArrayList<String> WhoIsLookup()
	{
		
	}
	
	public ArrayList<String> getIPAddress()
	{
		
	}
	
	public boolean checkIfReachable()
	{
		return true;
	}
	
	public ArrayList<int> findOpenPorts()
	{
		
	}
	
    public String getExtension(String hostname) 
    {
    	String result = "";
    	
        if(hostname.lastIndexOf(".") != -1 && hostname.lastIndexOf(".") != 0) {
        	result = hostname.substring(hostname.lastIndexOf(".")+1);
        	return result;
        }else {
        	return result;
        }
    }
    
    public boolean checkExtension(String e)
    {
    	String[] extensions = {"com", "ca", "net", "org", "uk", "us", "cn", "edu"};
    	int n = 8;
    	boolean check = false;
    	
    	for(int i = 0; i < n; ++i ) {
    		if(e.equalsIgnoreCase(extensions[i])) {
    			check = true;
    			break;
    		}
    	}
    	
    	return check;
    }
}