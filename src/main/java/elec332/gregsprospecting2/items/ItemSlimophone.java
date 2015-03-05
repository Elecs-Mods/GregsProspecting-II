package elec332.gregsprospecting2.items;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.*;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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
		super(id);
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
		//System.out.printf("ItemSlimophone.onUpdate: %d entities\n",
		//	world.loadedEntityList.size());
		if (world.isRemote) {
			decayBars();
			scanPerformed = false;
		}
		if (hasPower(stack)) {
			if (!world.isRemote) {
				//System.out.printf("ItemSlimophone: Updating battery\n");
				updateBattery(stack);
			}
			if (world.isRemote) {
				//System.out.printf("ItemSlimophone: Checking whether scan required\n");
				if (!scanPerformed) {
					//System.out.printf("ItemSlimophone: Performing scan\n");
					performScan(stack, world, entity, slotNum, inHand);
					scanPerformed = true;
				}
			}
		}
	}
	
	private void performScan(ItemStack stack, World world, Entity entity, int slotNum, boolean inHand) {
		double maxSignal = 0.0;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			for (Object obj : world.loadedEntityList) {
				//System.out.printf("ItemSlimophone.performScan: found %s\n", obj.getClass().getName());
				if (obj instanceof EntitySlime) {
					EntitySlime slime = (EntitySlime) obj;
					//System.out.printf("   Found slime at (%.2f,%.2f,%.2f) isCollidedVertically = %s\n",
					//	slime.posX, slime.posY, slime.posZ, slime.isCollidedVertically);
					SlimeData data = getSlimeData(slime);
					if (data.cooldownTimer > 0) {
						//System.out.printf("   Still cooling down\n");
						data.cooldownTimer -= 1;
					}
					else {
						if (slime.posY != data.posY) {
							data.posY = slime.posY;
							data.cooldownTimer = cooldownTime;
							Vec3 slimePos = Vec3.createVectorHelper(slime.posX, slime.posY, slime.posZ);
							Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
							double distFromPlayer = playerPos.distanceTo(slimePos);
							//System.out.printf("   Distance from player = %s\n", distFromPlayer);
							if (distFromPlayer <= maximumRange) {
								Vec3 player2slime = slimePos.addVector(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);
								Vec3 look = player.getLookVec();
								//System.out.printf("   To slime = %s\n", player2slime);
								//System.out.printf("   Look = %s\n", look);
								double gain = Math.max(player2slime.normalize().dotProduct(look), omnidirectionalGain);
								double signal = gain * (1.0 - distFromPlayer / maximumRange);
								//System.out.printf("   Signal = %s, gain = %s\n", signal, gain);
								//world.playSoundAtEntity(player, pingSound, (float) signal, 1.0F);
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
	
	private void dumpBarIntensities() {
		System.out.printf("ItemSlimophone: Bar intensities =");
		for (int i = 0; i < numBars; i++)
			System.out.printf(" %.2f", barIntensities[i]);
		System.out.printf("\n");
	}
	
	private void intensifyBars(double signal) {
		int n = (int) Math.round(signal * numBars);
		//System.out.printf("ItemSlimophone: Intensifying %d bars\n", n);
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
		stack.setItemDamage(stack.getItemDamage() + 1);
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
		// This needs to be true, otherwise the NBTTagCompound is not sent
		// from the server to the client.
		return true;
	}
	
	@Override
	public boolean isItemTool(ItemStack stack) {
		// ...but we don't want it to look like a tool, otherwise it will
		// be enchantable and maybe have other undesirable effects.
		return false;
	}
	
}

class SlimeData {

	public double posY;
	public int cooldownTimer;
	
}

