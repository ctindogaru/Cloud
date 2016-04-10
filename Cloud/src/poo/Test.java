package poo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

public class Test extends JFrame {
	
	JTextField input;
	JTextArea output;
	UserConfig userconfig;
	UserConfig.User user;
	Permission permisiune;
	Directory root;
	Commands commands;
	ReadAndWriteFromFile readOrWrite;
	CloudService[] cloudService;
	String exit;
	int stationNumber;
	String lastLine = "";
	JPanel insidePanel;
	JList list;
	DefaultListModel model;
	ArrayList<String> autocompleteWords;
	Logger log;
	
	
	public String getLastCommand() {
		String text = input.getText();
		input.setText("");
		return text;
	}
	
	public Test() {
		super();
		setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		input = new JTextField();
		input.setFont(new Font("Arial", Font.PLAIN, 14));
		
		input.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					lastLine = getLastCommand();
					try {
						executeCommand(lastLine);
					} catch (MyNotEnoughSpaceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					model.removeAllElements();
				}
				else if(e.getKeyCode() != KeyEvent.VK_CAPS_LOCK && e.getKeyCode() != KeyEvent.VK_SHIFT)
					if(input.getText().length() >= 3 && (input.getText().substring(0, 2).equals("cd") == true || input.getText().substring(0, 2).equals("rm") == true)){
						list.setVisible(true);
						autocompleteWords = new ArrayList<String>();
						model.removeAllElements();
						
						for(Directory aux: commands.currentDir.container) {
							autocompleteWords.add(aux.nume);
							model.addElement(aux.nume);
						}
						
						String possibilities = input.getText().substring(3, input.getText().length()) + e.getKeyChar() + ".*";
						
						for(String aux: autocompleteWords) {
							if(aux.matches(possibilities)) {
								if(model.contains(aux) == false)
									model.addElement(aux);
							}
							else model.removeElement(aux);
					}
					pack();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JPanel auxPanel = new JPanel();
		auxPanel.setLayout(new BoxLayout(auxPanel, BoxLayout.Y_AXIS));
		auxPanel.add(input);
		input.setPreferredSize(new Dimension(1000, 25));
		//auxPanel.setPreferredSize(new Dimension(1000, 35));
		JScrollPane inputScroll = new JScrollPane(auxPanel);
		//inputScroll.add(input);
		inputScroll.add(new JLabel("TETET"));
		insidePanel = new JPanel();
		insidePanel.setLayout(new BoxLayout(insidePanel, BoxLayout.Y_AXIS));
		//insidePanel.add(output);
		model = new DefaultListModel();
		list = new JList(model);
		auxPanel.add(list);
		
		log = Logger.getLogger("MyLog");  
	    FileHandler fh;
	    
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("C:/Users/Constantin/workspace/Cloud/MyLogFile.log");  
	        log.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	    
	    //log.setUseParentHandlers(false);
		
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				
				/*if(input.getText().length() >= 2) {
					if(input.getText().substring(0, 2).equals("cd") == true) {
						input.setText("cd " + (String) list.getSelectedValue());
					}
					else input.setText("rm " + (String) list.getSelectedValue());
				}*/
				
			}
			
		});
		
		list.setVisible(false);
		JScrollPane outputScroll = new JScrollPane(insidePanel);
		outputScroll.setPreferredSize(new Dimension(1000,400));
		inputScroll.setPreferredSize(new Dimension(1000,35));
		JPanel inputPanel = new JPanel();
		JPanel outputPanel = new JPanel();
		inputPanel.add(auxPanel);
		outputPanel.add(outputScroll);
		autocompleteWords = new ArrayList<String>();
		
		add(outputPanel);
		add(inputPanel, BorderLayout.SOUTH);

		pack();
		setVisible(true);
		
		
		
		userconfig = new UserConfig();
		user = userconfig.root;
		permisiune = new Permission(true, true, user);
		// directorul /
		root = new Directory("", permisiune);
		root.add(new Directory("Desktop", permisiune));
		root.add(new Directory("Documents", permisiune));
		root.add(new Directory("Downloads", permisiune));
		root.add(new Directory("Home", permisiune));
		root.add(new Directory("Music", permisiune));
		root.add(new Directory("Pictures", permisiune));
		root.add(new Directory("Videos", permisiune));
		root.add(new Directory("Trash", permisiune));
		
		user = userconfig.guest;
		permisiune = new Permission(false, false, user);

		commands = new Commands(root);
		Scanner keyboard = new Scanner(System.in);
		exit = "";
		
		readOrWrite = new ReadAndWriteFromFile();
		readOrWrite.readLastState(commands, userconfig, "stare_curenta.dat"); 
		commands.currentDir = commands.rootDir;
		//System.out.println(userconfig.listaUseri);
		

		
		cloudService = new CloudService[3];
		cloudService[0] = new CloudService();
		cloudService[1] = new CloudService();
		cloudService[2] = new CloudService();
		//readOrWrite.readLastState(cloudService, "CloudStation.dat");
		/*System.out.println(cloudService[0].dimensiuneCloud);
		System.out.println(cloudService[1].dimensiuneCloud);
		System.out.println(cloudService[2].dimensiuneCloud);*/
		stationNumber = 0;
		//System.out.println(userconfig.currentUser.username);
		
	}
	
	public void instantiateOutput() {
		output = new JTextArea();
		output.setText("");
		output.setFont(new Font("Arial", Font.PLAIN, 14));
		output.setEditable(false);
		insidePanel.add(output);
		pack();
	}
	
	public String printUserAndLocation() {
		return userconfig.currentUser.username + "@" + commands.currentPath + ": ";
	}
	
	public void executeCommand(String lastCommand) throws MyNotEnoughSpaceException {
		//while(exit.compareTo("exit") != 0) {
		//String newCommand = keyboard.nextLine();
		String words[] = lastCommand.split(" ");
		int n = words.length;
		if(n == 1) {
			if(words[0].compareTo("ls") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					commands.ls(commands.currentDir, output);
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("ls-r") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					commands.lsR(commands.currentDir, commands.currentPath, output);
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("cd") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					commands.cd(commands.rootDir);
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("cat") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					output.append("Introduceti numele fisierului!\n");
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("pwd") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					try {
						commands.pwd(output, log);
					}
					catch(MyPathTooLongException e) {
						e.printStackTrace();
					}
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("mkdir") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					output.append("Introduceti numele directorului!\n");
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("touch") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					output.append("Introduceti numele fisierului!\n");
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("rm") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					output.append("Introduceti numele fisierului/directorului pe care doriti sa-l stergeti!");
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("echo") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				output.append("Introduceti un mesaj!");
			}
			else if(words[0].compareTo("logout") == 0) {
				log.info("S-a delogat utilizatorul " + userconfig.currentUser.username);
				userconfig.logout();
				permisiune = new Permission(true, true, userconfig.currentUser);
			}
			else if(words[0].compareTo("userinfo") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				output.append(userconfig.userinfo());
			}
			else if(words[0].compareTo("exit") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				output.append("La revedere!");
				readOrWrite.writeCurrentState(commands.rootDir, userconfig.listaUseri, "stare_curenta.dat");
				readOrWrite.writeCurrentState(cloudService, "CloudStation.dat");
				System.out.println(cloudService[0].dimensiuneCloud);
				System.out.println(cloudService[1].dimensiuneCloud);
				System.out.println(cloudService[2].dimensiuneCloud);
				System.exit(0);
				//readOrWrite.writeCurrentState(commands.rootDir, userconfig.listaUseri);
			}
			else  {
				instantiateOutput();
				output.append(printUserAndLocation() + words[0] + "\n");
				output.append("Comanda nu e valida!");
			}
			
		}
		
		else if(n == 2) {
			if(words[0].compareTo("ls") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					commands.lsWithArgument(words[1], output);
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("ls-r") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0)
					commands.lsRWithArgument(words[1], output);
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("ls-a") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					if(words[1].compareTo("-POO") != 0) {
							commands.lsAWithArgument(words[1], output);
					}
					else {
						String[] columnNames = {"Nume",
				                "Tip",
				                "Dimensiune",
				                "Permisiuni",
				                "Cand a fost creat"};
						JTable table = new JTable(commands.lsLPOO(), columnNames);
						JScrollPane scroll = new JScrollPane(table);
						scroll.setPreferredSize(new Dimension(150,150));
						table.setFont(new Font("Arial", Font.PLAIN, 14));
						insidePanel.add(scroll);
						pack();
					}
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("userinfo") == 0 && words[1].compareTo("-POO") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				//insidePanel.setLayout(new GridLayout(2,1));
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					JList list = new JList(userconfig.userinfoPOO());
					list.setFont(new Font("Arial", Font.PLAIN, 14));
					insidePanel.add(list);
					pack();
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("cd") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					try {
						commands.cdWithArgument(words[1], log);
					}
					catch(MyInvalidPathException e) {
						e.printStackTrace();
					}
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("mkdir") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				int i = 0;
				if(commands.currentDir.permisiune.user.username.compareTo(userconfig.currentUser.username) == 0)
					i = 1;
				if(commands.currentDir.permisiune.user.username.compareTo("root") == 0)
					i = 1;
				//System.out.println(i);
				if(userconfig.currentUser.username.compareTo("guest") != 0 && i == 1) {
					commands.mkdir(words[1], permisiune);
					//commands.actualizareDimensiuni(commands.rootDir);
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("touch") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				int i = 0;
				if(commands.currentDir.permisiune.user.username.compareTo(userconfig.currentUser.username) == 0)
					i = 1;
				if(commands.currentDir.permisiune.user.username.compareTo("root") == 0)
					i = 1;
				int binarSauText = (int) (Math.random() * 2);
				String tip = "";
				if(binarSauText == 0)
					tip = "binar";
				else tip = "text";
				if(userconfig.currentUser.username.compareTo("guest") != 0 && i == 1) {
					commands.touch(words[1], permisiune, tip);
					commands.actualizareDimensiuni(commands.rootDir);
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			
			else if(words[0].compareTo("cat") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					try {
						commands.cat(words[1], output, log);
					}
					catch(MyInvalidPathException e) {
						e.printStackTrace();
					}
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else if(words[0].compareTo("echo") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				commands.echo(words[1], output);
			}
			
			else if(words[0].compareTo("upload") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					ArrayList<String> toBeDeleted = new ArrayList<String>();
					int trigger = cloudService[stationNumber].upload(words[1], commands, 0, toBeDeleted, stationNumber, log, userconfig.currentUser);
					if(trigger != 0) {
						if(stationNumber < 2) {
							// se va copia intreg directorul pe urmatoarea statie si se vor sterge din el subdirectoarele de pe prima statie, adica toBeDeleted
							toBeDeleted = cloudService[stationNumber].parent;
							int i;
							/*for(i = 0; i < toBeDeleted.size(); i++)
								System.out.println(toBeDeleted.get(i)); */
							cloudService[++stationNumber].upload(words[1], commands, 1, toBeDeleted, stationNumber, log, userconfig.currentUser);
						}
						else {
							log.info("");
							output.append("Nu e suficient loc pe cloud pentru a incarca tot directorul!");
						}
					}
					System.out.println(cloudService[0].dimensiuneCloud);
					System.out.println(cloudService[1].dimensiuneCloud);
					System.out.println(cloudService[2].dimensiuneCloud);
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			
			else if(words[0].compareTo("sync") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				if(userconfig.currentUser.username.compareTo("guest") != 0) {
					ArrayList<Directory> directoriesOnPrevStation = new ArrayList<Directory>();
					ArrayList<String> namesOnPrevStation = new ArrayList<String>();
					Directory syncDir1 = cloudService[0].sync(words[1], commands, directoriesOnPrevStation, namesOnPrevStation);
					directoriesOnPrevStation = cloudService[0].myCloud;
					namesOnPrevStation = cloudService[0].thisDirectorName;
					Directory syncDir2 = cloudService[1].sync(words[1], commands, directoriesOnPrevStation, namesOnPrevStation);
					directoriesOnPrevStation = cloudService[1].myCloud;
					namesOnPrevStation = cloudService[1].thisDirectorName;
					Directory syncDir3 = cloudService[2].sync(words[1], commands, directoriesOnPrevStation, namesOnPrevStation);
					if(syncDir1 != null) {
						System.out.println("radacina e pe prima statie");
						commands.currentDir.add(syncDir1);
					}
					else if(syncDir2 != null) {
						System.out.println("radacina e pe statia 2");
						commands.currentDir.add(syncDir2);
					}
					else if(syncDir3 != null) {
						System.out.println("radacina e pe statia 3");
						commands.currentDir.add(syncDir3);
					}
					commands.actualizareDimensiuni(commands.rootDir);
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			
			else if(words[0].compareTo("rm") == 0) {
				instantiateOutput();
				output.append(printUserAndLocation() + lastCommand + "\n");
				int i = 0;
				if(commands.currentDir.permisiune.user.username.compareTo(userconfig.currentUser.username) == 0)
					i = 1;
				if(commands.currentDir.permisiune.user.username.compareTo("root") == 0)
					i = 1;
				if(userconfig.currentUser.username.compareTo("guest") != 0 && i == 1) {
					try {
						commands.rm(words[1], log);
						commands.actualizareDimensiuni(commands.rootDir);
					}
					catch(MyInvalidPathException e) {
						e.printStackTrace();
					}
				}
				else output.append("Nu ai suficiente drepturi pentru aceasta comanda!");
			}
			else output.append("Comanda nu e valida!");
		}
		
		else if(words[0].compareTo("echo") == 0) {
			instantiateOutput();
			output.append(printUserAndLocation() + lastCommand + "\n");
			if(words[1].compareTo("-POO") == 0) {
				int i;
				String result = "";
				for(i = 2; i < words.length; i++)
					result += words[i] + " ";
				JOptionPane.showMessageDialog(null, result);
			}
			else {
				int i;
				for(i = 1; i < words.length; i++)
					commands.echo(words[i], output);
				//output.append("\n");
			}
		}
		
		else if(words[0].compareTo("login") == 0) {
			instantiateOutput();
			output.append(printUserAndLocation() + lastCommand + "\n");
			userconfig.login(words[1], words[2], output);
			permisiune = new Permission(true, true, userconfig.currentUser);
			log.info("S-a logat utilizatorul " + userconfig.currentUser.username);
		}
		
		else if(words[0].compareTo("newuser") == 0) {
			instantiateOutput();
			output.append(printUserAndLocation() + lastCommand + "\n");
			userconfig.newuser(words[1], words[2], words[3], words[4], output);
			permisiune = new Permission(true, true, userconfig.currentUser);
		}
		
		else {
			instantiateOutput();
			output.append(printUserAndLocation() + lastCommand + "\n");
			output.append("Numar gresit de argumente!");
		}
		
		exit = lastCommand;
	//}
	}
	
	
	
	public static void main(String[] args) {

		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		new Test();
		
	}
}
