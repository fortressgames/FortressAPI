package net.fortressgames.fortressapi.players;

import net.fortressgames.fortressapi.FortressAPI;
import net.fortressgames.fortressapi.PacketConnection;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class CustomPlayer {

	private final Player player;

	public CustomPlayer(Player player) {
		this.player = player;
	}

	/***
	 * Send a message to the action bar.
	 * Supports color codes.
	 *
	 * @param message Message to send to the action bar
	 */
	public void sendActionBar(String message) {
		PacketConnection connection = PacketConnection.getConnection(player);

		IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
		ClientboundSystemChatPacket packetPlayOutChat = FortressAPI.getInstance().getVersionHandler().actionBarGetClientboundSystemChatPacket(iChatBaseComponent);
		connection.sendPacket(packetPlayOutChat);
	}

	/**
	 * Send a clickable message to chat
	 *
	 * @param text message
	 * @param clickableText message part that can be clicked
	 * @param runCommand command to run after click
	 */
	public void sendClickableMessage(String text, String clickableText, String runCommand) {
		PacketConnection connection = PacketConnection.getConnection(player);

		IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\",\"extra\":" + "[{\"text\":\"" + clickableText + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":" + "\"/" + runCommand + "\"}}]}");
		ClientboundSystemChatPacket packet = FortressAPI.getInstance().getVersionHandler().clickMessageGetClientboundSystemChatPacket(iChatBaseComponent);
		connection.sendPacket(packet);
	}

	public void playSound(Sound sound) {
		player.playSound(player.getLocation(), sound, 1, 1);
	}
	public void playSound(String sound) {
		player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1, 1);
	}
}