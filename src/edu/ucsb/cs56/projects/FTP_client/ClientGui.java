
package edu.ucsb.cs56.projects.FTP_client;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.*;
import java.awt.event.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.io.IOException;

/**
 * A basic FTP and SFTP client
 * @version CS56, W14
 * @author Wenjie Huang
 * @author David Coffill
 */

public class ClientGui {
	
	private JFrame frame;
	private JPanel displayPanel;
	private JPanel loginPanel;
	private JPanel downloadPanel;
	private JButton connectButton;
	private JButton downloadButton;
	private JButton logoutButton;
	private JLabel	hostLabel;
	private JLabel pwLabel;
	private JLabel	fileListLabel;
	private JTextField hostField;
	private JScrollPane scroller;
	private JList fileList;
    private MyTableModel fileModel;
    private JTable fileTable;
    private String[] columnNames;
	private String selectedFile;
	private Vector<String> lists;
	private Client newClient;
	private JLabel statusLabel;
	private JPanel statusPanel;
	private ImageIcon statusIcon;
	private JComboBox protocolList;
	private JPasswordField pwField;
    private Object[][] files;

    public class MyTableModel extends DefaultTableModel {
        private boolean isEditable = true;

        public MyTableModel(Object[][] files, String[] columnNames) {
            super(files, columnNames);
        }

        public boolean isCellEditable(int row, int column) {
            return isEditable;
        }
        public void setEditable(boolean isEditable) {
            this.isEditable = isEditable;
        }
    }

	public ClientGui()	{
		frame 			= new JFrame("FTP Client");
		displayPanel	= new JPanel();
		loginPanel 	    = new JPanel();
		downloadPanel 	= new JPanel();
		statusPanel     = new JPanel();
		connectButton	= new JButton("Connect");
		downloadButton	= new JButton("Download");
		logoutButton	= new JButton("Logout");
        scroller        = new JScrollPane();
		hostLabel		= new JLabel("Host: ");
		statusLabel     = new JLabel(""); // Label that indicates program/connection status
		fileListLabel	= new JLabel("File List");
		pwLabel         = new JLabel("Password: ");
		hostField 		= new JTextField(20);
		pwField         = new JPasswordField(12);
        columnNames     = new String[] {"Permissions","Links","Owner","Group","Size",
                        "Month","Date","Time","File Name"};
		String[] protocols = {"SFTP://", "FTP://"};
		protocolList    = new JComboBox(protocols);
		//fileList		= new JList();
		selectedFile	= null;
		lists			= new Vector<String>();
		statusIcon      = new ImageIcon("./assets/dialog-error.png");
	}
	
	public void buildGui()	{
		// Request Swing to use system UI style, so the UI isn't as hideous
		// looking on most platforms
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		}
		catch (Exception ex) {
			System.out.println("Error: Could not set system look and feel");
		}


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.Y_AXIS));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

        loginListener login = new loginListener();

        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		downloadPanel.add(fileListLabel);

		//fileList.setVisibleRowCount(3);
		//fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frame.getContentPane().add(BorderLayout.SOUTH, statusPanel);
		loginPanel.add(hostLabel);
		loginPanel.add(protocolList);
		loginPanel.add(hostField);
		loginPanel.add(pwLabel);
		loginPanel.add(pwField);
		loginPanel.add(connectButton);
		statusPanel.add(statusLabel);

		displayPanel.add(downloadPanel);
		downloadPanel.setVisible(false);
		displayPanel.add(Box.createVerticalGlue());
		displayPanel.add(loginPanel);
		frame.getContentPane().add(BorderLayout.CENTER, displayPanel);
		frame.setSize(700, 400);
		frame.setVisible(true);
		
		connectButton.addActionListener(login);
		fileTable.addMouseListener(new selectListener());
		downloadButton.addActionListener(new downloadListener());
		logoutButton.addActionListener(new logoutListener());
		
	}
	
	class loginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String url = hostField.getText();
			String password;

			// Decide which protocol to use based on protocolList combobox selection
			if(protocolList.getSelectedIndex() == 0)
				newClient = new SftpClient();
			else if (protocolList.getSelectedIndex() == 1)
				newClient = new FtpClient();

			// Get password as char[] from pwField, convert to string
			password = new String(pwField.getPassword());

			if(newClient.connect(url, password))	{
                // Draw file interface
				loginPanel.setVisible(false);

                // Load files from directory
				files = newClient.listFile();
                System.out.println(files[0][0]);
				//lists.clear();

                fileModel = new MyTableModel(files,columnNames);
                fileTable = new JTable(fileModel);
                fileTable.setRowSelectionAllowed(true);
                fileTable.setColumnSelectionAllowed(false);
                fileModel.setEditable(false);
                System.out.println(fileTable.getRowCount());
                System.out.println(fileTable.getColumnCount());
                scroller = new JScrollPane(fileTable);

                downloadPanel.add(scroller);
                downloadPanel.add(downloadButton);
                downloadPanel.add(Box.createVerticalStrut(5));
                downloadPanel.add(logoutButton);
                downloadPanel.setVisible(true);
				statusLabel.setText("Connected to " + url);
				statusLabel.setIcon(null);
			}
			else {
				// Update statusLabel with an error message and error icon on connection failure
				statusLabel.setText("Error connecting to " + url + ", ensure the address is correct");
				statusLabel.setIcon(statusIcon);
			}
		}
	}
	
	class selectListener implements MouseListener	{
		
		public void mouseClicked(MouseEvent e)	{
			int index = fileTable.rowAtPoint(e.getPoint());
			
			if(index>=0) {
				selectedFile = (String)files[index][8];
				System.out.println("select "+selectedFile);
			}
		}

		
		public void mouseEntered(MouseEvent e)	{	}
		
		public void mouseExited(MouseEvent e)	{	} 
		
		public void mousePressed(MouseEvent e)	{	}
		
		public void mouseReleased(MouseEvent e) {	}
	}
	
	class downloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(selectedFile!=null)	{
				// Print out message to GUI bar if selection isn't a file
				if ( !(newClient.isFile(selectedFile)) ) {
					statusLabel.setText(selectedFile + " is not a file");
				} else {
					newClient.download(selectedFile);
					statusLabel.setText("Downloading " + selectedFile);
				}
			}
		}
	}
	
	class logoutListener implements ActionListener	{
		public void actionPerformed(ActionEvent e) {
			newClient.logout();
			downloadPanel.setVisible(false);
			loginPanel.setVisible(true);
			statusLabel.setText("");
			statusLabel.setIcon(null);
		}
	}

	class protocolListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JComboBox comboBox = (JComboBox)e.getSource();
			String protocolType = (String)comboBox.getSelectedItem();
		}
	}
	
	public static void main( String[] args ) {
		ClientGui gui = new ClientGui();
		gui.buildGui();
	}
}
