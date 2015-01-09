package com.multi.icyadmin.handlers.packets;

import com.multi.icyadmin.Core;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by MultiMote on 02.01.2015.
 */
public class PlayerInfoPacket implements IMessage, IMessageHandler<PlayerInfoPacket, IMessage> {

    boolean hasPerms;


    public PlayerInfoPacket() {
    }

    public PlayerInfoPacket(boolean hasPerms) {
        this.hasPerms = hasPerms;
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeBoolean(hasPerms);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        hasPerms = buffer.readBoolean();
    }

    @Override
    public IMessage onMessage(PlayerInfoPacket message, MessageContext ctx) {
        EntityPlayer player = Core.proxy.getMessagePlayer(ctx);
        Core.dynStorage.isPermissionsProvided = message.hasPerms;
        return null;
    }


}