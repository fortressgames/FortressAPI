package net.fortressgames.fortressapi.scoreboards;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardObject {

	@Getter private final Scoreboard scoreboard;

	private final Map<ScoreBoardManager.Entry, Team> values = new HashMap<>();
	private final Objective objective;

	public ScoreboardObject(String title) {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective(ChatColor.stripColor(title).replace(" ", ""), "dummy", "dummy");

		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(title);
	}

	public void addObject(ScoreBoardManager.Entry entry, String message) {
		Team team = scoreboard.registerNewTeam("TEAM" + scoreboard.getTeams().size());
		team.setPrefix(message);

		StringBuilder value = new StringBuilder();
		value.append(ChatColor.BOLD.toString().repeat(scoreboard.getTeams().size()));
		team.addEntry(value.toString());

		values.put(entry, team);

		objective.getScore(value.toString()).setScore(entry.getScore());
	}

	public void updateObject(ScoreBoardManager.Entry entry, String message) {
		values.get(entry).setPrefix(message);
	}
}