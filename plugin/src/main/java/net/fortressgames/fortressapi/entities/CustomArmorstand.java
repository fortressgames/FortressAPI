package net.fortressgames.fortressapi.entities;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.fortressgames.fortressapi.PacketConnection;
import net.minecraft.core.Vector3f;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class CustomArmorstand extends EntityUtil {

	private final EntityArmorStand entityArmorStand;
	@Getter private Location location;

	@Getter private ItemStack itemInMainHand = new ItemStack(Material.AIR);
	@Getter private ItemStack itemInOffHand = new ItemStack(Material.AIR);
	@Getter private ItemStack helmet = new ItemStack(Material.AIR);
	@Getter private ItemStack chestplate = new ItemStack(Material.AIR);
	@Getter private ItemStack leggings = new ItemStack(Material.AIR);
	@Getter private ItemStack boots = new ItemStack(Material.AIR);

	@Getter private EulerAngle headPose;
	@Getter private EulerAngle bodyPose;
	@Getter private EulerAngle leftArmPose;
	@Getter private EulerAngle rightArmPose;
	@Getter private EulerAngle leftLegPose;
	@Getter private EulerAngle rightLegPose;

	@Getter private boolean basePlate;
	@Getter private boolean invisible;
	@Getter private boolean arms;
	@Getter private boolean small;
	@Getter private boolean glowing;
	@Getter private boolean noGravity = true;
	@Getter private boolean customNameVisible;
	@Getter private boolean silent = true;

	@Getter private String customName = "NULL";

	public CustomArmorstand(Location location) {
		super(new EntityArmorStand(EntityTypes.d, ((CraftWorld) location.getWorld()).getHandle()));

		entityArmorStand = ((EntityArmorStand) getEntity());

		this.location = location;
		this.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		this.headPose = new EulerAngle(entityArmorStand.cg.b(), entityArmorStand.cg.c(), entityArmorStand.cg.d());
		this.bodyPose = new EulerAngle(entityArmorStand.ch.b(), entityArmorStand.ch.c(), entityArmorStand.ch.d());
		this.leftArmPose = new EulerAngle(entityArmorStand.ci.b(), entityArmorStand.ci.c(), entityArmorStand.ci.d());
		this.rightArmPose = new EulerAngle(entityArmorStand.cj.b(), entityArmorStand.cj.c(), entityArmorStand.cj.d());
		this.leftLegPose = new EulerAngle(entityArmorStand.ck.b(), entityArmorStand.ck.c(), entityArmorStand.ck.d());
		this.rightLegPose = new EulerAngle(entityArmorStand.cl.b(), entityArmorStand.cl.c(), entityArmorStand.cl.d());

		setSilent(true);
	}

	public void update(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.HEAD.NMS(), entityArmorStand.c(EnumItemSlot.HEAD.NMS()))))));

		updateArmor(player);
		updateName(player);
		sendLocationChangeArmorstand(player);
	}

	public void updateArmor(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.HEAD.NMS(), entityArmorStand.c(EnumItemSlot.HEAD.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.CHEST.NMS(), entityArmorStand.c(EnumItemSlot.CHEST.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.LEGS.NMS(), entityArmorStand.c(EnumItemSlot.LEGS.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.FEET.NMS(), entityArmorStand.c(EnumItemSlot.FEET.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.MAIN_HAND.NMS(), entityArmorStand.c(EnumItemSlot.MAIN_HAND.NMS()))))));

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.OFF_HAND.NMS(), entityArmorStand.c(EnumItemSlot.OFF_HAND.NMS()))))));
	}

	public void updateName(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true));
	}

	public void sendLocationChangeArmorstand(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), false));
		connection.sendPacket(new PacketPlayOutEntityTeleport(entityArmorStand));
	}

	public void spawn(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(entityArmorStand);
		PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true);
		connection.sendPacket(packetPlayOutSpawnEntity);
		connection.sendPacket(metadata);

		connection.sendPacket(new PacketPlayOutEntityEquipment(getId(),
				new ArrayList<>(Collections.singleton(new Pair<>(EnumItemSlot.HEAD.NMS(), entityArmorStand.c(EnumItemSlot.HEAD.NMS()))))));

		updateArmor(player);
	}

	public void sit(Player rider, Player viewer) {
		PacketConnection connection = PacketConnection.getConnection(viewer);

		PacketPlayOutMount onTopMountPacket = new PacketPlayOutMount(entityArmorStand);
		// fill mount packet with the trackerentry and ridingOn's passengers and a = ridingOn
		int[] bArray = new int[getPassengers().size() + 1];

		for(int i = 0; i < getPassengers().size(); ++i) {
			bArray[i] = getPassengers().get(i).ae();
		}

		bArray[getPassengers().size()] = ((CraftPlayer) rider).getHandle().ae();

		try {
			Field aField = onTopMountPacket.getClass().getDeclaredField("a");
			aField.setAccessible(true);
			aField.setInt(onTopMountPacket, getId());
			Field bField = onTopMountPacket.getClass().getDeclaredField("b");
			bField.setAccessible(true);
			bField.set(onTopMountPacket, bArray);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		connection.sendPacket(onTopMountPacket);
	}

	public void remove(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityDestroy(getId()));
	}

	public CustomArmorstand setLocation(Location location) {
		this.location = location;

		setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		return this;
	}

	public CustomArmorstand setItemInMainHand(ItemStack itemInMainHand) {
		setSlot(EnumItemSlot.MAIN_HAND.NMS(), CraftItemStack.asNMSCopy(itemInMainHand));
		this.itemInMainHand = itemInMainHand;

		return this;
	}

	public CustomArmorstand setItemInOffHand(ItemStack itemInOffHand) {
		setSlot(EnumItemSlot.OFF_HAND.NMS(), CraftItemStack.asNMSCopy(itemInOffHand));
		this.itemInOffHand = itemInOffHand;

		return this;
	}

	public CustomArmorstand setHelmet(ItemStack helmet) {
		setSlot(EnumItemSlot.HEAD.NMS(), CraftItemStack.asNMSCopy(helmet));
		this.helmet = helmet;

		return this;
	}

	public CustomArmorstand setChestplate(ItemStack chestplate) {
		setSlot(EnumItemSlot.CHEST.NMS(), CraftItemStack.asNMSCopy(chestplate));
		this.chestplate = chestplate;

		return this;
	}

	public CustomArmorstand setLeggings(ItemStack leggings) {
		setSlot(EnumItemSlot.LEGS.NMS(), CraftItemStack.asNMSCopy(leggings));
		this.leggings = leggings;

		return this;
	}

	public CustomArmorstand setBoots(ItemStack boots) {
		setSlot(EnumItemSlot.FEET.NMS(), CraftItemStack.asNMSCopy(boots));
		this.boots = boots;

		return this;
	}

	public CustomArmorstand setHeadPose(EulerAngle headPose) {
		entityArmorStand.a(new Vector3f((float) (headPose.getX() * (Math.PI * 180)),
				(float) (headPose.getY() * (Math.PI * 180)),
				(float) (headPose.getZ() * (Math.PI * 180))));
		this.headPose = headPose;

		return this;
	}

	public CustomArmorstand setBodyPose(EulerAngle bodyPose) {
		entityArmorStand.b(new Vector3f((float) (bodyPose.getX() * (Math.PI * 180)),
				(float) (bodyPose.getY() * (Math.PI * 180)),
				(float) (bodyPose.getZ() * (Math.PI * 180))));
		this.bodyPose = bodyPose;

		return this;
	}

	public CustomArmorstand setLeftArmPose(EulerAngle leftArmPose) {
		entityArmorStand.c(new Vector3f((float) (leftArmPose.getX() * (Math.PI * 180)),
				(float) (leftArmPose.getY() * (Math.PI * 180)),
				(float) (leftArmPose.getZ() * (Math.PI * 180))));
		this.leftArmPose = leftArmPose;

		return this;
	}

	public CustomArmorstand setRightArmPose(EulerAngle rightArmPose) {
		entityArmorStand.d(new Vector3f((float) (rightArmPose.getX() * (Math.PI * 180)),
				(float) (rightArmPose.getY() * (Math.PI * 180)),
				(float) (rightArmPose.getZ() * (Math.PI * 180))));
		this.rightArmPose = rightArmPose;

		return this;
	}

	public CustomArmorstand setLeftLegPose(EulerAngle leftLegPose) {
		entityArmorStand.e(new Vector3f((float) (leftLegPose.getX() * (Math.PI * 180)),
				(float) (leftLegPose.getY() * (Math.PI * 180)),
				(float) (leftLegPose.getZ() * (Math.PI * 180))));
		this.leftLegPose = leftLegPose;

		return this;
	}

	public CustomArmorstand setRightLegPose(EulerAngle rightLegPose) {
		entityArmorStand.f(new Vector3f((float) (rightLegPose.getX() * (Math.PI * 180)),
				(float) (rightLegPose.getY() * (Math.PI * 180)),
				(float) (rightLegPose.getZ() * (Math.PI * 180))));
		this.rightLegPose = rightLegPose;

		return this;
	}

	public CustomArmorstand setBasePlate(boolean basePlate) {
		entityArmorStand.s(basePlate);
		this.basePlate = basePlate;

		return this;
	}

	public CustomArmorstand setInvisible(boolean invisible) {
		entityArmorStand.j(invisible);
		this.invisible = invisible;

		return this;
	}

	public CustomArmorstand setArms(boolean arms) {
		entityArmorStand.r(arms);
		this.arms = arms;

		return this;
	}

	public CustomArmorstand setSmall(boolean small) {
		entityArmorStand.a(small);
		this.small = small;

		return this;
	}

	public CustomArmorstand setGlowing(boolean glowing) {
		setFlag(6, glowing);
		this.glowing = glowing;

		return this;
	}

	public CustomArmorstand setNoGravity(boolean noGravity) {
		setNoGravityNMS(noGravity);
		this.noGravity = noGravity;

		return this;
	}

	public CustomArmorstand setCustomNameVisible(boolean customNameVisible) {
		setCustomNameVisibleNMS(customNameVisible);
		this.customNameVisible = customNameVisible;

		return this;
	}

	public CustomArmorstand setSilent(boolean silent) {
		setSilentNMS(silent);
		this.silent = silent;

		return this;
	}

	public CustomArmorstand setCustomName(String customName) {
		if(customName == null) return this;
		IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', customName) + "\"}");

		setCustomNameNMS(iChatBaseComponent);

		this.customName = customName;

		return this;
	}

	public EntityArmorStand breakInto() {
		return entityArmorStand;
	}
}