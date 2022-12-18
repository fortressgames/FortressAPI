package net.fortressgames.fortressapi;

import lombok.Getter;
import net.fortressgames.fortressapi.commands.CommandModule;
import net.fortressgames.fortressapi.commands.cmd.SplineCommand;
import net.fortressgames.fortressapi.listeners.CloseInventoryListener;
import net.fortressgames.fortressapi.listeners.InventoryClickListener;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.fortressgames.fortressapi.splines.SplineModule;
import net.fortressgames.fortressapi.tasks.PlayerMoveTask;
import net.fortressgames.fortressapi.utils.ConsoleMessage;
import net.fortressgames.fortressapi.version.FortressAPI1_19_1_R1;
import net.fortressgames.fortressapi.version.FortressAPI1_19_R1;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FortressAPI extends JavaPlugin {

	@Getter private static FortressAPI instance;
	@Getter private VersionHandler versionHandler;
	private final List<VersionHandler> versionHandlers = new ArrayList<>() {
		{
			add(new FortressAPI1_19_1_R1());
			add(new FortressAPI1_19_R1());
		}
	};

	/**
	 * Called when plugin first loads by spigot and is called before onEnable
	 */
	@Override
	public void onLoad() {
		// Create Default folder
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		// Create spline folder
		if(!new File(getDataFolder() + "/Splines").exists()) {
			new File(getDataFolder() + "/Splines").mkdir();
		}
	}

	/**
	 * Called when the plugin is first loaded by Spigot
	 */
	@Override
	public void onEnable() {
		instance = this;

		switch(getServer().getClass().getPackage().getName().split("\\.")[3]) {
			case "v1_19_1_R1" -> versionHandler = versionHandlers.get(0);
			case "v1_19_R1" -> versionHandler = versionHandlers.get(1);
		}

		// Load splines
		for(File file : new File(getDataFolder() + "/Splines").listFiles()) {
			//TODO default world only, each spline needs their world
			SplineModule.getInstance().load(file, file.getName().replace(".csv", ""), Bukkit.getWorlds().get(0));
		}

		// Register events
		this.getServer().getPluginManager().registerEvents(PlayerModule.getInstance(), this);

		this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		this.getServer().getPluginManager().registerEvents(new CloseInventoryListener(), this);

		// Adds players after reload
		for(Player pp : Bukkit.getOnlinePlayers()) {
			PlayerModule.getInstance().addPlayer(pp);
		}

		CommandModule.registerCommand(new SplineCommand());

		new PlayerMoveTask().runTaskTimer(this, TimeUnit.MILLISECONDS, 5);

		getLogger().info(ConsoleMessage.GREEN + "Version: " + getDescription().getVersion() + " Enabled!" + ConsoleMessage.RESET);
	}

	/**
	 * Called when the server is restarted or stopped
	 */
	@Override
	public void onDisable() {
		getLogger().info(ConsoleMessage.RED + "Version: " + getDescription().getVersion() + " Disabled!" + ConsoleMessage.RESET);
	}
}