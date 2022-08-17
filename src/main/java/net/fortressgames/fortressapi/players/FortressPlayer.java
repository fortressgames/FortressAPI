package net.fortressgames.fortressapi.players;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class FortressPlayer extends CustomPlayer {

	@Getter private final Player player;

	public FortressPlayer(Player player) {
		super(player);
		this.player = player;
	}

	public static FortressPlayer getPlayer(Player player) {
		return UserModule.getInstance().getUser(player);
	}

	public InventoryView getOpenInventory() {
		return player.getOpenInventory();
	}
	public GameMode getGameMode() {
		return player.getGameMode();
	}
	public void setItemInOffHand(ItemStack itemInOffHand) {
		player.getInventory().setItemInOffHand(itemInOffHand);
	}
}