package net.fortressgames.fortressapi;

import net.fortressgames.fortressapi.utils.ConsoleMessage;
import net.fortressgames.fortressapi.utils.HexColor;
import org.bukkit.ChatColor;

public class Lang {

	public static final String RED = HexColor.valueOf("#F00000") + "❙ ";
	public static final String YELLOW = HexColor.valueOf("#E8F000") + "❙ ";
	public static final String GREEN = HexColor.valueOf("#30F000") + "❙ ";
	public static final String BLUE = HexColor.valueOf("#0050F0") + "❙ ";
	public static final String PINK = HexColor.valueOf("#E800F0") + "❙ ";

	public static final String LINE = ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "                                                                     ";

	public static final String PLAYER_NOT_FOUND = RED + "Player not found!";
	public static final String NO_PERMISSION = RED + "You do not have permission to do that!";
	public static final String PLAYERS_ONLY = RED + "Only players can do this command!";

	public static String ErrorConsole(String error) {
		return ConsoleMessage.RED + error + ConsoleMessage.RESET;
	}
}