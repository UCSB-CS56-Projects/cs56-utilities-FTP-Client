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
  
## g

  There are four issues, which is probably not enough to earn points1000. One of the issues: "GUI file list columns are inconsistent" is not clear, but others are fine.
  
## h
  We didn't add any issues.

## i

  There are four files. The "Client.java" contains an abstract class, which has two subclasses, called FtpClient and SftpClient stored in other two java files. The last file implements the ClientGui which mainly builds the frames and panels, sets the layout and buttons, and also implements different kinds of listners for basic features. All of the class names as well as the variable names are clear. The structure of the entire code is easy to read, though there are some indentation problems. The relationship between classes are pretty clear and the functions of those classes are object-oriented and well-organized. The comments beside are not verbose, but extremely helpful.
  
  If I have to give someone else that is going to work on the code, we would tell him/her what are the behaviors these classes have, how are those classes related, or how do they pass information to each other. We will also tell him/her about the current issues of this piece of code, what's the expectations, and how to run and test the code.

## j

  For this project, we don't have any JUnit tests yet, though we do have some exception cases. Most of the exception cases are not handled well; if the exception object is thrown, it just does nothing. There is only one exception in ClientGui.java that prints out a line of error message on the screen.
  
  We would try to work on the existed exception cases first. Basically, if we couldn't achieve the goal through this way, where the code should throw an exception object, we either print some message on the screen, or try another way to approach the goal. We might also add more exceptions for each user cases or user stories.

  


  
  
  
