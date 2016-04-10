package poo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

import poo.UserConfig.User;

public class ReadAndWriteFromFile {

	public void writeCurrentState(Directory rootDir, TreeSet<UserConfig.User> listaUseri, String fileName) {
		  // serialize the Queue
		  //System.out.println("serializing theData");
		  try {
		      FileOutputStream fout = new FileOutputStream(fileName);
		      ObjectOutputStream oos = new ObjectOutputStream(fout);
		      oos.writeObject(rootDir);
		      oos.writeObject(listaUseri);
		      oos.close();
		      }
		   catch (Exception e) { e.printStackTrace(); }
	}
	
	public void writeCurrentState(CloudService[] cloudService, String fileName) {
		  // serialize the Queue
		  //System.out.println("serializing theData");
		  try {
		      FileOutputStream fout = new FileOutputStream(fileName);
		      ObjectOutputStream oos = new ObjectOutputStream(fout);
		      oos.writeObject(cloudService[0]);
		      oos.writeObject(cloudService[1]);
		      oos.writeObject(cloudService[2]);
		      oos.close();
		      }
		   catch (Exception e) { e.printStackTrace(); }
	}
	
	public void readLastState(Commands commands, UserConfig userconfig, String fileName) {
		  // unserialize the Queue
		  //System.out.println("unserializing theData");
		  try {
		      FileInputStream fin = new FileInputStream(fileName);
		      ObjectInputStream ois = new ObjectInputStream(fin);
		      commands.rootDir = (Directory) ois.readObject();
		      userconfig.listaUseri = (TreeSet<UserConfig.User>) ois.readObject();
		      ois.close();
		      }
		   catch (Exception e) { e.printStackTrace(); }
	}
	
	public void readLastState(CloudService[] cloudService, String fileName) {
		  // unserialize the Queue
		  //System.out.println("unserializing theData");
		  try {
		      FileInputStream fin = new FileInputStream(fileName);
		      ObjectInputStream ois = new ObjectInputStream(fin);
		      cloudService[0] = new CloudService();
			  cloudService[1] = new CloudService();
			  cloudService[2] = new CloudService();
		      cloudService[0] = (CloudService) ois.readObject();
		      cloudService[1] = (CloudService) ois.readObject();
		      cloudService[2] = (CloudService) ois.readObject();
		      //System.out.println(cloudService.dimensiuneCloud);
		      ois.close();
		      }
		   catch (Exception e) { e.printStackTrace(); }
	}
}
