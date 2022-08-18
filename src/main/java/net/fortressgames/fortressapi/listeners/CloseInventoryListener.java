package net.fortressgames.fortressapi.listeners;

import net.fortressgames.fortressapi.events.event.InventoryCloseFortressEvent;
import net.fortressgames.fortressapi.gui.LoopTask;
import net.fortressgames.fortressapi.players.FortressPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloseInventoryListener implements Listener {

	public static List<Player> singleSub = new ArrayList<>();
	public static HashMap<Player, ItemStack> offhand = new HashMap<>();

	@EventHandler
	public void close(InventoryCloseFortressEvent e) {
		FortressPlayer player = e.getPlayer();

		if(player != null && player.getOpenMenu() != null && !singleSub.contains(player.getPlayer())) {

			if(player.getOpenMenu().getInventoryName().equalsIgnoreCase(player.getOpenInventory().getTitle())) {
				for(LoopTask loopTask : player.getOpenMenu().getLoopTasks().values()) {
					loopTask.cancel();
				}

				player.setOpenMenu(null);
			}
		}

		singleSub.remove(player.getPlayer());

		if(offhand.containsKey(player.getPlayer())) {
			player.setItemInOffHand(offhand.get(player.getPlayer()));
			offhand.remove(player.getPlayer());
		}
	}
}