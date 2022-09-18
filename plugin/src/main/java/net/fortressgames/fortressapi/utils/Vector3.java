package net.fortressgames.fortressapi.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

public class Vector3 {

	@Getter private int blockX;
	@Getter private int blockY;
	@Getter private int blockZ;

	public Vector3(int blockX, int blockY, int blockZ) {
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;
	}

	public Vector2 toVector2() {
		return new Vector2(blockX, blockZ);
	}

	public Vector3 clone() {
		return new Vector3(blockX, blockY, blockZ);
	}

	public Vector3 subtract(Vector3 vec) {
		this.blockX -= vec.getBlockX();
		this.blockY -= vec.getBlockY();
		this.blockZ -= vec.getBlockZ();
		return this;
	}

	public Vector3 normalize() {
		double length = this.length();
		this.blockX /= length;
		this.blockY /= length;
		this.blockZ /= length;
		return this;
	}

	private double length() {
		return Math.sqrt(NumberConversions.square(this.blockX) + NumberConversions.square(this.blockY) + NumberConversions.square(this.blockZ));
	}

	public Vector3 multiply(int m) {
		this.blockX *= (double)m;
		this.blockY *= (double)m;
		this.blockZ *= (double)m;
		return this;
	}

	public Vector3 add(Vector3 vec) {
		this.blockX += vec.blockX;
		this.blockY += vec.blockY;
		this.blockZ += vec.blockZ;
		return this;
	}

	public Location toLocation(World world) {
		return new Location(world, blockX, blockY, blockZ);
	}

	public String toString() {
		return blockX + ", " + blockY + ", " + blockZ;
	}
}