package net.fortressgames.fortressapi.splines;

import lombok.Getter;
import org.bukkit.Location;

public record Point(@Getter Location location, @Getter double pitch, @Getter double roll,
					@Getter int pointNumber, @Getter int speed) {
}