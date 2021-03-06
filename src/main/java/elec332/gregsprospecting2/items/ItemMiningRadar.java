package elec332.gregsprospecting2.items;

import elec332.core.config.ConfigCore;
import elec332.gregsprospecting2.init.ItemRegister;
import elec332.gregsprospecting2.lib.MiningRadarAction;
import elec332.gregsprospecting2.lib.SignalMode;
import elec332.gregsprospecting2.main.GregsProspectingII;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by gcewing, updated by Elec332.
 */
public class ItemMiningRadar extends Item {

	public static float hitBreakChance = 0.05F;
	public static int maxBatteryCharge = 18000;
	public static int maxRange = GregsProspectingII.MRMI;

	public ItemMiningRadar() {
		super();
		setMaxStackSize(1);
		//setMaxDamage(maxBatteryCharge);
		//setIconIndex(index);
		setNoRepair();
	}

	public int getRange(ItemStack stack) {
		return getTagCompound(stack).getInteger("range");
	}
	
	public void setRange(ItemStack stack, int value) {
		getTagCompound(stack).setInteger("range", value);
	}
	
	public SignalMode getSignalMode(ItemStack stack) {
		return SignalMode.valueOf(getTagCompound(stack).getString("signalMode"));
	}
	
	public void setSignalMode(ItemStack stack, SignalMode value) {
		getTagCompound(stack).setString("signalMode", value.toString());
	}
	
	public int getBatteryCharge(ItemStack stack) {
		return getTagCompound(stack).getInteger("charge");
	}
	
	public void setBatteryCharge(ItemStack stack, int value) {
		getTagCompound(stack).setInteger("charge", value);
	}
	
	public NBTTagCompound getTagCompound(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			tag.setInteger("range", 5);
			tag.setString("signalMode", SignalMode.Maximum.toString());
			tag.setInteger("charge", maxBatteryCharge);
			stack.setTagCompound(tag);
		}
		return tag;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotNum, boolean inHand) {
		if (!world.isRemote) {
			if (inHand) {
				updateBattery(stack);
			}
		}
	}
	
	public void updateBattery(ItemStack stack) {
		int charge = getBatteryCharge(stack);
		charge -= getRange(stack) / 5;
		if (charge < 0)
			charge = 0;
		setBatteryCharge(stack, charge);
	}
	
	public void performAction(ItemStack stack, MiningRadarAction action) {
		switch (action) {
			case IncreaseRange:
				increaseRange(stack);
				break;
			case DecreaseRange:
				reduceRange(stack);
				break;
			case SelectDiscriminationMode:
				setSignalMode(stack, SignalMode.Maximum);
				break;
			case SelectLinearMode:
				setSignalMode(stack, SignalMode.Sum);
				break;
		}
	}
	
	public void increaseRange(ItemStack stack) {
		int range = getRange(stack);
		if (range < maxRange)
			setRange(stack, range + 5);
	}
	
	public void reduceRange(ItemStack stack) {
		int range = getRange(stack);
		if (range > 5)
			setRange(stack, range - 5);
	}
	
	/*private void makeBroken(ItemStack stack) {
		stack.set = ;
		stack.setTagCompound(null);
	}*/

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		World world = player.worldObj;
		if (world.rand.nextFloat() < hitBreakChance) {
			world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.glass", 2.0F, world.rand.nextFloat() * 0.1F + 0.9F);
			//TODO: break item
		}
		return false;
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public boolean isItemTool(ItemStack stack) {
		return false;
	}
	
	public boolean isColor(ItemStack stack) {
		return stack.getItem() == ItemRegister.colorMiningRadar;
	}
}
