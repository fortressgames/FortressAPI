package net.fortressgames.fortressapi.entities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
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
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

public class CustomNPC {

	@Getter private final EntityPlayer entityPlayer;
	@Getter private final Location location;

	@Getter private boolean glowing;

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

	public void update(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), entityPlayer.ai(), true));

		updateArmor(player);
	}

	public void updateArmor(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.HEAD.NMS(), entityPlayer.c(EntityUtil.EnumItemSlot.HEAD.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.CHEST.NMS(), entityPlayer.c(EntityUtil.EnumItemSlot.CHEST.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.LEGS.NMS(), entityPlayer.c(EntityUtil.EnumItemSlot.LEGS.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.FEET.NMS(), entityPlayer.c(EntityUtil.EnumItemSlot.FEET.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.MAIN_HAND.NMS(), entityPlayer.c(EntityUtil.EnumItemSlot.MAIN_HAND.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.OFF_HAND.NMS(), entityPlayer.c(EntityUtil.EnumItemSlot.OFF_HAND.NMS()))))));
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

	public void setEquipment(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack mainHand, ItemStack offHand, Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id,
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.MAIN_HAND.NMS(), CraftItemStack.asNMSCopy(mainHand))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id,
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.OFF_HAND.NMS(), CraftItemStack.asNMSCopy(offHand))))));
		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id, new ArrayList<>(
				Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.FEET.NMS(), CraftItemStack.asNMSCopy(boots)))
		)));
		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id,
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.LEGS.NMS(), CraftItemStack.asNMSCopy(leggings))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id,
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.CHEST.NMS(), CraftItemStack.asNMSCopy(chestplate))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id,
				new ArrayList<>(Collections.singleton(new Pair<>(EntityUtil.EnumItemSlot.HEAD.NMS(), CraftItemStack.asNMSCopy(helmet))))));
	}

	public CustomNPC setGlowing(boolean glowing) {
		entityPlayer.b(6, glowing);
		this.glowing = glowing;

		return this;
	}
}