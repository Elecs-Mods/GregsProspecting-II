package elec332.gregsprospecting2.client.keys;

import elec332.core.client.KeyHandlerBase;
import elec332.gregsprospecting2.lib.MiningRadarAction;
import elec332.gregsprospecting2.main.GregsProspectingII;
import elec332.gregsprospecting2.network.MiningRadarActionPacket;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

//
/**
 * Created by Elec332 on 6-3-2015.
 */
public class KeyHandler extends KeyHandlerBase{
    public static KeyHandler instance = new KeyHandler();

    public KeyBinding key1 = makeKeyBinding("IncreaseRange", Keyboard.KEY_RBRACKET);
    public KeyBinding key2 = makeKeyBinding("DecreaseRange", Keyboard.KEY_LBRACKET);
    public KeyBinding key3 = makeKeyBinding("SelectLinearMode", Keyboard.KEY_L);
    public KeyBinding key4 = makeKeyBinding("SelectDiscriminationMode", Keyboard.KEY_M);

    public void registerKeys(){
        KHB.registerKeyBinding(key1);
        KHB.registerKeyBinding(key2);
        KHB.registerKeyBinding(key3);
        KHB.registerKeyBinding(key4);
    }

    @Override
    public void performAction(KeyBinding key) {
        if (MiningRadarAction.valueOf(key.getKeyDescription()) != null)
            GregsProspectingII.networkHandler.getNetworkWrapper().sendToServer(new MiningRadarActionPacket(MiningRadarAction.valueOf(key.getKeyDescription())));
    }

    public KeyBinding makeKeyBinding(String name, int kb){
        return new KeyBinding(name, kb, "key.categories.gregsprospecting");
    }
}
