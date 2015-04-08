package elec332.gregsprospecting2.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import elec332.core.network.AbstractPacket;
import elec332.gregsprospecting2.items.ItemMiningRadar;
import elec332.gregsprospecting2.lib.MiningRadarAction;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 6-3-2015.
 */
public class MiningRadarActionPacket extends AbstractPacket {
    public MiningRadarActionPacket(){
        super();
    }

    public MiningRadarActionPacket(MiningRadarAction action){
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("ID", action.ordinal());
        this.networkPackageObject = tag;
    }

    @Override
    public IMessage onMessage(AbstractPacket message, MessageContext ctx) {
        if (message.networkPackageObject != null) {
            ItemStack stack = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
            if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemMiningRadar)
                ((ItemMiningRadar) stack.getItem()).performAction(stack, MiningRadarAction.values()[message.networkPackageObject.getInteger("ID")]);
        }
        return null;
    }
}
