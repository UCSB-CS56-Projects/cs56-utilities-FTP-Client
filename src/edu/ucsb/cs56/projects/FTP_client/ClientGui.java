
package edu.ucsb.cs56.projects.FTP_client;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.*;

/**
 * A basic FTP and SFTP client
 * @version CS56, W14
 * @author Wenjie Huang
 * @author David Coffill
 * @author Jeffrey Chen
 */

public class ClientGui {
	
	private JFrame frame;
	private JPanel displayPanel;
	private JPanel loginPanel;
	private JPanel downloadPanel;
	private JButton connectButton;
    private JButton cdButton;
	private JButton downloadButton;
	private JButton logoutButton;
	private JLabel	hostLabel;
	private JLabel pwLabel;
	private JLabel	fileListLabel;
	private JTextField hostField;
	private JScrollPane scroller;
    private MyTableModel fileModel;
    private JTable fileTable;
    private String[] columnNames;
	private String selectedItem;
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
        cdButton        = new JButton("Change Directory");
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
                        "Month","Date","Time/Year","File Name"};
		String[] protocols = {"SFTP://", "FTP://"};
		protocolList    = new JComboBox(protocols);
		selectedItem = null;
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

                // Create fileModel and fileTable with attributes
                fileModel = new MyTableModel(files,columnNames);
                fileTable = new JTable(fileModel);
                fileTable.setRowSelectionAllowed(true);
                fileTable.setColumnSelectionAllowed(false);
                fileTable.getSelectionModel().addListSelectionListener
                        (new fileSelectionListener());
                fileModel.setEditable(false);
                System.out.println("Number of files: " + fileTable.getRowCount());
                scroller = new JScrollPane(fileTable);

                // Reset download panel and add components
                downloadPanel.removeAll();
                downloadPanel.add(scroller);
                downloadPanel.add(cdButton);
                downloadPanel.add(Box.createVerticalStrut(5));
                downloadPanel.add(downloadButton);
                downloadPanel.add(Box.createVerticalStrut(5));
                downloadPanel.add(logoutButton);
                downloadPanel.addMouseListener(new selectListener());
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

    class fileSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            //System.out.println("FileSelectionListener invoked!");
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if(!e.getValueIsAdjusting()) { // Prevents listener from invoking on unclick
                if (lsm.isSelectionEmpty()) {
                    System.out.println("Nothing selected");
                    selectedItem = null;
                } else {
                    // Find out which files are selected
                    int minIndex = lsm.getMinSelectionIndex();
                    int maxIndex = lsm.getMaxSelectionIndex();
                    //System.out.println("minIndex, maxIndex = "+minIndex+", "+maxIndex);
                    for (int i = minIndex; i <= maxIndex; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            selectedItem = (String) files[i][8];
                            System.out.println("'" + selectedItem + "' selected");
                        }
                    }
                }
            }
        }
    }

    // selectListener not used for fileTable
	class selectListener implements MouseListener	{
		public void mouseClicked(MouseEvent e)	{
            //System.out.println("selectListener called!");
			int index = fileTable.rowAtPoint(e.getPoint());
			
			if(index>=0) {
				selectedItem = (String)files[index][8];
				System.out.println("select "+ selectedItem);
			}
		}
		public void mouseEntered(MouseEvent e)	{	}
		public void mouseExited(MouseEvent e)	{	}
		public void mousePressed(MouseEvent e)	{	}
		public void mouseReleased(MouseEvent e) {	}
	}

    class cdListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        }
    }
	
	class downloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
            //System.out.println("downloadListener invoked!");
			if(selectedItem !=null)	{
				// Print out message to GUI bar if selection isn't a file
				if ( !(newClient.isFile(selectedItem)) ) {
					statusLabel.setText(selectedItem + " is not a file");
				} else {
					newClient.download(selectedItem);
					statusLabel.setText("Downloading " + selectedItem);
				}
			}
		}
	}
	
	class logoutListener implements ActionListener	{
		public void actionPerformed(ActionEvent e) {
            //System.out.println("logoutListener invoked!");
			newClient.logout();
			downloadPanel.setVisible(false);
			loginPanel.setVisible(true);
			statusLabel.setText("");
			statusLabel.setIcon(null);
            fileTable = null;
            fileModel = null;
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
