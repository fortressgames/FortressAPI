package net.fortressgames.fortressapi.utils;

import net.fortressgames.fortressapi.PacketConnection;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CustomTeamScore {

	private final ScoreboardTeam scoreboardTeam;

	public CustomTeamScore(String teamName) {
		this.scoreboardTeam = new ScoreboardTeam(new Scoreboard(), teamName);
		this.scoreboardTeam.g().add("test0110");
	}

	public void setNameTagVisibility(NameTagVisibility nameTagVisibility) {
		this.scoreboardTeam.a(NameTagVisibility.to(nameTagVisibility));
	}

	public void setColor(ChatFormatColor color) {
		this.scoreboardTeam.a(ChatFormatColor.to(color));
	}

	public void setPrefix(String prefix) {
		this.scoreboardTeam.b(IChatBaseComponent.ChatSerializer.b(prefix));
	}

	public void setSuffix(String suffix) {
		this.scoreboardTeam.c(IChatBaseComponent.ChatSerializer.b(suffix));
	}

	public void setDeathMessageVisibility(NameTagVisibility nameTagVisibility) {
		this.scoreboardTeam.b(NameTagVisibility.to(nameTagVisibility));
	}

	public void setCollisionRule(TeamPush teamPush) {
		this.scoreboardTeam.a(TeamPush.to(teamPush));
	}

	public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
		this.scoreboardTeam.b(canSeeFriendlyInvisibles);
	}

	public void setAllowFriendlyFire(boolean allowFriendlyFire) {
		this.scoreboardTeam.a(allowFriendlyFire);
	}

	/**
	 * Get all players from the team
	 */
	public List<String> getAllPlayers() {
		List<String> list = new ArrayList<>(scoreboardTeam.g());
		list.remove("test0110");
		return list;
	}

	/**
	 * Add player to the team
	 * @param player target
	 */
	public void addPlayer(Player player) {
		if(!scoreboardTeam.g().contains(player.getName())) {
			this.scoreboardTeam.g().add(player.getName());
		}
	}

	/**
	 * Remove player from the team
	 * @param player target
	 */
	public void removePlayer(Player player) {
		this.scoreboardTeam.g().remove(player.getName());
	}

	/**
	 * Remove all players from team
	 */
	public void clearAllPlayers() {
		scoreboardTeam.g().clear();
		scoreboardTeam.g().add("test0110");
	}

	/**
	 * Send the team packet to the player
	 * @param player target
	 */
	public void sendPacketTeam(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(PacketPlayOutScoreboardTeam.a(scoreboardTeam));
		connection.sendPacket(PacketPlayOutScoreboardTeam.a(scoreboardTeam, true));
	}

	public enum NameTagVisibility {
		ALWAYS, NEVER, HIDE_FOR_OTHER_TEAMS, HIDE_FOR_OWN_TEAM;

		@Nullable
		public static ScoreboardTeamBase.EnumNameTagVisibility to(NameTagVisibility nameTagVisibility) {
			switch (nameTagVisibility) {
				case ALWAYS -> {return ScoreboardTeamBase.EnumNameTagVisibility.a;}
				case NEVER -> {return ScoreboardTeamBase.EnumNameTagVisibility.b;}
				case HIDE_FOR_OTHER_TEAMS -> {return ScoreboardTeamBase.EnumNameTagVisibility.c;}
				case HIDE_FOR_OWN_TEAM -> {return ScoreboardTeamBase.EnumNameTagVisibility.d;}
			}

			return null;
		}
	}

	public enum TeamPush {
		ALWAYS, NEVER, PUSH_OTHER_TEAMS, PUSH_OWN_TEAM;

		@Nullable
		public static ScoreboardTeamBase.EnumTeamPush to(TeamPush teamPush) {
			switch (teamPush) {
				case ALWAYS -> {return ScoreboardTeamBase.EnumTeamPush.a;}
				case NEVER -> {return ScoreboardTeamBase.EnumTeamPush.b;}
				case PUSH_OTHER_TEAMS -> {return ScoreboardTeamBase.EnumTeamPush.c;}
				case PUSH_OWN_TEAM -> {return ScoreboardTeamBase.EnumTeamPush.d;}
			}

			return null;
		}
	}

	public enum ChatFormatColor {
		BLACK,
		DARK_BLUE,
		DARK_GREEN,
		DARK_AQUA,
		DARK_RED,
		DARK_PURPLE,
		GOLD,
		GRAY,
		DARK_GRAY,
		BLUE,
		GREEN,
		AQUA,
		RED,
		LIGHT_PURPLE,
		YELLOW,
		WHITE,
		OBFUSCATED,
		BOLD,
		STRIKETHROUGH,
		UNDERLINE,
		ITALIC,
		RESET
		;

		@Nullable
		public static EnumChatFormat to(ChatFormatColor chatFormatColor) {
			switch (chatFormatColor) {
				case BLACK -> {return EnumChatFormat.a;}
				case DARK_BLUE -> {return EnumChatFormat.b;}
				case DARK_GREEN -> {return EnumChatFormat.c;}
				case DARK_AQUA -> {return EnumChatFormat.d;}
				case DARK_RED -> {return EnumChatFormat.e;}
				case DARK_PURPLE -> {return EnumChatFormat.f;}
				case GOLD -> {return EnumChatFormat.g;}
				case GRAY -> {return EnumChatFormat.h;}
				case DARK_GRAY -> {return EnumChatFormat.i;}
				case BLUE -> {return EnumChatFormat.j;}
				case GREEN -> {return EnumChatFormat.k;}
				case AQUA -> {return EnumChatFormat.l;}
				case RED -> {return EnumChatFormat.m;}
				case LIGHT_PURPLE -> {return EnumChatFormat.n;}
				case YELLOW -> {return EnumChatFormat.o;}
				case WHITE -> {return EnumChatFormat.p;}
				case OBFUSCATED -> {return EnumChatFormat.q;}
				case BOLD -> {return EnumChatFormat.r;}
				case STRIKETHROUGH -> {return EnumChatFormat.s;}
				case UNDERLINE -> {return EnumChatFormat.t;}
				case ITALIC -> {return EnumChatFormat.u;}
				case RESET -> {return EnumChatFormat.v;}
			}

			return null;
		}
	}
}