package elec332.gregsprospecting2.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by gcewing, updated by Elec332.
 */
public class ItemSlimophone extends Item {

	private static int maxBatteryCharge = 100000;
	private static double maximumRange = 160.0; // blocks
	private static String pingSound = "mob.slime.small"; //"gcewing.prospecting.resources.slimefilt";
	private static double omnidirectionalGain = 0.2;
	private static int numBars = 6;
	private static double barDecayFactor = 0.9;
	private static int cooldownTime = 30; // ticks
	
	public static double barIntensities[] = new double[numBars];
	
	private static boolean scanPerformed;
	private static Map<Entity, SlimeData> slimeData = new WeakHashMap<Entity, SlimeData>();

	public ItemSlimophone(int id) {
		super();
		setMaxStackSize(1);
		setMaxDamage(maxBatteryCharge);
		//setIconIndex(22);
		setNoRepair();
	}
	
	/*public static void onTick() {
		decayBars();
		scanPerformed = false;
	}*/

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotNum, boolean inHand) {
		if (world.isRemote) {
			decayBars();
			scanPerformed = false;
		}
		if (hasPower(stack)) {
			if (!world.isRemote) {
				updateBattery(stack);
			}
			if (world.isRemote) {
				if (!scanPerformed) {
					performScan(world, entity);
					scanPerformed = true;
				}
			}
		}
	}
	
	private void performScan(World world, Entity entity) {
		double maxSignal = 0.0;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			for (Object obj : world.loadedEntityList) {
				if (obj instanceof EntitySlime) {
					EntitySlime slime = (EntitySlime) obj;
					SlimeData data = getSlimeData(slime);
					if (data.cooldownTimer > 0) {
						data.cooldownTimer -= 1;
					}
					else {
						if (slime.posY != data.posY) {
							data.posY = slime.posY;
							data.cooldownTimer = cooldownTime;
							Vec3 slimePos = Vec3.createVectorHelper(slime.posX, slime.posY, slime.posZ);
							Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
							double distFromPlayer = playerPos.distanceTo(slimePos);
							if (distFromPlayer <= maximumRange) {
								Vec3 player2slime = slimePos.addVector(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);
								Vec3 look = player.getLookVec();
								double gain = Math.max(player2slime.normalize().dotProduct(look), omnidirectionalGain);
								double signal = gain * (1.0 - distFromPlayer / maximumRange);
								world.playSound(player.posX, player.posY, player.posZ, pingSound, (float) signal, 1.0F, false);
								maxSignal = Math.max(maxSignal, signal);
							}
						}
					}
				}
			}
		}
		if (maxSignal > 0.0)
			intensifyBars(maxSignal);
	}
	
	private void intensifyBars(double signal) {
		int n = (int) Math.round(signal * numBars);
		for (int i = 0; i < n; i++)
			barIntensities[i] = 1.0;
	}
	
	public static void decayBars() {
		for (int i = 0; i < numBars; i++)
			barIntensities[i] *= barDecayFactor;
	}
	
	private boolean hasPower(ItemStack stack) {
		return stack.getItemDamage() < stack.getMaxDamage();
	}
	
	private void updateBattery(ItemStack stack) {
		//stack.setItemDamage();
		//stack.damageItem(1, null);
		stack.getItem().setDamage(stack, stack.getItemDamage() + 1);
	}
	
	private SlimeData getSlimeData(Entity slime) {
		SlimeData data = slimeData.get(slime);
		if (data == null) {
			data = new SlimeData();
			slimeData.put(slime, data);
		}
		return data;
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public boolean isItemTool(ItemStack stack) {
		return false;
	}
	
}

class SlimeData {
	public double posY;
	public int cooldownTimer;
}

