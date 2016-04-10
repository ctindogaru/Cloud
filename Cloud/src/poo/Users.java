package poo;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeSet;

import javax.swing.JTextArea;

class ComparatorUsername implements Comparator, Serializable {
	
	@Override
	public int compare(Object o1, Object o2) {
		UserConfig.User user1 = (UserConfig.User) o1;
		UserConfig.User user2 = (UserConfig.User) o2;
		return user1.username.compareTo(user2.username);
	}
}

class UserConfig implements Serializable {
	
	TreeSet<User> listaUseri;
	User currentUser;
	User guest;
	User root;
	
	public UserConfig() {
		listaUseri = new TreeSet<User>(new ComparatorUsername());
		currentUser = new User();
		root = new User("root", "root", "root", "root", new Date(), new Date());
		guest = new User("guest", "guest", "guest", "guest", new Date(), new Date());
		listaUseri.add(root);
		listaUseri.add(guest);
		currentUser = guest;
	}

	public class User implements Serializable{
	
		String username;
		String parola;
		String nume;
		String prenume;
		Date prima_logare;
		Date ultima_logare;
		
		public User(String username, String parola, String nume, String prenume, Date prima_logare, Date ultima_logare) {
			this.username = username;
			this.parola = parola;
			this.nume = nume;
			this.prenume = prenume;
			this.prima_logare = prima_logare;
			this.ultima_logare = ultima_logare;
		}
		
		public User() {
			
		}
		
		public String toString() {
			String result = "";
			result += username + " " + nume + " " + prenume + " " + prima_logare + " " + ultima_logare;
			return result;
		}
	}
	
	public void newuser(String usernameLogin, String passwordLogin, String numeLogin, String prenumeLogin, JTextArea output) {
		User user = new User(usernameLogin, passwordLogin, numeLogin, prenumeLogin, new Date(), new Date());
		listaUseri.add(user);
		currentUser = user;
		output.append("Sunteti logat ca si " + currentUser.username + "!\n");
	}
	
	public void login(String usernameLogin, String passwordLogin, JTextArea output) {
		int ok = 0;
		for(User user: listaUseri) {
			if(user.username.compareTo(usernameLogin) == 0 && user.parola.compareTo(passwordLogin) == 0) {
				currentUser = user;
				User user_aux = currentUser;
				user_aux.ultima_logare = new Date();
				listaUseri.remove(currentUser);
				listaUseri.add(user_aux);
				ok = 1;
				break;
			}
		}
		if(ok == 0)
			output.append("Username sau parola gresite!\n");
		else output.append("Sunteti logat ca si " + currentUser.username + "\n");
	}
	
	
	public void logout() {
		User guest_aux = guest;
		guest_aux.ultima_logare = new Date();
		listaUseri.remove(guest);
		listaUseri.add(guest_aux);
		currentUser = guest;
	}
	
	
	public String userinfo() {
		String result = "";
		result += "Username: " + currentUser.username + "\n" + "Nume: " + currentUser.nume + "\n" + "Prenume: " + currentUser.prenume + "\n";
		result += "Prima logare: " + currentUser.prima_logare + "\n" + "Ultima logare: " + currentUser.ultima_logare + "\n";
		return result;
	}
	public String[] userinfoPOO() {
		String[] result = new String[5];
		result[0] = "Username: " + currentUser.username;
		result[1] = "Nume: " + currentUser.nume;
		result[2] = "Prenume: " + currentUser.prenume;
		result[3] = "Prima logare: " + currentUser.prima_logare;
		result[4] = "Ultima logare: " + currentUser.ultima_logare;
		return result;
	}
}
