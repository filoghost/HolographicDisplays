package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.object.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class APIItemLine extends BaseItemLine implements ItemLine, APIHologramLine {

    private final APIHologram parent;

    public APIItemLine(APIHologram parent, ItemStack itemStack) {
        super(parent, itemStack);
        this.parent = parent;
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

}
