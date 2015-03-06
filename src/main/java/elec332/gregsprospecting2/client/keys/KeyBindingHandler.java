package elec332.gregsprospecting2.client.keys;

import java.util.EnumSet;

import elec332.gregsprospecting2.lib.MiningRadarAction;
import elec332.gregsprospecting2.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;

public class KeyBindingHandler extends KeyHandler {

	public KeyBindingHandler(KeyBindingList bindings) {
		super(bindings.getBindings(), bindings.getRepeats());
	}
	
	@Override
	public String getLabel() {
		return("Greg's Prospecting: " + this.getClass().getSimpleName());
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (tickEnd) {
			FMLClientHandler ch = FMLClientHandler.instance();
			if (ch.getClient().currentScreen == null && ch.getSide() == Side.CLIENT) {
				//System.out.printf("GregsProspecting: KeyBindingHandler.keyDown: %s\n", kb);
				if (kb instanceof MiningRadarKeyBinding) {
					//System.out.printf("GregsProspecting: KeyBindingHandler.keyDown: %s\n",
					//	((MiningRadarKeyBinding)kb).action);
					sendMiningRadarAction(((MiningRadarKeyBinding)kb).action);
				}
			}
		}
	}
	
	private void sendMiningRadarAction(MiningRadarAction action) {
		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
		if (player != null && player.worldObj != null && player.worldObj.isRemote) {
			PacketHandler.sendPacket(new byte[]{0, (byte) action.ordinal()});
//			ItemStack stack = player.getCurrentEquippedItem();
//			if (stack != null) {
//				Item item = stack.getItem();
//				if (item != null && item instanceof ItemMiningRadar)
//					((ItemMiningRadar)item).performAction(stack, action);
//			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
