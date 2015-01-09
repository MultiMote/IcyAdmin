package com.multi.icyadmin.proxy;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.handlers.ClientHandler;
import com.multi.icyadmin.handlers.KeyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;


/**
 * Created by MultiMote on 03.01.2015.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void clientInit() {
        FMLCommonHandler.instance().bus().register(new KeyHandler());
        KeyHandler.registerKeys();

    }

    @Override
    public EntityPlayer getMessagePlayer(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            return Minecraft.getMinecraft().thePlayer;
        } else if (ctx.side == Side.SERVER) {
            return ctx.getServerHandler().playerEntity;
        }

        return null;
    }

    @Override
    public boolean canClientUsePanel() {
        return Core.dynStorage.isPermissionsProvided;
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        ClientHandler handler = new ClientHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }
}
