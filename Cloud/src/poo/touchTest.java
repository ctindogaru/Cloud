package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class touchTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		
		cfs.commands.touch("file", cfs.permisiune, "binar");
		cfs.commands.ls(cfs.commands.rootDir, output);
		assertEquals(output.getText().contains("file"), true);
	}

}
