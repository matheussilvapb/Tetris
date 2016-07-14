import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private Board board;

	public Keyboard(Board board) {
		this.board = board;
	}

	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			board.moveLeft();
		}
		if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			board.moveRight();
		}
		if (event.getKeyCode() == KeyEvent.VK_DOWN) {
			board.moveDown();
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			board.hardDrop();
		}
		if (event.getKeyCode() == KeyEvent.VK_UP) {
			board.rotate();
		}
		if (event.getKeyCode() == KeyEvent.VK_R) {
			board.restartGame();
		}

	}

	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_E) {
			board.isEditMode();
		}
		if (event.getKeyCode() == KeyEvent.VK_S) {
			board.pieceStopped = true;
		}
	}

	public void keyTyped(KeyEvent event) {

	}

}
