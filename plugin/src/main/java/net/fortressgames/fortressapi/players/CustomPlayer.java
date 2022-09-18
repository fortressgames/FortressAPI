package net.fortressgames.fortressapi.players;

import lombok.Getter;
import lombok.Setter;
import net.fortressgames.fortressapi.FortressAPI;
import net.fortressgames.fortressapi.PacketConnection;
import net.fortressgames.fortressapi.gui.InventoryMenu;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomPlayer {

	private final Player player;

	// Current open menu of the player
	@Setter	@Getter	private InventoryMenu openMenu;

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

	public List<Integer> getAllItemTypeSlots(Material material) {
		List<Integer> list = new ArrayList<>();

		for(int slot = 0; slot <= 35; slot++) {
			// Item
			ItemStack itemStack = player.getInventory().getItem(slot);

			if(itemStack == null) continue;
			if(itemStack.getType().equals(material)) {
				list.add(slot);
			}
		}

		return list;
	}

	/**
	 * Check if item can fit
	 */
	public boolean hasInventorySpace(Material material, int amount) {

		for(int slot = 0; slot <= 35; slot++) {
			// Item
			ItemStack itemStack = player.getInventory().getItem(slot);

			if(itemStack == null) {
				return true;
			}

			if(itemStack.getType().equals(material)) {
				if(itemStack.getAmount() + amount <= itemStack.getMaxStackSize()) {
					return true;
				}
			}
		}

		return false;
	}
}