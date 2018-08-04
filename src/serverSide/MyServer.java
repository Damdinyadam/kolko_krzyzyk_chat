package serverSide;
/*@author Damdin-Yadam O.
* @version 1.0.0 June2018 
* 
* */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer 
{
	private static ArrayList<Socket> socketList = new ArrayList<Socket>(2);// 2 graczy
	
	/**
	 * G³ówna metoda uruchamiania serwera
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(5555);
			System.out.println("Server up and running!");
			
			while(true)
			{
				Socket cs = ss.accept();
				
				socketList.add(cs);

				if(socketList.size() == 2)
				{
					ClientHandler ch = new ClientHandler(socketList.get(0),socketList.get(1));
					ch.start();
					socketList.clear();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
