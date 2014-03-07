utilities-FTP-Client
====================

W14 ready! (Andrew Berls)

An FTP client that supports FTP and SFTP (file transfer over SSH) protocols.  The client can connect to an FTP/SFTP server and download files from the root directory.

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


Developer notes
---------------

This project is organized in a very straightforward manner.  Client is an abstract class meant to abstract the workings of a given protocol from the rest of the program logic, such as the user interface.

FtpClient and SftpClient are implementations that extend Client, implementing FTP and SFTP protocols respectively.  Because all of their public methods behave identically, they are completely interchangeable in UI code.

FtpClient leverages Apache's [FTPClient](https://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html) for the actual FTP protocol implementations, while SftpClient leverages [JSch](http://epaul.github.io/jsch-documentation/javadoc/) for its implementation of SSH/SFTP.

New protocols can be easily implemented by extending and implementing each of Client's public methods in a manner consistent with Client's method outputs.

All GUI code is located in ClientGui, while FtpClient and SftpClient each contain their own CLI main methods (that could probably be consolidated in to one CLI class in the future).

Note that this program currently does not support switching directories (though the code is in place to do so with minor modification).

Building
--------
To build this application, simply run `ant compile` from the project's root directory and it will compile all source files.