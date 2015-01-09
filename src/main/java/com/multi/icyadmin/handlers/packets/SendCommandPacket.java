package com.multi.icyadmin.handlers.packets;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.NodeActionsEnum;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandBase;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by MultiMote on 08.01.2015.
 */
public class SendCommandPacket implements IMessage, IMessageHandler<SendCommandPacket, IMessage> {

    int action;
    String target;


    public SendCommandPacket() {
    }

    public SendCommandPacket(String target, NodeActionsEnum action) {
        this.target = target == null ? "" : target;
        this.action = action.ordinal();
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(action);
        ByteBufUtils.writeUTF8String(buffer, target);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        action = buffer.readInt();
        target = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public IMessage onMessage(SendCommandPacket message, MessageContext ctx) {
        EntityPlayer player = Core.proxy.getMessagePlayer(ctx);
        EntityPlayer target = null;
        try {
            target = CommandBase.getPlayer(player, message.target);
        } catch (PlayerNotFoundException ignored) {
        }
        NodeActionsEnum action = NodeActionsEnum.getValueById(message.action);
        Core.packets.sendTo(new PlayerInfoPacket(Core.proxy.canPlayerUsePanel(player)), (EntityPlayerMP) player);
        Core.actionHandler.serverWork(message.target, player, target, action);
        return null;
    }


}


