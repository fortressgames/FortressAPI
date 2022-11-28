package net.fortressgames.fortressapi;

import lombok.Getter;
import net.fortressgames.fortressapi.listeners.CloseInventoryListener;
import net.fortressgames.fortressapi.listeners.InventoryClickListener;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.fortressgames.fortressapi.tasks.PlayerMoveTask;
import net.fortressgames.fortressapi.utils.ConsoleMessage;
import net.fortressgames.fortressapi.version.FortressAPI1_19_1_R1;
import net.fortressgames.fortressapi.version.FortressAPI1_19_R1;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
	 * Called when the plugin is first loaded by Spigot
	 */
	@Override
	public void onEnable() {
		instance = this;

		switch(getServer().getClass().getPackage().getName().split("\\.")[3]) {
			case "v1_19_1_R1" -> versionHandler = versionHandlers.get(0);
			case "v1_19_R1" -> versionHandler = versionHandlers.get(1);
		}

		// Register events
		this.getServer().getPluginManager().registerEvents(PlayerModule.getInstance(), this);

		this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		this.getServer().getPluginManager().registerEvents(new CloseInventoryListener(), this);

		// Adds players after reload
		for(Player pp : Bukkit.getOnlinePlayers()) {
			PlayerModule.getInstance().addPlayer(pp);
		}

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