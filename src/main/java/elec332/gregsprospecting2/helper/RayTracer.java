package elec332.gregsprospecting2.helper;

import cpw.mods.fml.client.FMLClientHandler;
import elec332.gregsprospecting2.lib.SignalMode;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class RayTracer {

	private int range; // Range of the scan in blocks
	private double x0, y0, z0; // Origin of scan relative to centre of block containing player
	private SignalMode signalMode;
	private float density[][][]; // Hardness values of blocks within range
	
	private static float gain = 2.0F;
	private static float reflectance[]; // block ID --> reflected signal
	
	static {
		/*reflectance = new float[Blocks.blocksList.length];
		World world = FMLClientHandler.instance().getClient().theWorld;
		float default_reflectance = Blocks.stone.getBlockHardness(world, 0,0,0);
		for (Block block : Blocks.blocksList) {
			if (block != null) {
				try {
					reflectance[block.blockID] = block.getBlockHardness(world, 0,0,0);
				}
				catch (Exception e) {
					reflectance[block.blockID] = default_reflectance;
				}
			}	
		}
			
		reflectance[Block.waterStill.blockID] = 1.75F;
		reflectance[Block.waterMoving.blockID] = 1.75F;
		reflectance[Block.wood.blockID] = 0.75F;

		reflectance[Block.planks.blockID] = 0.5F;
		reflectance[Block.fence.blockID] = reflectance[Block.planks.blockID];
		reflectance[Block.fenceGate.blockID] = reflectance[Block.planks.blockID];
		reflectance[Block.stairsWoodOak.blockID] = reflectance[Block.planks.blockID];
		reflectance[Block.doorWood.blockID] = reflectance[Block.planks.blockID];
		reflectance[Block.trapdoor.blockID] = reflectance[Block.planks.blockID];
		
		reflectance[Block.cobblestone.blockID] = reflectance[Block.stone.blockID] - 0.2F;
		reflectance[Block.cobblestoneMossy.blockID] = reflectance[Block.cobblestone.blockID];
		reflectance[Block.stairsCobblestone.blockID] = reflectance[Block.cobblestone.blockID];
		//reflectance[Block.stairSingle.blockID] = reflectance[Block.cobblestone.blockID];
		//reflectance[Block.stairDouble.blockID] = reflectance[Block.cobblestone.blockID];
		
		reflectance[Block.brick.blockID] = 1.0F;
		reflectance[Block.stairsBrick.blockID] = reflectance[Block.brick.blockID];
		
		reflectance[Block.lavaStill.blockID] = reflectance[Block.stone.blockID];
		reflectance[Block.lavaMoving.blockID] = reflectance[Block.stone.blockID];
		
		reflectance[Block.oreCoal.blockID] = 2.5F;
		reflectance[Block.oreIron.blockID] = 3.0F;
		reflectance[Block.oreRedstone.blockID] = 3.2F;
		reflectance[Block.oreGold.blockID] = 3.5F;
		reflectance[Block.oreLapis.blockID] = 4.0F;
		reflectance[Block.obsidian.blockID] = 4.25F;
		reflectance[Block.oreDiamond.blockID] = 4.5F;
		
		reflectance[Block.blockIron.blockID] = 3.0F;
		reflectance[Block.blockGold.blockID] = 3.5F;
		reflectance[Block.blockLapis.blockID] = 4.0F;
		reflectance[Block.blockDiamond.blockID] = 4.5F;*/
	}
	
	public RayTracer(EntityPlayer player, int range, SignalMode signalMode) {
		this.range = range;
		this.signalMode = signalMode;
		World world = player.worldObj;
		double px = player.posX, py = player.posY, pz = player.posZ;
		int ipx = (int) Math.floor(px);
		int ipy = (int) Math.floor(py);
		int ipz = (int) Math.floor(pz);
		x0 = px - ipx - 0.5;
		y0 = py - ipy - 0.5;
		z0 = pz - ipz - 0.5;
		int n = 2 * range + 1;
		density = new float[n][n][n];
		for (int ix = - range; ix <= range; ix++) {
			for (int iy = - range; iy <= range; iy++) {
				for (int iz = - range; iz <= range; iz++) {
					Block blockId = world.getBlock(ipx + ix, ipy + iy, ipz + iz);
					density[range + ix][range + iy][range + iz] = blockId.getBlockHardness(world, ipx + ix, ipy + iy, ipz + iz);
				}
			}
		}
	}
	
	private void dump() {
		System.out.printf("%3s", "");
		for (int ix = 0; ix <= 2 * range; ix++)
			System.out.printf(" %5d", ix);
		System.out.printf("\n");
		for (int iz = 0; iz <= 2 * range; iz++) {
			System.out.printf("%3d", iz);
			for (int ix = 0; ix <= 2 * range; ix++) {
				float d = 0;
				for (int iy = 0; iy <= 2 * range; iy++)
					d += density[ix][iy][iz];
				System.out.printf(" %5.1f", d);
			}
			System.out.printf("\n");
		}
	}
	
	public float trace(double vx, double vy, double vz) {
		double ax = Math.abs(vx);
		double ay = Math.abs(vy);
		double az = Math.abs(vz);
		if (ax >= ay && ax >= az) {
			if (vx > 0)
				return traceCase(vx, vy, vz, 1, vy/vx, vz/vx);
			else
				return traceCase(vx, vy, vz, -1, -vy/vx, -vz/vx);
		}
		else if (ay >= ax && ay >= az) {
			if (vy > 0)
				return traceCase(vx, vy, vz, vx/vy, 1, vz/vy);
			else
				return traceCase(vx, vy, vz, -vx/vy, -1, -vz/vy);
		}
		else {
			if (vz > 0)
				return traceCase(vx, vy, vz, vx/vz, vy/vz, 1);
			else
				return traceCase(vx, vy, vz, -vx/vz, -vy/vz, -1);
		}
	}
	
	private float traceCase(double vx, double vy, double vz,
			double dx, double dy, double dz)
	{
		int r = range;
		float signal = 0;
		int count = 0;
		for (int n = 1; n <= r; n++) {
			double x = x0 + n * dx;
			double y = y0 + n * dy;
			double z = z0 + n * dz;
			int i = (int)Math.round(x);
			int j = (int)Math.round(y);
			int k = (int)Math.round(z);
			if (i < -r || i > r || j < -r || j > r || k < -r || k > r)
				break;
			float d = density[r+i][r+j][r+k];
			switch (signalMode) {
				case Average:
					signal += d;
					count += 1;
				case Sum:
					signal += d;
					count = 1;
					break;
				case Maximum:
					if (d > signal) {
						signal = d;
						count = 1;
					}
					break;
			}
		}
		if (count > 0)
			signal /= count;
		if (signalMode != SignalMode.Maximum)
			signal /= (range / 5);
		if (signalMode == SignalMode.Maximum)
			signal *= gain;
		return signal;
	}
	
}
