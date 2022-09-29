package net.fortressgames.fortressapi.players;

import lombok.Getter;

public class FortressPlayerHolder {

	@Getter private final FortressPlayer fortressPlayer;

	public FortressPlayerHolder(FortressPlayer fortressPlayer) {
		this.fortressPlayer = fortressPlayer;
	}
}