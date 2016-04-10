package poo;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import javax.swing.JTextArea;

import org.junit.Test;

public class cdTest {

	@Test
	public void test() throws MyInvalidPathException {
		CreateFileSystem cfs = new CreateFileSystem();
		
		Logger log = Logger.getLogger("MyLog");
		cfs.commands.cdWithArgument("Desktop", log);
		assertEquals(cfs.commands.currentPath, "/Desktop");
	}

}
