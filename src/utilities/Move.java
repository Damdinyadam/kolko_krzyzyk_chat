package utilities;

import java.io.Serializable;


public class Move implements Serializable
{
	private static final long serialVersionUID = 3555895583089407347L;
	private String player;
	private int buttonPressed;
	private boolean playAgain;
	private String win;
	
	/**
	 * Konstruktor, kt�ry bierze gracza (X lub O) i po�o�enie klikni�tego przycisku

	 * @param player - X or O
	 * @param buttonPressed - po�o�enie naci�ni�tego przycisku
	 */
	public Move(String player, int buttonPressed)
	{
		this.player = player;
		this.buttonPressed = buttonPressed;
	}
	
	/**
	 * Konstruktor, kt�ry przyjmuje strina i gracz
	 * @param win - either yes or no
	 * @param player - X or O
	 */
	public Move(String win, String player)
	{
		this.win = win;
		this.player = player;
	}
	
	/**
	 * metoda zwracania warto�ci isWin, aby sprawdzi�, czy gra by�a wygrana
	 * @return win
	 */
	public String isWin() 
	{
		return win;
	}

	/**
	 * metoda ustalania warto�ci wygranej
	 * @param win
	 */
	public void setWin(String win) 
	{
		this.win = win;
	}

	/**
	 * Metoda ponownego ustawienia warto�ci odtwarzania, je�li jest prawd�, to gracz chce zagra� w inn� gr�
	 * @param playAgain
	 */
	public Move(boolean playAgain)
	{
		this.playAgain = playAgain;
	}
	
	/**
	 * 
Metoda zwracaj�ca warto�� playAgain jako warto�� logiczn�
	 * @return playAgain
	 */
	public boolean isPlayAgain() 
	{
		return playAgain;
	}

	/**
	 * @param playAgain
	 */
	public void setPlayAgain(boolean playAgain) 
	{
		this.playAgain = playAgain;
	}

	/**
	 *  return a String value
	 * @return player - X or O
	 */
	public String getPlayer() 
	{
		return player;
	}
	
	/**
	 * set the value of the player string
	 * @param player - player to set
	 */
	public void setPlayer(String player) 
	{
		this.player = player;
	}
	
	/** return the button that was clicked
	 * @return buttonPressed
	 */
	public int getMove() 
	{
		return buttonPressed;
	}
	
	/**set the value of the location the button was pressed
	 * @param buttonPressed
	 */
	public void setMove(int buttonPressed) 
	{
		this.buttonPressed = buttonPressed;
	}
}
