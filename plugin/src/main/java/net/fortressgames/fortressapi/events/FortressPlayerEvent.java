package net.fortressgames.fortressapi.events;

import net.fortressgames.fortressapi.players.FortressPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class FortressPlayerEvent extends Event {

	protected FortressPlayer player;

	public FortressPlayerEvent(FortressPlayer who) {
		this.player = who;
	}

	@NotNull
	public final FortressPlayer getPlayer() {
		return this.player;
	}
}