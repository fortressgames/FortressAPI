package net.fortressgames.fortressapi;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public record PacketConnection(Player player) {

	public static PacketConnection getConnection(Player player) {
		return new PacketConnection(player);
	}

	public void sendPacket(Packet<?> packet) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().b;

		connection.a(packet);
	}
}