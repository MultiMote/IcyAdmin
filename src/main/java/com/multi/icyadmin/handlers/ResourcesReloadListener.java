package com.multi.icyadmin.handlers;

import com.multi.icyadmin.utils.MenuParser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

/**
 * Created by MultiMote on 07.01.2015.
 */
public class ResourcesReloadListener implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        MenuParser.instance.parseMenu(I18n.format("icyadmin.menufile"));
    }
}
