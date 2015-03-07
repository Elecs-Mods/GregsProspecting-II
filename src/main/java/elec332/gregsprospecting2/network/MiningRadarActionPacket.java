package elec332.gregsprospecting2.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import elec332.core.network.AbstractPacket;
import elec332.gregsprospecting2.items.ItemMiningRadar;
import elec332.gregsprospecting2.lib.MiningRadarAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by Elec332 on 6-3-2015.
 */
public class MiningRadarActionPacket extends AbstractPacket {
    public MiningRadarActionPacket(){
        super();
    }

    public MiningRadarActionPacket(MiningRadarAction action){
        this.networkPackageObject = action;
    }


    @Override
    public IMessage onMessage(AbstractPacket message, MessageContext ctx) {
        ItemStack stack = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
        Item item = stack.getItem();
        if(item != null && item instanceof ItemMiningRadar)
            ((ItemMiningRadar) item).performAction(stack, (MiningRadarAction) networkPackageObject);
        return null;
    }
}
