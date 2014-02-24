

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
 * An interface that different transfer protocols can implement
 * @version CS56, W14
 * @author David Coffill
 */


public interface Client {

	/**
	*	Change directory
	* 	@param target directory
	*/
	public void ChangeDirectory(String dir);


	/**
	 * List all files in current directory
	 * @return String array of file names
	 */
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


	/**
	 * Establish connection to remote host
	 * @param host hostname of remote host (such as csil.cs.ucsb.edu)
	 * @param username username to authenticate with
	 * @param password password to authenticate with
	 * @return true if the connection was successful, false if unsuccessful
	 */
	public boolean connect (String host, String username, String password);


	/**
	 * Explicitly close the connection the remote host
	 */
	public void logout();
}