package poo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Logger;

class MyNotEnoughSpaceException extends Exception {
	
	double dimensiune;
	UserConfig.User user;
	Date data;
	
	
	public MyNotEnoughSpaceException() {
		super("Nu este suficient spatiu pe Cloud!");
	}
	
	public MyNotEnoughSpaceException(String message) {
		super(message);
	}
	
	public MyNotEnoughSpaceException(double dimensiune, UserConfig.User user, Date data) {
		this.dimensiune = dimensiune;
		this.user = user;
		this.data = data;
	}
	
	public String toString() {
		String result = "Exceptie! ";
		result += "Directorul cu dimensiunea de " + dimensiune + " nu poate fi uploadat in Cloud\n";
		result += "Comanda a fost data de " + user.username + "la ora si data " + data;
		return result;
	}
}

public class CloudService implements Serializable{
	
	// parent are de fapt intelesul de thisDirectorName si invers; le-am incurcat eu initial si le-am lasat asa
	ArrayList<Directory> myCloud;
	ArrayList<String> parent;
	ArrayList<String> thisDirectorName;
	double dimensiuneCloud;
	
	public CloudService() {
		myCloud = new ArrayList<Directory>();
		parent = new ArrayList<String>();
		thisDirectorName = new ArrayList<String>();
		this.dimensiuneCloud = 0;
	}
	
	public void normalUpload(String cale, Commands commands) {
		Directory keepDirectory = commands.currentDir;
		commands.changePath(commands.currentDir, cale);
		Directory toUploadDir = commands.currentDir;
		
		for(Directory aux: myCloud) {
			if(aux.nume.compareTo(toUploadDir.nume) == 0) {
				dimensiuneCloud -= aux.dimensiune;
				myCloud.remove(aux);
			}
		}
		
		ReadAndWriteFromFile rw = new ReadAndWriteFromFile();
		String fileName = cale + "upload.dat";
		rw.writeCurrentState(toUploadDir, null, fileName);
		Directory aux = new Directory();
		Commands commandsAux = new Commands(new Directory());
		UserConfig userconfigAux = new UserConfig();
		rw.readLastState(commandsAux, userconfigAux, fileName);
		myCloud.add(commandsAux.rootDir);
		parent.add("Fara parinte");
		thisDirectorName.add("Fara parinte");
		dimensiuneCloud += commandsAux.rootDir.dimensiune;	
		commands.currentDir = keepDirectory;
	}
	
	
	public void searchForSubdirectoriesToUpload(Directory dir, ReadAndWriteFromFile rw) {
		
		for(Directory subdir: dir.container) {
			if(subdir.container != null) {
				if(subdir.dimensiune + dimensiuneCloud <= 10240) {
					rw.writeCurrentState(subdir, null, subdir.nume + "upload.dat");
					Directory aux = new Directory();
					Commands commandsAux = new Commands(new Directory());
					UserConfig userconfigAux = new UserConfig();
					rw.readLastState(commandsAux, userconfigAux, subdir.nume + "upload.dat");
					myCloud.add(commandsAux.rootDir);
					parent.add(subdir.nume);
					thisDirectorName.add(dir.nume);
					dimensiuneCloud += commandsAux.rootDir.dimensiune;	
				}
				else searchForSubdirectoriesToUpload(subdir, rw);
			}
		}
	}
	
	
	public void firstSplitUpload(String cale, Commands commands) {
		Directory keepDirectory = commands.currentDir;
		commands.changePath(commands.currentDir, cale);
		Directory toUploadDir = commands.currentDir;
		
		for(Directory aux: myCloud) {
			if(aux.nume.compareTo(toUploadDir.nume) == 0) {
				dimensiuneCloud -= aux.dimensiune;
				myCloud.remove(aux);
			}
		}
		
		commands.currentDir = keepDirectory;
		ReadAndWriteFromFile rw = new ReadAndWriteFromFile();
		//imi va scrie o parte din subdirectoare pe statie si imi va returna ultimul subdirector care n-a mai incaput pe statie
		searchForSubdirectoriesToUpload(toUploadDir, rw);
	}
	
	
	public void removeChild(Commands commands, String dirName) {
		for(Directory subdir: commands.rootDir.container) 
			if(subdir.container != null) {
				if(subdir.nume.compareTo(dirName) == 0) {
					commands.rootDir.container.remove(subdir);
					break;
				}
				else {
					commands.rootDir = subdir;
					removeChild(commands, dirName);
				}
			}
	}
	
	public void secondSplitUpload(String cale, Commands commands, ArrayList<String> toBeDeleted) {
		Directory keepDirectory = commands.currentDir;
		commands.changePath(commands.currentDir, cale);
		Directory toUploadDir = commands.currentDir;
		
		for(Directory aux: myCloud) {
			if(aux.nume.compareTo(toUploadDir.nume) == 0) {
				dimensiuneCloud -= aux.dimensiune;
				myCloud.remove(aux);
			}
		}
		
		
		ReadAndWriteFromFile rw = new ReadAndWriteFromFile();
		String fileName = cale + "upload.dat";
		rw.writeCurrentState(toUploadDir, null, fileName);
		Directory aux = new Directory();
		Commands commandsAux = new Commands(new Directory());
		UserConfig userconfigAux = new UserConfig();
		rw.readLastState(commandsAux, userconfigAux, fileName);
		
		Directory holdTheRootReference = commandsAux.rootDir;
		int i;
		for(i = 0; i < toBeDeleted.size(); i++)
			if(toBeDeleted.get(i).compareTo("Fara parinte") != 0) {
				removeChild(commandsAux, toBeDeleted.get(i));
				commandsAux.rootDir = holdTheRootReference;
			}
		
		myCloud.add(commandsAux.rootDir);
		commandsAux.actualizareDimensiuni(commandsAux.rootDir);
		dimensiuneCloud += commandsAux.rootDir.dimensiune;	
		commands.currentDir = keepDirectory;
		
	}
	
