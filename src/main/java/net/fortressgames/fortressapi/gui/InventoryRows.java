package net.fortressgames.fortressapi.gui;

import lombok.Getter;

public enum InventoryRows {
	ROW1(9),
	ROW2(18),
	ROW3(27),
	ROW4(36),
	ROW5(45),
	ROW6(54);

	@Getter private final int size;

	InventoryRows(int size) {
		this.size = size;
	}
}