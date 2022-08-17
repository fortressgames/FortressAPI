package net.fortressgames.fortressapi.events;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InventoryFortressEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	protected InventoryView inventoryView;

	public InventoryFortressEvent(InventoryView inventoryView) {
		this.inventoryView = inventoryView;
	}

	public Inventory getInventory() {
		return this.inventoryView.getTopInventory();
	}

	public List<HumanEntity> getViewers() {
		return this.inventoryView.getTopInventory().getViewers();
	}

	public InventoryView getView() {
		return this.inventoryView;
	}

	@NotNull
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
