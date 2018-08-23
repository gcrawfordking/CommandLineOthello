import java.util.concurrent.TimeUnit;


public class Main {
	
	public static void main(String args[]) throws InterruptedException
	{
		// create new board
		Board Othello = new Board();
		// print it
		Othello.render();
		
		//while not game over, run game

		while (!Othello.gameOver())
		{
			//human turn
			Othello.humanTurn(true);
			TimeUnit.SECONDS.sleep(1);
			Othello.render();
			
			//cpu turn
			Othello.cpuTurn();
			TimeUnit.SECONDS.sleep(2);
			Othello.render();
		}
		
		//game over! display scores and winner!
		Othello.printFinalResults();
	}
}
