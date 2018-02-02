# Claudia Zeng, Kerry Mo

## a
  An FTP client that supports FTP and SFTP (file transfer over SSH) protocols. The client can connect to an FTP/SFTP server and download files from the root directory.
  
## b
  * As a user, I can run `ant runGUI` so that I can choose what protocol to use when connecting to the remote server, and enter the host in the form [user@]hostname[:port] (e.g. jsmith@csil.cs.ucsb.edu:1234).
  
  * As a user, I can omit username and/or port number if not applicable, and defaults will be used.
  
  * As a user, I can enter my password in the field so that I can connect to the server.
  
  * As a user, I can click the download button so that I can download selected files.
  
  * As a user, I can quit the main screen.
  
## c
  The project runs. When it runs, it asks you to give the URL in format of `[user@]hostname[:port]`, for example, anonymous@localhost:2121. And then after you enter the password, you can type in the file name to download selected files.

## d
  * As a user, I can connect to the server so that I can download selected multiple files.

  * As a user, I can connect to the server so that I can download selected folder.
  
## e

  We suggest to move the test part before introducing `ant run` and `ant compile`, since we need to have server connected in order to test the demo. Also, it would be nicer if the instructions can be more clear about what shall we do next after testing FTP server connection. For example, the input URL when your test it is actually anonymous@localhost:2121, instead of username@csil.cs.ucsb.edu:1234.
  Also, more information about FTP server would be helpful to us to understand what's this project for.
  
## f

  This project is run by Ant. The build.xml is fine. No description should be added. (Checked by ant -p)
  


  
  
  
