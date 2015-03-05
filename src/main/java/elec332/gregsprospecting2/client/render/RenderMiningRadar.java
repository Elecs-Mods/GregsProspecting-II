package elec332.gregsprospecting2.client.render;

import elec332.gregsprospecting2.helper.RayTracer;
import elec332.gregsprospecting2.helper.CoordRotator;
import elec332.gregsprospecting2.items.ItemMiningRadar;
import elec332.gregsprospecting2.main.GregsProspecting;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

public class RenderMiningRadar extends Rendering implements IItemRenderer {

	static String mono_hud = "mining_radar_hud.png";
	static String color_hud = "color_mr_hud.png";

	static double hudWidth = 160;
	static double hudHeight = 160;
	static double screenLeft = 16;
	static double screenBottom = 48;
	static double screenWidth = 128;
	static double screenHeight = 96;
	
	static double fieldOfView = 90; // degrees
	static double tanA = Math.tan((fieldOfView / 2) * Math.PI / 180);
	static int hResolution = 32;
	static int vResolution = 24;
	static float phosphorColor[] = new float[]{1.0F, 218/255.0F, 22/255.0F}; // amber
	static float maxSignal = 10.0F;
	
	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
		//System.out.printf("RenderMiningRadar.handleRenderType: %s?\n", type);
		switch (type) {
			case INVENTORY:
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
		return (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			&& helper == ItemRendererHelper.EQUIPPED_BLOCK;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		//System.out.printf("RenderMiningRadar.renderItem: type %s\n", type);
		switch (type) {
			case INVENTORY:
				renderInInventory(stack, data);
				break;
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
				renderEquipped(stack, data);
				break;
		}
	}
	
	void renderInInventory(ItemStack stack, Object[] data) {
		RenderItem ri = new RenderItem();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		Icon icon = stack.getIconIndex();
		//ri.renderTexturedQuad(0, 0, icon % 16 * 16, icon / 16 * 16, 16, 16);
		ri.renderIcon(0, 0, icon, 16, 16);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		double charge = batteryCondition(stack);
		if (charge < 1.0) {
			// Following based on code from RenderItem.renderItemOverlayIntoGUI()
			int var11 = (int) Math.round(13.0 * charge);
			int var7 = (int) Math.round(255.0 * charge);
			int var9 = 255 - var7 << 16 | var7 << 8;
			int var10 = (255 - var7) / 4 << 16 | 16128;
			Tessellator var8 = Tessellator.instance;
			renderQuad(var8, 2, 13, 13, 2, 0);
			renderQuad(var8, 2, 13, 12, 1, var10);
			renderQuad(var8, 2, 13, var11, 1, var9);
		}
		GL11.glPopAttrib();
	}
	
	void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6)
	{
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setColorOpaque_I(par6);
		par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + 0), 0.0D);
		par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + par5), 0.0D);
		par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + par5), 0.0D);
		par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + 0), 0.0D);
		par1Tessellator.draw();
	}

	void renderEquipped(ItemStack stack, Object[] data) {
		//System.out.printf("RenderMiningRadar.renderEquipped\n");
		Entity entity = (Entity) data[1];
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0)
				renderFirstPerson(stack, player);
			else
				renderThirdPerson(stack, player);
		}
	}
	
	void renderThirdPerson(ItemStack stack, EntityPlayer player) {
		GL11.glRotatef(60, 0, 1, 0);
		GL11.glTranslated(-1.5, 0.7, 0.5);
		Tessellator tess = Tessellator.instance;
		int pass = 0;
		Icon icon = player.getItemIcon(stack, pass);
		float u0 = icon.getMinU();
		float v0 = icon.getMinV();
		float u1 = icon.getMaxU();
		float v1 = icon.getMaxV();
		ItemRenderer.renderItemIn2D(tess, u0, v0, u1, v1, 16, 16, 1/16.0F);
	}

	void renderFirstPerson(ItemStack stack, EntityPlayer player) {
		saveAttributes();
		GL11.glRotatef(-45, 0, 1, 0);
		GL11.glTranslated(-0.5, 1.25, 0.75);
		GL11.glScaled(1/hudWidth, 1/hudHeight, 1);
		drawBackground(stack);
		if (isBroken(stack)) {
			//System.out.printf("RenderMiningRadar: is broken\n");
			drawCracks();
		}
		else if (hasPower(stack)) {
			//System.out.printf("RenderMiningRadar: has power\n");
			drawDisplay(player, stack);
			drawIndicators(stack);
		}
		//else
			//System.out.printf("RenderMiningRadar: out of power\n");
		restoreAttributes();
	}
	
	void saveAttributes() {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
	
	void restoreAttributes() {
		GL11.glPopAttrib();
	}
	
	void drawBackground(ItemStack stack) {
		ItemMiningRadar mr = (ItemMiningRadar)stack.getItem();
		bindTexture(mr.isColor(stack) ? color_hud : mono_hud);
		drawTexRect(0, 0, hudWidth, hudHeight, 0, 0, 1, 1);
	}
	
	void drawDisplay(EntityPlayer player, ItemStack stack) {
		//System.out.printf("RenderMiningRadar: Performing scan\n");
		ItemMiningRadar mr;
		if(stack.getItem() instanceof ItemMiningRadar)
			mr = (ItemMiningRadar)stack.getItem(); //GregsProspecting.miningRadar;
		else
			return;
		boolean isColor = mr.isColor(stack);
		double pitch = -player.rotationPitch * Math.PI / 180;
		double yaw = (180 - player.rotationYaw) * Math.PI / 180;
		
		//System.out.printf("pitch = %f yaw = %f\n", pitch, yaw);
		
		CoordRotator rot = new CoordRotator(pitch, yaw);
		double pixelWidth = screenWidth / hResolution;
		double pixelHeight = screenHeight / vResolution;
		
		RayTracer rt = new RayTracer(player, mr.getRange(stack), mr.getSignalMode(stack));
		//System.out.printf("Neighbourhood densities:\n");
		//rt.dump();
		
		Tessellator tess = new Tessellator();
		tess.setTranslation(screenLeft, screenBottom, 0);
		tess.startDrawingQuads();
		double zs = -(hResolution / 2) / tanA;

		for (int j = 0; j < vResolution; j++) {
			double ys = j - vResolution / 2 + 0.5;
			for (int i = 0; i < hResolution; i++) {
				double xs = i - hResolution / 2 + 0.5;
				
				// Rotate x, y, z according to player orientation
				rot.rotate(xs, ys, zs);
				
				float signal = rt.trace(rot.x, rot.y, rot.z);
				//System.out.printf("%d, %d --> %s\n", i, j, signal);
				float y = signal / maxSignal;
				if (y > 1)
					y = 1;
				float h = (2 * y) % 1F;
				if (isColor) {
					float r = 0, g = 0, b = 0;
					if (h < 1/2F) {
						g = 2F * h;
						r = 1F - g;
					}
					else if (h < 3/4F) {
						b = 4F * (h - 1/2F);
						g = 1F - g;
					}
					else {
						r = 4F * (h - 3/4F);
						b = 1F - g;
					}
					tess.setColorOpaque_F(y * r, y * g, y * b);
				}
				else
					tess.setColorOpaque_F(
						y * phosphorColor[0],
						y * phosphorColor[1], 
						y * phosphorColor[2]);
				tessRect(tess, i * pixelWidth, j * pixelHeight, pixelWidth, pixelHeight);
			}
		}
		tess.draw();
	}
	
	void drawCracks() {
		bindTexture("mining_radar_cracks.png");
		drawTexRect(14, 34, 132, 112, 0, 0, 1, 1);
	}

	void drawIndicators(ItemStack stack) {
		ItemMiningRadar mr = GregsProspecting.miningRadar;
		bindTextureSet("mining_radar_indicators.png", 83, 9);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int b = 36; // Base of indicator area
		int h = 9; // Height of indicator area
		// Range setting
		int rw = 9;
		int r = mr.getRange(stack) / 5 - 1;
		drawTexSubRect(19, b, rw, h, r * rw, 0);
		// Signal processing mode
		int m = 0, mw = 0;
		switch (mr.getSignalMode(stack)) {
			case Maximum: m = 27; mw = 17; break;
			case Sum: m = 44; mw = 17; break;
		default:
			break;
		}
		if (m != 0)
			drawTexSubRect(33, b, mw, h, m, 0);
		// Battery charge
		int bc = (int) (18 * batteryCondition(stack));
		drawTexSubRect(119, b, 22, h, 61, 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glColor3fv(phosphorColor);
		drawRect(140 - bc, b + 1, bc, h - 2);
	}
	
	float batteryCondition(ItemStack stack) {
		// Fraction of charge remaining in battery
		//float charge = ItemMiningRadar.getBatteryCharge(stack);
		//float maxCharge = ItemMiningRadar.maxBatteryCharge;
		if(stack != null && stack.getItem() instanceof ItemMiningRadar)
		{
			float charge = ((ItemMiningRadar)stack.getItem()).getBatteryCharge(stack);
			float maxCharge = ((ItemMiningRadar)stack.getItem()).maxBatteryCharge;
			return charge / maxCharge;
		}
//		System.out.printf("batteryCondition: charge = %f, maxCharge = %f\n",
//			charge, maxCharge);
		return 0;
	}
	
	boolean hasPower(ItemStack stack) {
		//int charge = ItemMiningRadar.getBatteryCharge(stack);
//		System.out.printf("RenderMiningRadar.hasPower: charge = %d\n", charge);
		if(stack != null && stack.getItem() instanceof ItemMiningRadar)
		{
			int charge = ((ItemMiningRadar)stack.getItem()).getBatteryCharge(stack);
			return charge > 0;
		}
		return false;
	}

	boolean isBroken(ItemStack stack) {
		return stack.itemID == GregsProspecting.brokenMiningRadar.itemID;
	}
	
	void glColor3fv(float[] c) {
		GL11.glColor3f(c[0], c[1], c[2]);
	}

}
