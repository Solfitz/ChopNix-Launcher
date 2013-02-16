package com.chopnix.panes;

import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.border.TitledBorder;
import com.chopnix.panes.UserD;
import com.chopnix.panes.Channel;



public class IrcPane extends JPanel{
	private static final ListModel nickListModel = null;
	protected static final UserD userb = null;
	protected Channel main;
	
	public IrcPane() {
		setBackground(Color.GRAY);
		setLayout(null);
		{
			JPanel nickholder = new JPanel();
			nickholder.setBackground(Color.GRAY);
			nickholder.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			nickholder.setBounds(698, 0, 171, 301);
			add(nickholder);
			nickholder.setLayout(null);
			{
				JList nickList=new JList(nickListModel);
				nickList.setBounds(6, 16, 155, 274);
				nickholder.add(nickList);
			}
		}
		{
			/*JPanel confignuttonholder = new JPanel();
			confignuttonholder.setBackground(Color.GRAY);
			confignuttonholder.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			confignuttonholder.setBounds(719, 300, 109, 49);
			add(confignuttonholder);
			confignuttonholder.setLayout(null);
			{
				JButton configurationButton = new JButton("Configuration");
				configurationButton.setBounds(6, 16, 97, 23);
				confignuttonholder.add(configurationButton);
				configurationButton.addActionListener(new ActionListener () {
					public void actionPerformed(ActionEvent evt) {
						ircstart1.userb.setText(UserD.getNick());
						userb.setText(UserD.getRealName());
						serverTField.setText("irc.chopnix.com");
						confWindow.setVisible(true);
					}
				});
			}*/
		}
		{
			JPanel tholder = new JPanel();
			tholder.setBackground(Color.GRAY);
			tholder.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			tholder.setBounds(10, 297, 684, 52);
			add(tholder);
			tholder.setLayout(null);
			{
				final JTextField textInputField = new JTextField();
				textInputField.setBounds(11, 16, 662, 29);
				tholder.add(textInputField);
				textInputField.addActionListener(new ActionListener() {               
					private JTabbedPane tabbedPane;

					public void actionPerformed(ActionEvent evt) {
						textInputStrArray = new String[3];
						textInputStr=textInputField.getText();
						textInputField.setText("");
						
						textInputStrArray[0]=textInputStr;
						textInputStrArray=textInputStrArray[0].split(" ", 2);
						
						if (textInputStrArray[0].toLowerCase().equals("/server")==true) {
							//TextArea.append("You want to connect to " + textInputStrArray[1] + "\n");
							try {
								ircServer=new Socket(textInputStrArray[1], 6667);
								is = new BufferedReader(new InputStreamReader(ircServer.getInputStream()));
								os = new PrintWriter(ircServer.getOutputStream(), true);
								main.addTextInTextArea("Connected to " + textInputStrArray[1] + "\n");
								osConnected=true;
								//nickTField.setEditable(false);
								//realNameTField.setEditable(false);
								//serverTField.setEditable(false);
								os.println("USER " + userb.getNick() + " 0 * :" + userb.getRealName() + "\n" );
								os.println("NICK " + userb.getNick() + "\n");
							}
							catch (UnknownHostException e) {
								main.addTextInTextArea("Could not connect to " + textInputStrArray[1] + "\n");
							}
							catch (IOException e) {
								main.addTextInTextArea("Could not get Input or Output stream.\n");
							}
							
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/cmd")==true) {
							os.println(textInputStrArray[1] + "\n");
							main.addTextInTextArea(textInputStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/join")==true) {
							
							os.println("JOIN " + textInputStrArray[1] + "\n");
							main.addTextInTextArea("Attempting to join " + textInputStrArray[1] + "\n");
							channel=textInputStrArray[1];
							
							
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/part")==true) {
							os.println("PART " + textInputStrArray[1] + "\n");
							
							if (channelHashMap.containsKey(textInputStrArray[1])==true) {
								HashMap tabTitles;
								for (int i = 0; i < tabbedPane.getTabCount(); i++) {
									if (tabbedPane.getTitleAt(i).equals(textInputStrArray[1])==true) {
										tabbedPane.removeTabAt(i);
										channelHashMap.remove(textInputStrArray[1]);
									}
								}
							}
							main.addTextInTextArea("Attempting to part " + textInputStrArray[1] + "\n");
						}
						
						else if ((textInputStr.charAt(0) != '/') && (tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Main")==false)) {
							os.println("PRIVMSG " + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + " :" + textInputStr + "\n");
							((Channel) channelHashMap.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()))).addTextInTextArea("<" + userb.getNick() + "> " + textInputStr + "\n");
							
						}
						
						
						else if (textInputStrArray[0].toLowerCase().equals("/msg")==true) {
							String[] msgStringArray;
							msgStringArray=new String[5];
							msgStringArray=textInputStrArray[1].split(" ", 2);
							if (channelHashMap.containsKey(msgStringArray[0])==false) {
								channelHashMap.put(msgStringArray[0], new Channel());
								tabbedPane.addTab(msgStringArray[0], ((Channel) channelHashMap.get(msgStringArray[0])).getChannelPanel());
								((Channel) channelHashMap.get(msgStringArray[0])).setIsChannel(false);
							}
							os.println("PRIVMSG " + msgStringArray[0] + " :" + msgStringArray[1]);
							((Channel) channelHashMap.get(msgStringArray[0])).addTextInTextArea("<" + userb.getNick() + "> " + msgStringArray[1] + "\n");
						}
						
						else if ((textInputStrArray[0].toLowerCase().equals("/action")) || (textInputStrArray[0].toLowerCase().equals("/me"))) {
							
							os.println("PRIVMSG " + channel + " :\u0001ACTION " + textInputStrArray[1] + "\n");
							((Channel) channelHashMap.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()))).addTextInTextArea(userb.getNick() + " " + textInputStrArray[1] + "\n");
							JTextArea theTextArea = null;
							theTextArea.append(userb.getNick() + " " + textInputStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/invite")==true) {
							os.println("INVITE " + textInputStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/whois")==true) {
							os.println("WHOIS " + textInputStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/whowas")==true) {
							os.println("WHOWAS " + textInputStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/who")==true) {
							os.println("WHO " + textInputStrArray[1] + "\n");
						}
						
						//		else if ((textInputStr.toLowerCase().equals("/quit")==true)) {
							//		os.println("QUIT\n");
							//	theTextArea.append("Quitting\n");
							//}
						
						else if (textInputStrArray[0].toLowerCase().equals("/quit")==true) {
							os.println("QUIT :" + textInputStrArray[1] + "\n");
							main.addTextInTextArea("Quitting: (" + textInputStrArray[1] + ")\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/nick")==true) {
							os.println("NICK :" + textInputStrArray[1] + "\n");
							//		theTextArea.append("NICK :" + textInputStrArray[1] + "\n");
							
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/mode")==true) {
							os.println("MODE " + textInputStrArray[1] + "\n");
							main.addTextInTextArea("MODE :" + textInputStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/topic")==true) {
							String[] topicStrArray;
							topicStrArray = new String[2];
							topicStrArray=textInputStrArray[1].split(" ", 2);
							os.println("TOPIC " + topicStrArray[0] + " :" + topicStrArray[1] + "\n");
						}
						
						else if (textInputStrArray[0].toLowerCase().equals("/kick")==true) {
							os.println("KICK " + textInputStrArray[1] + "\n");
							
						}
						
						else {
							main.addTextInTextArea(textInputStr.concat("\n"));
							
						}
					}
				});
				textInputField.setFont(new Font("MonoSpaced", Font.BOLD, 12));
			}
		}
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(10, 0, 684, 313);
		add(panel);
		panel.setLayout(null);
		{
			final JTabbedPane dholder = new JTabbedPane();
			dholder.setBackground(Color.GRAY);
			dholder.setBounds(2, 11, 680, 284);
			panel.add(dholder);
			{
				final JTextArea TextArea = new JTextArea();
				TextArea.setBackground(Color.DARK_GRAY);
				TextArea.setForeground(Color.GREEN);
				dholder.addTab("New tab", null, TextArea, null);
				TextArea.setLineWrap(true);
				TextArea.setWrapStyleWord(true);
				TextArea.setFont(new Font("MonoSpaced",Font.BOLD,12));
				TextArea.setEditable(false);
				TextArea.setPreferredSize(new java.awt.Dimension(763, 262));
			}
			{
				jTextArea_IL = main.getChannelPanel();
				dholder.addTab("Main", null, jTextArea_IL, null);
				jTextArea_IL.setPreferredSize(new java.awt.Dimension(724, 301));
			}
		}
	}
	
	
	public class getRootPane {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 955350984633316807L;
	private static final JTabbedPane tabbedPane = null;
	private static final JTextArea theTextArea = null;
	static String textInputStr;
	static String textOutputStr;
	static String[] textInputStrArray;
	static String[] textOutputStrArray;
	static String[] privmsgStrArray;
	static String[] actionStrArray;
	static String[] namesList;
	static Socket ircServer=null;
	static BufferedReader is=null;
	static PrintWriter os=null;
	private JPanel jTextArea_IL;
	static String fromServer=null;
	static boolean connectedToServer=false;
	static boolean osConnected=false;
	static String channel;
	static boolean inChannel=false;
	static boolean nickSuccessfullyChanged=true;
	static boolean firstItemRemoved=false;
	static HashMap channelHashMap;
	//HashMap channelHashMap = new HashMap();
	
//	static String fromServer=null;
	/**
	 * @param args
	 * @return 
	 */
	public void ircstart(String[] args) {
		channelHashMap=new HashMap();
		final JFrame confWindow = new JFrame("Configuration");
		final UserD userb = new UserD();

		final Channel main = new Channel();

		userb.loadInfo();
		
		JPanel confPanel = new JPanel();
		confPanel.setLayout(new GridLayout(14,1));
		final JLabel nickLabel = new JLabel("Nick:");
		confPanel.add(nickLabel);
		
		final JTextField nickTField = new JTextField();
		nickTField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
				
				if (connectedToServer==false) {
					userb.setNick(nickTField.getText());
					userb.saveInfo();
				}
			}
			
			public void keyPressed(KeyEvent evt) { }
			public void keyReleased(KeyEvent evt) { 
				userb.setNick(nickTField.getText());
				userb.saveInfo();
			}
		
		});
		
		confPanel.add(nickTField);
		
		final JLabel realNameLabel = new JLabel("Real name:");
		confPanel.add(realNameLabel);
		
		final JTextField realNameTField = new JTextField();
		realNameTField.addKeyListener(new KeyListener () {
			public void keyTyped(KeyEvent evt) {
				userb.setRealName(realNameTField.getText());
				userb.saveInfo();
			}
			
			public void keyPressed(KeyEvent evt) { }
			public void keyReleased(KeyEvent evt) { 
				userb.setRealName(realNameTField.getText());
				userb.saveInfo();
			}

		});
		
		confPanel.add(realNameTField);
		
		final JLabel serverLabel = new JLabel("Server:");
		confPanel.add(serverLabel);
		
		final JTextField serverTField = new JTextField();
		serverTField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
				userb.setServer(serverTField.getText());
				userb.saveInfo();
			}
			public void keyPressed(KeyEvent evt) { }
			public void keyReleased(KeyEvent evt) {
				userb.setServer(serverTField.getText());
				userb.saveInfo();
			}
			
		});
		
		confPanel.add(serverTField);
		
	/*	final JButton connectButton = new JButton("Connect");
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String serverStr;
				serverStr=serverTField.getText();
				AbstractButton textInputField;
				textInputField.setText("/server " + serverStr);
				textInputField.postActionEvent();
				textInputField.setText("");
				
			}
		});
		*/
	//	confPanel.add(connectButton);
		
