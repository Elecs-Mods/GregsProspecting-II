package elec332.gregsprospecting2.client.keys;

import java.util.ArrayList;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingList {

	ArrayList <KeyBinding> bindings;
	ArrayList <Boolean> repeats;
	
	public KeyBindingList() {
		bindings = new ArrayList <KeyBinding> ();
		repeats = new ArrayList <Boolean> ();
	}
	
	public void add(KeyBinding binding) {
		add(binding, false);
	}

	public void add(KeyBinding binding, boolean repeat) {
		bindings.add(binding);
		repeats.add(repeat);
	}
	
	public KeyBinding[] getBindings() {
		return bindings.toArray(new KeyBinding[bindings.size()]);
	}
	
	public boolean[] getRepeats() {
		int n = bindings.size();
		boolean[] result = new boolean[n];
		for (int i = 0; i < n; i++)
			result[i] = repeats.get(i).booleanValue();
		return result;
	}

}
