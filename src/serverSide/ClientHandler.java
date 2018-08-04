package serverSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import utilities.InputListener;
import utilities.Message;

public class ClientHandler extends Thread implements Observer
{
	private ObjectOutputStream			oos1,oos2;
	private Socket						cs1,cs2;
	private InputListener				lis1,lis2;
	
	/**
	 * Konstruktor
	 *  * @param cs1 - Client socket 1
	 * @param cs2 - Client socket 2
	 */
	public ClientHandler(Socket cs1, Socket cs2)
	{
		this.cs1 = cs1;
		this.cs2 = cs2;
		
		try 
		{
			oos1 = new ObjectOutputStream(cs1.getOutputStream());
			oos2 = new ObjectOutputStream(cs2.getOutputStream());
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda uruchamiana, gdy serwer uruchamia w¹tek obs³ugi klienta
	 */
	public void run()
	{
			lis1 = new InputListener(1,cs1,this);
			lis2 = new InputListener(2,cs2,this);
			
			Thread thread1 = new Thread(lis1);
			Thread thread2 = new Thread(lis2);
			thread1.start();
			thread2.start();
			
			try
			{
				Message m = new Message("Server","Opponent found! You may start chatting with your opponent!",
																						new Date());
				oos1.writeObject(m);
				
				m = new Message("Server","Opponent found! You may start chatting with your opponent!",new Date());
				oos2.writeObject(m);
				
				while(cs1.isConnected() && cs2.isConnected());
				
				cs1.close();
				cs2.close();
				oos1.close();
				oos2.close();
				
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
	}

	/**
	 * Metoda pisania obiektów do przeciwnika
	 */
	@Override
	public synchronized void update(Observable o, Object arg) 
	{
		InputListener listener = (InputListener)o;
		
		try
		{
			if(listener.getListenerNumber() == 1)
			{
				oos2.writeObject(arg);		
			}
			else
			{
				oos1.writeObject(arg);				
			}
		}
		catch (IOException e)
		{
			//e.printStackTrace();
		}
		
	}
		

	
	
}
