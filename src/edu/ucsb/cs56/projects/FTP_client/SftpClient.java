package edu.ucsb.cs56.projects.FTP_client;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

/**
 * A class to support the SFTP protocol
 * @author David Coffill
 */
public class SftpClient extends Client {
	private JSch client;
	ChannelSftp cSftp;
	private Vector<ChannelSftp.LsEntry> fileList;
	private String[][] stringFileList;
    private String delimiters;
    private String[] temp;

	public SftpClient() {
		client = new JSch();
		fileList = null;
		stringFileList = null;
		port = 22; // Set default SSH/SFTP port number
	}

	/**
	 * List all files in current directory, including attributes, to both stdout and stringFileList
	 * @return String array of file names
	 */
	public String[][] listFile() {
        System.out.println("*************File List************");
        fileList=null;
        stringFileList=null;
        delimiters = "[ ]+";
		try {
            System.out.println("Try...");
			fileList = cSftp.ls(".");
            System.out.println("fileList parsed!");
			int size = fileList.size();
            temp = new String[size];
            System.out.println("Try to copy vector into temp");
			stringFileList = new String[size][9];
			for (int i = 0; i < size; ++i) {
                temp[i] = fileList.get(i).toString();
                //System.out.println(temp[i]);
				stringFileList[i] = temp[i].split(delimiters);
                //System.out.println(stringFileList[i][0]);
			}
		}
		catch (SftpException ex) {
			ex.printStackTrace();
		}
		return stringFileList;
	}

	/**
	 * Establish connection to remote host via SFTP protocol
	 * @param host hostname of remote host (such as csil.cs.ucsb.edu)
	 * @param username username to authenticate with
	 * @param password password to authenticate with
	 * @return true if the connection was successful, false if unsuccessful
	 */
	public boolean connect (String host, String username, String password) {
		try {
			Session session = client.getSession(username, host, port);
			session.setPassword(password);

			// Disable strict host key checking so we avoid lots of authentication hassle
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();

			cSftp = (ChannelSftp)channel;

		}
		catch (JSchException ex) {
			System.out.println("Connection failed");
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean connect (String url, String password) {
			parseURL(url);
		return connect(hostname, username, password);
	}


	/**
	 * Change directory on the remote host
	 * @param dir directory to change into on remote host
	 */
	public void ChangeDirectory(String dir) {
		try {
			cSftp.cd(dir);
		}
		catch (SftpException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Check if the specified filename is actually a file (as opposed to a directory, etc.)
	 * @param filename filename to check
	 * @return true if filename is a file, false otherwise
	 */
	public boolean isFile(String filename) {
		for(ChannelSftp.LsEntry entry : fileList) {
			if (filename.equals(entry.getFilename()) && !(entry.getAttrs().isDir()) ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Download a file from the remote host to the current directory on the local machine
	 * @param input file name to retrieve from the remote host
	 */
	public void download(String input){
		if (isFile(input)) {
			try {
				File file = new File(input);
				OutputStream outputStream = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}
				cSftp.get(input, outputStream);
			}
			catch (SftpException ex) {
				ex.printStackTrace();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		else {
			System.out.println(input + " is not a file");
		}
	}


	/**
	 * Explicitly close the connection the remote host
	 */
	public void logout() {
		cSftp.disconnect();
	}


	public static void main (String[] args)	{

		System.out.println("Client start!");
		Client newClient = new SftpClient();
		System.out.println("Enter URL ( user@host[:port] ) :");
		Scanner sc = new Scanner (System.in);
		String url = sc.nextLine();

		// Ask for password without echoing input to the console
		Console c = System.console();
		char[] password = c.readPassword("Input your password: ");

		newClient.connect(url, new String(password));

		String[][] f = newClient.listFile();
		System.out.println("input file to download:");
		String input = sc.nextLine();
		newClient.download(input);
	}

}
