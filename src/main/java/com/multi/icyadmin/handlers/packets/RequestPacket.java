package com.multi.icyadmin.handlers.packets;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.IncludesEnum;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by MultiMote on 02.01.2015.
 */
public class RequestPacket implements IMessage, IMessageHandler<RequestPacket, IMessage> {

    byte request;

    public RequestPacket() {
    }

    public RequestPacket(IncludesEnum request) {
        this.request = (byte) request.ordinal();
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeByte(request);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        request = buffer.readByte();
    }

    @Override
    public IMessage onMessage(RequestPacket message, MessageContext ctx) {
        EntityPlayer player = Core.proxy.getMessagePlayer(ctx);
        IncludesEnum req = IncludesEnum.getValueById(message.request);
        Core.packets.sendTo(new ResponsePacket(req, "", (byte) 1), ((EntityPlayerMP) player));
        if (Core.proxy.canPlayerUsePanel(player)) {
            if (req == IncludesEnum.ADMIN_LOGS) {
                if (Core.dynStorage.admin_logs.isEmpty()) {
                    Core.packets.sendTo(new ResponsePacket(req, "Nothing here.", (byte) 2), ((EntityPlayerMP) player));
                } else {
                    for (String s : Core.dynStorage.admin_logs) {
                        Core.packets.sendTo(new ResponsePacket(req, s, (byte) 2), ((EntityPlayerMP) player));
                    }
                }
            }
        } else Core.packets.sendTo(new ResponsePacket(req, "Fuck you.", (byte) 2), ((EntityPlayerMP) player)); //todo
        Core.packets.sendTo(new ResponsePacket(req, "", (byte) 3), ((EntityPlayerMP) player));
        return null;
    }


}