package net.fortressgames.fortressapi.splines;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public class SplineNode {

	@Getter private final Location location;
	@Getter private final EulerAngle headPos;
	@Getter private final int nodeNumber;

	public SplineNode(Location location, double pitch, double roll, int nodeNumber) {
		this.location = location;
		this.headPos = new EulerAngle(
				pitch + (roll / 5.8f),
				-(roll / 5.8f) * (roll / 5.8f),
				roll
		);
		this.nodeNumber = nodeNumber;
	}
}