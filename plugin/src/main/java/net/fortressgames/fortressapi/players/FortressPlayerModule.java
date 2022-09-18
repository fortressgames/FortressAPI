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

public class FortressPlayerModule implements Listener {

	private static FortressPlayerModule instance;
	private final HashMap<Player, FortressPlayer> users = new HashMap<>();

	public static FortressPlayerModule getInstance() {
		if(instance == null) {
			instance = new FortressPlayerModule();
		}

		return instance;
	}

	public FortressPlayer getUser(Player player) {
		return this.users.get(player);
	}

	public List<FortressPlayer> getAllUsers() {
		return new ArrayList<>(users.values());
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

		this.users.put(player, new FortressPlayer(player));
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage("");

		this.users.remove(player);
	}
}