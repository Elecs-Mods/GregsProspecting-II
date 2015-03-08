package elec332.gregsprospecting2.proxies;

import elec332.gregsprospecting2.client.keys.KeyHandler;
import elec332.gregsprospecting2.client.render.RenderMiningRadar;
import elec332.gregsprospecting2.client.render.RenderSlimophone;
import elec332.gregsprospecting2.init.ItemRegister;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    @Override
    public void registerKeyBindings() {
        KeyHandler.instance.registerKeys();
    }

    @Override
    public void registerRenderer() {
        IItemRenderer MiningRadarR = new RenderMiningRadar();
        MinecraftForgeClient.registerItemRenderer(ItemRegister.miningRadar, MiningRadarR);
        MinecraftForgeClient.registerItemRenderer(ItemRegister.colorMiningRadar, MiningRadarR);
        MinecraftForgeClient.registerItemRenderer(ItemRegister.brokenMiningRadar, MiningRadarR);
        MinecraftForgeClient.registerItemRenderer(ItemRegister.slimophone, new RenderSlimophone());
    }
}
