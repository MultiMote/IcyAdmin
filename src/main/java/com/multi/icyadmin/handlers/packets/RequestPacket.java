package com.multi.icyadmin.handlers.packets;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.RequestEnum;
import com.multi.icyadmin.utils.FileProcessor;
import cpw.mods.fml.common.network.ByteBufUtils;
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
    String customData;

    public RequestPacket() {
    }

    public RequestPacket(RequestEnum request, String customData) {
        this.request = (byte) request.ordinal();
        this.customData = customData;
    }

    public RequestPacket(RequestEnum request) {
        this(request, "");
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeByte(request);
        ByteBufUtils.writeUTF8String(buffer, customData);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        request = buffer.readByte();
        customData = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public IMessage onMessage(RequestPacket message, MessageContext ctx) {
        EntityPlayer player = Core.proxy.getMessagePlayer(ctx);
        RequestEnum req = RequestEnum.getValueById(message.request);
        Core.packets.sendTo(new ResponsePacket(req, "", (byte) 1), ((EntityPlayerMP) player));
        if (Core.proxy.canPlayerUsePanel(player)) {
            if (req == RequestEnum.ADMIN_LOGS) {
                if (Core.dynStorage.admin_logs.isEmpty()) {
                    Core.packets.sendTo(new ResponsePacket(req, "Nothing here.", (byte) 2), ((EntityPlayerMP) player));
                } else {
                    for (String s : Core.dynStorage.admin_logs) {
                        Core.packets.sendTo(new ResponsePacket(req, s, (byte) 2), ((EntityPlayerMP) player));
                    }
                }
            } else if (req == RequestEnum.DEATH_LOGS) {
                if (Core.dynStorage.last_deads.isEmpty()) {
                    Core.packets.sendTo(new ResponsePacket(req, "Nothing here.", (byte) 2), ((EntityPlayerMP) player));
                } else {
                    for (String s : Core.dynStorage.last_deads) {
                        Core.packets.sendTo(new ResponsePacket(req, s, (byte) 2), ((EntityPlayerMP) player));
                    }
                }
            } else if (req == RequestEnum.SEND_ACTION) {
                FileProcessor.appendToAdminLog(player.getCommandSenderName() + " " + message.customData);
            }
        } else Core.packets.sendTo(new ResponsePacket(req, "Fuck you.", (byte) 2), ((EntityPlayerMP) player)); //todo
        Core.packets.sendTo(new ResponsePacket(req, "", (byte) 3), ((EntityPlayerMP) player));
        return null;
    }


}