package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class mkdirTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		
		cfs.commands.mkdir("Craciun", cfs.permisiune);
		cfs.commands.ls(cfs.commands.rootDir, output);
		assertEquals(output.getText().contains("Craciun"), true);
	}

}
