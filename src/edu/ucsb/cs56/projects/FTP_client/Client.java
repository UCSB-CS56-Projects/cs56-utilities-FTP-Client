

package edu.ucsb.cs56.projects.FTP_client;

import java.util.*;
import java.io.File;
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
import org.apache.commons.net.ftp.FTPConnectionClosedException;

/**
 * An FTP client support anonymous login and download files on current directory.
 * @version CS56, S13
 * @author Wenjie Huang  
 */

public class Client {
	private FTPClient client;
	private FTPFile[] fileList;
	/** Constructor
    */
	public Client()	{
		client = new FTPClient();
		fileList = null;
	}
	
	/** 
    *	Change directory 
    * 	@param target directory
    */
	
	public void ChangeDirectory(String dir)	{
		try {
			client.changeWorkingDirectory(dir);
		}
		catch (IOException e){ }
	}
	
	/** 
    *	Show file list on current directory 
    */
	
	public FTPFile[] listFile()	{
		System.out.println("*************File List************");
		fileList=null;
		try {
		fileList = client.listFiles();
		for(FTPFile f : fileList)	
			System.out.println(f.toString());	}
		catch (IOException e)	{}
		return fileList;
	}
	
	/** 
    *	Determine if the file is a regular file
    */
	
	public boolean isFile(String filename)	{
		for(FTPFile f : fileList)
			if(filename.equals(f.getName())&&f.isFile())
				return true;
		return false;
		
	}
	
	/** 
    *	Download file user input on current directory. 
    * 	@param file name to download
    */
	public void download(String input)	{
		
		String[] filenames = input.split("/");
		String filename = filenames[filenames.length-1];
		if(isFile(filename))	{
		try
		{
			File file = new File(filename);
			OutputStream outputstream = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			client.retrieveFile(input, outputstream);			
		}
		catch (FTPConnectionClosedException e){System.out.println("Connection closed"); }
		catch (IOException e){	}
		
		System.out.println("Download "+filename);
	
		}
		
		else System.out.println(filename+" is not a file.");
		
	}
	
	/** 
     *  Connect to server using anonymous login
     * 	@param host name
     */
	
	public boolean connect (String host)	{
		
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
			
			}
		catch(IOException e) {
			System.out.println("Connection fail.");
			return false;
		}
		return true;
	}
	
	/** 
     *  
     * 	
     */
     
    public void logout()	{
			try {
					client.logout();
				} 
			catch(IOException ioe) {	}
	}
	
	public static void main (String[] args)	{
		
		System.out.println("Client start!");
		Client newClient = new Client();
		
		System.out.println("input host name:");
		Scanner sc = new Scanner (System.in);
		String host = sc.nextLine(); 
		newClient.connect(host);
		newClient.listFile();
		System.out.println("input file to download:");
		sc = new Scanner (System.in);
		String input = sc.nextLine();
		newClient.download(input);
	}
	
}
