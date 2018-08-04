package clientSide;
/*@author Damdin-Yadam O.
* @version 1.0.0 June2018 
* 
* */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import utilities.*;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements ActionListener, Observer
{
	private int turn = 0;
	public String reConnect;
	private JButton button[] = new JButton[9];
	private JPanel north, gameBoard, center, leftPanel, rightPanel, nPanel, lowerPanel, chatPanel;
	private JLabel label, cLabel, welcome;
	private JTextArea ta;
	private JTextField msgToSend, ipAddress;
	private JButton discButton, connButton, sendButton;
	private Socket socket;
	private ObjectOutputStream oos;
	private InputListener listener; 
	private String currentPlayer;
	private boolean win = false;
	
	private int[][] winCombo = new int[][] {
			{0,1,2}, //
			{3,4,5}, //
			{6,7,8}, //
			
			{0,3,6}, //
			{1,4,7}, //
			{2,5,8}, //
			
			{0,4,8}, //
			{2,4,6}	 //
			};
	
	/**
	 *  GUI klienta
	 */
	public ClientGUI()
	{			
		this.setTitle("Kó³ko i krzy¿yk");
		this.setBounds(300,100,800,500);
		this.setResizable(false);
		this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
               disconnect();
               System.exit(0);
            }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
         });
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout(5,5));
		
		north = new JPanel(new FlowLayout());
		north.setBackground(Color.WHITE);
		north.setBorder(BorderFactory.createRaisedBevelBorder());
		welcome = new JLabel("Witaj !");
		welcome.setForeground(new Color(59, 89, 182));
		welcome.setFont(new Font("Tahoma",Font.BOLD,20));
		
		label = new JLabel("Enter your IP address: ");
		label.setForeground(new Color(59, 89, 182));
		label.setFont(new Font("Tahoma",Font.BOLD,12));
		ipAddress = new JTextField(20);
		connButton = new JButton("Connect");
		connButton.setBackground(new Color(59, 89, 182));
		connButton.setForeground(Color.WHITE);
		connButton.addActionListener(this);
		discButton = new JButton("Disconnect");
		discButton.setBackground(new Color(59, 89, 182));
		discButton.setForeground(Color.WHITE);
		discButton.setEnabled(false);
		discButton.addActionListener(this);
		north.add(welcome);
		north.add(Box.createHorizontalStrut(40));
		north.add(label);
		north.add(ipAddress);
		north.add(connButton);
		north.add(discButton);
		pane.add(north, BorderLayout.NORTH);
		
		center = new JPanel(new GridLayout(1,2));
		center.setBorder(BorderFactory.createRaisedBevelBorder());
		
		//Lewa strona
		leftPanel = new JPanel(new BorderLayout());
		gameBoard = new JPanel(new GridLayout(3,3));
		leftPanel.add(gameBoard, BorderLayout.CENTER);
		center.add(leftPanel);
		
		//Prawa strona
		rightPanel = new JPanel(new BorderLayout());
		nPanel = new JPanel(new BorderLayout(5,5));
		nPanel.setBackground(Color.WHITE);
		cLabel = new JLabel("Chat",SwingConstants.CENTER);
		cLabel.setForeground(new Color(59, 89, 182));
		cLabel.setFont(new Font("Tahoma",Font.BOLD,18));
		nPanel.add(cLabel);
		
		chatPanel = new JPanel(new FlowLayout());
		chatPanel.setBackground(Color.WHITE);
		lowerPanel = new JPanel(new BorderLayout(5,5));
		sendButton = new JButton("Send");
		sendButton.setBackground(new Color(59, 89, 182));
		sendButton.setForeground(Color.WHITE);
		sendButton.addActionListener(this);
		lowerPanel.add(sendButton);
	
		msgToSend = new JTextField(23);
		msgToSend.addActionListener(this);
		ta = new JTextArea(21,30);
		
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setEditable(false);
		DefaultCaret caret = (DefaultCaret)ta.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		chatPanel.add(new JScrollPane(ta));
		chatPanel.add(msgToSend);
		chatPanel.add(sendButton);
		
		rightPanel.add(nPanel, BorderLayout.NORTH);
		rightPanel.add(chatPanel, BorderLayout.CENTER);
		rightPanel.add(lowerPanel, BorderLayout.SOUTH);
		center.add(rightPanel);
		
		//dodaj obie strony do centr
		pane.add(center);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//dodaj przyciski do game board
		for(int i = 0 ;i < 9;i++)
		{
			button[i] = new JButton();
			gameBoard.add(button[i]);
			button[i].setEnabled(false);
			button[i].setBackground(Color.black);
			button[i].addActionListener(this);
		}
		
		//pierwszy gracz, który do³¹czy, gracz x
		currentPlayer = "X";
	}
	
	/**
	 * Metoda oczyszczania planszy wszystkich ruchów
	 */
	public void resetGameBoard()
	{
		for(int i = 0 ;i < 9;i++)
		{
			button[i].setText("");
			button[i].setEnabled(true);
			button[i].setBackground(Color.black);
			win = false;
			turn = 0;
		}
	}
	
	/**
	 * Metoda zmiany gracza po ka¿dym ruchu
	 */
	public void changePlayer()
	{
		if(currentPlayer.equals("X"))
		{
			currentPlayer = "O";
		}
		else
		{
			currentPlayer = "X";
		}
	}

	/**
	 * Metoda wysy³ania wiadomoœci do przeciwnika informuj¹cego, ¿e gracz jest
*       roz³¹czony i od³¹czony odtwarzacz
	 */
	public synchronized void disconnect()
	{
		try 
		{
			Message mes = new Message("Opponent disconnected!");
			oos.writeObject(mes);

			oos.close();
			socket.close();
			listener = null;
			
			for(int i = 0 ;i < 9;i++)
			{
				button[i].setText("");
				button[i].setEnabled(false);
				button[i].setBackground(Color.black);
			}
	
		} catch (IOException e1) 
		{
			//e1.printStackTrace();
		}
	}
	
	/**
	 * Metoda aktualizowania GUI klienta na podstawie typu obiektu przekazywanego do metody
       Ponownie z³¹czy przeciwnika, jeœli drugi przeciwnik od³¹czy siê od nich. Miejsca poruszaj¹ siê po
       GUI innych przeciwników. Monituje u¿ytkownika, aby zagra³ ponownie, jeœli gra siê zakoñczy.
	 * @param o - observable object
	 * @param arg - a specified object
	 */
	@Override
	public synchronized void update(Observable o, Object arg) 
	{
		if(arg instanceof Message)
		{
			Message m = (Message)arg;
			if(m.getMove() != null)
			{
				ta.append(m.getMove() + "\n");
				if(m.getMove().equals("Opponent disconnected!"))
				{
					try 
					{
						int answer = JOptionPane.showConfirmDialog(null,"Your opponent has left. Would you like to find a new match?", "Confirm",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(answer == JOptionPane.NO_OPTION)
							{
								for(int i = 0 ;i < 9;i++)
								{
									button[i].setEnabled(false);
									button[i].setText("");
									button[i].setBackground(Color.black);
								}
								oos.close();
								socket.close();
								listener = null;
								ta.append("Disconnected.\n");
								connButton.setEnabled(true);
								discButton.setEnabled(false);
							}
							if(answer == JOptionPane.YES_OPTION)
							{
								resetGameBoard();
								oos.close();
								socket.close();
								listener = null;
								ta.append("Finding new opponent...\n");
								
								//zapisz adres ip w zmiennej i przenieœ na serwer
								socket = new Socket(reConnect,5555);
								oos = new ObjectOutputStream(socket.getOutputStream()); //for sending
									
								listener = new InputListener(socket,ClientGUI.this);
								Thread thread1 = new Thread(listener);
								thread1.start(); //uruchamia w¹tek, aby s³uchaæ danych wejœciowych	
								reConnect = "";
							}
								
					} catch (IOException e) 
					{
						e.printStackTrace();
					} 
				}
			}
			else
			{
				String msg = "From " + m.getIp() + ": " + m.getMsg() + " " + m.getTimeStamp();
				ta.append(msg+"\n");
			}

		}
		else if(arg instanceof Move)
		{
			Move m = (Move)arg;
			if(m.isWin() != null)
			{
				if(m.isWin().equals("yes"))
				{
					JOptionPane.showMessageDialog(null, "Player " + m.getPlayer() + " wins!");
					int answer = JOptionPane.showConfirmDialog(null,"Do you want to play again?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(answer == JOptionPane.NO_OPTION)
					{
						resetGameBoard();
						discButton.setEnabled(false);
						connButton.setEnabled(true);
						disconnect();
					}
					if(answer == JOptionPane.YES_OPTION)
					{
						resetGameBoard();
					}
					if(answer == JOptionPane.CLOSED_OPTION)
					{
						try 
						{
							socket.close();
							oos.close();
							listener = null;
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
						System.exit(0);
					}
				}
				if(m.isWin().equals("no"))
				{
					JOptionPane.showMessageDialog(null, "Tie!");
					int answer = JOptionPane.showConfirmDialog(null,"Do you want to play again?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(answer == JOptionPane.NO_OPTION)
					{
						resetGameBoard();
						discButton.setEnabled(false);
						connButton.setEnabled(true);
						disconnect();
					}
					if(answer == JOptionPane.YES_OPTION)
					{
						resetGameBoard();
					}
					if(answer == JOptionPane.CLOSED_OPTION)
					{
						try 
						{
							socket.close();
							oos.close();
							listener = null;
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
						System.exit(0);
					}
				}
			}
			else
			{		
				turn++;
				currentPlayer = m.getPlayer();
				int i = m.getMove(); 
				button[i].setText(currentPlayer);
	            button[i].setForeground(Color.black);
				button[i].setFont(button[i].getFont().deriveFont(72.0f));
	            button[i].setEnabled(false);
	            if(currentPlayer.equals("O"))
	            {
					button[i].setBackground(Color.red);
				}
				else
				{
					button[i].setBackground(Color.blue);
				}
				changePlayer();
				
				//sprawiaj¹, ¿e przyciski s¹ dotykalne
				for(int x = 0; x < button.length; x++)
	            {
	                if(button[x].getText().equals(""))
	                {
	                	button[x].setEnabled(true);
          			}
	            }
			}
		}
	}
	
	/************************EVENTS*********************************/
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		win = false;
		if(e.getSource() == connButton || e.getSource() == ipAddress)
		{
			if(ipAddress.getText() == null || ipAddress.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Please enter valid host name");
			}
			else
			{
				for(int i = 0 ;i < 9;i++)
				{
					button[i].setEnabled(true);
					button[i].setText("");
				}
				
				ta.setForeground(Color.BLUE);
				ta.append("Searching for opponent...\n");
				reConnect = ipAddress.getText();
				try 
				{
					socket = new Socket(ipAddress.getText(),5555);
					oos = new ObjectOutputStream(socket.getOutputStream()); //for sending
					
					listener = new InputListener(socket,ClientGUI.this);
					Thread thread1 = new Thread(listener);
					thread1.start(); //
				} 
				catch (UnknownHostException e1) 
				{
					//e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Please enter a valid IP address.");
				} catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				discButton.setEnabled(true);
				connButton.setEnabled(false);
			}
		}
		
		if(e.getSource() == sendButton || e.getSource() == msgToSend)
		{
			DateFormat df = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss");
			Date today = Calendar.getInstance().getTime();
			String reportDate = df.format(today);
			
			if(!msgToSend.getText().equals(""))
			{
				Message m = new Message(ipAddress.getText(), msgToSend.getText(), new Date());
				try 
				{
					oos.writeObject(m);
					ta.append("You: " + msgToSend.getText() + " " + reportDate + "\n");
					msgToSend.setText("");
					
				} catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}	
		}
		
		if(e.getSource() == discButton)
		{
			connButton.setEnabled(true);
			discButton.setEnabled(false);
			ta.append("Disconnected.\n");
			disconnect();
		}

			 for(int i = 0; i < 9; i++)
			 {
		            if(e.getSource() == button[i])
		            {
		            	turn++;
		            	if(turn % 2 == 0)
		        		{
		        			currentPlayer = "O";// number is even
		        		}
		        		else
		        		{
		        			currentPlayer = "X"; // number is odd
		        		}	
		                button[i].setText(currentPlayer);
		                button[i].setForeground(Color.black);
		    			button[i].setFont(button[i].getFont().deriveFont(72.0f));
		                button[i].setEnabled(false);
		                if(currentPlayer.equals("O"))
		    			{
		    				button[i].setBackground(Color.red);
		    			}
		    			else
		    			{
		    				button[i].setBackground(Color.blue);
		    			}
		                Message mes = new Message("Opponent moved.");
		                Move m = new Move(currentPlayer, i);
		                try 
		                {
							oos.writeObject(m);
							oos.writeObject(mes);
						} catch (IOException e1) 
		                {
							e1.printStackTrace();
						}
		                
		                //ustaw przyciski niedotykalne po ruchu
		                for(int x = 0; x < button.length; x++)
		                {
		                	if(button[x].getText().equals(""))
                			{
		                		button[x].setEnabled(false);
                			}
		                }
		            }
		        }
			 
		//przechodŸ przez tablicê 2D i sprawdŸ wszystkie mo¿liwe kombinacje
		for(int x = 0; x < button.length-1; x++)
			if(button[winCombo[x][0]].getText() != "" && 
					button[winCombo[x][0]].getText().equals(button[winCombo[x][1]].getText()) && 
					button[winCombo[x][1]].getText().equals(button[winCombo[x][2]].getText()))
				win = true;
		
		if(win == true)
		{
			Move m = new Move("yes", currentPlayer);
			try 
			{
				oos.writeObject(m);
			} catch (IOException e3) 
			{
				e3.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
			int answer = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(answer == JOptionPane.NO_OPTION)
			{
				resetGameBoard();
				discButton.setEnabled(false);
				connButton.setEnabled(true);
				disconnect();
			}
			if(answer == JOptionPane.YES_OPTION)
			{
				resetGameBoard();
			}
			if(answer == JOptionPane.CLOSED_OPTION)
			{
				disconnect();
			}
			
		}
		else if(win == false && turn == 9)
		{
			Move m = new Move("no", currentPlayer);
			try 
			{
				oos.writeObject(m);
			} catch (IOException e3) 
			{
				e3.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Tie!");
			int answer = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(answer == JOptionPane.NO_OPTION)
			{
				resetGameBoard();
				discButton.setEnabled(false);
				connButton.setEnabled(true);
				disconnect();
			}
			if(answer == JOptionPane.YES_OPTION)
			{
				resetGameBoard();
			}
			if(answer == JOptionPane.CLOSED_OPTION)
			{
				disconnect();
			}
		}
			
	}
}

