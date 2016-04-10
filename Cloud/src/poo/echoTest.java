package poo;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Test;

public class echoTest {

	@Test
	public void test() {
		CreateFileSystem cfs = new CreateFileSystem();
		JTextArea output = new JTextArea();
		cfs.commands.echo("testing command echo", output);
		// metoda mea lasa si un spatiu la final, de-aia l-am pus acolo
		assertEquals("testing command echo ", output.getText());
	}

}
