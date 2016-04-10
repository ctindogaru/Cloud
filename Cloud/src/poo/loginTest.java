package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class loginTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		cfs.userconfig.currentUser = cfs.userconfig.guest;
		cfs.userconfig.login("root", "root", output);
		assertEquals(cfs.userconfig.currentUser.username, "root");
	}

}
