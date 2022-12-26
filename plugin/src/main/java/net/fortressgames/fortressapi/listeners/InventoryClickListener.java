package net.fortressgames.fortressapi.listeners;

import net.fortressgames.fortressapi.gui.InventoryMenu;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void click(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		InventoryMenu inventoryMenu = PlayerModule.getInstance().getOpenMenu().get(player);

		if(player.getOpenInventory().getType() != InventoryType.CHEST) {
			if(player.getGameMode() == GameMode.CREATIVE) return;
		}

		if(inventoryMenu != null) {

			if(e.getClickedInventory() == null) return;

			if(e.getAction() == InventoryAction.HOTBAR_SWAP) {
				e.setCancelled(true);
				return;
			}

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