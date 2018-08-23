import java.util.Scanner;


public class Board {
	private int[][] discs;
	private int [][][] gainsMap;
	
	public Board(){
		int row; int col;
		
		// initialize board. 1 is white piece, -1 is black piece.
		discs = new int[8][8];
		gainsMap = new int[8][8][9];
		for (row = 0; row < 8; row++)
		{
			for (col = 0; col < 8; col++)
			{
				discs[row][col] = 0;
			}
		}
		
		// starting position of board
		discs[3][3] = 1;
		discs[4][4] = 1;
		discs[3][4] = -1;
		discs[4][3] = -1;
	}
	
	public void render()
	{
		System.out.println("   A  B  C  D  E  F  G  H");
		
		for (int row = 0; row < 8; row++)
		{
			System.out.print(row+1);
			for (int col = 0; col < 8; col++)
			{
				if      (discs[row][col] == 1) System.out.print("  O");
				else if (discs[row][col] == -1) System.out.print("  X");
				else    System.out.print("  -");
			}
			System.out.println();
		}
	}
	
	public void renderGainsMap()
	{
		System.out.println("   A  B  C  D  E  F  G  H");
		
		for (int row = 0; row < 8; row++)
		{
			System.out.print(row+1);
			for (int col = 0; col < 8; col++)
			{
				if(gainsMap[row][col][8]>0)      System.out.print("  " + gainsMap[row][col][8]);
				else    System.out.print("  -");
			}
			System.out.println();
		}
	}
	
	private void makeMove(int row, int col, int team, int[] gainsArr) {
		int[] newDir; int rowDir = -1; int colDir = -1;
		int currCol = col; int currRow = row;
		
		//place disc down
		discs[row][col] = team;
		
		// flip discs in all applicable directions
		for (int i = 0; i < 8; i++)
		{
			if (gainsArr[i] > 0)
			{
				for (int j = 0; j < gainsArr[i]; j++)
				{		
					currRow = currRow + rowDir;
					currCol = currCol + colDir;
					discs[currRow][currCol] = team;
				}
			}
			
			newDir = clockwiseOrient(rowDir, colDir);
			rowDir = newDir[0];
			colDir = newDir[1];
			currRow = row;
			currCol = col;
		
		}
	}
	
	public void humanTurn(boolean printDiv)
	{
		int row; int col; int[] gainsArr = new int[9];
		if (printDiv) printDivider(1);

		System.out.print("Pick your move: ");
		
		//process arguments
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String pos = scanner.next();
			scanner.nextLine();
			row = Integer.parseInt(pos.substring(1, 2)) -1;
			col = strToCol(pos.substring(0, 1));
		
			//human move! human is 1
			gainsArr = calcGains(row, col, 1);
			
			if (gainsArr[8] == 0)
			{
				System.out.println("Not a valid move. Try again.");
				//process arguments
				humanTurn(false);
			}
			
			makeMove(row, col, 1, gainsArr);
			System.out.println();
	}

	public void cpuTurn() {
		// stand-in algo -- just make random move
		printDivider(-1);
		System.out.println();
		makeGainsMap(-1);
		//renderGainsMap();
		outerloop:
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (gainsMap[row][col][8] > 0)
					{
						makeMove(row, col, -1, gainsMap[row][col]);
						break outerloop;
					}
			}
		}
	}
	
	public int strToCol(String col)
	{
		int ret = -1;
		switch(col)
		{
			case "A": ret = 0;
			break;
			case "B": ret = 1;
			break;
			case "C": ret = 2;
			break;
			case "D": ret = 3;
			break;
			case "E": ret = 4;
			break;
			case "F": ret = 5;
			break;
			case "G": ret = 6;
			break;
			case "H": ret = 7;
			break;
		}
		return ret;
	}
	
