package elec332.gregsprospecting2.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.helper.ModInfoHelper;
import elec332.core.util.items.baseItem;
import elec332.gregsprospecting2.items.ItemMiningRadar;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ItemRegister {
    public static final ItemRegister instance = new ItemRegister();
    static String ModID;

    public static ItemMiningRadar miningRadar, colorMiningRadar, industrialMiningRadar, industrialMiningRadarCharged;
    public static Item battery, mandrel, batteryCasing, carbonRod,
            electrolytePaste, bowlAppleJuice, carbonFilament, cathodeRayTube, magnetron,
            fibreglassBoard, microwaveHorn, brokenMiningRadar, slimophone, parabolicMicrophone,
            piezoTransducer, quartzWafer, triode, ledBarGraph, redPhosphor, greenPhosphor, bluePhosphor,
            colorCRT, colorMiningRadarCircuit;

    public void preInit(FMLPreInitializationEvent event){
        ModID = ModInfoHelper.getModID(event);
    }

    public void init(FMLInitializationEvent event){
        //miningRadar = new baseItem("miningRadar", ItemMiningRadar.class, "Mining Radar");
        battery = createItem("battery", "Battery");
        mandrel = createItem("mandrel", "Wooden Mandrel");
        mandrel.setContainerItem(mandrel);
        batteryCasing = createItem("batteryCasing", "Battery Casing");
        carbonRod = createItem("carbonRod", "Carbon Rod");
        electrolytePaste = createItem("electrolytePaste", "Electrolyte Paste");
        bowlAppleJuice = createItem("bowlAppleJuice", "Apple Juice");
        bowlAppleJuice.setContainerItem(Items.bowl);
        carbonFilament = createItem("carbonFilament", "Carbon Filament");
        cathodeRayTube = createItem("cathodeRayTube", "Cathode Ray Tube");
        magnetron = createItem("magnetron", "Magnetron");
        fibreglassBoard = createItem("fibreglassBoard", "Fibreglass Board");
        piezoTransducer = createItem("piezoTransducer", "Piezo Transducer");
        colorCRT = createItem("colorCRT", "Colour CRT");
        greenPhosphor = createItem("greenPhosphor", "Green Phosphor");
        ledBarGraph = createItem("ledBarGraph", "LED Bar Graph");
        microwaveHorn = createItem("microwaveHorn", "Microwave Horn");
        quartzWafer = createItem("quartzWafer", "Quartz Wafer");
        triode = createItem("triode", "Triode");
        redPhosphor = createItem("redPhosphor", "Red Phosphor");
        bluePhosphor = createItem("bluePhosphor", "Blue Phosphor");
        parabolicMicrophone = createItem("parabolicMicrophone", "Parabolic Microphone");
        colorMiningRadarCircuit = createItem("colorMiningRadarCircuit", "Colour Mining Radar Circuit");
        //brokenMiningRadar = new Item("brokenMiningRadar", ItemMiningRadar.class, "Broken Mining Radar");
        //slimophone = new Item("slimophone", ItemSlimophone.class, "Slimophone");
        //colorMiningRadar = new Item("colorMiningRadar", ItemMiningRadar.class, "Colour Mining Radar");
    }

    Item createItem(String s, String toLocalise){
        return new baseItem(s, Tab, ModID);
    }

    public static CreativeTabs Tab = new CreativeTabs("GregsProspecting2") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return miningRadar;
        }
    };
}
