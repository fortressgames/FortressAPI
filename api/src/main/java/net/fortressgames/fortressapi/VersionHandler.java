package net.fortressgames.fortressapi;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface VersionHandler {

	String getName(Entity entity);

	List<Entity> getPassengers(Entity entity);

	ClientboundSystemChatPacket actionBarGetClientboundSystemChatPacket(IChatBaseComponent iChatBaseComponent);

	ClientboundSystemChatPacket clickMessageGetClientboundSystemChatPacket(IChatBaseComponent iChatBaseComponent);
}