package net.fortressgames.fortressapi.listeners;

import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ClickEvent {

	protected static final HashMap<Entity, BiConsumer<Player, Entity>> rightClick = new HashMap<>();
	protected static final HashMap<Entity, BiConsumer<Player, Entity>> LeftClick = new HashMap<>();

	public static void addClickEntity(Entity entity, BiConsumer<Player, Entity> consumer, ClickType clickType) {
		if(clickType.equals(ClickType.RIGHT)) {
			rightClick.put(entity, consumer);
		} else {
			LeftClick.put(entity, consumer);
		}
	}

	public static void removeClickEntity(Entity entity, ClickType clickType) {
		if(clickType.equals(ClickType.RIGHT)) {
			rightClick.remove(entity);
		} else {
			LeftClick.remove(entity);
		}
	}
}