package net.fortressgames.fortressapi.listeners;

import net.fortressgames.fortressapi.gui.LoopTask;
import net.fortressgames.fortressapi.players.CustomPlayer;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloseInventoryListener implements Listener {

	public static List<Player> singleSub = new ArrayList<>();
	public static HashMap<Player, ItemStack> offhand = new HashMap<>();

	@EventHandler
	public void close(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		CustomPlayer customPlayer = PlayerModule.getInstance().getPlayer((Player) e.getPlayer());

		if(customPlayer == null) return;

		if(customPlayer.getOpenMenu() != null && !singleSub.contains(player)) {

			if(customPlayer.getOpenMenu().getInventoryName().equalsIgnoreCase(player.getOpenInventory().getTitle())) {
				for(LoopTask loopTask : customPlayer.getOpenMenu().getLoopTasks().values()) {
					loopTask.cancel();
				}

				customPlayer.setOpenMenu(null);
			}
		}

		singleSub.remove(player);

		if(offhand.containsKey(player)) {
			player.getInventory().setItemInOffHand(offhand.get(player));
			offhand.remove(player);
		}
	}
}