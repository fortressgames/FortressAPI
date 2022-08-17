package net.fortressgames.fortressapi.commands;

import net.fortressgames.fortressapi.FortressAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class CommandModule {

	private static CommandMap commandMap;

	public static void registerCommand(CommandBase command) {
		getCommandMap().register(FortressAPI.getInstance().getName(), command);
	}

	private static CommandMap getCommandMap() {
		if(commandMap == null) {
			try {
				if(Bukkit.getPluginManager() instanceof SimplePluginManager) {
					Field f = SimplePluginManager.class.getDeclaredField("commandMap");
					f.setAccessible(true);

					commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
				}
			} catch (Exception e) {
				e.printStackTrace();
				commandMap = null;
			}
		}
		return commandMap;
	}
}