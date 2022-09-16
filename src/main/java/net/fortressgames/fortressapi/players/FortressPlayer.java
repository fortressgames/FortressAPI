package net.fortressgames.fortressapi.players;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
	public static FortressPlayer getPlayer(HumanEntity humanEntity) {
		if(humanEntity instanceof Player) {
			return FortressPlayerModule.getInstance().getUser((Player)humanEntity);
		}
		return null;
	}

	public InventoryView getOpenInventory() {
		return player.getOpenInventory();
	}
	public void openInventory(Inventory inventory) {
		player.openInventory(inventory);
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
	public ItemStack getItemInOffHand() {
		return player.getInventory().getItemInOffHand();
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
	public void spawnParticle(Particle particle, double x, double y, double z, int count) {
		player.spawnParticle(particle, x, y, z, count);
	}
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offX, double offY, double offZ) {
		player.spawnParticle(particle, x, y, z, count, offX, offY, offZ);
	}
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offX, double offY, double offZ, double speed) {
		player.spawnParticle(particle, x, y, z, count, offX, offY, offZ, speed);
	}
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double speed) {
		player.spawnParticle(particle, x, y, z, count, 0, 0, 0, speed);
	}
}