package net.fortressgames.fortressapi.eventlisteners;

import net.fortressgames.fortressapi.events.event.InventoryClickFortressEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventListener implements Listener {

	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent e) {
		Bukkit.getPluginManager().callEvent(new InventoryClickFortressEvent(e.getView(), e.getSlotType(), e.getSlot(), e.getClick(), e.getAction()));
	}
}