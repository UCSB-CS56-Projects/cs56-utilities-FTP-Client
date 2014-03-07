utilities-FTP-Client
====================

W14 ready! (Andrew Berls)

An FTP client that supports FTP and SFTP (file transfer over SSH) protocols.  The client can connect to an FTP/SFTP server and download files.

This client currently does not support switching directories (though the code is in place to do so with minor modification).

GUI Usage
---------

Run the GUI with `ant runGui`.  Choose what protocol to use when connecting to the remote server, and enter the host in the form [user@]hostname[:port] (e.g. jsmith@csil.cs.ucsb.edu:1234).

You can omit username and/or port number if not applicable, and defaults will be used.  If applicable, enter your password in the password field.  Click connect to connect to the server, and choose the file you wish to download.  Click the download button to download the selected file.  You can then download additional files, or quit to the main screen.

Command Line Usage
------------------

Run the FTP command line interface with `ant run`.

Run the SFTP command line interface with `ant runSecure`

As with the GUI, enter the hostname and password (if applicable), and then type the file to download.


Screenshots
-----------
![](http://cs.ucsb.edu/~dcoffill/cs56/W14/project/cs56_utilities_FTP_Client/login-screenshot.png)
Logging on to a server


![](http://cs.ucsb.edu/~dcoffill/cs56/W14/project/cs56_utilities_FTP_Client/connected-screenshot.png)
Connected to a server, ready to download files


Building
--------
To build this application, simply run `ant compile` from the project's root directory and it will compile all source files.