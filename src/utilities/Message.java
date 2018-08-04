package utilities;


import java.io.Serializable;
import java.util.Date;

import javax.swing.JButton;

public class Message implements Serializable
{
	private static final long serialVersionUID = -9110357862076326880L;
	
	private String ip;
	private String msg;
	private String move;
	private Date timeStamp;

	/**
	 * 
	 * @param move - string który ma zostaæ wys³any jako wiadomoœæ
	 */
	public Message(String move)
	{
		this.move = move;
	}
	
	/**
	 * Konstruktor, który ustawia adres IP klienta, wiadomoœæ do wys³ania 
	 * @param ip
	 * @param msg
	 * @param timeStamp
	 */
	public Message(String ip, String msg, Date timeStamp)
	{
		this.ip = ip;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	/**
	 Metoda zwracania strina
	 * @return move
	 */
	public String getMove() 
	{
		return move;
	}

	/**
	 *Metoda ustawiania strina do wys³ania
	 * @param move
	 */
	public void setMove(String move) 
	{
		this.move = move;
	}
	
	/**
	 * return ip adres
	 * @return ip
	 */
	public String getIp() 
	{
		return ip;
	}

	/**
	 * Metoda ustaw the ip 
	 * @param ip 
	 */
	public void setIp(String ip) 
	{
		this.ip = ip;
	}

	/**
	
	 * @return msg
	 */
	public String getMsg() 
	{
		return msg;
	}

	/**
	 * @param msg
	 */
	public void setMsg(String msg) 
	{
		this.msg = msg;
	}

	/**
	 * @return timeStamp
	 */
	public Date getTimeStamp() 
	{
		return timeStamp;
	}

	/**
	 * Metoda set the time stamp as a date
	 * @param timeStamp
	 */
	public void setTimeStamp(Date timeStamp) 
	{
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() 
	{
		return "ip=" + ip + ", msg=" + msg + ", timeStamp="
				+ timeStamp;
	}
}
