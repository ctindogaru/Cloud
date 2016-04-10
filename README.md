# Cloud

For running the application you must import the project, then change the 
"fh = new FileHandler("C:/Users/Constantin/workspace/Cloud/MyLogFile.log");" line from the Test class with the location where you imported the project.
After that you're good to go.
You will see that once you start the application, you will be logged in as guest. Login as root and get access to all commands. To do that type "login root root".

This is a simulation of a Unix-like operating system.
The following commands are available: ls, cd, cat, pwd, mkdir, rm, touch, echo, newuser, userinfo, login, logout, upload, sync.
The upload command copies your data on the cloud, so once you delete it you can still get it back using sync command, which downloads the data back from the cloud.

Format of commands:

echo message - print the message on the screen

echo -POO message - pop up the message in a new window

userinfo - informations about the current user

ls - list all the files in the current directory

ls file_name - list all the files in the file

ls-r - list all the files recursively

ls-a -POO - list all the information about the files in a table

login username password

cd path_name - change current directory to path

cd without any arguments change the directory to home directory

cat file_name - print the content from file on the screen

touch file_name - create a new file

mkdir directory_name - create a new directory

pwd - print the current location

rm file_name - remove a file

upload file_name - upload the given file on the cloud

sync file_name - gets the file from the cloud

newuser username password last_name first_name - create a new user

logout - logout the current user, you will now be logged in as guest

exit - exit the application and save all the changes

All that being said, enjoy!
