package net.fortressgames.fortressapi.events;

import net.fortressgames.fortressapi.players.FortressPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public abstract class InventoryInteractFortressEvent extends InventoryFortressEvent implements FortressCancellable {

	private Result result;

	public InventoryInteractFortressEvent(InventoryView transaction) {
		super(transaction);
		this.result = Result.DEFAULT;
	}

	public FortressPlayer getPlayer() {
		if(this.getView().getPlayer() instanceof Player player) {
			return FortressPlayer.getPlayer(player);
		}

		return null;
	}

	public void setResult(Result newResult) {
		this.result = newResult;
	}

	public Result getResult() {
		return this.result;
	}

	public boolean isCancelled() {
		return this.getResult() == Result.DENY;
	}

	public void setCancelled(boolean toCancel) {
		this.setResult(toCancel ? Result.DENY : Result.ALLOW);
	}
}
