package elec332.gregsprospecting2.proxies;

import elec332.gregsprospecting2.client.keys.KeyHandler;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    @Override
    public void registerKeyBindings() {
        KeyHandler.instance.registerKeys();
    }
}
