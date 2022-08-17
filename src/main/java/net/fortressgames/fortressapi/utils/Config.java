package net.fortressgames.fortressapi.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import net.fortressgames.fortressapi.FortressAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

	@Getter private final File file;
	@Getter private final YamlConfiguration config;

	@SneakyThrows
	public Config(String name) {
		this.file = new File(FortressAPI.getInstance().getDataFolder() + "/" + name + ".yml");
		if(!file.exists()) file.createNewFile();

		this.config = YamlConfiguration.loadConfiguration(file);
	}

	@SneakyThrows
	public Config(File file) {
		this.file = file;
		if(!file.exists()) file.createNewFile();

		this.config = YamlConfiguration.loadConfiguration(file);
	}

	@SneakyThrows
	public void save() {
		this.config.save(this.file);
	}
}