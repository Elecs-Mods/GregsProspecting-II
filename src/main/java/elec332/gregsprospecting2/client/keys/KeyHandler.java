package elec332.gregsprospecting2.client.keys;

import elec332.core.client.KeyHandlerBase;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

//
/**
 * Created by Elec332 on 6-3-2015.
 */
public class KeyHandler extends KeyHandlerBase{
    public static KeyHandler instance = new KeyHandler();

    public KeyBinding key1 = new KeyBinding("SwitchMode??", Keyboard.KEY_B, "key.categories.gregsprospecting");

    public void registerKeys(){
        KHB.registerKeyBinding(key1);
    }

    @Override
    public void performAction(KeyBinding key) {
        if (key == key1){
            //do stuff
        }
    }
}
