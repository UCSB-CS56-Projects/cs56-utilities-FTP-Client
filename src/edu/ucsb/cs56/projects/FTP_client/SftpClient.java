package edu.ucsb.cs56.projects.FTP_client;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPFile;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by David Coffill on 2/20/14.
 */
public class SftpClient implements Client {
	private JSch client;
	ChannelSftp cSftp;
	private ChannelSftp.LsEntry fileList;
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
			Vector<ChannelSftp.LsEntry> fileList = cSftp.ls(".");

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
		//@@@ FIXME stub
	}

	public boolean isFile(String filename) {
		// @@@FIXME stub;
		return false;
	}

	public void download(String input){
		// @@@ FIXME stub;
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
	}

}
