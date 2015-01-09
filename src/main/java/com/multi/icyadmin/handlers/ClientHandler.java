package com.multi.icyadmin.handlers;

import com.multi.icyadmin.data.CapabilitiesEnum;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by MultiMote on 09.01.2015.
 */
public class ClientHandler {
    @SubscribeEvent
    public void renderNamePlate(RenderLivingEvent.Specials.Pre e) {
        if (e.entity instanceof EntityPlayer && PlayerProps.get(((EntityPlayer) e.entity)).isPropEnabled(CapabilitiesEnum.HIDE_NAME)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre e) {
        if (PlayerProps.get(e.entityPlayer).isPropEnabled(CapabilitiesEnum.INVINCIBILITY)) {
            e.setCanceled(true);
        }
    }
}
