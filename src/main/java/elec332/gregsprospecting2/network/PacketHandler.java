package elec332.gregsprospecting2.network;

import elec332.gregsprospecting2.lib.MiningRadarAction;
import elec332.gregsprospecting2.items.ItemMiningRadar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	
	@Override 
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (packet.data.length >= 2)
		{
			int code = packet.data[1];
			MiningRadarAction[] val = MiningRadarAction.values();
			if (code >= 0 && code < val.length)
			{
				if(player != null)
				{
					EntityPlayer pl = (EntityPlayer) player;
					ItemStack stack = pl.getCurrentEquippedItem();
					if (stack != null)
					{
						Item item = stack.getItem();
						if(item != null && item instanceof ItemMiningRadar)
							((ItemMiningRadar)item).performAction(stack, val[code]);
					}
				}
			}
				
		}
	}
	
	public static void sendPacket(byte[] data)
	{
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "gce.prospecting";
		pkt.length = data.length;
		pkt.data = data;
		
		if(pkt != null)
		{
			PacketDispatcher.sendPacketToServer(pkt);
		}
	}

}
