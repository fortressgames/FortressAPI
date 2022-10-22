package net.fortressgames.fortressapi.players;

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
	private final HashMap<Player, CustomPlayer> users = new HashMap<>();

	public static PlayerModule getInstance() {
		if(instance == null) {
			instance = new PlayerModule();
		}

		return instance;
	}

	public CustomPlayer getPlayer(Player player) {
		return this.users.get(player);
	}

	public List<Player> getOnlinePlayers() {
		return new ArrayList<>(users.keySet());
	}

	public Player getPlayer(String name) {
		Player target = null;

		for(Player pp : users.keySet()) {
			if(pp.getName().equals(name)) target = pp;
		}

		return target;
	}

	public Player getPlayer(UUID name) {
		Player target = null;

		for(Player pp : users.keySet()) {
			if(pp.getUniqueId().equals(name)) target = pp;
		}

		return target;
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage("");

		this.users.put(player, new CustomPlayer(player));
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage("");

		this.users.remove(player);
	}
}