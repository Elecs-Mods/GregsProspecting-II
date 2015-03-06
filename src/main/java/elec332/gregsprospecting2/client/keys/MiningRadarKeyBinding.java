package elec332.gregsprospecting2.client.keys;

import elec332.gregsprospecting2.lib.MiningRadarAction;
import net.minecraft.client.settings.KeyBinding;

public class MiningRadarKeyBinding extends KeyBinding {

	public MiningRadarAction action;

	public MiningRadarKeyBinding(String name, int value, MiningRadarAction action) {
		super(name, value, null);
		this.action = action;
	}

}
