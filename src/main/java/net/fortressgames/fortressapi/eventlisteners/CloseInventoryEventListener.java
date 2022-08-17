package net.fortressgames.fortressapi.eventlisteners;

import net.fortressgames.fortressapi.events.event.InventoryCloseFortressEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseInventoryEventListener implements Listener {

	@EventHandler
	public void InventoryCloseEvent(InventoryCloseEvent e) {
		Bukkit.getPluginManager().callEvent(new InventoryCloseFortressEvent(e.getView()));
	}
}