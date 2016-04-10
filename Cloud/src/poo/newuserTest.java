package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class newuserTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();

		cfs.userconfig.newuser("gigi", "gigipass", "Dornea", "Georgian", output);
		// la mine atunci cand creez un user, il logheaza automat, asadar daca s-a creat cu succes, gigi ar trebui sa fie acum logat
		
		
		assertEquals(cfs.userconfig.currentUser.username, "gigi");
	}

}
