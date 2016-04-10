package poo;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import javax.swing.JTextArea;

import org.junit.Test;

public class rmTest {

	@Test
	public void test() throws MyInvalidPathException {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		
		
		Logger log = Logger.getLogger("MyLog"); 
		cfs.commands.rm("Desktop", log);
		cfs.commands.ls(cfs.commands.rootDir, output);
		assertEquals(output.getText().contains("Desktop"), false);
	}

}
