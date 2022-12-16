package net.fortressgames.fortressapi.splines;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplineModule {

	private static SplineModule instance;
	private final HashMap<String, List<Point>> splines = new HashMap<>();

	public static SplineModule getInstance() {
		if (instance == null) {
			instance = new SplineModule();
		}
		return instance;
	}

	public List<String> getAll() {
		return new ArrayList<>(splines.keySet());
	}

	public List<Point> getSpline(String spline) {
		return this.splines.get(spline);
	}

	//TODO
	// offSetLocation does nothing
	@SneakyThrows
	public void load(File file, String splineName, Location offSetLocation) {
		List<Point> points = new ArrayList<>();

		for(String rawPoint : FileUtils.readLines(file, Charset.defaultCharset())) {
			points.add(loadPoint(rawPoint, offSetLocation, points.size()));
		}

		splines.put(splineName, points);
	}

	private Point loadPoint(String rawPoint, Location offSetLocation, int currentPoint) {
		//TODO
		// This needs to be updated so each spline has its own world link.
		// Right now splines only load in the default world
		World defaultWorld = Bukkit.getWorlds().get(0);
		String[] split = rawPoint.replace("\t", ",").split(",");

		Vector vector = new Vector(Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]));
		Location vectorRoll = new Vector(Double.parseDouble(split[7]), Double.parseDouble(split[8]), Double.parseDouble(split[9])).toLocation(defaultWorld);
		Location vectorPitch = new Vector(Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6])).toLocation(defaultWorld);

		double xzLength = Math.sqrt(vectorRoll.getX()*vectorRoll.getX() + vectorRoll.getZ()*vectorRoll.getZ());
		double roll = Math.atan2(xzLength, vectorRoll.getY()) - Math.PI / 2;

		double pitch = Math.atan2(xzLength, vectorPitch.getY()) - Math.PI / 2;

		//TODO
		// Speed is default to 0 splines have no information on speed
		return new Point(
				new Location(
						Bukkit.getWorlds().get(0),
						offSetLocation.getX() + Double.parseDouble(split[1]),
						offSetLocation.getY() + Double.parseDouble(split[2]),
						offSetLocation.getZ() + Double.parseDouble(split[3])
				).setDirection(vector),

				pitch,
				roll,
				currentPoint,
				0);
	}
}