//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base - Generic Client Proxy
//
//------------------------------------------------------------------------------------------------

package elec332.gregsprospecting2.greg_basemodstuff;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import net.minecraftforge.client.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.client.registry.*;

public class BaseModClient implements IGuiHandler {

//	interface IRenderType {
//		public void setRenderType(int id);
//	}

	static class IDBinding<T> {
		public int id;
		public T object;
	}
	
	static class BRBinding extends IDBinding<ISimpleBlockRenderingHandler> {}
	
	static Map<String, BRBinding>
		blockRenderers = new HashMap<String, BRBinding>();

	public Minecraft mc;
	BaseMod base;

	Map<Integer, Class<? extends GuiScreen>> screenClasses =
		new HashMap<Integer, Class<? extends GuiScreen>>();

	public BaseModClient(BaseMod mod) {
		//System.out.printf("%s: BaseModClient()\n", this);
		base = mod;
		mc = ModLoader.getMinecraftInstance();
		//loadDependentClasses();
		registerScreens();
		registerRenderers();
		registerSounds();
		registerOther();
		registerImplicitBlockRenderers();
	}
	
	void registerImplicitBlockRenderers() {
		for (BaseMod.IBlock block : base.registeredBlocks) {
			String name = block.getQualifiedRendererClassName();
			if (name != null) {
				BRBinding b = getBlockRendererForName(name);
				if (b != null) {
					//System.out.printf("BaseModClient: Binding renderer id %s to %s\n", b.id, block);
					block.setRenderType(b.id);
				}
			}
		}
	}
	
	BRBinding getBlockRendererForName(String name) {
		//System.out.printf("BaseModClient: Getting block renderer class %s\n", name);
		BRBinding b = blockRenderers.get(name);
		if (b == null) {
			//System.out.printf("BaseModClient: Loading block renderer class %s\n", name);
			Class cls;
			ISimpleBlockRenderingHandler h;
			try {
				cls = Class.forName(name);
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(String.format("Block renderer class %s not found", name));
			}
			try {
				h = (ISimpleBlockRenderingHandler)cls.newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			b = new BRBinding();
			b.id = RenderingRegistry.getNextAvailableRenderId();
			b.object = h;
			RenderingRegistry.registerBlockHandler(b.id, h);
			blockRenderers.put(name, b);
		}
		return b;
	}
	
//	boolean classIsPresent(String name) {
//		try {
//			Class.forName(name);
//			return true;
//		}
//		catch (ClassNotFoundException e) {
//			return false;
//		}
//	}
	
	String qualifyName(String name) {
		return getClass().getPackage().getName() + "." + name;
	}
	
	void registerOther() {}
	
	//-------------- Screen registration --------------------------------------------------------
	
	void registerScreens() {
		//
		//  Make calls to addScreen() here.
		//
		//  Screen classes registered using these methods must implement either:
		//
		//  (1) A static method create(EntityPlayer player, World world, int x, int y, int z)
		//  (2) A constructor MyScreen(EntityPlayer player, World world, int x, int y, int z)
		//
		//System.out.printf("%s: BaseModClient.registerScreens\n", this);
	}
	
	public void addScreen(Enum id, Class<? extends GuiScreen> cls) {
		addScreen(id.ordinal(), cls);
	}

	public void addScreen(int id, Class<? extends GuiScreen> cls) {
		screenClasses.put(id, cls);
	}
	
	//-------------- Renderer registration --------------------------------------------------------
	
	void registerRenderers() {
		// Make calls to addBlockRenderer(), addItemRenderer() and addTileEntityRenderer() here
	}

	void addBlockRenderer(BaseMod.IBlock block, ISimpleBlockRenderingHandler renderer) {
		addBlockRenderer(renderer, block);
	}
	
	void addBlockRenderer(ISimpleBlockRenderingHandler renderer, BaseMod.IBlock... blocks) {
		int renderID = RenderingRegistry.getNextAvailableRenderId();
		for (BaseMod.IBlock block : blocks) {
			System.out.printf("BaseModClient: Registering %s with id %s for %s\n", renderer, renderID, block);
			block.setRenderType(renderID);
			RenderingRegistry.registerBlockHandler(renderID, renderer);
		}
	}
	
	void addItemRenderer(Item item, IItemRenderer renderer) {
		MinecraftForgeClient.registerItemRenderer(item.itemID, renderer);
	}
	
	void addItemRenderer(Block block, IItemRenderer renderer) {
		MinecraftForgeClient.registerItemRenderer(block.idDropped(0, null, 0), renderer);
	}
	
	void addTileEntityRenderer(Class <? extends TileEntity> teClass, TileEntitySpecialRenderer renderer) {
		ClientRegistry.bindTileEntitySpecialRenderer(teClass, renderer);
	}
	
	void addEntityRenderer(Class<? extends Entity> entityClass, Render renderer) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
	}
	
	//-------------- Rendering --------------------------------------------------------
	
	public void bindTexture(String path) {
		ResourceLocation rsrc = base.resourceLocation("textures/" + path);
		TextureManager tm = Minecraft.getMinecraft().func_110434_K();
		tm.func_110577_a(rsrc);
	}
	
	//-------------- Internal --------------------------------------------------------
	
	void registerSounds() {
		try {
			//System.out.printf("BaseModClient.registerSounds\n");
			SoundPool pool = mc.sndManager.soundPoolSounds;
			String namePrefix = base.modPackage.replace(".", "/") + "/";
			String subdir = "sounds";
			Set<String> items = base.listResources(subdir);
			for (String item : items) {
				String soundName = base.modPackage + ":" + item;
				System.out.printf("BaseModClient.registerSounds: %s\n", soundName);
				pool.addSound(soundName);
				//String soundName = namePrefix + item;
				//URL soundURL = new URL(base.resourceURL, subdir + "/" + item);
				////System.out.printf("BaseModClient.registerSounds: name = %s url = %s\n", soundName, soundURL);
				//pool.addSound(soundName, soundURL);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns a Container to be displayed to the user. 
	 * On the client side, this needs to return a instance of GuiScreen
	 * On the server side, this needs to return a instance of Container
	 *
	 * @param ID The Gui ID Number
	 * @param player The player viewing the Gui
	 * @param world The current world
	 * @param x X Position
	 * @param y Y Position
	 * @param z Z Position
	 * @return A GuiScreen/Container to be displayed to the user, null if none.
	 */
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return base.getServerGuiElement(id, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Class cls = screenClasses.get(id);
		if (cls != null)
			return base.createGuiElement(cls, player, world, x, y, z);
		else
			return getGuiScreen(id, player, world, x, y, z);
	}
	
	GuiScreen getGuiScreen(int id, EntityPlayer player, World world, int x, int y, int z) {
		//  Called when screen id not found in registry
		System.out.printf("%s: BaseModClient.getGuiScreen: No GuiScreen class found for gui id %d\n", 
			this, id);
		return null;
	}

}
