package net.fortressgames.fortressapi.entities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.fortressgames.fortressapi.FortressAPI;
import net.fortressgames.fortressapi.PacketConnection;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.function.Consumer;

public class CustomNPC {

	private final EntityPlayer entityPlayer;
	@Getter private final Location location;

	@Getter	private final int id;
	@Setter	@Getter	private Consumer<Player> click;

	public CustomNPC(Location location, String displayName, SkinCatch skinCatch, Consumer<Player> click) {
		this.location = location;
		this.click = click;

		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

		GameProfile profile = new GameProfile(UUID.randomUUID(), ChatColor.translateAlternateColorCodes('&', displayName));

		profile.getProperties().put("textures", new Property("textures", skinCatch.getValue(), skinCatch.getSignature()));

		this.entityPlayer = new EntityPlayer(server, worldServer, profile, null);

		this.entityPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		//0x40 : Hat
		//0x20 right leg
		//0x10 left leg
		//0x08 left arm
		//0x04 right arm
		//0x02 body
		//0x01 cape
		byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
		this.entityPlayer.ai().b(new DataWatcherObject<>(17, DataWatcherRegistry.a), b);

		this.id = entityPlayer.ae();
	}

	public CustomNPC(Location location, String displayName, SkinCatch skinCatch) {
		this.location = location;

		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

		GameProfile profile = new GameProfile(UUID.randomUUID(), ChatColor.translateAlternateColorCodes('&', displayName));

		profile.getProperties().put("textures", new Property("textures", skinCatch.getValue(), skinCatch.getSignature()));

		this.entityPlayer = new EntityPlayer(server, worldServer, profile, null);

		this.entityPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		//0x40 : Hat
		//0x20 right leg
		//0x10 left leg
		//0x08 left arm
		//0x04 right arm
		//0x02 body
		//0x01 cape
		byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
		this.entityPlayer.ai().b(new DataWatcherObject<>(17, DataWatcherRegistry.a), b);

		this.id = entityPlayer.ae();
	}

	public void spawn(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.entityPlayer));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entityPlayer));
		connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.entityPlayer, (byte) 0));
		connection.sendPacket(new PacketPlayOutEntityMetadata(id, this.entityPlayer.ai(), true));

		PacketPlayOutPlayerInfo playOutPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer);

		new BukkitRunnable() {
			@Override
			public void run() {
				connection.sendPacket(playOutPlayerInfo);
				teleport(location);
			}
		}.runTaskLater(FortressAPI.getInstance(), 100);
	}

	public void remove(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityDestroy(id));
	}

	public void teleport(Location location) {
		PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(id, (byte)(location.getYaw() * 256.0f / 360.0f), (byte)location.getPitch(), true);

		for(Player pp : Bukkit.getOnlinePlayers()) {
			PacketConnection connection = PacketConnection.getConnection(pp);


			this.entityPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			connection.sendPacket(new PacketPlayOutEntityTeleport(this.entityPlayer));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.entityPlayer, (byte)(location.getYaw() * 256.0f / 360.0f)));
			connection.sendPacket(packetPlayOutEntityLook);
		}
	}
}