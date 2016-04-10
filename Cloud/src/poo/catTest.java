package poo;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import javax.swing.JTextArea;

import org.junit.Test;

public class catTest {

	@Test
	public void test() throws MyInvalidPathException {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		File file = new File("fisier", "binar", cfs.permisiune);
		cfs.root.add(file);
		
		Logger log = Logger.getLogger("MyLog"); 
		cfs.commands.cat(file.nume, output, log);
		assertEquals(file.dateContinute + " ", output.getText());
	}

}
