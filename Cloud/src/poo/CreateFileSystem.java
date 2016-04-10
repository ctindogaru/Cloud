package poo;

import java.util.Scanner;

public class CreateFileSystem {

	UserConfig userconfig;
	UserConfig.User user;
	Permission permisiune;
	Directory root;
	Commands commands;
	
	public CreateFileSystem() {
		
		userconfig = new UserConfig();
		user = userconfig.root;
		permisiune = new Permission(true, true, user);
		// directorul /
		root = new Directory("", permisiune);
		root.add(new Directory("Desktop", permisiune));
		root.add(new Directory("Documents", permisiune));
		root.add(new Directory("Downloads", permisiune));
		root.add(new Directory("Home", permisiune));
		root.add(new Directory("Music", permisiune));
		root.add(new Directory("Pictures", permisiune));
		root.add(new Directory("Videos", permisiune));
		root.add(new Directory("Trash", permisiune));
	
		commands = new Commands(root);
		
	}
	
}
