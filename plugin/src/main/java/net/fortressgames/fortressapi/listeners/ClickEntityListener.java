package net.fortressgames.fortressapi.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.SneakyThrows;
import net.fortressgames.fortressapi.FortressAPI;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class ClickEntityListener extends PacketAdapter implements Listener {

	public ClickEntityListener() {
		super(FortressAPI.getInstance(), PacketType.Play.Client.USE_ENTITY);
	}

	@SneakyThrows
	@Override
	public void onPacketReceiving(PacketEvent e) {
		PacketContainer packet = e.getPacket();

		for(Entity entity : FortressAPI.click.keySet()) {

			if(entity.getEntityId() == packet.getIntegers().read(0)) {
				PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet.getHandle();

				Field valueB = useEntity.getClass().getDeclaredField("b");
				valueB.setAccessible(true);
				Object objectValueB = valueB.get(useEntity);

				try {
					objectValueB.getClass().getDeclaredField("a");
					//LEFT CLICK
					FortressAPI.click.get(entity).accept(entity);

				} catch (NoSuchFieldException ee) {
					//RIGHT CLICK
				}
			}
		}
	}
}