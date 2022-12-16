package net.fortressgames.fortressapi.tasks;

import net.fortressgames.fortressapi.FortressRunnable;
import net.fortressgames.fortressapi.events.PlayerMoveTaskEvent;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.Bukkit;

public class PlayerMoveTask extends FortressRunnable {

	@Override
	public void run() {
		PlayerModule.getInstance().getOnlinePlayers().forEach(player -> Bukkit.getPluginManager().callEvent(new PlayerMoveTaskEvent(player)));
	}
}