package net.fortressgames.fortressapi.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LoopTask extends BukkitRunnable {

	private final InventoryMenu inventoryMenu;
	private final List<ItemStack> itemStacks;
	private final int slot;

	private int count = 0;

	public LoopTask(List<ItemStack> itemStacks, InventoryMenu inventoryMenu, int slot) {
		this.itemStacks = itemStacks;
		this.inventoryMenu = inventoryMenu;
		this.slot = slot;
	}

	@Override
	public void run() {

		inventoryMenu.updateItem(itemStacks.get(count), slot);
		count++;

		if(count >= itemStacks.size()) {
			count = 0;
		}
	}
}