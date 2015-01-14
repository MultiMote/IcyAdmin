package com.multi.icyadmin.handlers.packets;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.ItemListNode;
import com.multi.icyadmin.data.MenuElement;
import com.multi.icyadmin.data.RequestEnum;
import com.multi.icyadmin.gui.InfiPanelGui;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * Created by MultiMote on 02.01.2015.
 */
public class ResponsePacket implements IMessage, IMessageHandler<ResponsePacket, IMessage> {


    byte response;
    byte phase;
    String value;

    public ResponsePacket() {
    }

    public ResponsePacket(RequestEnum response, String value, byte phase) {
        this.response = (byte) response.ordinal();
        this.value = value;
        this.phase = phase;
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeByte(response);
        buffer.writeByte(phase);
        ByteBufUtils.writeUTF8String(buffer, value);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        response = buffer.readByte();
        phase = buffer.readByte();
        value = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public IMessage onMessage(ResponsePacket message, MessageContext ctx) {
        // EntityPlayer player = Core.proxy.getMessagePlayer(ctx);
        RequestEnum req = RequestEnum.getValueById(message.response);

        if (req == RequestEnum.ADMIN_LOGS || req == RequestEnum.DEATH_LOGS) {
            GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
            if (guiScreen instanceof InfiPanelGui) {
                if (message.phase == 1) ((InfiPanelGui) guiScreen).flushIncludes(req);
                if (message.phase == 2) {
                    Core.dynStorage.menus.add(MenuElement.create("$INC_" + req, ItemListNode.title(message.value)));
                }
                if (message.phase == 3) ((InfiPanelGui) guiScreen).reloadMenu(false);
            }
        }
        return null;
    }


}