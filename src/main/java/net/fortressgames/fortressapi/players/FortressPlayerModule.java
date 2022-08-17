package net.fortressgames.fortressapi.players;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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