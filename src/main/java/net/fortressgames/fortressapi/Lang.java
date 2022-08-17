package net.fortressgames.fortressapi;

import org.bukkit.ChatColor;

public class Lang {

	public static final String LINE = ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "                                                                     ";

	public static final String PLAYER_NOT_FOUND = ChatColor.RED + "Player not found!";
	public static final String NO_PERMISSION = ChatColor.RED + "You do not have permission to do that!";

	public static String ErrorConsole(String error) {
		return "\033[0;31m" + error + "\033[0m";
	}
}