package com.multi.icyadmin.handlers;

import com.multi.icyadmin.data.CapabilitiesEnum;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by MultiMote on 09.01.2015.
 */
public class ClientHandler {
    boolean invPrev;

    @SubscribeEvent
    public void renderNamePlate(RenderLivingEvent.Specials.Pre e) {
        if (e.entity instanceof EntityPlayer && PlayerProps.get(((EntityPlayer) e.entity)).isPropEnabled(CapabilitiesEnum.HIDE_NAME)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre e) {
        boolean inv = PlayerProps.get(e.entityPlayer).isPropEnabled(CapabilitiesEnum.INVISIBILITY);

        if (invPrev != inv) {
            try {
                ReflectionHelper.setPrivateValue(Render.class, e.renderer, inv ? 0.0F : 1.0F, "shadowOpaque", "field_76987_f");
            } catch (ReflectionHelper.UnableToAccessFieldException ex) {
                ex.printStackTrace();
            }
            invPrev = inv;
        }

        if (inv) {
            e.setCanceled(true);
        }
    }
}
