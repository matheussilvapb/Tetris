import java.util.Arrays;

public class HighScores implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int[] topFive;

	public HighScores() {
		topFive = new int[5];
		for (int i = 0; i < topFive.length; i++) {
			topFive[i] = 0;
		}
	}

	public void addScore(Score score) {
		if (score.score() > topFive[0]) {
			topFive[0] = score.score();
			Arrays.sort(topFive);
		}
	}

	public String toStringHtml() {
		String output = "<html>Top 5:<br>";
		for (int i = topFive.length - 1; i >= 0; i--) {
			output += ((topFive.length - i) + "- " + topFive[i] + "<br>");
		}
		output += "</html>";
		return output;
	}

}
