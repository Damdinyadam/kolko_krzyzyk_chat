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
Metoda, kt�ra pobiera odwo�anie do gniazda klienta z GUI klienta i odwo�anie do obserwatora
	 * @param socket - the client socket
	 * @param ob - the clientGUI observer
	 */
	public InputListener(Socket socket, Observer ob)
	{
		this.socket = socket;
		this.addObserver(ob);
	}
	
	/**
	 *Nie jest domy�lnym konstruktorem, kt�ry pobiera numer s�uchacza, gniazdo i obiekt obserwatora
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
	 * Metoda uzyskania numeru s�uchacza
	 * @return listenerNumber
	 */
	public int getListenerNumber() 
	{
		return listenerNumber;
	}

	/**
	 Metoda ustawiania numeru s�uchacza
	 * @param listenerNumber - the listener number
	 */
	public void setListenerNumber(int listenerNumber) 
	{
		this.listenerNumber = listenerNumber;
	}

	/**
	 * Metoda uruchamiana po nawi�zaniu po��czenia z serwerem. Pobiera odniesienie do strumienia wej�ciowego obiektu
      i ma niesko�czon� p�tl�, kt�ra czeka, a� co� zostanie wys�ane do strumienia wej�ciowego, po odczytaniu obiektu
       obserwatorzy s� powiadamiani.
	 */
	@Override
	public void run() 
	{
			try 
			{
				ois = new ObjectInputStream(socket.getInputStream()); 
				
				while(true)
				{
					Object o = ois.readObject(); //czeka i czyta, co zosta�o wys�ane
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