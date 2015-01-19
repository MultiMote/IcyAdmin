package com.multi.icyadmin.handlers;

import com.mojang.authlib.GameProfile;
import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.RequestEnum;
import com.multi.icyadmin.handlers.packets.PlayerInfoPacket;
import com.multi.icyadmin.handlers.packets.ResponsePacket;
import com.multi.icyadmin.utils.FileProcessor;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MultiMote on 09.01.2015.
 */
public class CommandHandler extends CommandBase {

    @Override
    public String getCommandName() {
        return "icyadmin";
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "icyadmin add/remove <User>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length > 1) {
            if (args[0].equals("add")) {
                MinecraftServer minecraftserver = MinecraftServer.getServer();
                GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(args[1]);
                if (gameprofile == null) {
                    throw new CommandException("Can't add user, profile not exists.");
                } else {
                    if (Core.dynStorage.permissed_users.contains(gameprofile.getId().toString())) {
                        throw new CommandException("User already added.");
                    }
                    Core.dynStorage.permissed_users.add(gameprofile.getId().toString());

                    try {
                        EntityPlayer target = CommandBase.getPlayer(sender, args[1]);
                        Core.packets.sendTo(new PlayerInfoPacket(Core.proxy.canPlayerUsePanel(target)), (EntityPlayerMP) target);
                    } catch (PlayerNotFoundException ignored) {
                    }

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "User added."));
                    FileProcessor.writeUsers();
                }
            } else if (args[0].equals("rem") || args[0].equals("remove")) {
                MinecraftServer minecraftserver = MinecraftServer.getServer();
                GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(args[1]);
                if (gameprofile == null) {
                    throw new CommandException("Can't add user, profile not exists.");
                } else {
                    if (!Core.dynStorage.permissed_users.contains(gameprofile.getId().toString())) {
                        throw new CommandException("Nothing to remove.");
                    }
                    Core.dynStorage.permissed_users.remove(gameprofile.getId().toString());

                    try {
                        EntityPlayer target = CommandBase.getPlayer(sender, args[1]);
                        PlayerProps.get(target).flush();
                        Core.packets.sendTo(new ResponsePacket(RequestEnum.FLY, "", (byte)2), (EntityPlayerMP) target);
                        Core.packets.sendTo(new PlayerInfoPacket(Core.proxy.canPlayerUsePanel(target)), (EntityPlayerMP) target);
                    } catch (PlayerNotFoundException ignored) {
                    }

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "User removed."));
                    FileProcessor.writeUsers();
                }
            } else throw new WrongUsageException(getCommandUsage(sender));
        } else throw new WrongUsageException(getCommandUsage(sender));
    }

}