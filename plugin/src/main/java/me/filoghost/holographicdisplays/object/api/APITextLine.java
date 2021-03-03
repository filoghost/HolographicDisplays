package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseTextLine;

public class APITextLine extends BaseTextLine implements TextLine, APIHologramLine {

    private final APIHologram parent;

    public APITextLine(APIHologram parent, NMSManager nmsManager, String text) {
        super(parent, nmsManager, text);
        this.parent = parent;
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }

    @Override
    protected boolean isAllowPlaceholders() {
        return parent.isAllowPlaceholders();
    }

}
