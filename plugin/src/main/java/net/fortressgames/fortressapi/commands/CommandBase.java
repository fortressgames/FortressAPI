package net.fortressgames.fortressapi.commands;

import net.fortressgames.fortressapi.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class CommandBase extends Command {

	public CommandBase(String cmd, String permission, String... aliases) {
		super(cmd);
		this.setPermission(permission);
		this.setAliases(Arrays.asList(aliases));
		this.setPermissionMessage(Lang.NO_PERMISSION);
	}

	@Override
	public final boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
		if(!testPermission(sender)) return false;

		execute(sender, args);
		return true;
	}

	public abstract void execute(CommandSender sender, String[] args);

	@Override
	@NotNull
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
		return super.tabComplete(sender, alias, args);
	}
}