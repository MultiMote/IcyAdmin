package com.multi.icyadmin.handlers;

import com.multi.adapter.BukkitP;
import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.ActionsEnum;
import com.multi.icyadmin.data.RequestEnum;
import com.multi.icyadmin.handlers.packets.ResponsePacket;
import com.multi.icyadmin.utils.FileProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;

/**
 * Created by MultiMote on 08.01.2015.
 */
public class ActionHandler {

    public boolean clientCheck(String targetName, EntityPlayer player, EntityPlayer target, ActionsEnum action) {
        return !(action.isRequiresTarget() && checkTarget(targetName, target, player, false));
    }

    public void serverWork(String targetName, EntityPlayerMP player, EntityPlayerMP target, ActionsEnum action) {
        if (!Core.proxy.canPlayerUsePanel(player)) {
            printNoPerms(player);
            FileProcessor.appendToAdminLog(player.getCommandSenderName() + " tries to use command, but no perms.");
            return;
        }

        PlayerProps props = PlayerProps.get(player);


        if (action.getProp() != null) {
            props.toggleProp(action.getProp());
        }else if (action == ActionsEnum.FLY_TOGGLE) {
            player.capabilities.allowFlying = !player.capabilities.allowFlying;
            Core.packets.sendTo(new ResponsePacket(RequestEnum.FLY, "", player.capabilities.allowFlying ? (byte)3 : (byte)2), player);
        }

        if (action.isRequiresTarget() && checkTarget(targetName, target, player, true)) return;
        PlayerProps propsTarget;
        if (target == null) return;
        propsTarget = PlayerProps.get(target);

        if (action == ActionsEnum.HEAL) {
            target.setHealth(target.getMaxHealth());
        } else if (action == ActionsEnum.KILL) {
            if(BukkitP.isBukkit())
                BukkitP.killPlayer(target.getUniqueID());
            else
                target.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
        } else if (action == ActionsEnum.POISON) {
            target.addPotionEffect(new PotionEffect(Potion.poison.id, 600, 1));
        } else if (action == ActionsEnum.FEED) {
            if(BukkitP.isBukkit())
                BukkitP.feedPlayer(player.getUniqueID(), false);
            else
                target.getFoodStats().addStats(20, 0.8F);
        } else if (action == ActionsEnum.STARVE) {
            if(BukkitP.isBukkit())
                BukkitP.feedPlayer(player.getUniqueID(), true);
            else {
                target.getFoodStats().setFoodLevel(1);
                target.getFoodStats().setFoodSaturationLevel(0);
            }
        } else if (action == ActionsEnum.DISMOUNT) {
            target.mountEntity(null);
        } else if (action == ActionsEnum.IGNITE) {
            target.setFire(8);
        } else if (action == ActionsEnum.CLEAR_INVENTORY) {
            target.inventory.clearInventory(null, -1);
            target.inventoryContainer.detectAndSendChanges();

            if (!target.capabilities.isCreativeMode) {
                target.updateHeldItem();
            }
        } else if (action == ActionsEnum.UNPOTION) {
            target.clearActivePotions();
        } else if (action == ActionsEnum.FREEZE) {
            propsTarget.freeze();
        } else if (action == ActionsEnum.UNFREEZE) {
            propsTarget.unFreeze();
        }

        //FileProcessor.appendToAdminLog(player.getCommandSenderName() + " toggled " + action);


    }


    public boolean checkTarget(String name, EntityPlayer checkfor, EntityPlayer sender, boolean server) {
        if (name == null) {
            printChatError(sender, StatCollector.translateToLocal("icyadmin.notarget"), server);
            return true;
        }
        if (name.trim().equals("")) {
            printChatError(sender, StatCollector.translateToLocal("icyadmin.notarget"), server);
            return true;
        }
        if (checkfor == null && server) {
            printChatError(sender, "Player must be online!", true);
            return true;
        }
        return false;
    }

    public void printChatError(EntityPlayer player, String msg, boolean server) {
        player.addChatMessage(new ChatComponentText("[IcyErr" + (server ? "Server" : "Client") + "] " + msg).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

    public void printNoPerms(EntityPlayer player) {
        player.addChatMessage(new ChatComponentTranslation("icyadmin.permissions").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }
}
