package elec332.gregsprospecting2.main;

import elec332.gregsprospecting2.network.PacketHandler;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Info.modID, name = Info.modName, version = Info.versionNumber)

@NetworkMod(clientSideRequired=true, serverSideRequired = true, versionBounds = Info.versionBounds,
	channels={"gce.prospecting"}, packetHandler = PacketHandler.class)

public class GregsProspecting extends BaseMod {
	
	public static GregsProspecting instance;
	

	
	public GregsProspecting() {
		super();
		instance = this;
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}
	
	@Init
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
	
	BaseModClient initClient() {
		return new GregsProspectingClient(this);
	}

	@Override
	void registerItems() {

	}
	
	void registerRecipes() {

	}

}
