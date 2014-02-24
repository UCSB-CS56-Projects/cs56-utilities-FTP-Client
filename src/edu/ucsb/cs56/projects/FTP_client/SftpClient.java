package edu.ucsb.cs56.projects.FTP_client;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by David Coffill on 2/20/14.
 */
public class SftpClient implements Client {
	private JSch client;
	ChannelSftp cSftp;
	private Vector<ChannelSftp.LsEntry> fileList;
	private String[] stringFileList;

	public SftpClient() {
		client = new JSch();
		fileList = null;
		stringFileList = null;
	}

	public String[] listFile() {
		System.out.println("*************File List************");
		fileList=null;
		stringFileList=null;
		try {
			fileList = cSftp.ls(".");

			int size = fileList.size();
			stringFileList = new String[size];
			for (int i = 0; i < size; ++i) {
				stringFileList[i] = fileList.get(i).toString();
				System.out.println(stringFileList[i]);

			}
		}
		catch (SftpException ex) {
			ex.printStackTrace();
		}
		return stringFileList;
	}

	public boolean connect (String host, String username, String password) {
		try {
			int port = 22;
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

	public void ChangeDirectory(String dir) {
		try {
			cSftp.cd(dir);
		}
		catch (SftpException ex) {
			ex.printStackTrace();
		}
	}

	public boolean isFile(String filename) {
		// Check filename against each entry in the directory.  If filename isn't a directory, return true
		for(ChannelSftp.LsEntry entry : fileList) {
			if (filename.equals(entry.getFilename()) && !(entry.getAttrs().isDir()) ) {
				return true;
			}
		}

		return false;
	}

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
	}


	public void logout() {
		cSftp.disconnect();
	}

	public static void main (String[] args)	{

		System.out.println("Client start!");
		Client newClient = new SftpClient();
		System.out.println("input host name:");
		Scanner sc = new Scanner (System.in);
		String host = sc.nextLine();

		System.out.println("Input username: ");
		String username = sc.nextLine();

		// Ask for password without echoing input to the console
		Console c = System.console();
		char[] password = c.readPassword("Input your password: ");

		newClient.connect(host, username, new String(password));

		String[] f = newClient.listFile();
		System.out.println("input file to download:");
		String input = sc.nextLine();
		newClient.download(input);
	}

}
