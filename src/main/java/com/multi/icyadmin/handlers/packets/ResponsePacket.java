package com.multi.icyadmin.handlers.packets;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.ItemListNode;
import com.multi.icyadmin.data.MenuElement;
import com.multi.icyadmin.data.RequestEnum;
import com.multi.icyadmin.gui.InfiPanelGui;
import com.multi.icyadmin.handlers.ResourcesReloadListener;
import com.multi.icyadmin.utils.MenuParser;
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
    boolean sendFile;
    byte[] fileBytes;

    public ResponsePacket() {
    }

    public ResponsePacket(RequestEnum response, String value, byte phase) {
        this.response = (byte) response.ordinal();
        this.value = value;
        this.phase = phase;
    }

    public ResponsePacket(RequestEnum response, String value, byte phase, String file) {
        this(response, value, phase);
        fileBytes = MenuParser.instance.serverMenuToBytes(file);
        if (fileBytes != null) sendFile = true;
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeByte(response);
        buffer.writeByte(phase);
        ByteBufUtils.writeUTF8String(buffer, value);
        buffer.writeBoolean(sendFile);
        if (sendFile) {
            buffer.writeInt(fileBytes.length); //without big files, we are sending text menus
            buffer.writeBytes(fileBytes);
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        response = buffer.readByte();
        phase = buffer.readByte();
        value = ByteBufUtils.readUTF8String(buffer);
        sendFile = buffer.readBoolean();
        if (sendFile) {
            int len = buffer.readInt();
            fileBytes = buffer.readBytes(len).array();
        }
    }

    @Override
    public IMessage onMessage(ResponsePacket message, MessageContext ctx) {
        // EntityPlayer player = Core.proxy.getMessagePlayer(ctx);
        RequestEnum req = RequestEnum.getValueById(message.response);
        //System.out.println(req + " " + message.phase);
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
        if (req == RequestEnum.REQUEST_MENU_HASH) {
            if (message.phase == 2) {
                ResourcesReloadListener.setAllowCustom(true);
                ResourcesReloadListener.setBlockReloading(false);
                String hash = MenuParser.instance.getMenuHash("custom.menu", false);
                Core.packets.sendToServer(new RequestPacket(RequestEnum.SEND_MENU_HASH, hash));
            }
            if (message.phase == 4) {
                Core.logger.warn("Server doesn't accepts custom menus, reading default menu...");
                //MenuParser.instance.removeMenu("custom.menu"); //unworking, magic
                ResourcesReloadListener.setAllowCustom(false);
                ResourcesReloadListener.update();
            }
        }
        if (req == RequestEnum.SEND_MENU) {
            if (message.phase == 2) {
                Core.logger.info("Received custom menu, writing...");
                boolean b = MenuParser.instance.bytesToClientMenu(message.fileBytes, "custom.menu");
                if (b) {
                    Core.logger.info("Successfully written received menu! Reloading...");
                    ResourcesReloadListener.setBlockReloading(false);
                    ResourcesReloadListener.setAllowCustom(true);
                    ResourcesReloadListener.update();
                    ResourcesReloadListener.setBlockReloading(true);
                } else {
                    Core.logger.error("Can't write received menu!");
                }
            }
        }
        return null;
    }


}