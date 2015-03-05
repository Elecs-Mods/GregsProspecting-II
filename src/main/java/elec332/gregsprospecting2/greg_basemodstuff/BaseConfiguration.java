//------------------------------------------------------
//
//   Greg's Mod Base - Configuration
//
//------------------------------------------------------

package elec332.gregsprospecting2.greg_basemodstuff;

import java.io.*;
import java.util.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.registry.*;

public class BaseConfiguration extends Configuration {

	public boolean extended = false;
	int nextVillagerID = 100;
	
	public BaseConfiguration(File file) {
		super(file);
	}
	
	public boolean getBoolean(String category, String key, boolean defaultValue) {
		return get(category, key, defaultValue).getBoolean(defaultValue);
	}
	
	public int getInteger(String category, String key, int defaultValue) {
		return get(category, key, defaultValue).getInt();
	}
	
	public int getVillager(String key, String skin) {
		VillagerRegistry reg = VillagerRegistry.instance();
		Property prop = get("villagers", key, -1);
		int id = prop.getInt();
		if (id == -1) {
			id = allocateVillagerId(reg);
			prop.set(id);
		}
		reg.registerVillagerId(id);
		return id;
	}
	
	int allocateVillagerId(VillagerRegistry reg) {
		Collection<Integer> inUse = VillagerRegistry.getRegisteredVillagers();
		for (;;) {
			int id = nextVillagerID++;
			if (!inUse.contains(id))
				return id;
		}
	}
	
	@Override
	public Property get(String category, String key, String defaultValue, String comment, Property.Type type) {
		if (!hasKey(category, key))
			extended = true;
		return super.get(category, key, defaultValue, comment, type);
	}

}
