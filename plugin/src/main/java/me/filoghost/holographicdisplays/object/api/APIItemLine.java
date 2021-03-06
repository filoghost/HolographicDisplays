package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class APIItemLine extends BaseItemLine implements ItemLine, APIHologramLine {

    private final APIHologram parent;

    public APIItemLine(APIHologram parent, NMSManager nmsManager, ItemStack itemStack) {
        super(parent, nmsManager, itemStack);
        this.parent = parent;
    }

    @Override
    public APIHologram getParent() {
        return parent;
    }

}
