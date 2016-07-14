import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Board board;

	private Image blue, cyan, green, magenta, orange, red, yellow,
			restartbutton;

	public final static int BLOCK_SIZE = 24;

	public static int RESTART_IMG_X;
	public static int RESTART_IMG_Y;

	private JLabel scoreLabel;
	private JLabel highScoresLabel;
	private JLabel actualMode;

	public Panel(Board board, int frameHeight) {
		super();
		try {
			blue = ImageIO.read(new File("resources/blue.png"));
			cyan = ImageIO.read(new File("resources/cyan.png"));
			green = ImageIO.read(new File("resources/green.png"));
			magenta = ImageIO.read(new File("resources/magenta.png"));
			orange = ImageIO.read(new File("resources/orange.png"));
			red = ImageIO.read(new File("resources/red.png"));
			yellow = ImageIO.read(new File("resources/yellow.png"));
			restartbutton = ImageIO
					.read(new File("resources/restartbutton.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		this.board = board;
		RESTART_IMG_X = BLOCK_SIZE * 11;
		RESTART_IMG_Y = frameHeight - BLOCK_SIZE * 5;
		scoreLabel = new JLabel("Score: ");
		this.add(scoreLabel);
		highScoresLabel = new JLabel("Top 5: ");
		this.add(highScoresLabel);
		actualMode = new JLabel();
		this.add(actualMode);
	}

	private void drawGrid(Graphics g) {
		// Draws the grid behind the pieces
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(BLOCK_SIZE * j, BLOCK_SIZE * i, BLOCK_SIZE,
						BLOCK_SIZE);
			}
		}
	}

	private void drawBlock(Graphics g, int i, int j, Image img) {
		// Draws a block on the correct position of the panel
		g.drawImage(img, BLOCK_SIZE * j, BLOCK_SIZE * i, BLOCK_SIZE,
				BLOCK_SIZE, this);
	}

	private void drawNextBlock(Graphics g, char piece) {
		if (piece == 'I') {
			drawBlock(g, 5, 11, cyan);
			drawBlock(g, 5, 12, cyan);
			drawBlock(g, 5, 13, cyan);
			drawBlock(g, 5, 14, cyan);
		} else if (piece == 'J') {
			drawBlock(g, 4, 12, blue);
			drawBlock(g, 5, 12, blue);
			drawBlock(g, 5, 13, blue);
			drawBlock(g, 5, 14, blue);
		} else if (piece == 'L') {
			drawBlock(g, 5, 12, orange);
			drawBlock(g, 5, 13, orange);
			drawBlock(g, 5, 14, orange);
			drawBlock(g, 4, 14, orange);
		} else if (piece == 'S') {
			drawBlock(g, 5, 12, green);
			drawBlock(g, 5, 13, green);
			drawBlock(g, 4, 13, green);
			drawBlock(g, 4, 14, green);
		} else if (piece == 'Z') {
			drawBlock(g, 4, 12, red);
			drawBlock(g, 4, 13, red);
			drawBlock(g, 5, 13, red);
			drawBlock(g, 5, 14, red);
		} else if (piece == 'T') {
			drawBlock(g, 5, 12, magenta);
			drawBlock(g, 5, 13, magenta);
			drawBlock(g, 4, 13, magenta);
			drawBlock(g, 5, 14, magenta);
		} else if (piece == 'O') {
			drawBlock(g, 4, 12, yellow);
			drawBlock(g, 5, 12, yellow);
			drawBlock(g, 4, 13, yellow);
			drawBlock(g, 5, 13, yellow);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// Draws block images on the correct positions based on board.matrix()
		super.paintComponent(g);
		scoreLabel.setLocation(BLOCK_SIZE * 12, BLOCK_SIZE * 2);
		scoreLabel.setText("Score: " + board.score());
		highScoresLabel.setLocation(BLOCK_SIZE * 12, BLOCK_SIZE * 8);
		highScoresLabel.setText(board.topFive());
		actualMode.setLocation(BLOCK_SIZE * 12, BLOCK_SIZE);
		actualMode.setText(board.actualMode());
		drawGrid(g);
		drawNextBlock(g, board.nextPiece());
		g.drawImage(restartbutton, BLOCK_SIZE * 11, BLOCK_SIZE * 15, 100, 35,
				this);
		char[][] matrix = board.matrix(); // See matrix() method in Board
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 'I') {
					drawBlock(g, i, j, cyan);
				} else if (matrix[i][j] == 'J') {
					drawBlock(g, i, j, blue);
				} else if (matrix[i][j] == 'L') {
					drawBlock(g, i, j, orange);
				} else if (matrix[i][j] == 'S') {
					drawBlock(g, i, j, green);
				} else if (matrix[i][j] == 'Z') {
					drawBlock(g, i, j, red);
				} else if (matrix[i][j] == 'T') {
					drawBlock(g, i, j, magenta);
				} else if (matrix[i][j] == 'O') {
					drawBlock(g, i, j, yellow);
				}
			}
		}
	}

}
