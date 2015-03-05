
package edu.ucsb.cs56.projects.FTP_client;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
                        "Month","Date","Time/Year","Item Name"};
		String[] protocols = {"SFTP://", "FTP://"};
		protocolList    = new JComboBox(protocols);
		selectedItem    = null;
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
        cdButton.addActionListener(new cdListener());
		downloadButton.addActionListener(new downloadListener());
		logoutButton.addActionListener(new logoutListener());
	}

    public void buildTable () {
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

        // Set size of specific columns
        TableColumn permColumn  = fileTable.getColumn("Permissions");
        TableColumn linksColumn = fileTable.getColumn("Links");
        TableColumn ownerColumn = fileTable.getColumn("Owner");
        TableColumn groupColumn = fileTable.getColumn("Group");
        TableColumn sizeColumn  = fileTable.getColumn("Size");
        TableColumn monthColumn = fileTable.getColumn("Month");
        TableColumn dateColumn  = fileTable.getColumn("Date");
        TableColumn timeColumn  = fileTable.getColumn("Time/Year");
        permColumn.setMinWidth(85);
        permColumn.setMaxWidth(85);
        linksColumn.setPreferredWidth(45);
        linksColumn.setMinWidth(45);
        linksColumn.setMaxWidth(100);
        ownerColumn.setPreferredWidth(80);
        ownerColumn.setMinWidth(80);
        ownerColumn.setMaxWidth(120);
        groupColumn.setPreferredWidth(80);
        groupColumn.setMinWidth(80);
        groupColumn.setMaxWidth(120);
        sizeColumn.setPreferredWidth(60);
        sizeColumn.setMinWidth(60);
        sizeColumn.setMaxWidth(80);
        monthColumn.setMinWidth(50);
        monthColumn.setMaxWidth(50);
        dateColumn.setMinWidth(50);
        dateColumn.setMaxWidth(50);
        timeColumn.setMinWidth(65);
        timeColumn.setMaxWidth(65);

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

			if(newClient.connect(url, password)) {
                // Draw file interface
				loginPanel.setVisible(false);
				statusLabel.setText("Connected to " + url);
				statusLabel.setIcon(null);
                buildTable();
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
                            System.out.println("'"+selectedItem+"' selected");
                        }
                    }
                }
            }
        }
    }

    // selectListener not used for fileTable
	class selectListener implements MouseListener {
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
            System.out.println("cdListener invoked");
            String url = hostField.getText();
            // Check if a selection is made
            if(selectedItem !=null) {
                // Do nothing if selectedItem is a file
                if ( !(newClient.isDir(selectedItem)) ) {
                    statusLabel.setText(selectedItem+" is not a directory");
                }
                // Otherwise change working directory to selectedItem
                else {
                    newClient.ChangeDirectory(selectedItem);
                    statusLabel.setText
                            ("Changing to '"+selectedItem+"' directory");
                    System.out.println
                            ("Changing to '"+selectedItem+"' directory");
                    statusLabel.setText("Connected to " + url);
                    statusLabel.setIcon(null);
                    buildTable();
                }
            }
        }
    }
	
	class downloadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
            //System.out.println("downloadListener invoked!");
			if(selectedItem !=null)	{
				// Print out message to GUI bar if selection isn't a file
				if ( !(newClient.isFile(selectedItem)) ) {
					statusLabel.setText(selectedItem + " is not a file");
				}
                else {
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
