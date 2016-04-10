package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class userinfoTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		String result = cfs.userconfig.userinfo();
		
		assertEquals(result.contains("Username: " + cfs.userconfig.currentUser.username + "\n" + "Nume: " + cfs.userconfig.currentUser.nume + "\n" + "Prenume: " + cfs.userconfig.currentUser.prenume + "\n"), true);
	}
}
