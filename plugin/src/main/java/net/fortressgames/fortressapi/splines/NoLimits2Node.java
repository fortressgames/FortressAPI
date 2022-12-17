package net.fortressgames.fortressapi.splines;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * no. PosX PosY PosZ FrontX FrontY FrontZ LeftX LeftY LeftZ UpX UpY UpZ
 *
 * PosX = location x
 * PosY = location y
 * PosZ = location z
 * FrontX = direction x
 * FrontY = direction y
 * FrontZ = direction z
 * LeftX = left rail x
 * LeftY = left rail y
 * LeftZ = left rail z
 * UpX = ?
 * UpY = ?
 * UpZ = ?
 */
public class NoLimits2Node {

	private final World world;

	@Getter private Vector pos;
	@Getter private Vector front;
	@Getter private Vector left;
	@Getter private Vector up;

	public NoLimits2Node(World world) {
		this.world = world;
	}

	public NoLimits2Node convert(String rawInput) {
		String[] split = rawInput.replace("\t", ",").split(",");

		pos = new Vector(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
		front = new Vector(Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]));
		left = new Vector(Double.parseDouble(split[7]), Double.parseDouble(split[8]), Double.parseDouble(split[9]));
		up = new Vector(Double.parseDouble(split[10]), Double.parseDouble(split[11]), Double.parseDouble(split[12]));

		return this;
	}

	public Location getLocation() {
		return new Location(world, pos.getX(), pos.getY(), pos.getZ()).setDirection(front);
	}

	public double getPitch() {
		return Math.toRadians(-Math.asin(front.normalize().getY())) * 5.8f;
	}

	public double getRoll() {
		return Math.toRadians(-Math.asin(left.normalize().getY())) * 5.8f;
	}
}