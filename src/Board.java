import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

public class Board implements ActionListener {

	/*
	 * This class holds the logic of the program, it consists on a 20x10 2D
	 * array of Blocks, and a Timer to pace the game.
	 */

	private Block[][] matrix;
	private Timer timer;

	char[] pieceSequence;
	int index;

	public Board() {
		matrix = new Block[20][10];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = new Block(false, false);
			}
		}
		timer = new Timer(500, this);
		pieceSequence = new char[7];
		pieceSequence[0] = 'I';
		pieceSequence[1] = 'J';
		pieceSequence[2] = 'L';
		pieceSequence[3] = 'S';
		pieceSequence[4] = 'Z';
		pieceSequence[5] = 'T';
		pieceSequence[6] = 'O';
		index = 0;
	}

	public void init() {
		timer.start();
		shuffleSequence();
		generatePiece();
	}

	private void generatePiece() {
		/*
		 * Gets wich piece needs to be generated from 'pieceSequence', and
		 * generates it. When it reaches the end of 'pieceSequence', it calls
		 * shuffleSequence() to regenerate the pattern.
		 */
		if (pieceSequence[index] == 'I') {
			matrix[0][3].init('I');
			matrix[0][4].init('I');
			matrix[0][5].init('I');
			matrix[0][6].init('I');
		} else if (pieceSequence[index] == 'J') {
			matrix[0][3].init('J');
			matrix[1][3].init('J');
			matrix[1][4].init('J');
			matrix[1][5].init('J');
		} else if (pieceSequence[index] == 'L') {
			matrix[0][5].init('L');
			matrix[1][3].init('L');
			matrix[1][4].init('L');
			matrix[1][5].init('L');
		} else if (pieceSequence[index] == 'S') {
			matrix[0][5].init('S');
			matrix[0][4].init('S');
			matrix[1][4].init('S');
			matrix[1][3].init('S');
		} else if (pieceSequence[index] == 'Z') {
			matrix[0][3].init('Z');
			matrix[0][4].init('Z');
			matrix[1][4].init('Z');
			matrix[1][5].init('Z');
		} else if (pieceSequence[index] == 'T') {
			matrix[0][4].init('T');
			matrix[1][3].init('T');
			matrix[1][4].init('T');
			matrix[1][5].init('T');
		} else if (pieceSequence[index] == 'O') {
			matrix[0][4].init('O');
			matrix[0][5].init('O');
			matrix[1][4].init('O');
			matrix[1][5].init('O');
		}
		if (index == 6) {
			shuffleSequence();
			index = 0;
		}
		index++;
	}

	public void shuffleSequence() {
		// Uses Random() to shuffle the array pieceSequence.
		Random rnd = new Random();
		for (int i = 0; i < pieceSequence.length - 1; i++) {
			// Generates a random index, and then switches with the element at i
			int index = rnd.nextInt(pieceSequence.length);
			char tmp = pieceSequence[index];
			pieceSequence[index] = pieceSequence[i];
			pieceSequence[i] = tmp;
		}
	}

	public void moveToLeft() {
		if (canMoveLeft()) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					moveOneBlock(i, j, false);
				}
			}
		}
	}

	public void moveToRight() {
		if (canMoveRight()) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = matrix[0].length - 1; j >= 0; j--) {
					moveOneBlock(i, j, true);
				}
			}
		}
	}

	private void debug() {
		String text = "";
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j].show())
					text += "1";
				else
					text += "0";
			}
			text += "\n";
		}
		System.out.println(text);
	}

	private void moveDown(int i, int j, boolean force) {
		/*
		 * Moves down the block at i j position. If force is false, it will only
		 * move the blocks with moving() == true.
		 */
		if (matrix[i][j].moving() || force) {
			matrix[i + 1][j] = matrix[i][j];
			matrix[i][j] = new Block(false, false);
		}
	}

	private void moveLineDown(int n, boolean force) {
		// Moves all blocks at line n down
		for (int i = 0; i < matrix[0].length; i++) {
			moveDown(n, i, force);
		}
	}

	private boolean canMoveRight() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j].moving()) {
					if (j == 9
							|| (!matrix[i][j + 1].moving() && matrix[i][j + 1]
									.show())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j].moving()) {
					if (j == 0
							|| (!matrix[i][j - 1].moving() && matrix[i][j - 1]
									.show())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean canMoveDown() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j].moving()) {
					if (i == 19
							|| (!matrix[i + 1][j].moving() && matrix[i + 1][j]
									.show()))
						return false;
				}
			}
		}
		return true;
	}

	private void moveOneBlock(int i, int j, boolean right) {
		if (right && matrix[i][j].moving()) {
			matrix[i][j + 1] = matrix[i][j];
			matrix[i][j] = new Block(false, false);
		} else if (matrix[i][j].moving()) {
			matrix[i][j - 1] = matrix[i][j];
			matrix[i][j] = new Block(false, false);
		}
	}

	private void stopAll() {
		// Stops all blocks
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j].stop();
			}
		}
	}

	private boolean detectFullLine(int i) {
		// Returns true if line i is completed, else returns false
		for (int j = 0; j < matrix[0].length; j++) {
			if (!matrix[i][j].show())
				return false;
		}
		return true;
	}

	private void clearLine(int n) {
		// Clears line n, then moves all other lines above down
		for (int i = n - 1; i >= 0; i--) {
			moveLineDown(i, true);
		}
	}

	private void clearFullLines() {
		// Detects and clears full lines
		for (int i = 0; i < matrix.length; i++) {
			if (detectFullLine(i)) {
				clearLine(i);
			}
		}
	}

	public char[][] matrix() {
		/*
		 * Returns a 2D array of chars. It holds the type of block, depending of
		 * the shape it makes.
		 */
		char[][] out = new char[20][10];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				out[i][j] = matrix[i][j].type();
			}
		}
		return out;
	}

	private void update() {
		if (canMoveDown()) {
			for (int i = matrix.length - 1; i >= 0; i--) {
				moveLineDown(i, false);
			}
		} else {
			stopAll();
			clearFullLines();
			generatePiece();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
		// debug();
	}

}
