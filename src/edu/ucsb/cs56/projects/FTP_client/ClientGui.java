
package edu.ucsb.cs56.projects.FTP_client;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.io.IOException;

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
	private Vector<String> lists	;
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
		 selectedFile	= null;
		 lists			= new Vector<String>();
		
		 
		 newClient 		= new Client();
		
	}
	
	public void buildGui()	{
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.Y_AXIS));
		
		
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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
		downloadPanel.setVisible(false);
		displayPanel.add(Box.createVerticalGlue());
		displayPanel.add(loginPanel);
		frame.getContentPane().add(BorderLayout.CENTER, displayPanel);
		frame. setSize(500,400);
		frame.setVisible(true);
		
		connectButton.addActionListener(new loginListener());
		fileList.addMouseListener(new selectListener());
		downloadButton.addActionListener(new downloadListener());
		logoutButton.addActionListener(new logoutListener());
		
	}
	
	class loginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {		
			String hostname = hostField.getText();
			
			if(newClient.connect(hostname))	{
			
			loginPanel.setVisible(false);
			FTPFile[] file = newClient.listFile();
			lists.clear();
			for(FTPFile f : file)
				lists.add(f.toString());
			fileList.setListData(lists);
			downloadPanel.setVisible(true);
			
			}
		}
		
	}
	
	class selectListener implements MouseListener	{
		
		public void mouseClicked(MouseEvent e)	{
			int index = fileList.locationToIndex(e.getPoint());
			
			if(index>=0)	{
				String[] file = lists.get(index).split(" ");
				selectedFile = file[file.length-1];
				System.out.println("select "+selectedFile);
			}
		
		}

		
		public void mouseEntered(MouseEvent e)	{	}
		
		public void mouseExited(MouseEvent e)	{	} 
		
		public void mousePressed(MouseEvent e)	{	}
		
		public void mouseReleased(MouseEvent e) {	}
	}
	
	class downloadListener implements ActionListener	{
		public void actionPerformed(ActionEvent e) {
			if(selectedFile!=null)	
				newClient.download(selectedFile);
		}
	}
	
	class logoutListener implements ActionListener	{
		public void actionPerformed(ActionEvent e) {
					newClient.logout();
					downloadPanel.setVisible(false);
					loginPanel.setVisible(true);
				
			}
		
	}
	
	public static void main( String[] args )	{
		ClientGui gui = new ClientGui();
		gui.buildGui();
		
	}
	
	
}
