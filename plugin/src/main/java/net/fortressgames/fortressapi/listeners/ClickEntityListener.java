package net.fortressgames.fortressapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.SneakyThrows;
import net.fortressgames.fortressapi.FortressAPI;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ClickEntityListener extends PacketAdapter implements Listener {

	public ClickEntityListener() {
		super(FortressAPI.getInstance(), PacketType.Play.Client.USE_ENTITY);
	}

	private final List<Player> delay = new ArrayList<>();

	@Override
	public void onPacketReceiving(PacketEvent e) {
		PacketContainer packet = e.getPacket();

		if(delay.contains(e.getPlayer())) return;

		delay.add(e.getPlayer());
		Bukkit.getScheduler().runTask(FortressAPI.getInstance(), () -> delay.remove(e.getPlayer()));

		// loop all click entities
		for(Map.Entry<Entity, BiConsumer<Player, Entity>> map : ClickEvent.LeftClick.entrySet()) {

			// check if the ID matches current click entity on event
			if(map.getKey().getBukkitEntity().getEntityId() == packet.getIntegers().read(0)) {
				PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet.getHandle();

				Object objectValue = getFieldObject(packetPlayInUseEntity);

				try {
					objectValue.getClass().getDeclaredField("a");
					//LEFT CLICK

					Bukkit.getScheduler().runTask(FortressAPI.getInstance(), () -> ClickEvent.LeftClick.get(map.getKey()).accept(e.getPlayer(), map.getKey()));

				} catch (NoSuchFieldException ee) {
					//RIGHT CLICK
				}
			}
		}

		// loop all click entities
		for(Map.Entry<Entity, BiConsumer<Player, Entity>> map : ClickEvent.rightClick.entrySet()) {

			// check if the ID matches current click entity on event
			if(map.getKey().getBukkitEntity().getEntityId() == packet.getIntegers().read(0)) {
				PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet.getHandle();

				Object objectValue = getFieldObject(packetPlayInUseEntity);

				try {
					objectValue.getClass().getDeclaredField("a");
					//LEFT CLICK

				} catch (NoSuchFieldException ee) {
					//RIGHT CLICK

					Bukkit.getScheduler().runTask(FortressAPI.getInstance(), () -> ClickEvent.rightClick.get(map.getKey()).accept(e.getPlayer(), map.getKey()));
				}
			}
		}
	}

	@SneakyThrows
	private Object getFieldObject(PacketPlayInUseEntity packetPlayInUseEntity) {
		Field value = packetPlayInUseEntity.getClass().getDeclaredField("b");
		value.setAccessible(true);

		return value.get(packetPlayInUseEntity);
	}
}