package utilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class InputListener extends Observable implements Runnable
{

	private Socket socket;
	private ObjectInputStream ois;
	private int listenerNumber;
	
	/**
	 * 
Metoda, która pobiera odwo³anie do gniazda klienta z GUI klienta i odwo³anie do obserwatora
	 * @param socket - the client socket
	 * @param ob - the clientGUI observer
	 */
	public InputListener(Socket socket, Observer ob)
	{
		this.socket = socket;
		this.addObserver(ob);
	}
	
	/**
	 *Nie jest domyœlnym konstruktorem, który pobiera numer s³uchacza, gniazdo i obiekt obserwatora
	 * @param listenerNumber - the listener number
	 * @param socket - the client socket object
	 * @param ob - the client GUI observer object
	 */
	public InputListener(int listenerNumber, Socket socket, Observer ob)
	{
		this.listenerNumber = listenerNumber;
		this.socket = socket;
		this.addObserver(ob);
	}
	
	/**
	 * Metoda uzyskania numeru s³uchacza
	 * @return listenerNumber
	 */
	public int getListenerNumber() 
	{
		return listenerNumber;
	}

	/**
	 Metoda ustawiania numeru s³uchacza
	 * @param listenerNumber - the listener number
	 */
	public void setListenerNumber(int listenerNumber) 
	{
		this.listenerNumber = listenerNumber;
	}

	/**
	 * Metoda uruchamiana po nawi¹zaniu po³¹czenia z serwerem. Pobiera odniesienie do strumienia wejœciowego obiektu
      i ma nieskoñczon¹ pêtlê, która czeka, a¿ coœ zostanie wys³ane do strumienia wejœciowego, po odczytaniu obiektu
       obserwatorzy s¹ powiadamiani.
	 */
	@Override
	public void run() 
	{
			try 
			{
				ois = new ObjectInputStream(socket.getInputStream()); 
				
				while(true)
				{
					Object o = ois.readObject(); //czeka i czyta, co zosta³o wys³ane
					setChanged();
					notifyObservers(o); 
				}
			} catch (IOException e) 
			{
				//e.printStackTrace();
			} catch (ClassNotFoundException e) 
			{
				//e.printStackTrace();
			} 
		

	}
}