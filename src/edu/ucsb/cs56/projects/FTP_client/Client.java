package edu.ucsb.cs56.projects.FTP_client;

import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;
public class Client {
	private FTPClient client;
	
	public Client()	{
		client = new FTPClient();
	}
	public void go ()	{
		System.out.println("input host name:");
		Scanner sc = new Scanner (System.in);
		String host = sc.nextLine();
		try {
			client.connect(host);
			System.out.println("Connected to " + host + ".");
			System.out.println(client.getReplyString());
			client.login("anonymous", "anonymous");

			int reply = client.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply))	{
				client.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
				}
			System.out.println("*************File List************");
			FTPFile [] files = client.listFiles();
			for(FTPFile f : files)	
				System.out.println(f.toString());
			}
		catch(IOException e) {
			System.out.println("Connection fail.");
		} 
		
	}
	public static void main (String args[])	{
		
		System.out.println("Client start!");
		Client newClient = new Client();
		newClient.go();
	}
	
}
