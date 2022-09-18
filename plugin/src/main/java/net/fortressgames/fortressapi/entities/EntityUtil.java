package net.fortressgames.fortressapi.entities;

import lombok.Getter;
import net.fortressgames.fortressapi.FortressAPI;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class EntityUtil {

	@Getter private final Entity entity;

	public EntityUtil(Entity entity) {
		this.entity = entity;
	}

	public int getId() {
		return this.entity.ae();
	}

	protected void setPositionRotation(double x, double y, double z, float yaw, float pitch) {
		this.entity.b(x, y, z, yaw, pitch);
	}

	protected DataWatcher getDataWatcher() {
		return entity.ai();
	}

	protected String getName() {
		return FortressAPI.getInstance().getVersionHandler().getName(entity);
	}

	protected void setLocation(double x, double y, double z, float yaw, float pitch) {
		this.entity.a(x, y, z, yaw, pitch);
	}

	protected void setSlot(net.minecraft.world.entity.EnumItemSlot enumitemslot, ItemStack itemstack) {
		entity.a(enumitemslot, itemstack);
	}

	protected void setSilentNMS(boolean flag) {
		entity.d(flag);
	}

	protected void setNoGravityNMS(boolean flag) {
		entity.e(flag);
	}

	protected void setCustomNameNMS(@Nullable IChatBaseComponent ichatbasecomponent) {
		entity.b(ichatbasecomponent);
	}

	protected void setCustomNameVisibleNMS(boolean flag) {
		entity.n(flag);
	}

	protected void setFlag(int i, boolean flag) {
		entity.b(i, flag);
	}

	protected List<Entity> getPassengers() {
		return FortressAPI.getInstance().getVersionHandler().getPassengers(entity);
	}

	public enum EnumItemSlot {
		MAIN_HAND(net.minecraft.world.entity.EnumItemSlot.a),
		OFF_HAND(net.minecraft.world.entity.EnumItemSlot.b),
		FEET(net.minecraft.world.entity.EnumItemSlot.c),
		LEGS(net.minecraft.world.entity.EnumItemSlot.d),
		CHEST(net.minecraft.world.entity.EnumItemSlot.e),
		HEAD(net.minecraft.world.entity.EnumItemSlot.f);

		private final net.minecraft.world.entity.EnumItemSlot NMS;

		EnumItemSlot(net.minecraft.world.entity.EnumItemSlot NMS) {
			this.NMS = NMS;
		}

		public net.minecraft.world.entity.EnumItemSlot NMS() {
			return NMS;
		}
	}
}