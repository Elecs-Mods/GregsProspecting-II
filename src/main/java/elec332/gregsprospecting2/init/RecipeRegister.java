package elec332.gregsprospecting2.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import static elec332.gregsprospecting2.init.ItemRegister.*;

/**
 * Created by Elec332 on 5-3-2015.
 */
public class RecipeRegister {
    public static RecipeRegister instance = new RecipeRegister();

    public void init(FMLInitializationEvent event){
        GameRegistry.addShapelessRecipe(new ItemStack(ItemRegister.miningRadar, 1), new ItemStack(ItemRegister.miningRadar, 1, -1), ItemRegister.battery);
        GameRegistry.addRecipe(new ItemStack(mandrel), "W", "W", "W", 'W', Blocks.planks);
        GameRegistry.addShapelessRecipe(new ItemStack(batteryCasing, 6), Items.iron_ingot, mandrel);
        GameRegistry.addRecipe(new ItemStack(carbonRod, 6), "C", "C", "C", 'C', Items.coal);
        GameRegistry.addShapelessRecipe(new ItemStack(bowlAppleJuice), Items.apple, Items.bowl);
        GameRegistry.addShapelessRecipe(new ItemStack(electrolytePaste, 6), bowlAppleJuice, new ItemStack(Items.dye, 1, 15));
        GameRegistry.addShapelessRecipe(new ItemStack(battery, 1), batteryCasing, electrolytePaste, carbonRod);
        GameRegistry.addSmelting(Items.string, new ItemStack(carbonFilament, 1), 0.0f);
        GameRegistry.addRecipe(new ItemStack(cathodeRayTube), "|gG", "|gf", "|gG",
                '|', Blocks.glass_pane, 'g', Items.glowstone_dust, 'G', Blocks.glass, 'f', carbonFilament);
        GameRegistry.addRecipe(new ItemStack(magnetron, 1), "rGr", "IGI", "rfr",
                'r', Items.redstone, 'G', Blocks.glass, 'I', Items.iron_ingot, 'f', carbonFilament);
        GameRegistry.addShapelessRecipe(new ItemStack(fibreglassBoard, 1), Blocks.glass, Items.slime_ball);
        GameRegistry.addRecipe(new ItemStack(microwaveHorn, 1), "  I", "II ", "  I", 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(miningRadar, 1), "CMH", "LFB", "grg",
                'C', cathodeRayTube, 'M', magnetron, 'H', microwaveHorn, 'L', Blocks.lever, 'F', fibreglassBoard,
                'B', battery, 'g', Items.gold_nugget, 'r', Items.redstone);
        GameRegistry.addRecipe(new ItemStack(slimophone, 1), "ltp", "grb", 'l', ledBarGraph, 't', triode, 'p', parabolicMicrophone,
                'g', Items.gold_nugget, 'r', Items.redstone, 'b', battery);
        GameRegistry.addRecipe(new ItemStack(ledBarGraph, 1), "rgB", "rgB", "rgB",
                'r', Items.redstone, 'g', Items.glowstone_dust, 'B', new ItemStack(Blocks.wool, 1, 15));
        GameRegistry.addShapelessRecipe(new ItemStack(slimophone, 1), new ItemStack(slimophone, 1, -1), battery);
        GameRegistry.addRecipe(new ItemStack(parabolicMicrophone, 1), " W", "Wp", " W", 'W', Blocks.planks, 'p', piezoTransducer);
        GameRegistry.addRecipe(new ItemStack(piezoTransducer, 1), "IqI", 'I', Items.iron_ingot, 'q', quartzWafer);
        GameRegistry.addSmelting(Blocks.sandstone, new ItemStack(quartzWafer), 0.0f);
        GameRegistry.addRecipe(new ItemStack(triode), " I ", "GrG", " f ",
                'I', Items.iron_ingot, 'G', Blocks.glass, 'r', Items.redstone, 'f', carbonFilament);
        GameRegistry.addShapelessRecipe(new ItemStack(redPhosphor), Items.blaze_powder, new ItemStack(Items.dye, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(greenPhosphor), Items.blaze_powder, new ItemStack(Items.dye, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(bluePhosphor), Items.blaze_powder, new ItemStack(Items.dye, 1, 4));
        GameRegistry.addRecipe(new ItemStack(colorCRT), "|rf", "|gf", "|bf",
                '|', Blocks.glass_pane, 'r', redPhosphor, 'g', greenPhosphor, 'b', bluePhosphor, 'f', carbonFilament);
        GameRegistry.addRecipe(new ItemStack(colorMiningRadarCircuit), "rrr", " b ", " I ",
                'r', Items.redstone, 'b', fibreglassBoard, 'I', Items.iron_ingot);
        GameRegistry.addShapelessRecipe(new ItemStack(colorMiningRadar), miningRadar, colorCRT, colorMiningRadarCircuit);
        GameRegistry.addRecipe(new ItemStack(colorMiningRadar), "CMH", "LFB", "grg",
                'C', colorCRT, 'M', magnetron, 'H', microwaveHorn, 'L', Blocks.lever, 'F', colorMiningRadarCircuit,
                'B', battery, 'g', Items.gold_nugget, 'r', Items.redstone);
        GameRegistry.addShapelessRecipe(new ItemStack(colorMiningRadar), new ItemStack(colorMiningRadar, 1, -1), battery);
    }
}
