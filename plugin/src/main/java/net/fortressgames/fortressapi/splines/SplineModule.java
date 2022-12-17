package net.fortressgames.fortressapi.splines;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplineModule {

	private static SplineModule instance;
	private final HashMap<String, List<SplineNode>> splines = new HashMap<>();

	public static SplineModule getInstance() {
		if (instance == null) {
			instance = new SplineModule();
		}
		return instance;
	}

	public List<String> getAll() {
		return new ArrayList<>(splines.keySet());
	}

	public List<SplineNode> getSpline(String spline) {
		return this.splines.get(spline);
	}

	//TODO
	// offSetLocation does nothing
	// default world only, each spline needs their world

	@SneakyThrows
	public void load(File file, String splineName, Location offSetLocation, World world) {
		List<NoLimits2Node> nl2Nodes = new ArrayList<>();

		// Convert all values into NL2 classes
		for(String rawPoint : FileUtils.readLines(file, Charset.defaultCharset())) {
			if(rawPoint.contains("FrontX")) continue;

			nl2Nodes.add(new NoLimits2Node(world).convert(rawPoint));
		}

		List<SplineNode> splineNodes = new ArrayList<>();

		nl2Nodes.forEach(noLimits2Node -> splineNodes.add(new SplineNode(
				noLimits2Node.getLocation(),
				noLimits2Node.getPitch(),
				noLimits2Node.getRoll(),
				splineNodes.size()
		)));

		splines.put(splineName, splineNodes);
	}
}