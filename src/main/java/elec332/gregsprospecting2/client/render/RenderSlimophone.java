package elec332.gregsprospecting2.client.render;

import elec332.gregsprospecting2.items.ItemSlimophone;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderSlimophone extends Rendering implements IItemRenderer {

	public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}
	
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		//System.out.printf("RenderSlimophone.renderItem\n");
		switch (type) {
			case INVENTORY:
				renderInInventory(stack, data);
				break;
		default:
			break;
		}
	}

	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
		return false;
	}
	
	void renderInInventory(ItemStack stack, Object[] data) {
		RenderItem ri = new RenderItem();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		IIcon icon = stack.getIconIndex();
		ri.renderIcon(0, 0, icon, 16, 16);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		renderBars();
		GL11.glPopAttrib();
	}
	
	void renderBars() {
		double[] bars = ItemSlimophone.barIntensities;
		for (int i = 0; i < bars.length; i++) {
			GL11.glColor3f((float) bars[i], 0.0F, 0.0F);
			drawRect(2, 13 - 2 * i, 3, -1);
		}
	}
	
}
