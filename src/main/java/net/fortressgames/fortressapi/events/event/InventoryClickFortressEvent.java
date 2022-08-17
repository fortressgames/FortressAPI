package net.fortressgames.fortressapi.events.event;

import lombok.Getter;
import net.fortressgames.fortressapi.events.InventoryInteractFortressEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryClickFortressEvent extends InventoryInteractFortressEvent {

	private static final HandlerList handlers = new HandlerList();
	@Getter private final ClickType click;
	@Getter private final InventoryAction action;
	private final InventoryType.SlotType slot_type;
	@Getter private final int slot;
	@Getter private final int rawSlot;
	private ItemStack current;
	@Getter private final int hotbarButton;

	public InventoryClickFortressEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action) {
		super(view);
		this.current = null;
		this.hotbarButton = -1;
		this.slot_type = type;
		this.rawSlot = slot;
		this.slot = view.convertSlot(slot);
		this.click = click;
		this.action = action;
	}

	public InventoryType.SlotType getSlotType() {
		return this.slot_type;
	}

	public ItemStack getCursor() {
		return this.getView().getCursor();
	}

	public ItemStack getCurrentItem() {
		return this.slot_type == InventoryType.SlotType.OUTSIDE ? this.current : this.getView().getItem(this.rawSlot);
	}

	public boolean isRightClick() {
		return this.click.isRightClick();
	}

	public boolean isLeftClick() {
		return this.click.isLeftClick();
	}

	public boolean isShiftClick() {
		return this.click.isShiftClick();
	}

	/** @deprecated */
	@Deprecated
	public void setCursor(ItemStack stack) {
		this.getView().setCursor(stack);
	}

	public void setCurrentItem(ItemStack stack) {
		if (this.slot_type == InventoryType.SlotType.OUTSIDE) {
			this.current = stack;
		} else {
			this.getView().setItem(this.rawSlot, stack);
		}

	}

	public Inventory getClickedInventory() {
		return this.getView().getInventory(this.rawSlot);
	}

	@NotNull
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}