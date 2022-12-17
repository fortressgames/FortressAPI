package net.fortressgames.fortressapi.commands.cmd;

import net.fortressgames.fortressapi.Lang;
import net.fortressgames.fortressapi.commands.CommandBase;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import net.fortressgames.fortressapi.splines.SplineModule;
import net.fortressgames.fortressapi.splines.SplineNode;
import net.fortressgames.fortressapi.utils.HexColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SplineCommand extends CommandBase {

	public SplineCommand() {
		super("spline", "fortressapi.command.spline");
	}

	private final HashMap<Player, List<CustomArmorstand>> armorstands = new HashMap<>();

	@Override
	public void execute(CommandSender sender, String[] args) {

		if(sender instanceof Player player) {

			if(args.length == 0) {
				player.sendMessage(Lang.LINE);
				player.sendMessage(Lang.BLUE + "Usage: /spline view <splineName> <space> " + ChatColor.GRAY + "- Load the spline path to view");
				player.sendMessage(Lang.BLUE + "Usage: /spline unview <splineName> " + ChatColor.GRAY + "- Unload the spline path from view");

				player.sendMessage(Lang.LINE);
				player.sendMessage(HexColor.TEAL + "Splines: " +
						ChatColor.GRAY + "(" + ChatColor.RED + SplineModule.getInstance().getAll().size() + ChatColor.GRAY + ")");

				for(String spline : SplineModule.getInstance().getAll()) {
					player.sendMessage(HexColor.DARK_TURQUOISE + spline);
				}
				player.sendMessage(Lang.LINE);
				return;
			}

			//
			// VIEW
			//
			if(args[0].equalsIgnoreCase("view") && args.length >= 2) {
				if(SplineModule.getInstance().getSpline(args[1]) == null) {
					player.sendMessage(Lang.RED + "Unknown spline!");
					return;
				}

				List<SplineNode> points = SplineModule.getInstance().getSpline(args[1]);
				int baseSpawnValue = 50;

				// Check if player has input a value
				if(args.length == 3) {
					baseSpawnValue = Integer.parseInt(args[2]);
				}

				// Load armorstands
				int space = baseSpawnValue;
				armorstands.put(player, new ArrayList<>());

				for(int i = 0; i < points.size(); i++) {

					// Space skipping
					if(space == 0) {
						space = baseSpawnValue;
					} else {
						space--;
						continue;
					}

					CustomArmorstand armorstand = new CustomArmorstand(points.get(i).getLocation())
							.setCustomName("SPLINE: " + i + " / " + args[1])
							.setCustomNameVisible(true)
							.setBasePlate(false)
							.setHelmet(new ItemStack(Material.OBSERVER))
							.setHeadPose(points.get(i).getHeadPos());

					armorstand.spawn(player);
					armorstands.get(player).add(armorstand);
				}
				return;
			}

			if(args[0].equalsIgnoreCase("unview") && args.length >= 2) {
				if(SplineModule.getInstance().getSpline(args[1]) == null) {
					player.sendMessage(Lang.RED + "Unknown spline!");
					return;
				}

				if(armorstands.containsKey(player)) {

					for(CustomArmorstand customArmorstand : armorstands.get(sender)) {
						customArmorstand.remove(player);
					}

					armorstands.remove(player);
				}
				return;
			}
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if(args.length == 1) {
			return Arrays.asList("view", "unview");
		}

		if(args.length == 2) {
			return SplineModule.getInstance().getAll();
		}

		return super.tabComplete(sender, alias, args);
	}
}