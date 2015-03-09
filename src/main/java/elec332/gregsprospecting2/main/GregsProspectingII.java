package elec332.gregsprospecting2.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import elec332.core.config.ConfigCore;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.helper.ModInfoHelper;
import elec332.core.modBaseUtils.ModBase;
import elec332.core.modBaseUtils.modInfo;
import elec332.core.network.NetworkHandler;
import elec332.gregsprospecting2.init.BlockRegister;
import elec332.gregsprospecting2.init.CommandRegister;
import elec332.gregsprospecting2.init.ItemRegister;
import elec332.gregsprospecting2.init.RecipeRegister;
import elec332.gregsprospecting2.network.MiningRadarActionPacket;
import elec332.gregsprospecting2.proxies.CommonProxy;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = GregsProspectingII.ModID, name = GregsProspectingII.ModName, dependencies = modInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = modInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class GregsProspectingII extends ModBase {

    public static final String ModName = "Greg's Prospecting II"; //Human readable name
    public static final String ModID = "GregsProspecting-II";  //modid (usually lowercase)

    @SidedProxy(clientSide = "elec332.gregsprospecting2.proxies.ClientProxy", serverSide = "elec332.gregsprospecting2.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static GregsProspectingII instance;
    public static NetworkHandler networkHandler;

    public static int MRMI = 15;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.cfg = FileHelper.getConfigFileElec(event);
        networkHandler = new NetworkHandler(ModInfoHelper.getModID(event));
        networkHandler.registerServerPacket(MiningRadarActionPacket.class);
        loadConfiguration();
        MRMI = config.getInt("MaxRange", ConfigCore.CATEGORY_GENERAL, 15, 5, 50, "Sets the max. range for the MiningRadar");

        ItemRegister.instance.preInit(event);
        BlockRegister.instance.preInit(event);
        //setting up mod stuff

        loadConfiguration();
        MCModInfo.CreateMCModInfo(event, "Created by Elec332, original by gcewing." +
                        " Rendering and some internal logic also by gcewing",
                "This mod allows the user to find ores and slime-chunks easier",
                "Loading URL...", "logo",
                new String[]{"Elec332"});
        notifyEvent(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        loadConfiguration();
        ItemRegister.instance.init(event);
        BlockRegister.instance.init(event);
        RecipeRegister.instance.init(event);
        proxy.registerKeyBindings();
        proxy.registerRenderer();
        //register items/blocks

        notifyEvent(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadConfiguration();
        //Mod compat stuff

        notifyEvent(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegister.instance.init(event);
    }

    File cfg;

    @Override
    public File configFile() {
        return cfg;
    }

    @Override
    public String modID(){
        return ModID;
    }
}
