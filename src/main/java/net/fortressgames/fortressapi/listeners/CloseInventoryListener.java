package net.fortressgames.fortressapi.listeners;

import net.fortressgames.fortressapi.gui.LoopTask;
import net.fortressgames.fortressapi.players.FortressPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloseInventoryListener implements Listener {

	public static List<FortressPlayer> singleSub = new ArrayList<>();
	public static HashMap<FortressPlayer, ItemStack> offhand = new HashMap<>();

	@EventHandler
	public void close(InventoryCloseEvent e) {
		FortressPlayer player = FortressPlayer.getPlayer(e.getPlayer());

		if(player == null) return;

		if(player.getOpenMenu() != null && !singleSub.contains(player)) {

			if(player.getOpenMenu().getInventoryName().equalsIgnoreCase(player.getOpenInventory().getTitle())) {
				for(LoopTask loopTask : player.getOpenMenu().getLoopTasks().values()) {
					loopTask.cancel();
				}

				player.setOpenMenu(null);
			}
		}

		singleSub.remove(player);

		if(offhand.containsKey(player)) {
			player.setItemInOffHand(offhand.get(player));
			offhand.remove(player);
		}
	}
}