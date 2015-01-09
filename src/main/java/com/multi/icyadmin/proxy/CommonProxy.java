package com.multi.icyadmin.proxy;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.handlers.EventHandlers;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class CommonProxy {
    public void clientInit() {

    }

    public EntityPlayer getMessagePlayer(MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            return ctx.getServerHandler().playerEntity;
        }
        return null;
    }

    public boolean isPlayerOp(EntityPlayer player) {
        return player.canCommandSenderUseCommand(2, "");
    }

    public boolean canPlayerUsePanel(EntityPlayer player) {
        return /*isPlayerOp(player) || */Core.dynStorage.permissed_users.contains(player.getUniqueID().toString());
    }

    public boolean canClientUsePanel() {
        return false;
    }

    public void registerHandlers() {
        EventHandlers handlers = new EventHandlers();
        MinecraftForge.EVENT_BUS.register(handlers);
        FMLCommonHandler.instance().bus().register(handlers);
    }
}
