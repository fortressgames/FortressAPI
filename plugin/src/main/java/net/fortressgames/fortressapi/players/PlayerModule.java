package net.fortressgames.fortressapi.players;

import lombok.Getter;
import net.fortressgames.fortressapi.events.PlayerMoveTaskEvent;
import net.fortressgames.fortressapi.gui.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerModule implements Listener {

	private static PlayerModule instance;
	@Getter private final HashMap<Player, InventoryMenu> openMenu = new HashMap<>();

	public static PlayerModule getInstance() {
		if(instance == null) {
			instance = new PlayerModule();
		}

		return instance;
	}

	public List<Player> getOnlinePlayers() {
		return new ArrayList<>(openMenu.keySet());
	}

	public Player getPlayer(String name) {
		Player target = null;

		for(Player pp : openMenu.keySet()) {
			if(pp.getName().equals(name)) target = pp;
		}

		return target;
	}

	public Player getPlayer(UUID name) {
		Player target = null;

		for(Player pp : openMenu.keySet()) {
			if(pp.getUniqueId().equals(name)) target = pp;
		}

		return target;
	}

	public void addPlayer(Player player) {
		this.openMenu.put(player, null);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		addPlayer(player);

		Bukkit.getPluginManager().callEvent(new PlayerMoveTaskEvent(player));
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		this.openMenu.remove(e.getPlayer());
	}
}