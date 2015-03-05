package elec332.gregsprospecting2.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import elec332.gregsprospecting2.init.BlockRegister;
import elec332.gregsprospecting2.init.CommandRegister;
import elec332.gregsprospecting2.init.ItemRegister;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModBase;
import elec332.core.modBaseUtils.modInfo;
import elec332.gregsprospecting2.proxies.CommonProxy;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = GregsProspectingII.ModName, name = GregsProspectingII.ModID, dependencies = modInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = modInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class GregsProspectingII extends ModBase {

    public static final String ModName = "Greg's Prospecting II"; //Human readable name
    public static final String ModID = "GregsProspecting-II";  //modid (usually lowercase)

    @SidedProxy(clientSide = "elec332.gregsprospecting2.proxies.ClientProxy", serverSide = "elec332.gregsprospecting2.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static GregsProspectingII instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.cfg = FileHelper.getConfigFileElec(event);
        loadConfiguration();
        ItemRegister.instance.preInit(event);
        BlockRegister.instance.preInit(event);
        //setting up mod stuff

        loadConfiguration();
        MCModInfo.CreateMCModInfo(event, "Created by Elec332, original by Greg?",
                "mod description",
                "website link", "logo",
                new String[]{"Elec332"});
        notifyEvent(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        loadConfiguration();
        ItemRegister.instance.init(event);
        BlockRegister.instance.init(event);
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