//		JPopupMenu closeMenu = new JPopupMenu(String)

// insert here
		
		namesList=new String[1];
		namesList[0]=" ";
		
		DefaultListModel nickListModel=new DefaultListModel();
		nickListModel.addElement(" ");

		Component nickList = null;
		JScrollPane nickListScrollPane=new JScrollPane(nickList);
		nickListScrollPane.setPreferredSize(new Dimension(150,500));
		

			
		confWindow.setContentPane(confPanel);
		confWindow.setSize(300,300);
		confWindow.setResizable(false);

		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		while (true) {
			
			try {
				while ((fromServer=is.readLine()) != null) {		
					textOutputStrArray=new String[5];
					privmsgStrArray=new String[5];
					textOutputStrArray=fromServer.split(" :", 2);
					
					if (textOutputStrArray[0].equals("PING")==true) {
						os.println("PONG :" + textOutputStrArray[1] + "\n");
						break;
					}
					
					textOutputStrArray=fromServer.split(" ", 4);
					
					if (textOutputStrArray[1].equals("TOPIC")==true) {
						((Channel) channelHashMap.get(textOutputStrArray[2])).addTextInTextArea(textOutputStrArray[0].substring(1) + " has changed the topic to: " + textOutputStrArray[3].substring(1));
					}
					
					if (textOutputStrArray[1].equals("PRIVMSG")==true) {
						privmsgStrArray=textOutputStrArray[0].split("!", 2);
						privmsgStrArray[0]=privmsgStrArray[0].substring(1);
						
						if (channelHashMap.containsKey(textOutputStrArray[2])==true) {
						
							if (textOutputStrArray[3].startsWith(":\u0001ACTION")) {
								actionStrArray=textOutputStrArray[3].split(" ", 2);
								((Channel) channelHashMap.get(textOutputStrArray[2])).addTextInTextArea(privmsgStrArray[0] + " " + actionStrArray[1] + "\n");

							}
							else {
						
								((Channel) channelHashMap.get(textOutputStrArray[2])).addTextInTextArea("<" + privmsgStrArray[0] + "> " + textOutputStrArray[3].substring(1) + "\n");
							
							}
						}
						
						else if (channelHashMap.containsKey(privmsgStrArray[0])==true) {
							((Channel) channelHashMap.get(privmsgStrArray[0])).addTextInTextArea("<" + privmsgStrArray[0] + "> " + textOutputStrArray[3].substring(1) + "\n");
						}
						
						else {
							channelHashMap.put(privmsgStrArray[0], new Channel());
							tabbedPane.addTab(privmsgStrArray[0], (( Channel) channelHashMap.get(privmsgStrArray[0])).getChannelPanel());
							((Channel) channelHashMap.get(privmsgStrArray[0])).setIsChannel(false);
							((Channel) channelHashMap.get(privmsgStrArray[0])).addTextInTextArea("<" + privmsgStrArray[0] + "> " + textOutputStrArray[3].substring(1) + "\n");
						}
						
						
						break;
					}
					
					if (textOutputStrArray[1].equals("NICK")==true) {
						privmsgStrArray=textOutputStrArray[0].split("!", 2);
						privmsgStrArray[0]=privmsgStrArray[0].substring(1);
			
						
						ArrayList allChannelList = new ArrayList(channelHashMap.values());
						int nameIndex=0;
						for (int i=0; i < allChannelList.size(); i++) {
							
							nameIndex=((Channel) allChannelList.get(i)).getNameListIndexOf(privmsgStrArray[0]);
							if (nameIndex != -1) {
								((Channel) allChannelList.get(i)).setNameListElementAt(textOutputStrArray[2].substring(1), nameIndex);
								((Channel) allChannelList.get(i)).addTextInTextArea(privmsgStrArray[0] + " is now known as " + textOutputStrArray[2].substring(1) + "\n");
							}
							else {
								nameIndex=((Channel) allChannelList.get(i)).getNameListIndexOf("@" + privmsgStrArray[0]);
								if (nameIndex != -1) {
									((Channel) allChannelList.get(i)).setNameListElementAt("@" + (textOutputStrArray[2].substring(1)), nameIndex);
									((Channel) allChannelList.get(i)).addTextInTextArea(privmsgStrArray[0] + "is now known as " + textOutputStrArray[2].substring(1) + "\n");
								}
								else {
									nameIndex=((Channel) allChannelList.get(i)).getNameListIndexOf("+" + privmsgStrArray[0]);
									if (nameIndex != -1) {
										((Channel) allChannelList.get(i)).setNameListElementAt("+" + (textOutputStrArray[2].substring(1)), nameIndex);
										((Channel) allChannelList.get(i)).addTextInTextArea(privmsgStrArray[0] + "is now known as " + textOutputStrArray[2].substring(1) + "\n");
									}
								}
							}
								
						}
						
						break;
					}
					
					if (textOutputStrArray[1].equals("QUIT")==true) {
						privmsgStrArray=textOutputStrArray[0].split("!", 2);
						privmsgStrArray[0]=privmsgStrArray[0].substring(1);
						
						ArrayList allChannelList=new ArrayList(channelHashMap.values());
						
						for (int i=0; i < allChannelList.size(); i++) {
							int index;
							index=((Channel) allChannelList.get(i)).getNameListIndexOf(privmsgStrArray[0]);
							if (index != -1) {
								((Channel) allChannelList.get(i)).nameListRemoveElementAt(index);
								((Channel) allChannelList.get(i)).addTextInTextArea(privmsgStrArray[0] + " has quit the network:" + textOutputStrArray[3] + "\n");
							}
							
							else {
								index=((Channel) allChannelList.get(i)).getNameListIndexOf("@" + privmsgStrArray[0]);
								if (index != -1) {
									((Channel) allChannelList.get(i)).nameListRemoveElementAt(index);
									((Channel) allChannelList.get(i)).addTextInTextArea(privmsgStrArray[0] + " has quit the network:" + textOutputStrArray[3] + "\n");
								}
								
								else {
									index=((Channel) allChannelList.get(i)).getNameListIndexOf("+" + privmsgStrArray[0]);
									if (index != -1) {
										((Channel) allChannelList.get(i)).nameListRemoveElementAt(index);
										((Channel) allChannelList.get(i)).addTextInTextArea(privmsgStrArray[0] + " has quit the network:" + textOutputStrArray[3] + "\n");
									}
								}
							}
						}	
						
						break;
					}
					
					if (textOutputStrArray[1].equals("PART")==true) {
						privmsgStrArray=textOutputStrArray[0].split("!", 2);
						privmsgStrArray[0]=privmsgStrArray[0].substring(1);
						
						if (((Channel) (channelHashMap.get(textOutputStrArray[2]))).nameListHandlePart(privmsgStrArray[0], userb.getNick())==-1) {
							firstItemRemoved=false;
						}
												
						((Channel) channelHashMap.get(textOutputStrArray[2])).addTextInTextArea(privmsgStrArray[0] + " has parted the channel." + "\n");
						
						break;
					}
					
					if ((textOutputStrArray[1].equals("JOIN")==true && inChannel==true)) {
						
						privmsgStrArray=textOutputStrArray[0].split("!", 2);
						privmsgStrArray[0]=privmsgStrArray[0].substring(1);
						
						((Channel) channelHashMap.get(textOutputStrArray[2].substring(1))).addToNameList(privmsgStrArray[0]);
						((Channel) channelHashMap.get(textOutputStrArray[2].substring(1))).addTextInTextArea(privmsgStrArray[0] + " has joined the channel.\n");
						break;
					}
					
					textOutputStrArray=fromServer.split(" ", 12);
					
					int index=0;
					int cindex=0;
					String pom="";
					String oov="";
					
					if (textOutputStrArray[1].equals("MODE")==true) {
						
						ArrayList allChannelList = new ArrayList(channelHashMap.values());
						while (true) {
							
						
							try {
								if (textOutputStrArray[3].charAt(index)=='+') {
									pom="plus";
									oov="indeterminate";
									index++;
								}
						
								else if (textOutputStrArray[3].charAt(index)=='-') {
									pom="minus";
									oov="indeterminate";
									index++;
								}
								
							   if (textOutputStrArray[3].charAt(index)=='o') {
									oov="op";
									cindex++;
									index++;
								}
								
								else if (textOutputStrArray[3].charAt(index)=='v') {
									oov="voice";
									cindex++;
									index++;
								}
								
								if ((pom.equals("plus")==true) && (oov.equals("op")==true)) {
									int nameToOpIndex;
									String nameToOpString;
									
									if (((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf(textOutputStrArray[3+cindex])==-1) {
										break;
									}
									
									nameToOpIndex=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf(textOutputStrArray[3+cindex]);
						
									nameToOpString=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListElementAt(nameToOpIndex);
						
									((Channel) channelHashMap.get(textOutputStrArray[2])).setNameListElementAt("@" + nameToOpString, nameToOpIndex);
						
								}
								
								else if ((pom.equals("plus")==true) && (oov.equals("voice")==true)) {
									int nameToVoiceIndex;
									String nameToVoiceString;
								
									if (((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf(textOutputStrArray[3+cindex])==-1) {
										break;
									}
												
									nameToVoiceIndex=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf(textOutputStrArray[3+cindex]);
									nameToVoiceString=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListElementAt(nameToVoiceIndex);
									((Channel) channelHashMap.get(textOutputStrArray[2])).setNameListElementAt("+" + nameToVoiceString, nameToVoiceIndex);
		
								}
								
								else if ((pom.equals("minus")==true) && ((oov.equals("op")==true) || (oov.equals("voice")==true))) {
									int nameToDeopIndex;
									String nameToDeopString;
									if (oov.equals("op")==true) {
										
										if (((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf("@" + textOutputStrArray[3+cindex])==-1) {
											break;
										}
										
									}
									
									else if (oov.equals("voice")==true) {
										
										if (((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf("+" + textOutputStrArray[3+cindex])==-1) {
											break;
										}
									
									}
									
									if (oov.equals("op")==true) {
										
										nameToDeopIndex=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf("@" + textOutputStrArray[3+cindex]);
									
									}
									
									else {
										nameToDeopIndex=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListIndexOf("+" + textOutputStrArray[3+cindex]);
									}
									nameToDeopString=((Channel) channelHashMap.get(textOutputStrArray[2])).getNameListElementAt(nameToDeopIndex);
									((Channel) channelHashMap.get(textOutputStrArray[2])).setNameListElementAt(nameToDeopString.substring(1), nameToDeopIndex);
									
									break;
								}
								
								else {
									break;
								}
								
								
							}
						
							catch (IndexOutOfBoundsException e) {
								break;
						
							}
							
						}
					}
							
					textOutputStrArray=fromServer.split(" ", 6);
					
					if ((textOutputStrArray[1].equals("353"))==true) {
						
						int spaceCount=0;
						for (int i=0; i < textOutputStrArray[5].length(); i++ ) {
							if (textOutputStrArray[5].charAt(i)==' ') {
								spaceCount++;
							}
						}
						
						if (channelHashMap.containsKey(textOutputStrArray[4])==false) {
						
							channelHashMap.put(textOutputStrArray[4], new Channel());
							tabbedPane.addTab(textOutputStrArray[4], ((Channel) channelHashMap.get(textOutputStrArray[4])).getChannelPanel());
							((Channel) channelHashMap.get(textOutputStrArray[4])).setIsChannel(true);
						}
									
						namesList=new String[spaceCount];
						namesList=textOutputStrArray[5].split(" ", (spaceCount + 1));
						
						namesList[0]=namesList[0].substring(1);
						
						for (int i=0; i < (spaceCount + 1); i++) {
							
							((Channel) channelHashMap.get(textOutputStrArray[4])).addToNameList(namesList[i]);
						}
						
						inChannel=true;
						main.addTextInTextArea(fromServer + "\n");
						
					}
					else { 
						main.addTextInTextArea(fromServer + "\n");
					}
				}
			}
			catch (IOException e) {
					theTextArea.append("IOException\n");
			}
			
			catch (NullPointerException e ) {
				
			}
			
		}
		
	}
	
	private void initGUI() {
		try {
			{
				this.setPreferredSize(new java.awt.Dimension(850, 430));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}