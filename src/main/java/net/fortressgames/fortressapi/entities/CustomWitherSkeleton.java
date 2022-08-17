package net.fortressgames.fortressapi.entities;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.fortressgames.fortressapi.PacketConnection;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class CustomWitherSkeleton extends EntityUtil {

	private final EntitySkeletonWither entitySkeletonWither;

	@Getter private Location location;

	@Getter private ItemStack itemInMainHand = new ItemStack(Material.AIR);
	@Getter private ItemStack itemInOffHand = new ItemStack(Material.AIR);
	@Getter private ItemStack helmet = new ItemStack(Material.AIR);
	@Getter private ItemStack chestplate = new ItemStack(Material.AIR);
	@Getter private ItemStack leggings = new ItemStack(Material.AIR);
	@Getter private ItemStack boots = new ItemStack(Material.AIR);

	@Getter private boolean glowing;
	@Getter private boolean noGravity = true;
	@Getter private boolean customNameVisible;
	@Getter private boolean silent = true;

	@Getter private String customName = "NULL";

	public CustomWitherSkeleton(Location location) {
		super(new EntitySkeletonWither(EntityTypes.bf, ((CraftWorld) location.getWorld()).getHandle()));

		this.location = location;
		this.entitySkeletonWither = ((EntitySkeletonWither) getEntity());
		setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public void update(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.HEAD.NMS(), entitySkeletonWither.c(EnumItemSlot.HEAD.NMS()))))));

		updateArmor(player);
		updateName(player);
		sendLocationChange(player);
	}

	public void updateName(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true);
		connection.sendPacket(metadata);
	}

	public void sendLocationChange(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), false));
		connection.sendPacket(new PacketPlayOutEntityTeleport(entitySkeletonWither));
	}

	public void updateArmor(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.HEAD.NMS(), entitySkeletonWither.c(EnumItemSlot.HEAD.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.CHEST.NMS(), entitySkeletonWither.c(EnumItemSlot.CHEST.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.LEGS.NMS(), entitySkeletonWither.c(EnumItemSlot.LEGS.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.FEET.NMS(), entitySkeletonWither.c(EnumItemSlot.FEET.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.MAIN_HAND.NMS(), entitySkeletonWither.c(EnumItemSlot.MAIN_HAND.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.OFF_HAND.NMS(), entitySkeletonWither.c(EnumItemSlot.OFF_HAND.NMS()))))));
	}

	public void spawn(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutSpawnEntity(entitySkeletonWither));
		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true));
		connection.sendPacket(new PacketPlayOutEntityHeadRotation(entitySkeletonWither, (byte) location.getYaw()));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.HEAD.NMS(), entitySkeletonWither.c(EnumItemSlot.HEAD.NMS()))))));

		updateArmor(player);
	}

	public void remove(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		PacketPlayOutEntityDestroy kill = new PacketPlayOutEntityDestroy(getId());
		connection.sendPacket(kill);
	}

	public CustomWitherSkeleton setLocation(Location location) {
		this.location = location;

		setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		return this;
	}

	public CustomWitherSkeleton setItemInMainHand(ItemStack itemInMainHand) {
		setSlot(EnumItemSlot.MAIN_HAND.NMS(), CraftItemStack.asNMSCopy(itemInMainHand));
		this.itemInMainHand = itemInMainHand;

		return this;
	}

	public CustomWitherSkeleton setItemInOffHand(ItemStack itemInOffHand) {
		setSlot(EnumItemSlot.OFF_HAND.NMS(), CraftItemStack.asNMSCopy(itemInOffHand));
		this.itemInOffHand = itemInOffHand;

		return this;
	}

	public CustomWitherSkeleton setHelmet(ItemStack helmet) {
		setSlot(EnumItemSlot.HEAD.NMS(), CraftItemStack.asNMSCopy(helmet));
		this.helmet = helmet;

		return this;
	}

	public CustomWitherSkeleton setChestplate(ItemStack chestplate) {
		setSlot(EnumItemSlot.CHEST.NMS(), CraftItemStack.asNMSCopy(chestplate));
		this.chestplate = chestplate;

		return this;
	}

	public CustomWitherSkeleton setLeggings(ItemStack leggings) {
		setSlot(EnumItemSlot.LEGS.NMS(), CraftItemStack.asNMSCopy(leggings));
		this.leggings = leggings;

		return this;
	}

	public CustomWitherSkeleton setBoots(ItemStack boots) {
		setSlot(EnumItemSlot.FEET.NMS(), CraftItemStack.asNMSCopy(boots));
		this.boots = boots;

		return this;
	}

	public CustomWitherSkeleton setGlowing(boolean glowing) {
		setFlag(6, glowing);
		this.glowing = glowing;

		return this;
	}

	public CustomWitherSkeleton setNoGravity(boolean noGravity) {
		setNoGravityNMS(noGravity);
		this.noGravity = noGravity;

		return this;
	}

	public CustomWitherSkeleton setCustomNameVisible(boolean customNameVisible) {
		setCustomNameVisibleNMS(customNameVisible);
		this.customNameVisible = customNameVisible;

		return this;
	}

	public CustomWitherSkeleton setSilent(boolean silent) {
		setSilentNMS(silent);
		this.silent = silent;

		return this;
	}

	public CustomWitherSkeleton setCustomName(String customName) {
		if(customName == null) return this;
		IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', customName) + "\"}");

		setCustomNameNMS(iChatBaseComponent);

		this.customName = customName;

		return this;
	}
}