package net.fortressgames.fortressapi;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import lombok.SneakyThrows;
import net.fortressgames.fortressapi.commands.CommandModule;
import net.fortressgames.fortressapi.commands.cmd.SplineCommand;
import net.fortressgames.fortressapi.listeners.ClickEntityListener;
import net.fortressgames.fortressapi.listeners.CloseInventoryListener;
import net.fortressgames.fortressapi.listeners.InventoryClickListener;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.fortressgames.fortressapi.splines.SplineModule;
import net.fortressgames.fortressapi.tasks.PlayerMoveTask;
import net.fortressgames.fortressapi.utils.ConsoleMessage;
import net.fortressgames.fortressapi.version.FortressAPI1_19_1_R1;
import net.fortressgames.fortressapi.version.FortressAPI1_19_R1;
import org.bukkit.Bukkit;
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
	@SneakyThrows
	public void onLoad() {
		// Create Default folder
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		saveConfig();

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

		switch(Bukkit.getVersion().split("\\(MC: ")[1].replace(")", "")) {
			case "1.19.2", "1.19.1" -> versionHandler = versionHandlers.get(0);
			case "1.19" -> versionHandler = versionHandlers.get(1);
		}

		ClickEntityListener entityListener = new ClickEntityListener();
		this.getServer().getPluginManager().registerEvents(entityListener, this);
		ProtocolLibrary.getProtocolManager().addPacketListener(entityListener);

		// Load splines
		for(File file : new File(getDataFolder() + "/Splines").listFiles()) {
			String name = file.getName().replace(".csv", "");
			if(getConfig().contains(name)) {
				SplineModule.getInstance().load(file, name, Bukkit.getWorld(getConfig().getString(name)));
			}
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