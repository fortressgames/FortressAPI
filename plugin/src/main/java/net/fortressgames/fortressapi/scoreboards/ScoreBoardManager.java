package net.fortressgames.fortressapi.scoreboards;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ScoreBoardManager {

	private static ScoreBoardManager instance;
	private final HashMap<Player, ScoreBoardView> playerBoard = new HashMap<>();

	public static ScoreBoardManager getInstance() {
		if(instance == null) {
			instance = new ScoreBoardManager();
		}

		return instance;
	}

	public void sendScoreboard(Player player, ScoreBoardView scoreBoardView) {
		if(playerBoard.containsKey(player)) {
			playerBoard.replace(player, scoreBoardView);
		} else {
			playerBoard.put(player, scoreBoardView);
		}

		scoreBoardView.sendBoard(player);
	}

	public String getTag(Player player) {
		if(playerBoard.containsKey(player)) return playerBoard.get(player).getTag();

		return "NULL";
	}

	public void remove(Player player) {
		this.playerBoard.remove(player);
	}

	public ScoreBoardView getScoreBoardView(Player player) {
		return playerBoard.get(player);
	}

	public enum Entry {
		SCORE14(14),
		SCORE13(13),
		SCORE12(12),
		SCORE11(11),
		SCORE10(10),
		SCORE9(9),
		SCORE8(8),
		SCORE7(7),
		SCORE6(6),
		SCORE5(5),
		SCORE4(4),
		SCORE3(3),
		SCORE2(2),
		SCORE1(1),
		SCORE0(0);

		@Getter private final int score;

		Entry(int i) {
			this.score = i;
		}
	}
}