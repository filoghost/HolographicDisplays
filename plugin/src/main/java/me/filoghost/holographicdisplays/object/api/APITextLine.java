package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.legacy.api.v2.V2TextLineAdapter;
import me.filoghost.holographicdisplays.object.base.BaseTextLine;

public class APITextLine extends BaseTextLine implements TextLine, APITouchableLine {

    private final APIHologram parent;
    private final V2TextLineAdapter v2Adapter;

    public APITextLine(APIHologram parent, String text) {
        super(parent, text);
        this.parent = parent;
        this.v2Adapter = new V2TextLineAdapter(this);
    }

    @Override
    public APIHologram getParent() {
        return parent;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return parent.isAllowPlaceholders();
    }

    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        v2Adapter.onNewTouchHandlerChange(getTouchHandler(), touchHandler);
        super.setTouchHandler(touchHandler);
    }
    
    @Override
    public V2TextLineAdapter getV2Adapter() {
        return v2Adapter;
    }

}