public void makeGainsMap(int team)
{
	for (int row = 0; row < 8; row++)
	{
		for (int col = 0; col < 8; col++)
		{
			gainsMap[row][col] = calcGains(row, col, team);
		}
	}
	
}
	
	
public int[] calcGains(int row, int col, int team)
{
	// calculates possible gains in all 8 directions, clockwise from NW diagonal
	int[] returnArr = new int[9];
	int colDir = -1; int rowDir = -1; 
	int gains = 0;
	int currCol = col; int currRow = row;
	int[] newDir = new int[2];
	
	// if space is already occupied then return 0s
	if (Math.abs(discs[row][col]) > 0)
	{
		return returnArr;
	}
	
	else
	{
		// for each direction
		for (int i = 0; i< 8; i++)
		{
			gains = 0;
			currRow = row + rowDir;
			currCol = col + colDir;
			
			while (inBounds(currRow, currCol) && discs[currRow][currCol] == -team)
			{
				gains++;
				currRow = currRow + rowDir;
				currCol = currCol + colDir;
			}
			
			//while loop stopped on opposing piece (success)
			if (inBounds(currRow, currCol) && discs[currRow][currCol] == team)
			{
				returnArr[i] = gains;
			}
			
			//direction vector is missing endpoint (fail)
			else
			{
				returnArr[i] = 0;
			}
			
			newDir = clockwiseOrient(rowDir, colDir);
			rowDir = newDir[0];
			colDir = newDir[1];
		}
		
		//position 8 is sum of all possible gains
		for (int i = 0; i < 8; i++)
		{
			returnArr[8] = returnArr[8] + returnArr[i];
		}
		
		return returnArr;
	}
}

private boolean inBounds(int row, int col)
{
	boolean returnVal = true;
	
	if (row < 0 || row > 7) returnVal = false;
	if (col < 0 || col > 7) returnVal = false;
	
	return returnVal;
}

private int[] clockwiseOrient(int rowDir, int colDir)
{

	int[] returnArr = new int[2];
	
	if (rowDir == -1)
			{
				colDir++;
				if (colDir == 2)
						{
							rowDir = 0;
							colDir = 1;
						}
			}

	else if (rowDir == 0)
			{
				if (colDir == 1) rowDir = 1;
				if (colDir == -1) rowDir = -1;
			}
	else
			{
				colDir--;
				if (colDir == -2)
					{
						rowDir = 0;
						colDir = -1;
					}
			}
	
	
	returnArr[0] = rowDir;
	returnArr[1] = colDir;
	return returnArr;
}
	
	public void printDivider(int team)
	{
		String turnStr;
		if (team == -1) turnStr = "CPU turn";
		else            turnStr = "Your turn";
		
		System.out.println();
		System.out.println("=================================================");
		System.out.println("                " + turnStr);
		System.out.println("=================================================");
		System.out.println();
	}
	
	public boolean hasMove(int team)
	{
		makeGainsMap(team);
//		System.out.println();
//		System.out.println("Team: " + team);
//		renderGainsMap();
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (gainsMap[row][col][8] > 0) return true;
			}
		}
		
		return false;
	}

	public boolean gameOver() {
		
		if (hasMove(-1)) return false;
		if (hasMove(1)) return false;
		return true;
		
	}

	public void printFinalResults() {
		int humanScore = 0; int cpuScore = 0;
		System.out.println();
		System.out.println("*****************\\-------------//***************");
		System.out.println("******************\\ GAME OVER //****************");
		System.out.println("*******************\\---------//*****************");
		
		humanScore = calcScore(1);
		cpuScore = calcScore(-1);
		System.out.println();
		System.out.println("You: " + humanScore);
		System.out.println("CPU: " + cpuScore);
		System.out.println();
		
		if (humanScore > cpuScore) System.out.println("You win!");
		else System.out.println("You lose!");
	}

	private int calcScore(int team) {
		int counter = 0;
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (discs[row][col] == team) counter++;
			}
		}
		return counter;
	}

}
