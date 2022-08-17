package net.fortressgames.fortressapi.events.event;

import net.fortressgames.fortressapi.events.InventoryFortressEvent;
import net.fortressgames.fortressapi.players.FortressPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class InventoryCloseFortressEvent extends InventoryFortressEvent {

	private static final HandlerList handlers = new HandlerList();

	public InventoryCloseFortressEvent(InventoryView inventoryView) {
		super(inventoryView);
	}

	public FortressPlayer getPlayer() {
		if(this.getView().getPlayer() instanceof Player player) {
			return FortressPlayer.getPlayer(player);
		}

		return null;
	}

	@NotNull
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}