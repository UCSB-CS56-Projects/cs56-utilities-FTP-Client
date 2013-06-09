
package edu.ucsb.cs56.projects.FTP_client;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;

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
	private JScrollPane scroller	;
	private JList fileList			;
	private String selectedFile		;
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
		 hostField 		= new JTextField(20);
		 String[] ss = {"a","b","c","d","e"};
		 fileList		= new JList();
		 scroller		= new JScrollPane(fileList);
		
		 
		 newClient 		= new Client();
		
	}
	
	public void buildGui()	{
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.Y_AXIS));
		
		
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		downloadPanel.add(fileListLabel);
		downloadPanel.add(scroller);
		fileList.setVisibleRowCount(3);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		downloadPanel.add(downloadButton);
		downloadPanel.add(Box.createVerticalStrut(5));
		downloadPanel.add(logoutButton);
		
		loginPanel.add(hostLabel);
		loginPanel.add(hostField);
		loginPanel.add(connectButton);

		displayPanel.add(downloadPanel);
		//downloadPanel.setVisible(false);
		displayPanel.add(Box.createVerticalGlue());
		displayPanel.add(loginPanel);
		frame.getContentPane().add(BorderLayout.CENTER, displayPanel);
		frame. setSize(500,400);
		frame.setVisible(true);
		
		connectButton.addActionListener(new loginListener());
		
	}
	
	class loginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {		
			String hostname = hostField.getText();
			
			if(newClient.connect(hostname))	{
			
			loginPanel.setVisible(false);
			Vector<String> lists = new Vector<String>();
			FTPFile[] file = newClient.listFile();
			for(FTPFile f : file)
				lists.add(f.toString());
			fileList.setListData(lists);
			downloadPanel.setVisible(true);
			
			}
		}
		
	}
	
	public static void main( String[] args )	{
		ClientGui gui = new ClientGui();
		gui.buildGui();
		
	}
	
	
}
