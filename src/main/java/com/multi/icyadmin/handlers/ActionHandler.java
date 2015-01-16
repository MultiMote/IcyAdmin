package com.multi.icyadmin.handlers;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.ActionsEnum;
import com.multi.icyadmin.utils.FileProcessor;
import net.minecraft.entity.player.EntityPlayer;
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

    public void serverWork(String targetName, EntityPlayer player, EntityPlayer target, ActionsEnum action) {
        if (!Core.proxy.canPlayerUsePanel(player)) {
            printNoPerms(player);
            FileProcessor.appendToAdminLog(player.getCommandSenderName() + " tries to use command, but no perms.");
            return;
        }

        PlayerProps props = PlayerProps.get(player);

        if (action.getProp() != null) {
            props.toggleProp(action.getProp());
        }

        if (action.isRequiresTarget() && checkTarget(targetName, target, player, true)) return;

        if (action == ActionsEnum.HEAL) {
            target.setHealth(target.getMaxHealth());
        } else if (action == ActionsEnum.KILL) {
            target.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
        } else if (action == ActionsEnum.POISON) {
            target.addPotionEffect(new PotionEffect(Potion.poison.id, 600, 1));
        } else if (action == ActionsEnum.FEED) {
            target.getFoodStats().addStats(20, 0.8F);
        } else if (action == ActionsEnum.STARVE) {
            target.getFoodStats().setFoodLevel(1);
            target.getFoodStats().setFoodSaturationLevel(0);
        } else if (action == ActionsEnum.DISMOUNT) {
            target.mountEntity(null);
        }

        //FileProcessor.appendToAdminLog(player.getCommandSenderName() + " toggled " + action);


    }


    public boolean checkTarget(String name, EntityPlayer checkfor, EntityPlayer sender, boolean server) {
        if (name == null) {
            printChatError(sender, "Select target player!", server);
            return true;
        }
        if (name.trim().equals("")) {
            printChatError(sender, "Select target player!", server);
            return true;
        }
        if (checkfor == null && server) {
            printChatError(sender, "Player must be online!", server);
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
