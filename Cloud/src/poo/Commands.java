package poo;

import java.util.logging.Logger;

import javax.swing.JTextArea;

class MyInvalidPathException extends Exception {
	
	public MyInvalidPathException() {
		super("Aceasta cale nu exista!");
	}
	public String toString() {
		return "Exceptie! Aceasta cale nu exista!";
	}
	
	public MyInvalidPathException(String message) {
		super(message);
	}
}

class MyPathTooLongException extends MyInvalidPathException {
	
	public MyPathTooLongException() {
		super("Calea depaseste 255 caractere");
	}
	
	public String toString() {
		return "Exceptie! Calea depaseste 255 caractere!";
	}
}


public class Commands {
	
	Directory currentDir;
	Directory rootDir;
	String currentPath;
	
	public Commands(Directory dir) {
		currentDir = dir;
		rootDir = dir;
		currentPath = "/";
	}
	
	
	public void changePath(Directory dir, String cale) {
		int i;
		String[] words = cale.split("/");
		int n = words.length;
		String caleRamasa = "";
		if(cale.charAt(0) == '/') {
			dir = rootDir;
			for(i = 1; i < n; i++)
				words[i-1] = words[i];
			words[n-1] = null;
			n = n-1;
		}
		for(i = 1; i < n - 1; i++)
			caleRamasa += words[i] + "/";
		if(n == 1)
			caleRamasa += words[0];
		else caleRamasa += words[i];
		
		for(Directory aux: dir.container) {
			if(aux.nume.compareTo(words[0]) == 0) {
				if(n == 1) {
					currentDir = aux;
				}
				else this.changePath(aux, caleRamasa);
			}
		}
	}
	
	
	public void ls(Directory dir, JTextArea output) {
		if(dir instanceof File)
			output.append(dir.toString());
		else {
			for(Directory aux: dir.container)
				output.append(aux.nume + " ");
			//output.append("\n");
		}
	}
	
	
	public void lsWithArgument(String word, JTextArea output) {
		Directory aux = currentDir;
		this.changePath(currentDir, word);
		if(aux != currentDir) {
			this.ls(currentDir, output);
			currentDir = aux;
		}
		else output.append("Nu exista aceasta cale!");
	}
	
	
	public void lsR(Directory dir, String cale, JTextArea output) {
		if(dir instanceof File == false) {
			for(Directory aux: dir.container)
				if(aux instanceof File == false) {
					if(cale == "/")
						output.append("/" + aux.nume + "\n");
					else output.append(cale + "/" + aux.nume + "\n");
					if(cale == "/")
						this.lsR(aux, cale + aux.nume, output);
					else this.lsR(aux, cale + "/" + aux.nume, output);
				}
				else output.append(cale + "/" + aux.nume + "\n");
		}
		else output.append(cale + "/" + dir.nume + "\n");
	}
	
	public Object[][] lsLPOO() {
		
		int i = 0;
		for(Directory aux: currentDir.container) {
			i++;
		}
		Object[][] obj = new Object[i][5];
		i = 0;
		Directory keepDirectory = currentDir;
		for(Directory aux: currentDir.container) {
			if(aux instanceof File) {
				String result = "";
				if(aux.permisiune.read == true)
					result += "r";
				if(aux.permisiune.write == true)
					result += "w ";
				result += aux.permisiune.user.username;
				Object[] objAux = {aux.nume, "fisier " + ((File) aux).tip, aux.dimensiune, result, aux.createdDate };
				obj[i] = objAux;
				i++;
			}
			else {
				String result = "";
				if(aux.permisiune.read == true)
					result += "r";
				if(aux.permisiune.write == true)
					result += "w ";
				result += aux.permisiune.user.username;
				Object[] objAux = {aux.nume, "director", aux.dimensiune, result, aux.createdDate };
				obj[i] = objAux;
				i++;
			}
		}
		
		currentDir = keepDirectory;
		
		return obj;
	}
	
	
	public void lsRWithArgument(String word, JTextArea output) {
		Directory aux = currentDir;
		this.changePath(currentDir, word);
		if(aux != currentDir) {
			String auxPath = currentPath;
			if(word.charAt(0) == '/')
				currentPath = word;
			else currentPath += word + "/";
			this.lsR(currentDir, currentPath, output);
			currentDir = aux;
			currentPath = auxPath;
		}
		else output.append("Nu exista aceasta cale!");
	}
	
	public void lsAWithArgument(String word, JTextArea output) {
		Directory aux = currentDir;
		this.changePath(currentDir, word);
		if(aux != currentDir) {
			output.append(currentDir.toString());
			currentDir = aux;
		}
		else output.append("Nu exista acest director/fisier!");
	}
	
	
	public void cd(Directory root) {
		currentDir = root;
		currentPath = "/";
	}
	
	
	public void cdWithArgument(String word, Logger log) throws MyInvalidPathException {
		Directory aux = currentDir;
		this.changePath(currentDir, word);
		if(aux != currentDir) {
			if(word.charAt(0) == '/')
				currentPath = word;
			else {
				if(currentPath == "/")
					currentPath += word;
				else currentPath += "/" + word;
			}
		}
		else { 
			log.info(new MyInvalidPathException().toString());
			throw new MyInvalidPathException();
		}
	}
	
	
	public void cat(String word, JTextArea output, Logger log) throws MyInvalidPathException {
		
		Directory aux = currentDir;
		for(Directory subdir: aux.container)
			if(subdir.nume.matches(word)) {
				this.changePath(currentDir, subdir.nume);
				if(aux != currentDir) {
					File file = (File) currentDir;
					output.append(file.dateContinute + " ");
					currentDir = aux;
				}
				else {
					log.info(new MyInvalidPathException().toString());
					throw new MyInvalidPathException();
				}
			}
	}
	
	public void pwd(JTextArea output, Logger log) throws MyPathTooLongException {
		if(currentPath.length() <= 255)
			output.append(currentPath);
		else {
			log.info(new MyPathTooLongException().toString());
			throw new MyPathTooLongException();
		}
	}
	
	public void rm(String word, Logger log) throws MyInvalidPathException {
		Directory dir = currentDir;
		this.changePath(currentDir, word);
		if(dir != currentDir) {
			dir.container.remove(currentDir);
			currentDir = dir;
		}
		else  {
			log.info(new MyInvalidPathException().toString());
			throw new MyInvalidPathException();
		}
	}
	
	public void mkdir(String word, Permission permisiune) {
		Directory subDir = new Directory(word, permisiune);
		currentDir.container.add(subDir);
		currentDir.dimensiune += subDir.dimensiune;
	}
	
	public void touch(String word, Permission permisiune, String tip) {
		File file = new File(word, tip, permisiune);
		currentDir.container.add(file);
		currentDir.dimensiune += file.dimensiune;
	}
	
	public void echo(String mesaj, JTextArea output) {
		output.append(mesaj + " ");
	}
	
	public String getLastFromPath() {
		if(currentPath.compareTo("/") == 0)
			return "";
		String[] words = currentPath.split("/");
		int n = words.length;
		return words[n-1];
	}
	
	public void actualizareDimensiuni(Directory dir) {
		dir.dimensiune = 0;
		for(Directory subdir: dir.container) {
			if(subdir.container != null)
				this.actualizareDimensiuni(subdir);
			dir.dimensiune += subdir.dimensiune;
		}
	}

}
