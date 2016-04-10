package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class lsTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		cfs.commands.ls(cfs.root, output);
		assertEquals("Desktop Documents Downloads Home Music Pictures Videos Trash ", output.getText());
	}

}
