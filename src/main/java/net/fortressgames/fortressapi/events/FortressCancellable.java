package net.fortressgames.fortressapi.events;

public interface FortressCancellable {

	boolean isCancelled();

	void setCancelled(boolean value);
}