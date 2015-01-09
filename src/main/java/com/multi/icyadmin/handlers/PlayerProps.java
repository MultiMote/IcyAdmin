package com.multi.icyadmin.handlers;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.Defines;
import com.multi.icyadmin.data.CapabilitiesEnum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.List;

/**
 * Created by MultiMote on 09.01.2015.
 */
public class PlayerProps implements IExtendedEntityProperties {

    public static final String ID = Defines.MOD_ID;
    private static final int PROPS_ID = 27;
    private final EntityPlayer player;

    private int entityCleanTimer;

    public PlayerProps(EntityPlayer player) {
        this.player = player;
        this.player.getDataWatcher().addObject(PROPS_ID, 0);
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(ID, new PlayerProps(player));
    }

    public static PlayerProps get(EntityPlayer player) {
        return (PlayerProps) player.getExtendedProperties(ID);
    }

    public int getProps() {
        return player.getDataWatcher().getWatchableObjectInt(PROPS_ID);
    }

    public void setProps(int bytes) {
        player.getDataWatcher().updateObject(PROPS_ID, bytes);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {

    }

    @Override
    public void init(Entity entity, World world) {

    }

    public void enableProp(CapabilitiesEnum prop) {
        setProps(getProps() | prop.getCode());
    }

    public void toggleProp(CapabilitiesEnum prop) {
        setProps(getProps() ^ prop.getCode());
        boolean whatnow = isPropEnabled(prop);
        Core.logger.info(player.getCommandSenderName() + " toggled " + prop + " " + (whatnow ? "on." : "off."));
        if (whatnow) {
            player.addChatMessage(new ChatComponentTranslation("icyadmin.turned.on", prop.toString()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        } else {
            player.addChatMessage(new ChatComponentTranslation("icyadmin.turned.off", prop.toString()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        }

    }

    public void disableProp(CapabilitiesEnum prop) {
        setProps(getProps() & ~prop.getCode());
    }

    public boolean isPropEnabled(CapabilitiesEnum prop) {
        return (getProps() & prop.getCode()) > 0;
    }

    public void tick() {
        if (entityCleanTimer > 0) entityCleanTimer--;

        if (isPropEnabled(CapabilitiesEnum.REMOVE_HOSTILES) && entityCleanTimer <= 0) {
            entityCleanTimer = 40;
            List entities = player.worldObj.getEntitiesWithinAABB(EntityMob.class, player.boundingBox.expand(5, 5, 5));
            for (Object obj : entities) {
                ((EntityMob) obj).setDead();
            }
        }


    }


}
