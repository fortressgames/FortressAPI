package net.fortressgames.fortressapi;

import lombok.Getter;
import net.fortressgames.fortressapi.listeners.CloseInventoryListener;
import net.fortressgames.fortressapi.listeners.InventoryClickListener;
import net.fortressgames.fortressapi.players.FortressPlayerModule;
import net.fortressgames.fortressapi.utils.ConsoleMessage;
import net.fortressgames.fortressapi.version.FortressAPI1_19_1_R1;
import net.fortressgames.fortressapi.version.FortressAPI1_19_R1;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class FortressAPI extends JavaPlugin {

	@Getter private static FortressAPI instance;
	@Getter private VersionHandler versionHandler;
	private final List<VersionHandler> versionHandlers = new ArrayList<VersionHandler>() {
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
		this.getServer().getPluginManager().registerEvents(FortressPlayerModule.getInstance(), this);

		this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		this.getServer().getPluginManager().registerEvents(new CloseInventoryListener(), this);

		// Trigger join event again for online players this will only trigger if the server has been reloaded
		// and players are still online. This is so the user.class data is triggers for online players after reload.
		for(Player player : Bukkit.getOnlinePlayers()) {
			Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player, ""));
		}

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