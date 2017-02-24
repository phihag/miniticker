package de.phihag.miniticker;

public class Match {
	public Setup setup;
	public int[][] network_score;
	public String presses_json;

	private static boolean p1_won(int p1, int p2) {
		if (((p1 == 11) && (p2 < 10)) || ((p1 > 11) && (p2 <= p1 - 2)) || (p1 == 15)) {
			return true;
		}
		return false;
	}
	
	private static int game_winner(int[] game) {
		if (p1_won(game[0], game[1])) return 1;
		if (p1_won(game[1], game[0])) return 2;
		return 0;
	}
	
	/**
	 * @return 0 if nobody won, 1 if home team, 2 if away team
	 */
	public int winner() {
		if (setup.counting == null) {
			return 0;
		}
		assert setup.counting.equals("5x11_15");

		int[] won_games = {0, 0};
		if (network_score == null) {
			return 0;
		}
		
		for (int[] game : network_score) {
			int gwinner = game_winner(game);
			if (gwinner != 0) {
				won_games[gwinner - 1]++;
			}
			
			if (won_games[0] >= 3) return 1;
			if (won_games[1] >= 3) return 2;
		}
		return 0;
	}
}
