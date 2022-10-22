package net.fortressgames.fortressapi.gui;

import lombok.Getter;
import lombok.Setter;
import net.fortressgames.fortressapi.FortressAPI;
import net.fortressgames.fortressapi.listeners.CloseInventoryListener;
import net.fortressgames.fortressapi.players.CustomPlayer;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class InventoryMenu {

//Inventory numbers:

// |                                            |
// | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  | 8  |
// | 9  | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 |
// | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26 |
// | 27 | 28 | 29 | 30 | 31 | 32 | 33 | 34 | 35 |
// | 36 | 37 | 38 | 39 | 40 | 41 | 42 | 43 | 45 |
// | 46 | 47 | 48 | 49 | 50 | 51 | 52 | 53 | 54 |
// |                                            |

	@Getter private final String inventoryName;

	@Getter private final List<Integer> lockedSlots = new ArrayList<>();
	@Setter @Getter private boolean canMove = false;

	@Getter private final Inventory inventory;
	@Getter private final Player player;

	@Getter private final HashMap<Integer, Consumer<InventoryClickEvent>> slots = new HashMap<>();

	@Getter private final HashMap<Integer, LoopTask> loopTasks = new HashMap<>();

	public InventoryMenu(Player player, InventoryRows rows, String inventoryName) {
		this.inventory = Bukkit.createInventory(player, rows.getSize(), inventoryName);
		this.player = player;
		this.inventoryName = inventoryName;
	}

	public InventoryMenu(Player player, InventoryRows rows, String image, String title) {
		this.inventory = Bukkit.createInventory(player, rows.getSize(), "§f\uF808" + image + "\uF80B\uF80A\uF809\uF808\uF801\uF80A\uF809§f\uF808§f" + title + "\uF80A\uF808\uF803\uF80B\uF80A\uF809\uF808\uF806");
		this.player = player;
		this.inventoryName = "§f\uF808" + image + "\uF80B\uF80A\uF809\uF808\uF801\uF80A\uF809§f\uF808§f" + title + "\uF80A\uF808\uF803\uF80B\uF80A\uF809\uF808\uF806";
	}

	public InventoryMenu(Player player, InventoryType type, String inventoryName) {
		this.inventory = Bukkit.createInventory(player, type, inventoryName);
		this.player = player;
		this.inventoryName = inventoryName;
	}

	/**
	 * Set item in inventory
	 *
	 * @param itemStack item
	 * @param slot inventory slot
	 */
	public void setItem(ItemStack itemStack, int slot) {
		inventory.setItem(slot, itemStack);
	}

	/**
	 * Set item in inventory
	 *
	 * @param itemStack item
	 * @param slot inventory slot
	 */
	public void setItem(ItemStack itemStack, int slot, boolean locked) {
		inventory.setItem(slot, itemStack);

		if(locked) {
			if(!lockedSlots.contains(slot)) lockedSlots.add(slot);
		}
	}

	/**
	 * Set item in inventory and click action
	 *
	 * @param itemStack item
	 * @param slot inventory slot
	 * @param event click action
	 */
	public void setItem(ItemStack itemStack, int slot, Consumer<InventoryClickEvent> event, boolean locked) {
		inventory.setItem(slot, itemStack);

		if(slots.containsKey(slot)) {
			slots.replace(slot, event);
		} else {
			slots.put(slot, event);
		}

		if(locked) {
			if(!lockedSlots.contains(slot)) lockedSlots.add(slot);
		}
	}

	/**
	 * Set item in inventory and click action
	 *
	 * @param itemStack item
	 * @param slot inventory slot
	 * @param event click action
	 */
	public void setItem(ItemStack itemStack, int slot, Consumer<InventoryClickEvent> event) {
		inventory.setItem(slot, itemStack);

		if(slots.containsKey(slot)) {
			slots.replace(slot, event);
		} else {
			slots.put(slot, event);
		}
	}

	/**
	 * Update item for slot
	 *
	 * @param itemStack item
	 * @param slot inventory slot
	 */
	public void updateItem(ItemStack itemStack, int slot) {
		inventory.setItem(slot, itemStack);
	}

	/**
	 * Update item and click event
	 *
	 * @param itemStack item
	 * @param slot inventory slot
	 * @param event InventoryClickEvent
	 */
	public void updateItem(ItemStack itemStack, int slot, Consumer<InventoryClickEvent> event) {
		inventory.setItem(slot, itemStack);

		if(slots.containsKey(slot)) {
			slots.replace(slot, event);
		} else {
			slots.put(slot, event);
		}
	}

	/**
	 * Open the inventory
	 */
	public void openInventory() {
		CustomPlayer customPlayer = PlayerModule.getInstance().getPlayer(player);

		if(customPlayer.getOpenMenu() != null) {
			customPlayer.setOpenMenu(this);
			CloseInventoryListener.singleSub.add(player);

		} else {
			customPlayer.setOpenMenu(this);
		}

		player.openInventory(this.inventory);
		CloseInventoryListener.offhand.put(player, player.getInventory().getItemInOffHand());
	}

	/**
	 * Set row of items
	 *
	 * @param itemStack item
	 * @param row row number
	 */
	public void setRow(ItemStack itemStack, InventoryRows row, boolean locked) {
		switch (row) {
			case ROW1:
				for(int i = 0; i < 9; i++) {
					setItem(itemStack, i, locked);
				}
				break;
			case ROW2:
				for(int i = 0; i < 9; i++) {
					setItem(itemStack, i +9, locked);
				}
				break;
			case ROW3:
				for(int i = 0; i < 9; i++) {
					setItem(itemStack, i +18, locked);
				}
				break;
			case ROW4:
				for(int i = 0; i < 9; i++) {
					setItem(itemStack, i +27, locked);
				}
				break;
			case ROW5:
				for(int i = 0; i < 9; i++) {
					setItem(itemStack, i +36, locked);
				}
				break;
			case ROW6:
				for(int i = 0; i < 9; i++) {
					setItem(itemStack, i +45, locked);
				}
				break;
		}
	}

	public void addLoop(List<ItemStack> itemStacks, int slot, int delay) {
		if(loopTasks.containsKey(slot)) {
			loopTasks.get(slot).cancel();

			loopTasks.replace(slot, new LoopTask(itemStacks, this, slot));
		} else {
			loopTasks.put(slot, new LoopTask(itemStacks, this, slot));
		}

		loopTasks.get(slot).runTaskTimer(FortressAPI.getInstance(), 0, delay);
	}

	public void removeLoop(int slot) {
		loopTasks.get(slot).cancel();

		loopTasks.remove(slot);
	}

	public void lockSlots(List<Integer> slots) {
		lockedSlots.addAll(slots);
	}

	public void unlockSlots(List<Integer> slots) {
		lockedSlots.removeAll(slots);
	}

	protected ItemStack createBackItem() {
		return new ItemBuilder(Material.STICK).customModelData(4).name(ChatColor.GOLD + "Go back").build();
	}
	protected ItemStack createNextPage() {
		return new ItemBuilder(Material.STICK).customModelData(2).name(ChatColor.GOLD + "Next page").build();
	}
	protected ItemStack createPreviousPage() {
		return new ItemBuilder(Material.STICK).customModelData(4).name(ChatColor.GOLD + "Previous page").build();
	}
	protected ItemStack createNextPageFade() {
		return new ItemBuilder(Material.STICK).customModelData(3).name(ChatColor.GRAY + "Next page").build();
	}
	protected ItemStack createPreviousPageFade() {
		return new ItemBuilder(Material.STICK).customModelData(5).name(ChatColor.GRAY + "Previous page").build();
	}
}