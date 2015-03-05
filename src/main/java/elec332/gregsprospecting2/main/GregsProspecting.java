package elec332.gregsprospecting2.main;

import elec332.gregsprospecting2.network.PacketHandler;
import elec332.gregsprospecting2.items.ItemMiningRadar;
import elec332.gregsprospecting2.items.ItemSlimophone;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	
	public static ItemMiningRadar miningRadar, colorMiningRadar, industrialMiningRadar, industrialMiningRadarCharged;
	public static Item battery, mandrel, batteryCasing, carbonRod,
		electrolytePaste, bowlAppleJuice, carbonFilament, cathodeRayTube, magnetron,
		fibreglassBoard, microwaveHorn, brokenMiningRadar, slimophone, parabolicMicrophone,
		piezoTransducer, quartzWafer, triode, ledBarGraph, redPhosphor, greenPhosphor, bluePhosphor,
		colorCRT, colorMiningRadarCircuit;
	
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
		miningRadar = newItem("miningRadar", ItemMiningRadar.class, "Mining Radar");
		battery = newItem("battery", "Battery");
		mandrel = newItem("mandrel", "Wooden Mandrel");
		mandrel.setContainerItem(mandrel);
		batteryCasing = newItem("batteryCasing", "Battery Casing");
		carbonRod = newItem("carbonRod", "Carbon Rod");
		electrolytePaste = newItem("electrolytePaste", "Electrolyte Paste");
		bowlAppleJuice = newItem("bowlAppleJuice", "Apple Juice");
		bowlAppleJuice.setContainerItem(Item.bowlEmpty);
		carbonFilament = newItem("carbonFilament", "Carbon Filament");
		cathodeRayTube = newItem("cathodeRayTube", "Cathode Ray Tube");
		magnetron = newItem("magnetron", "Magnetron");
		fibreglassBoard = newItem("fibreglassBoard", "Fibreglass Board");
		piezoTransducer = newItem("piezoTransducer", "Piezo Transducer");
		colorCRT = newItem("colorCRT", "Colour CRT");
		greenPhosphor = newItem("greenPhosphor", "Green Phosphor");
		ledBarGraph = newItem("ledBarGraph", "LED Bar Graph");
		microwaveHorn = newItem("microwaveHorn", "Microwave Horn");
		quartzWafer = newItem("quartzWafer", "Quartz Wafer");
		triode = newItem("triode", "Triode");
		redPhosphor = newItem("redPhosphor", "Red Phosphor");
		bluePhosphor = newItem("bluePhosphor", "Blue Phosphor");
		parabolicMicrophone = newItem("parabolicMicrophone", "Parabolic Microphone");
		colorMiningRadarCircuit = newItem("colorMiningRadarCircuit", "Colour Mining Radar Circuit");
		brokenMiningRadar = newItem("brokenMiningRadar", ItemMiningRadar.class, "Broken Mining Radar");		
		slimophone = newItem("slimophone", ItemSlimophone.class, "Slimophone");
		colorMiningRadar = newItem("colorMiningRadar", ItemMiningRadar.class, "Colour Mining Radar");
	}
	
	void registerRecipes() {
		newShapelessRecipe(miningRadar, 1, new ItemStack(miningRadar, 1, -1), battery);
		newRecipe(mandrel, 1, "W", "W", "W", 'W', Block.planks);
		newShapelessRecipe(batteryCasing, 6, Item.ingotIron, mandrel);
		newRecipe(carbonRod, 6, "C", "C", "C", 'C', Item.coal);
		newShapelessRecipe(bowlAppleJuice, 1, Item.appleRed, Item.bowlEmpty);
		newShapelessRecipe(electrolytePaste, 6, bowlAppleJuice, new ItemStack(Item.dyePowder, 1, 15));
		newShapelessRecipe(battery, 1, batteryCasing, electrolytePaste, carbonRod);
		newSmeltingRecipe(carbonFilament, 1, Item.silk);
		newRecipe(cathodeRayTube, 1, "|gG", "|gf", "|gG",
			'|', Block.thinGlass, 'g', Item.glowstone, 'G', Block.glass, 'f', carbonFilament);
		newRecipe(magnetron, 1, "rGr", "IGI", "rfr",
			'r', Item.redstone, 'G', Block.glass, 'I', Item.ingotIron, 'f', carbonFilament);
		newShapelessRecipe(fibreglassBoard, 1, Block.glass, Item.slimeBall);
		newRecipe(microwaveHorn, 1, "  I", "II ", "  I", 'I', Item.ingotIron);
		newRecipe(miningRadar, 1, "CMH", "LFB", "grg",
			'C', cathodeRayTube, 'M', magnetron, 'H', microwaveHorn, 'L', Block.lever, 'F', fibreglassBoard,
			'B', battery, 'g', Item.goldNugget, 'r', Item.redstone);
		newRecipe(slimophone, 1, "ltp", "grb", 'l', ledBarGraph, 't', triode, 'p', parabolicMicrophone,
			'g', Item.goldNugget, 'r', Item.redstone, 'b', battery);
		newRecipe(ledBarGraph, 1, "rgB", "rgB", "rgB",
			'r', Item.redstone, 'g', Item.glowstone, 'B', new ItemStack(Block.cloth, 1, 15));
		newShapelessRecipe(slimophone, 1, new ItemStack(slimophone, 1, -1), battery);
		newRecipe(parabolicMicrophone, 1, " W", "Wp", " W", 'W', Block.planks, 'p', piezoTransducer);
		newRecipe(piezoTransducer, 1, "IqI", 'I', Item.ingotIron, 'q', quartzWafer);
		newSmeltingRecipe(quartzWafer, 1, Block.sandStone);
		newRecipe(triode, 1, " I ", "GrG", " f ", 
			'I', Item.ingotIron, 'G', Block.glass, 'r', Item.redstone, 'f', carbonFilament);
		newShapelessRecipe(redPhosphor, 1, Item.blazePowder, new ItemStack(Item.dyePowder, 1, 1));
		newShapelessRecipe(greenPhosphor, 1, Item.blazePowder, new ItemStack(Item.dyePowder, 1, 2));
		newShapelessRecipe(bluePhosphor, 1, Item.blazePowder, new ItemStack(Item.dyePowder, 1, 4));
		newRecipe(colorCRT, 1, "|rf", "|gf", "|bf",
			'|', Block.thinGlass, 'r', redPhosphor, 'g', greenPhosphor, 'b', bluePhosphor, 'f', carbonFilament);
		newRecipe(colorMiningRadarCircuit, 1, "rrr", " b ", " I ",
			'r', Item.redstone, 'b', fibreglassBoard, 'I', Item.ingotIron);
		newShapelessRecipe(colorMiningRadar, 1, miningRadar, colorCRT, colorMiningRadarCircuit);
		newRecipe(colorMiningRadar, 1, "CMH", "LFB", "grg",
			'C', colorCRT, 'M', magnetron, 'H', microwaveHorn, 'L', Block.lever, 'F', colorMiningRadarCircuit,
			'B', battery, 'g', Item.goldNugget, 'r', Item.redstone);
		newShapelessRecipe(colorMiningRadar, 1, new ItemStack(colorMiningRadar, 1, -1), battery);
	}

}
