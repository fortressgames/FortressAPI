package net.fortressgames.fortressapi.utils;

import lombok.Getter;
import lombok.Setter;

public class Vector2 {

	@Setter	@Getter private int blockX;
	@Setter @Getter private int blockZ;

	public Vector2(int blockX, int blockZ) {
		this.blockX = blockX;
		this.blockZ = blockZ;
	}

	@Override
	public String toString() {
		return blockX + "," + blockZ;
	}
}