package com.multi.icyadmin.handlers;

import com.multi.icyadmin.utils.MenuParser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

/**
 * Created by MultiMote on 07.01.2015.
 */
public class ResourcesReloadListener implements IResourceManagerReloadListener {
    private static boolean allowCustom = true;
    private static boolean blockReloading;

    public static void update() {
        if (blockReloading) return;
        if (!allowCustom || !MenuParser.instance.checkAndParseCustom()) {
            MenuParser.instance.parseMenu(I18n.format("icyadmin.menufile"), false);
        }
    }

    public static void setAllowCustom(boolean allowCustom) {
        ResourcesReloadListener.allowCustom = allowCustom;
    }

    public static void setBlockReloading(boolean blockReloading) {
        ResourcesReloadListener.blockReloading = blockReloading;
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        update();
    }
}
