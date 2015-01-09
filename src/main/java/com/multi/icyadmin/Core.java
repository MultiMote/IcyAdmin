package com.multi.icyadmin;

/**
 * Created by MultiMote on 02.01.2015.
 */

import com.multi.icyadmin.data.DynamicStorage;
import com.multi.icyadmin.handlers.ActionHandler;
import com.multi.icyadmin.handlers.CommandHandler;
import com.multi.icyadmin.handlers.ResourcesReloadListener;
import com.multi.icyadmin.handlers.packets.PlayerInfoPacket;
import com.multi.icyadmin.handlers.packets.SendCommandPacket;
import com.multi.icyadmin.proxy.CommonProxy;
import com.multi.icyadmin.utils.FileProcessor;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = Defines.MOD_ID, version = Defines.MOD_VERSION, name = Defines.MOD_NAME)
public class Core {
    @Instance(Defines.MOD_ID)
    public static Core instance;

    public static Logger logger = LogManager.getLogger(Defines.MOD_NAME);

    public static DynamicStorage dynStorage;
    public static ActionHandler actionHandler;

    @SidedProxy(clientSide = "com.multi.icyadmin.proxy.ClientProxy", serverSide = "com.multi.icyadmin.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper packets;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        dynStorage = new DynamicStorage();
        actionHandler = new ActionHandler();
        FileProcessor.readVars();
        FileProcessor.readUsers();
        proxy.registerHandlers();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.clientInit();
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.getResourceManager() instanceof SimpleReloadableResourceManager) { //i dunno
            ((SimpleReloadableResourceManager) mc.getResourceManager()).registerReloadListener(new ResourcesReloadListener());
        }

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packets = NetworkRegistry.INSTANCE.newSimpleChannel(Defines.MOD_ID);
        packets.registerMessage(SendCommandPacket.class, SendCommandPacket.class, 0, Side.SERVER);
        packets.registerMessage(PlayerInfoPacket.class, PlayerInfoPacket.class, 1, Side.CLIENT);
    }


    @EventHandler
    public void startServer(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHandler());
    }

}
