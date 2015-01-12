package com.multi.icyadmin.handlers;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.CapabilitiesEnum;
import com.multi.icyadmin.handlers.packets.PlayerInfoPacket;
import com.multi.icyadmin.utils.FileProcessor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class EventHandlers {

   /* @SubscribeEvent
    public void resourcesReloaded(TextureStitchEvent event) {
        System.out.println(">>>>>>>>>>>>>>>>>> stitch");
    }*/

    @SubscribeEvent
    public void getAttacked(LivingAttackEvent event) {
        if (event.entity instanceof EntityPlayer) {
            if (PlayerProps.get((EntityPlayer) event.entity).isPropEnabled(CapabilitiesEnum.INVULNERABILITY)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.entity instanceof EntityPlayer) {
            FileProcessor.appendToDeathLog(((EntityPlayer) event.entity).func_110142_aN().func_151521_b().getFormattedText().replaceAll("§r", ""));
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Core.packets.sendTo(new PlayerInfoPacket(Core.proxy.canPlayerUsePanel(event.player)), (EntityPlayerMP) event.player);
    }


    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            if (PlayerProps.get((EntityPlayer) event.entity) == null) {
                EntityPlayer player = (EntityPlayer) event.entity;
                PlayerProps.register(player);
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerProps.get(event.player).tick();
        }
    }

    @SubscribeEvent
    public void useTick(PlayerUseItemEvent.Tick event) {
        if (event.item.getItem() == Items.bow && PlayerProps.get(event.entityPlayer).isPropEnabled(CapabilitiesEnum.FAST_SHOOTING)) {
            if (event.duration < event.item.getItem().getMaxItemUseDuration(event.item)) {
                event.entityPlayer.stopUsingItem();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void arrowLoose(ArrowLooseEvent event) {
        if (PlayerProps.get(event.entityPlayer).isPropEnabled(CapabilitiesEnum.FAST_SHOOTING)) {
            event.charge = 30;
        }

    }

}
