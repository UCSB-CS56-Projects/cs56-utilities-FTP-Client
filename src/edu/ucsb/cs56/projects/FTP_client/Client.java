

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


public interface Client {
	/** Constructor
	*/

	/**
	*	Change directory
	* 	@param target directory
	*/
	
	public void ChangeDirectory(String dir);


	/** 
	*	Show file list on current directory
	*/
	
	//public FTPFile[] listFile();
	public String[] listFile();

	/** 
	*	Determine if the file is a regular file
	*/
	
	public boolean isFile(String filename);
	
	/** 
	*	Download file user input on current directory.
	* 	@param file name to download
	*/
	public void download(String input);
	
	public boolean connect (String host, String username, String password);
	public void logout();
}