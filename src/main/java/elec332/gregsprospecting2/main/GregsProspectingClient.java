//------------------------------------------------------------------------------------------------
//
//   Greg's Prospecting - Client Proxy
//
//------------------------------------------------------------------------------------------------

package elec332.gregsprospecting2.main;

import elec332.gregsprospecting2.client.keys.KeyBindingHandler;
import elec332.gregsprospecting2.client.keys.KeyBindingList;
import elec332.gregsprospecting2.client.keys.MiningRadarKeyBinding;
import elec332.gregsprospecting2.client.render.RenderMiningRadar;
import elec332.gregsprospecting2.client.render.RenderSlimophone;
import elec332.gregsprospecting2.lib.MiningRadarAction;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import net.minecraftforge.client.IItemRenderer;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GregsProspectingClient extends BaseModClient {

	public GregsProspectingClient(BaseMod mod) {
		super(mod);
		registerKeys();
	}
	
	@Override
	public void registerRenderers() {
		IItemRenderer rmr = new RenderMiningRadar();
		addItemRenderer(GregsProspecting.miningRadar, rmr);
		addItemRenderer(GregsProspecting.colorMiningRadar, rmr);
		addItemRenderer(GregsProspecting.brokenMiningRadar, rmr);
		addItemRenderer(GregsProspecting.slimophone, new RenderSlimophone());
	}
	
	public void registerKeys() {
		KeyBindingList keys = new KeyBindingList();
		bindKeys(keys);
		KeyBindingRegistry.registerKeyBinding(new KeyBindingHandler(keys));
	}
	
	void bindKeys(KeyBindingList keys) {
		bindKey(keys, "miningRadar.decRange", MiningRadarAction.DecreaseRange, Keyboard.KEY_LBRACKET, "Reduce Mining Radar Range");
		bindKey(keys, "miningRadar.incRange", MiningRadarAction.IncreaseRange, Keyboard.KEY_RBRACKET, "Increase Mining Radar Range");
		bindKey(keys, "miningRadar.linearMode", MiningRadarAction.SelectLinearMode, Keyboard.KEY_L, "Mining Radar Linear Mode");
		bindKey(keys, "miningRadar.discMode", MiningRadarAction.SelectDiscriminationMode, Keyboard.KEY_M, "Mining Radar Discrimination Mode");
	}

	void bindKey(KeyBindingList keys, String name, MiningRadarAction action, int defaultKey, String title) {
		String cfgName = "key." + name;
		String bindingName = "key.gcewing." + name;
		int key = base.config.getInteger("keys", cfgName, defaultKey);
		keys.add(new MiningRadarKeyBinding(bindingName, key, action));
		LanguageRegistry.instance().addStringLocalization(bindingName, title);
	}

}
