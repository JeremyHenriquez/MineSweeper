import java.util.Scanner;

public class NewGame {
	
	private Tile arr[][];
	private boolean firstClick = true;
	public boolean gameState = true;
	
	public NewGame() {
		System.out.println("Starting New Game...");
		arr = new Tile[9][9];
		createBoard();
	}
	class Tile {
		private boolean discovered;
		private boolean bomb;
		private boolean flagged;
		private int adjBombs;
		private int xCord;
		private int yCord;
		
		public Tile(int y, int x) {
			discovered = false;
			xCord = x;
			yCord = y;
			bombChanceE();
		}
		public void discover() {
			discovered = true;
		}
		public char getStatus() {
			if(discovered) {
				if(bomb) {
					return 'X';
				}else {
					if(adjBombs == 0) {
						return ' ';
					}
					//adding the zero gets rid of a problem where it an unknown sign
					return (char) ('0' + adjBombs);
				}
			}
			if(flagged) {
				return 'F';
			}
			return '*';
		}
		private void bombChanceE() {
			if(Math.random()<.08) {
				bomb = true;
			}else {
				bomb = false;
			}
			
		}
		public boolean isDiscovered() {
			return discovered;
		}
		public boolean isBomb() {
			return bomb;
		}
		public void armBomb() {
			bomb = true;
		}
		public int getAdjBombs() {
			return adjBombs;
		}
		public void setAdjBombs(int x) {
			adjBombs = x;
		}
		public void firstSafety() {
			bomb = false;
		}
	}

	public void createBoard() {
		System.out.print("  ");
		for(int m = 1; m<10; m++) {
			System.out.print(" "+ m + " ");
		}
		System.out.println();
		System.out.print("  ");
		for(int m = 1; m<10; m++) {
			System.out.print(" - ");
		}
		System.out.println();
		for(int i = 0; i<9;i++) {
			System.out.print(i+ 1 + "|");
			for(int j = 0; j<9;j++) {
				arr[i][j] = new Tile(i,j);
				System.out.print(" "+ arr[i][j].getStatus() + " ");
			}
			System.out.println();
		}
		getBombCount();
	}
	private void displayBoard() {
		System.out.print("  ");
		for(int m = 1; m<10; m++) {
			System.out.print(" "+ m + " ");
		}
		System.out.println();
		System.out.print("  ");
		for(int m = 1; m<10; m++) {
			System.out.print(" - ");
		}
		System.out.println();
		for(int i = 0; i<9;i++) {
			System.out.print(i+ 1 + "|");
			for(int j = 0; j<9;j++) {
				System.out.print(" "+ arr[i][j].getStatus() + " ");
			}
			System.out.println();
		}
	}
	private void discoverAll() {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++){
				arr[i][j].discover();
			}
		}
	}
	public void click(int x,int y) {
		x--;
		y--;
		if(firstClick) {
			firstClick(y,x);
		}else {
			arr[y][x].discover();
			if(arr[y][x].isBomb()) {
				System.out.println("GAME OVER!");
				discoverAll();
				gameState = false;
				displayBoard();
				return;
			}
		}
		checkForWin();
		displayBoard();
	}
	public void firstClick(int y, int x) {
		if( arr[y][x].isBomb()) {
			arr[y][x].firstSafety();
			int newX=0;
			int newY=0;
			do {
				newX = (int)(Math.random()*9);
				newY = (int)(Math.random()*9);

			}while(arr[y][x].isBomb());
			
			arr[newY][newX].armBomb();
			getBombCount();
		}
		firstClickReveal(y,x);
		firstClick = false;
	}
	private void getBombCount(){
		for(int i=0; i<9;i++) {
			for(int j=0; j<9;j++) {
				int count = 0;
				if(!arr[i][j].isBomb()) {
					for(int adjX = 1; adjX>=-1;adjX--) {
						for(int adjY= 1; adjY>=-1;adjY--) {
							if(adjY == 0 && adjX == 0 ) {
								
							}else {
								if(i+adjX >= 0 && j+adjY >=0 && i+adjX <=8 && j+adjY <=8 && arr[i+adjX][j+adjY].isBomb()) {
									count++;
								}
							}
						}
					}
					
				}
				arr[i][j].setAdjBombs(count);
			}
		}
	}
	private void checkForWin() {
		for(int i=0;i<9;i++) {
			for(int j=0; j<9;j++) {
				if(!arr[i][j].isBomb() && !arr[i][j].isDiscovered()) {
					return;
				}
			}
		}
		System.out.println("YOU WIN!");
		gameState = false;
	}
	private void firstClickReveal(int x, int y) {
		if(x< 0 || y <0 ||  x>8 || y >8 || arr[y][x].isDiscovered()) {
			return;
		}
		
		if(arr[y][x].getAdjBombs()>0) {
			return;
		}
		arr[y][x].discover();
		for(int adjX = 1; adjX>=-1;adjX--) {
			for(int adjY= 1; adjY>=-1;adjY--) {
				if(adjY == 0 && adjX ==0) {
					
				}else {
					firstClickReveal(x+adjX,y+adjY);
				}
			}
		}
	}
	public static void main(String[]args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			NewGame ng = new NewGame();
			while(ng.gameState) {
				int x =sc.nextInt();
				int y =sc.nextInt();
				if(x<=9 && x>=1 && y<=9 && y>=1) {
					ng.click(x,y);
				}else {
					System.out.println("Invalid input, stay within 1-9");
				}
						
			}	
			System.out.println("would you like to play again? Y/N");
			if(sc.next().equalsIgnoreCase("N")) {
				break;
			}
		}
	}
}
