package blackhole.model;

import java.util.ArrayList;
import java.util.Arrays;

import blackhole.model.util.Field;
import blackhole.model.util.Point;

public class BlackHoleEngine {
	private int currentFieldSize;
	private final ArrayList<Integer> boardSize = new ArrayList<>(Arrays.asList(5, 7, 9));
	private Field[][] board;
	private Field[][] tempBoard;
	private boolean whoseTurn = false; // false = RED; true = BLACK
	private Point clickedOn;
	private int redPoints = 0;
	private int blackPoints = 0;

	// Debug
	private void printArr(Field[][] arr) {
		System.out.println("===================================");
		for (int i = 0; i < currentFieldSize; ++i) {
			for (int j = 0; j < currentFieldSize; ++j) {
				System.out.print(arr[i][j] + "\t");
			}
			System.out.println();
		}
	}

	// Currently selected field
	public Point selected() {
		return clickedOn;
	}

	public ArrayList<Integer> getBoardSize() {
		return boardSize;
	}

	// Runs the party
	public void run(int num) {
		board = null;
		currentFieldSize = num;
		createField(num);
		redPoints = 0;
		blackPoints = 0;
		whoseTurn = false;
	}

	public int getCurrentFieldSize() {
		return currentFieldSize;
	}

	// Fills the @param array with empty fields
	public void fillWithNone(Field[][] array) {
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[i].length; ++j) {
				array[i][j] = Field.NONE;
			}
		}
	}

	// Builds the board
	private void createField(int num) {
		board = new Field[currentFieldSize][currentFieldSize];
		tempBoard = new Field[currentFieldSize][currentFieldSize]; // Using later on...
		int middlePoint = currentFieldSize / 2;
		fillWithNone(board);
		for (int i = 0; i < currentFieldSize; ++i) {
			for (int j = 0; j < currentFieldSize; ++j) {
				if (i == j && i != middlePoint) {
					board[i][j] = Field.BLACK;
				}
			}
			if (middlePoint != i)
				board[currentFieldSize - i - 1][i] = Field.RED;
		}
		board[middlePoint][middlePoint] = Field.BLACKHOLE;
	}

	// Switches players
	private void turn() {
		whoseTurn = !whoseTurn;
	}

	public Field getFieldType(int row, int col) {
		return board[row][col];
	}

	public boolean isWhite(int row, int col) {
		return row % 2 == col % 2;
	}

	// Checks whether the game is over or not
	public Field gameOver() {
		if (redPoints >= (currentFieldSize - 1) / 2) {
			return Field.RED;
		} else if (blackPoints >= (currentFieldSize - 1) / 2) {
			return Field.BLACK;
		}
		return null;
	}

	// Does things when you press a button
	public void pushButton(int row, int col) {
		if (clickedOn == null) {
			clickedOn(row, col);
			stepDirection();
		} else {
			if (new Point(row, col).equals(clickedOn)) {
				clearClick();
			} else {
				movePoint(row, col);
				clearClick();
				turn();
			}
		}
	}

	// Moves the selected field to the target destination
	private void movePoint(int row, int col) {
		if (isBlackhole(row, col)) {
			givePoints(clickedOn.getX(), clickedOn.getY());
		}
		if (clickedOn.getX() != row) {
			if (row < clickedOn.getX()) {
				while (row - 1 >= 0 && board[row - 1][col] == Field.NONE) {
					row--;
				}
				updateBoard(row, col);
				if (row - 1 >= 0 && board[row - 1][col] == Field.BLACKHOLE) {
					givePoints(row, col);
				}
			}

			if (row > clickedOn.getX()) {
				while (row + 1 < currentFieldSize && board[row + 1][col] == Field.NONE) {
					row++;
				}
				updateBoard(row, col);
				if (row + 1 < currentFieldSize && board[row + 1][col] == Field.BLACKHOLE) {
					givePoints(row, col);
				}
			}
		}

		if (clickedOn.getY() != col) {
			if (col < clickedOn.getY()) {
				while (col - 1 >= 0 && board[row][col - 1] == Field.NONE) {
					col--;
				}
				updateBoard(row, col);
				if (col - 1 >= 0 && board[row][col - 1] == Field.BLACKHOLE) {
					givePoints(row, col);
				}
			}

			if (col > clickedOn.getY()) {
				while (col + 1 < currentFieldSize && board[row][col + 1] == Field.NONE) {
					col++;
				}
				updateBoard(row, col);
				if (col + 1 < currentFieldSize && board[row][col + 1] == Field.BLACKHOLE) {
					givePoints(row, col);
				}
			}
		}
	}

	// Gives points to the player whose piece was on (X, Y) and deletes it
	private void givePoints(int row, int col) {
		increasePoints(getFieldType(row, col));
		deletePoint(row, col);
	}

	// Updates the board
	private void updateBoard(int row, int col) {
		this.board[row][col] = board[clickedOn.getX()][clickedOn.getY()];
		board[clickedOn.getX()][clickedOn.getY()] = Field.NONE;
		int middlePoint = currentFieldSize / 2;
		board[middlePoint][middlePoint] = Field.BLACKHOLE;
	}

	// Deletes the piece from (X, Y)
	private void deletePoint(int x, int y) {
		board[x][y] = Field.NONE;
	}

	// Gives a point to the player @param field
	private void increasePoints(Field field) {
		if (field == Field.RED) {
			redPoints++;
		} else {
			blackPoints++;
		}
	}

	// Checks the directions where you can move
	private void stepDirection() {
		fillWithNone(tempBoard);
		// Check north
		if (clickedOn.getY() > 0 && (board[clickedOn.getX()][(clickedOn.getY() - 1)] == Field.NONE
				|| board[clickedOn.getX()][(clickedOn.getY() - 1)] == Field.BLACKHOLE)) {
			tempBoard[clickedOn.getX()][clickedOn.getY() - 1] = Field.RED;
		}

		// Check south
		if (clickedOn.getY() < currentFieldSize - 1 && (board[clickedOn.getX()][(clickedOn.getY() + 1)] == Field.NONE
				|| board[clickedOn.getX()][(clickedOn.getY() + 1)] == Field.BLACKHOLE)) {
			tempBoard[clickedOn.getX()][clickedOn.getY() + 1] = Field.RED;
		}

		// Check west
		if (clickedOn.getX() > 0 && (board[clickedOn.getX() - 1][(clickedOn.getY())] == Field.NONE
				|| board[clickedOn.getX() - 1][(clickedOn.getY())] == Field.BLACKHOLE)) {
			tempBoard[clickedOn.getX() - 1][clickedOn.getY()] = Field.RED;
		}

		// Check east
		if (clickedOn.getX() < currentFieldSize - 1 && (board[clickedOn.getX() + 1][(clickedOn.getY())] == Field.NONE
				|| board[clickedOn.getX() + 1][(clickedOn.getY())] == Field.BLACKHOLE)) {
			tempBoard[clickedOn.getX() + 1][clickedOn.getY()] = Field.RED;
		}

		// Self
		tempBoard[clickedOn.getX()][clickedOn.getY()] = Field.RED;
	}

	// Checks if given coordinate is a blackhole or not
	public boolean isBlackhole(int row, int col) {
		return (double) row + 1 == Math.ceil((double) currentFieldSize / 2.0)
				&& (double) col + 1 == Math.ceil((double) currentFieldSize / 2.0);
	}

	// Setter for clickedOn
	public void clickedOn(int i, int j) {
		this.clickedOn = new Point(i, j);
	}

	// Clears the currently selected piece
	public void clearClick() {
		clickedOn = null;
	}

	// Returns whether piece on (X, Y) is enabled or not
	public boolean isEnabled(int i, int j) {
		if (clickedOn == null) {
			if (whoseTurn == false) { // RED turn
				if (board[i][j] == Field.RED) {
					return true;
				}
			}
			if (whoseTurn == true) { // BLACK turn
				if (board[i][j] == Field.BLACK) {
					return true;
				}
			}
			return false;
		} else {
			// Field.RED indicates the possible field(s) where one can step, nothing else.
			if (tempBoard[i][j] == Field.RED) {
				return true;
			}
			return false;
		}
	}

}
