package com.multi.icyadmin.handlers;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.Defines;
import com.multi.icyadmin.gui.InfiPanelGui;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * Created by MultiMote on 02.01.2015.
 */
public class KeyHandler {

    public static final KeyBinding show_panel = new KeyBinding("key.iadmin.open", Keyboard.KEY_GRAVE, "key.categories.multiplayer");

    public static void registerKeys() {
        ClientRegistry.registerKeyBinding(show_panel);
    }


    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) return; //it cant be happened

        if (Keyboard.isKeyDown(show_panel.getKeyCode())) {
            if (Core.proxy.canClientUsePanel()) {
                mc.displayGuiScreen(new InfiPanelGui());
            } else {
                mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(Defines.MOD_ID, "other.no"), 10F, 1F, (float) mc.thePlayer.posX, (float) mc.thePlayer.posY, (float) mc.thePlayer.posZ));
            }


        }

    }
}
