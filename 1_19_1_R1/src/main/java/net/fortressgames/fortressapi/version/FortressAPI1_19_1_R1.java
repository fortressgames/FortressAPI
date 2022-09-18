package net.fortressgames.fortressapi.version;

import net.fortressgames.fortressapi.VersionHandler;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class FortressAPI1_19_1_R1 implements VersionHandler {

	public String getName(Entity entity) {
		return entity.cq();
	}

	public List<Entity> getPassengers(Entity entity) {
		return entity.cI();
	}

	public ClientboundSystemChatPacket actionBarGetClientboundSystemChatPacket(IChatBaseComponent iChatBaseComponent) {
		return new ClientboundSystemChatPacket(iChatBaseComponent, true);
	}

	public ClientboundSystemChatPacket clickMessageGetClientboundSystemChatPacket(IChatBaseComponent iChatBaseComponent) {
		return new ClientboundSystemChatPacket(iChatBaseComponent, false);
	}
}