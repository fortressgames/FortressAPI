package net.fortressgames.fortressapi.entities;

import lombok.Getter;
import org.bukkit.OfflinePlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Get a skin of a player, used for NPC's
 */
public class SkinCatch {

	@Getter	private String value;
	@Getter private String signature;

	@Getter private final OfflinePlayer player;

	public SkinCatch(String value, String signature, OfflinePlayer skinID) {
		this.value = value;
		this.signature = signature;
		this.player = skinID;
	}

	public SkinCatch(OfflinePlayer skinID) {
		this.player = skinID;

		String urlContents = getUrlContents("https://sessionserver.mojang.com/session/minecraft/profile/" +
				skinID.getUniqueId().toString().replaceAll("-", "") + "?unsigned=false").replace(" ", "");

		try {
			this.value = urlContents.split("\"value\":\"")[1].split("\",")[0];
			this.signature = urlContents.split("\"signature\":\"")[1].split("\"")[0];

		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("COUNT NOT FIND PLAYER FOR SKIN");
		}
	}

	private String getUrlContents(String spec) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(spec).openConnection().getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			bufferedReader.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return stringBuilder.toString();
	}
}