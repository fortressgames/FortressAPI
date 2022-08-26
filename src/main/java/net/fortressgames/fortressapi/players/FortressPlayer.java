package net.fortressgames.fortressapi.players;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FortressPlayer extends CustomPlayer {

	@Getter private final Player player;

	public FortressPlayer(Player player) {
		super(player);
		this.player = player;
	}

	public static FortressPlayer getPlayer(Player player) {
		return FortressPlayerModule.getInstance().getUser(player);
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
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
	public World getWorld() {
		return player.getWorld();
	}
	public Location getLocation() {
		return player.getLocation();
	}
	public void sendTitle(String mainText, String subText, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(mainText, subText, fadeIn, stay, fadeOut);
	}
	public void addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean particles) {
		player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier, false, particles));
	}
	public void addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
		player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier));
	}
	public void removePotionEffect(PotionEffectType potionEffectType) {
		player.removePotionEffect(potionEffectType);
	}
	public void stopSound(Sound sound) {
		player.stopSound(sound);
	}
}