package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.legacy.api.v2.V2ItemLineAdapter;
import me.filoghost.holographicdisplays.object.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class APIItemLine extends BaseItemLine implements ItemLine, APITouchableLine {

    private final APIHologram parent;
    private final V2ItemLineAdapter v2Adapter;

    public APIItemLine(APIHologram parent, ItemStack itemStack) {
        super(parent, itemStack);
        this.parent = parent;
        this.v2Adapter = new V2ItemLineAdapter(this);
    }

    @Override
    public APIHologram getParent() {
        return parent;
    }

    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        v2Adapter.onNewTouchHandlerChange(getTouchHandler(), touchHandler);
        super.setTouchHandler(touchHandler);
    }

    @Override
    public void setPickupHandler(PickupHandler pickupHandler) {
        v2Adapter.onNewPickupHandlerChange(getPickupHandler(), pickupHandler);
        super.setPickupHandler(pickupHandler);
    }
    
    @Override
    public V2ItemLineAdapter getV2Adapter() {
        return v2Adapter;
    }
    
}
