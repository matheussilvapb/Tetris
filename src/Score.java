public class Score {

	private int score;
	private boolean isTetris;
	private int[] linesClearedScore;

	public Score() {
		linesClearedScore = new int[5];
		linesClearedScore[0] = 0;
		linesClearedScore[1] = 100; // one line cleared
		linesClearedScore[2] = 225; // two lines cleared
		linesClearedScore[3] = 350; // three lines cleared
		linesClearedScore[4] = 500; // tetris
		score = 0;
		isTetris = false;
	}

	public void addToScore(int sequence) {
		// Adds to the score based on how many lines were cleared
		int add = 0;
		if (isTetris && sequence != 0) {
			add = 2 * linesClearedScore[sequence];
			isTetris = false;
		} else {
			add = linesClearedScore[sequence];
		}
		if (sequence == 4) {
			isTetris = true;
		}
		score += add;
	}

	public int score() {
		// Returns the current score
		return score;
	}

}
