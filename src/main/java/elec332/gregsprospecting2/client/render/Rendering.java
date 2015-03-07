package elec332.gregsprospecting2.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import elec332.gregsprospecting2.main.GregsProspectingII;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

//import net.minecraft.client.renderer.RenderEngine;

public class Rendering {

	Minecraft mc;
	//BaseModClient client;
	double boundTextureWidth, boundTextureHeight;
	
	public Rendering() {
		mc = Minecraft.getMinecraft();
		//System.out.printf("Rendering: GregsProspecting.instance = %s, client = %s\n",
		//	GregsProspecting.instance, GregsProspecting.instance.client);
		//client = GregsProspecting.instance.client;
	}

  void bindTexture(String name) {
	  ResourceLocation test = new ResourceLocation(GregsProspectingII.ModID + ":other/");
	  FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
	}

	void drawRect(double x, double y, double w, double h) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(x, y, 0);
		GL11.glVertex3d(x+w, y, 0);
		GL11.glVertex3d(x+w, y+h, 0);
		GL11.glVertex3d(x, y+h, 0);
		GL11.glEnd();
	}
	
	void drawRectWithColor(double x, double y, double w, double h, int r, int g, int b) {
		GL11.glColor3b((byte)r, (byte)g, (byte)b);
		drawRect(x, y, w, h);
	}

	void drawTexRect(double x, double y, double w, double h,
			double tx, double ty, double tw, double th) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(tx, 1 - ty);
		GL11.glVertex3d(x, y, 0);
		GL11.glTexCoord2d(tx + tw, 1 - ty);
		GL11.glVertex3d(x + w, y, 0);
		GL11.glTexCoord2d(tx + tw, 1 - ty - th);
		GL11.glVertex3d(x + w, y + h, 0);
		GL11.glTexCoord2d(tx, 1 - ty - th);
		GL11.glVertex3d(x, y + h, 0);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

  void bindTextureSet(String name, double w, double h) {
		bindTexture(name);
		boundTextureWidth = w;
		boundTextureHeight = h;
	}

	void drawTexSubRect(double x, double y, double w, double h,
			double tx, double ty)
	{
		double tw = boundTextureWidth;
		double th = boundTextureHeight;
		double u0 = tx / tw;
		double v0 = 1 - ty / th;
		double u1 = (tx + w) / tw;
		double v1 = 1 - (ty + h) / th;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(u0, v0);
		GL11.glVertex3d(x, y, 0);
		GL11.glTexCoord2d(u1, v0);
		GL11.glVertex3d(x + w, y, 0);
		GL11.glTexCoord2d(u1, v1);
		GL11.glVertex3d(x + w, y + h, 0);
		GL11.glTexCoord2d(u0, v1);
		GL11.glVertex3d(x, y + h, 0);
		GL11.glEnd();
	}
	
	void tessRect(Tessellator tess, double x, double y, double w, double h) {
		tess.addVertex(x, y, 0);
		tess.addVertex(x+w, y, 0);
		tess.addVertex(x+w, y+h, 0);
		tess.addVertex(x, y+h, 0);
	}
	
}
