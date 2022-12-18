package net.fortressgames.fortressapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.Method;
import java.util.function.Supplier;


public class EntityTeleportUtil {

	public static void teleport(Location location, Entity entity) {
		try {
			methods[1].invoke(methods[0].invoke(entity), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final Method[] methods = ((Supplier<Method[]>) () -> {
		try {
			Method getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity").getDeclaredMethod("getHandle");
			return new Method[] {
					getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class)
			};
		} catch (Exception ex) {
			return null;
		}
	}).get();
}
