package poo;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

class Permission implements Serializable {
	boolean read;
	boolean write;
	UserConfig.User user;
	
	public Permission(boolean read, boolean write, UserConfig.User user) {
		this.read = read;
		this.write = write;
		this.user = user;
	}
	
	public String toString() {
		String result = "";
		result = "read " + read + " " + "write " + write + " " + user;
		return result;
	}
}

class Directory implements Serializable {
	String nume;
	double dimensiune;
	Date createdDate;
	Permission permisiune;
	ArrayList<Directory> container;
	
	public Directory() {
		
	}
	
	public Directory(String nume, Permission permisiune) {
		this.nume = nume;
		this.createdDate = new Date();
		this.permisiune = permisiune;
		container = new ArrayList<>();
		this.dimensiune = 0;
	}
	
	public Directory add(Directory dir) {
		container.add(dir);
		this.dimensiune += dir.dimensiune;
		return dir;
	}
	
	public void remove(Directory dir) {
		container.remove(dir);
		this.dimensiune -= dir.dimensiune;
	}
	
	public Directory getIndex(int index) {
		return container.get(index);
	}
	
	public String toString() {
		String result = "";
		result = "Director: " + nume + "\nDimensiune: " + dimensiune;
		result += "\nData si ora creare: " + createdDate;
		result += "\nPermisiuni: ";
		if(permisiune.read == true)
			result += "read ";
		if(permisiune.write == true)
			result += "write ";
		result += "ale utilizatorului " + permisiune.user.username;
		return result;
	}
}

class File extends Directory {
	String tip;
	byte[] dateContinute;

	public File() {
		super();
	}
	
	public File(String nume, String tip, Permission permisiune) {
		
		super();
		this.createdDate = new Date();
		this.permisiune = permisiune;
		this.nume = nume;
		this.tip = tip;
		
		SecureRandom rand = new SecureRandom();
		int randomNum = (int)(Math.random()*4000);	
		this.dateContinute = new BigInteger(randomNum, rand).toString(32).getBytes();
		this.dimensiune = dateContinute.length;
		
	}
	
	public String toString() {
		String result = "";
		result = "Fisier: " + nume + "\nDimensiune: " + dimensiune;
		result += "\nTip: " + tip + "\nData si ora creare: " + createdDate;
		result += "\nPermisiuni: ";
		if(permisiune.read == true)
			result += "read ";
		if(permisiune.write == true)
			result += "write ";
		result += "ale utilizatorului " + permisiune.user.username;
		return result;
	}
}
