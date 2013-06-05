
package edu.ucsb.cs56.projects.FTP_client;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class ClientGui {
	
	private JFrame frame 			;
	private JPanel displayPanel		;
	private JPanel loginPanel 		;
	private JPanel downloadPanel	;
	private JButton connectButton	;
	private JButton downloadButton	;
	private JButton logoutButton	;
	private JLabel	hostLabel		;
	private JLabel	fileListLabel	;
	private JTextField hostField 	;
	
	private Client newClient 		;
	
	public ClientGui()	{
		 frame 			= new JFrame("FTP Client");
		 displayPanel	= new JPanel();
		 loginPanel 	= new JPanel();
		 downloadPanel 	= new JPanel();
		 connectButton	= new JButton("Connect");
		 downloadButton	= new JButton("Download");
		 logoutButton	= new JButton("Logout");
		 hostLabel		= new JLabel("Host: ");
		 fileListLabel	= new JLabel("File List");
		 hostField 		= new JTextField(50);
		 newClient 		= new Client();
		
	}
	
	public void buildGui()	{
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
}
