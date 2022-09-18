package net.fortressgames.fortressapi.version;

import net.fortressgames.fortressapi.VersionHandler;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class FortressAPI1_19_R1 implements VersionHandler {

	public String getName(Entity entity) {
		return entity.cr();
	}

	public final List<Entity> getPassengers(Entity entity) {
		return entity.cJ();
	}

	public ClientboundSystemChatPacket actionBarGetClientboundSystemChatPacket(IChatBaseComponent iChatBaseComponent) {
		return new ClientboundSystemChatPacket(iChatBaseComponent, 2);
	}

	public ClientboundSystemChatPacket clickMessageGetClientboundSystemChatPacket(IChatBaseComponent iChatBaseComponent) {
		return new ClientboundSystemChatPacket(iChatBaseComponent, 1);
	}
}
