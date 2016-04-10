package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class logoutTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		cfs.userconfig.currentUser = cfs.userconfig.root;
		cfs.userconfig.logout();
		assertEquals(cfs.userconfig.currentUser.username, "guest");
	}

}
