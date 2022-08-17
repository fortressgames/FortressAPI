package net.fortressgames.fortressapi.listeners;

import net.fortressgames.fortressapi.events.event.InventoryClickFortressEvent;
import net.fortressgames.fortressapi.gui.InventoryMenu;
import net.fortressgames.fortressapi.players.FortressPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void click(InventoryClickFortressEvent e) {
		FortressPlayer player = e.getPlayer();

		if(player.getOpenInventory().getType() != InventoryType.CHEST) {
			if(player.getGameMode() == GameMode.CREATIVE) return;
		}

		if(player.getOpenMenu() != null) {

			if(e.getClickedInventory() == null) return;

			if(e.getAction() == InventoryAction.HOTBAR_SWAP) {
				e.setCancelled(true);
				return;
			}

			InventoryMenu inventoryMenu = player.getOpenMenu();

			if(inventoryMenu.isCanMove()) {

				if(e.getClickedInventory().getType() == InventoryType.CHEST) {
					if(inventoryMenu.getLockedSlots().contains(e.getSlot())) {
						e.setCancelled(true);
					}
				}

			} else {
				e.setCancelled(true);
			}

			if(inventoryMenu.getSlots().containsKey(e.getSlot()) && e.getClickedInventory().equals(inventoryMenu.getInventory())) {
				inventoryMenu.getSlots().get(e.getSlot()).accept(e);
			}
		}
	}
}