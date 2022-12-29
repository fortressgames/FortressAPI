package net.fortressgames.fortressapi.listeners;

import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ClickEvent {

	protected static final HashMap<Entity, BiConsumer<Player, Entity>> click = new HashMap<>();

	public static void addClickEntity(Entity entity, BiConsumer<Player, Entity> consumer) {
		click.put(entity, consumer);
	}

	public static void removeClickEntity(Entity entity) {
		click.remove(entity);
	}
}