	public int upload(String cale, Commands commands, int remainingUpload, ArrayList<String> toBeDeleted, int stationNumber, Logger log, UserConfig.User user) throws MyNotEnoughSpaceException {
		
		// calea e de fapt directorul care se doreste a fi uploadat, doar ca de tip String, nu de tip Directory, asa ca-l convertesc
		// la Directory cu changePath (cale == toUploadDir)
		Directory keepDirectory = commands.currentDir;
		commands.changePath(commands.currentDir, cale);
		Directory toUploadDir = commands.currentDir;
		commands.actualizareDimensiuni(toUploadDir);
		
		/*System.out.println(toUploadDir.dimensiune);
		System.out.println(dimensiuneCloud);
		//System.out.println(remainingUpload); */
		
		//inseamna ca exista suficient loc, iar uploadarea se va face in conditii normale
		if(stationNumber == 2 && toUploadDir.dimensiune + dimensiuneCloud > 10240) {
			log.info(new MyNotEnoughSpaceException(toUploadDir.dimensiune, user , new Date()).toString());
			throw new MyNotEnoughSpaceException();
		}
		
		if(toUploadDir.dimensiune + dimensiuneCloud <= 10240 && remainingUpload == 0) {
			commands.currentDir = keepDirectory;
			this.normalUpload(cale, commands);
			return 0;
		}
		//inseamna ca nu exista suficient loc pe statia actuala pentru tot directorul si se va uploada doar o parte din el
		else if(toUploadDir.dimensiune + dimensiuneCloud > 10240 && remainingUpload == 0) {
			System.out.println("A intrat pe first upload");
			commands.currentDir = keepDirectory;
			this.firstSplitUpload(cale, commands);
			return 1;
		}
		//se va copia restul directorului pe urmatoarea statie
		else if(remainingUpload == 1) {
			System.out.println("A intrat pe second upload");
			commands.currentDir = keepDirectory;
			this.secondSplitUpload(cale, commands, toBeDeleted);
			return 2;
		}
		
		return 0;
			
	}
	
	public Directory sync(String cale, Commands commands, ArrayList<Directory> directoriesOnPrevStation, ArrayList<String> namesOnPrevStation) {
		
		Directory keepDirectory = commands.currentDir;
		commands.changePath(commands.currentDir, cale);
		Directory toSyncDir = commands.currentDir;
		
		//daca sunt pe prima statie si directorul e gasit inseamna ca directorul nu e scindat pe 2 statii si se afla de-a-ntregul pe prima statie
		if(directoriesOnPrevStation.size() == 0) {
			for(Directory aux: myCloud) {
				if(aux.nume.compareTo(cale) == 0) {
					if(keepDirectory != toSyncDir)
						keepDirectory.container.remove(toSyncDir);
					ReadAndWriteFromFile rw = new ReadAndWriteFromFile();
					String fileName = cale + "sync.dat";
					rw.writeCurrentState(aux, null, fileName);
					Commands commandsAux = new Commands(new Directory());
					UserConfig userconfigAux = new UserConfig();
					rw.readLastState(commandsAux, userconfigAux, fileName);
					commands.currentDir = keepDirectory;
					return commandsAux.rootDir;
				}
			}
		}
		
		else {
			for(Directory aux: myCloud) {
				if(aux.nume.compareTo(cale) == 0) {
					if(keepDirectory != toSyncDir)
						keepDirectory.container.remove(toSyncDir);
					ReadAndWriteFromFile rw = new ReadAndWriteFromFile();
					String fileName = cale + "sync.dat";
					rw.writeCurrentState(aux, null, fileName);
					Commands commandsAux = new Commands(new Directory());
					UserConfig userconfigAux = new UserConfig();
					rw.readLastState(commandsAux, userconfigAux, fileName);
					int i;
					
					Directory holdTheRootReference = commandsAux.rootDir;
					for(i = 0; i < namesOnPrevStation.size(); i++) {
						if(namesOnPrevStation.get(i).compareTo("Fara parinte") != 0) {
							//System.out.println(namesOnPrevStation.get(i));
							//System.out.println(directoriesOnPrevStation.get(i).nume);
							if(commandsAux.rootDir.nume.compareTo(namesOnPrevStation.get(i)) == 0) {
								commandsAux.rootDir.container.add(directoriesOnPrevStation.get(i));
							}
							else addChild(commandsAux, namesOnPrevStation.get(i), directoriesOnPrevStation.get(i));
						}
						
						commandsAux.rootDir = holdTheRootReference;
					}
					
					/*System.out.println(commandsAux.rootDir.container.get(0).nume);
					System.out.println(commandsAux.rootDir.container.get(1).nume);
					System.out.println(commandsAux.rootDir.container.get(2).nume);*/
					commands.currentDir = keepDirectory;
					return commandsAux.rootDir;
				}
			}
		}
		
		return null;
	}
	
	public void addChild(Commands commands, String dirName, Directory deAdaugat) {
		for(Directory subdir: commands.rootDir.container) {
			if(subdir.container != null) {
				if(subdir.nume.compareTo(dirName) == 0) {
					//System.out.println("found");
					subdir.container.add(deAdaugat);
					break;
				}
				else {
					commands.rootDir = subdir;
					addChild(commands, dirName, deAdaugat);
				}
			}
		}
	}
	
}
