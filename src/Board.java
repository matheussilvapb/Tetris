import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Board implements ActionListener {

	/*
	 * This class holds the logic of the program, it consists on a 20x10 2D
	 * array of Blocks, and a Timer to pace the game.
	 */

	private Block[][] matrix;
	private Timer timer;
	private Score score;
	private char currentPiece;
	private char[] pieceSequence;
	private int index, currentJ, currentI;
	private HighScores highScores;
	private Serialize<HighScores> serHighScore;
	private JFrame frame;
	public boolean pieceStopped = false;
	public boolean editMode = false;


	public char[][] matrix() {
		/*
		 * Returns a 2D array of chars. It holds the type of block, depending of
		 * the shape it makes.
		 */
		char[][] out = new char[20][10];
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				out[i - 1][j] = matrix[i][j].type();
			}
		}
		return out;
	}

	public Board(HighScores highScores, JFrame frame) {
		matrix = new Block[21][10];
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
		currentJ = 0;
		currentI = 0;
		currentPiece = '.';
		score = new Score();
		this.highScores = highScores;
		this.serHighScore = new Serialize<HighScores>();
		this.frame = frame;
	}

	public void init() {
		timer.start();
		index = 0;
		shuffleSequence();
		generatePiece();
		score = new Score();
	}

	public char nextPiece() {
		return pieceSequence[index];
	}

	public void moveLeft() {
		// Moves the current piece left, if canMoveLeft() returns true
		if (canMoveLeft()) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					moveOneBlock(i, j, false);
				}
			}
			currentJ--;
		}
	}

	public void moveRight() {
		// Moves the current piece right, if canMoveRight() returns true
		if (canMoveRight()) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = matrix[0].length - 1; j >= 0; j--) {
					moveOneBlock(i, j, true);
				}
			}
			currentJ++;
		}
	}

	public void moveDown() {
		// Moves the current piece down, if canMoveDown() returns true
		if (canMoveDown()) {
			for (int i = matrix.length - 1; i >= 0; i--) {
				for (int j = 0; j < matrix[0].length; j++) {
					if (matrix[i][j].moving())
						moveOneBlockDown(i, j);
				}
			}
			currentI++;
		}
	}

	public void hardDrop() {
		// 'hard drops' the piece
		while (canMoveDown()) {
			moveDown();
		}
		update();
	}

	public void rotate() {
		// If possible, rotates the piece 90 degrees right
		if (currentPiece == 'O')
			return;

		int oldJ = currentJ;

		if (currentJ == -1) {
			moveRight();
		} else if (currentJ == 8) {
			if (currentPiece == 'I') {
				moveTwoLeft();
			} else {
				moveLeft();
			}
		} else if (currentJ == 7 && currentPiece == 'I') {
			moveLeft();
		}

		if (canRotate()) {
			rotateInMatrix(currentI, currentJ);
		} else {
			while (currentJ != oldJ) {
				if (currentJ > oldJ) {
					moveLeft();
				} else if (currentJ < oldJ) {
					moveRight();
				}
			}
		}
	}

	public int score() {
		// Returns the current score
		return score.score();
	}

	public String topFive() {
		// Returns a String using HTML to format it for use in a JLabel
		return highScores.toStringHtml();
	}

	public void restartGame() {
		// Clean all the matrix.
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = new Block(false, false);
			}
		}
		init();
	}

	public void isEditMode() {
		if(editMode == false) { this.editMode = true; }
		else {this.editMode = false; }
	}

	public String actualMode() {
		if(editMode) { return "Editing"; }
		return "Normal";
	}


	private void generatePiece() {
		/*
		 * Gets wich piece needs to be generated from 'pieceSequence', and
		 * generates it. When it reaches the end of 'pieceSequence', it calls
		 * shuffleSequence() to regenerate the pattern.
		 */

		if (pieceSequence[index] == 'I') {
			matrix[1][3].init('I');
			matrix[1][4].init('I');
			matrix[1][5].init('I');
			matrix[1][6].init('I');
			currentPiece = 'I';
		} else if (pieceSequence[index] == 'J') {
			matrix[0][3].init('J');
			matrix[1][3].init('J');
			matrix[1][4].init('J');
			matrix[1][5].init('J');
			currentPiece = 'J';
		} else if (pieceSequence[index] == 'L') {
			matrix[0][5].init('L');
			matrix[1][3].init('L');
			matrix[1][4].init('L');
			matrix[1][5].init('L');
			currentPiece = 'L';
		} else if (pieceSequence[index] == 'S') {
			matrix[0][5].init('S');
			matrix[0][4].init('S');
			matrix[1][4].init('S');
			matrix[1][3].init('S');
			currentPiece = 'S';
		} else if (pieceSequence[index] == 'Z') {
			matrix[0][3].init('Z');
			matrix[0][4].init('Z');
			matrix[1][4].init('Z');
			matrix[1][5].init('Z');
			currentPiece = 'Z';
		} else if (pieceSequence[index] == 'T') {
			matrix[0][4].init('T');
			matrix[1][3].init('T');
			matrix[1][4].init('T');
			matrix[1][5].init('T');
			currentPiece = 'Z';
		} else if (pieceSequence[index] == 'O') {
			matrix[0][4].init('O');
			matrix[0][5].init('O');
			matrix[1][4].init('O');
			matrix[1][5].init('O');
			currentPiece = 'O';
		}
		currentJ = 3;
		currentI = 0;
		index++;
		if (index == 7) {
			shuffleSequence();
			index = 0;
		}
	}

	private void shuffleSequence() {
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

	private void moveTwoLeft() {
		// If possible, moves the current piece two times left
		moveLeft();

		if (canMoveLeft()) {
			moveLeft();
		} else {
			moveRight();
		}
	}

	private void moveOneBlockDown(int i, int j) {
		// Moves down the block at i j position.
		matrix[i + 1][j] = matrix[i][j];
		matrix[i][j] = new Block(false, false);
	}

	private boolean canMoveRight() {
		// Returns true if the piece can be moved right
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
		// Returns true if the piece can be moved left
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
		// Return true if the piece can be moved down
		if (gameOver())
			return false;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j].moving()) {
					if (i == 20
							|| (!matrix[i + 1][j].moving() && matrix[i + 1][j]
									.show()))
						return false;
				}
			}
		}
		return true;
	}

	private void moveOneBlock(int i, int j, boolean right) {
		// Move just one block to right(right == true) or left(right == false).
		if (right && matrix[i][j].moving()) {
			matrix[i][j + 1] = matrix[i][j];
			matrix[i][j] = new Block(false, false);
		} else if (matrix[i][j].moving()) {
			matrix[i][j - 1] = matrix[i][j];
			matrix[i][j] = new Block(false, false);
		}
	}

	private Block[][] getPiece() {
		// Returns a 3x3 or 4x4 matrix correspondent to the current piece
		Block[][] piece;

		if (currentPiece == 'I')
			piece = new Block[4][4];
		else
			piece = new Block[3][3];

		for (int m = 0; m < piece.length; m++) {
			for (int n = 0; n < piece.length; n++) {
				try {
					piece[m][n] = matrix[m + currentI][n + currentJ].clone();
				} catch (ArrayIndexOutOfBoundsException e) {
					piece[m][n] = new Block(true, false);
				}
			}
		}

		return piece;
	}

	private Block[][] rotatePiece(Block[][] piece, char pieceType) {
		// Returns the moving blocks of piece[][] rotated
		Block[][] rotated;

		if (pieceType == 'I')
			rotated = new Block[4][4];
		else
			rotated = new Block[3][3];

		for (int i = 0; i < rotated.length; i++) {
			for (int j = 0; j < rotated.length; j++) {
				if (piece[i][j].show() && !piece[i][j].moving())
					rotated[i][j] = piece[i][j].clone();
				else
					rotated[i][j] = new Block();
			}
		}

		int[] c = { 0, 2, 2, 0, 0, 2, 2, 0 };
		int[] c2 = { 0, 1, 2, 1, 0, 1, 2, 1 };

		for (int i = 0; i < 4; i++) {
			if (piece[c[i + 1]][c[i]].moving()) {
				rotated[c[i]][c[i + 3]] = piece[c[i + 1]][c[i]];
			}
			if (piece[c2[i + 1]][c2[i]].moving()) {
				rotated[c2[i]][c2[i + 3]] = piece[c2[i + 1]][c2[i]];
			}
		}

		if (pieceType == 'I') {
			rotated[1][3] = piece[3][1];
			rotated[3][1] = piece[1][3];
		}

		rotated[1][1] = piece[1][1];

		return rotated;
	}

	private boolean canRotate() {
		// Returns true if the current piece can be rotated
		Block[][] piece = getPiece();
		Block[][] rotated = rotatePiece(piece, currentPiece);

		for (int i = 0; i < rotated.length; i++) {
			for (int j = 0; j < rotated.length; j++) {
				if (piece[i][j].show() && !piece[i][j].moving()
						&& rotated[i][j].moving())
					return false;
			}
		}

		return true;
	}

	private void putPiece(Block[][] piece) {
		// Puts 'piece' back into the matrix, in the current position
		for (int m = 0; m < piece.length; m++) {
			for (int n = 0; n < piece.length; n++) {
				matrix[m + currentI][n + currentJ] = piece[m][n];
			}
		}
	}

	private void rotateInMatrix(int i, int j) {
		// Rotates the piece and puts it back into the matrix
		putPiece(rotatePiece(getPiece(), currentPiece));
	}

	private void stopAll() {
		// Stops all blocks
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j].stop();
			}
		}
	}

	private boolean gameOver() {
		// Check if the pieces can still move or they hit the top of the window.
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j].show() && !matrix[i][j].moving()) {
					return true;
				}
			}
		}
		return false;
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
			for (int j = 0; j < matrix[0].length; j++) {
				moveOneBlockDown(i, j);
			}
		}
	}

	private int clearFullLines() {
		// Detects and clears full lines, returns how many lines were cleared
		int lines = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (detectFullLine(i)) {
				clearLine(i);
				lines++;
			}
		}
		return lines;
	}

	private void update() {
		if(this.editMode) {
			if(pieceStopped) {
				stopAll();
				pieceStopped = false;
				generatePiece();
			}
		}
		else if (!gameOver()) {
			if (canMoveDown()) {
				moveDown();
			} else {
				stopAll();
				score.addToScore(clearFullLines());
				if (!gameOver()) {
					generatePiece();
				} else {
					JOptionPane.showMessageDialog(frame, "GAME OVER !\n"
							+ "Your score was: " + score.score(), "Tetris",
							JOptionPane.PLAIN_MESSAGE);
					highScores.addScore(score);
					serHighScore.save(highScores, "resources/highscores.ser");
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
	}

}
