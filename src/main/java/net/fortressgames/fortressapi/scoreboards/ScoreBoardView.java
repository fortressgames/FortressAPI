package net.fortressgames.fortressapi.scoreboards;

import org.bukkit.entity.Player;

public interface ScoreBoardView {

	void sendBoard(Player player);

	String getTag();
}