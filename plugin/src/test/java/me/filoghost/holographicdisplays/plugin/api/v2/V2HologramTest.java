/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.test.Mocks;
import me.filoghost.holographicdisplays.plugin.test.TestV2HologramManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("deprecation")
class V2HologramTest {

    V2HologramManager hologramManager = new TestV2HologramManager();

    V2Hologram hologram = hologramManager.createHologram(
            new ImmutablePosition("world", 0, 0, 0),
            Mocks.PLUGIN
    );

    @BeforeAll
    static void beforeAll() {
        Mocks.prepareEnvironment();
    }

    @Test
    void setGetTouchHandler() {
        TextLine textLine = hologram.appendTextLine("");

        TouchHandler touchHandler = player -> {};
        textLine.setTouchHandler(touchHandler);
        assertThat(textLine.getTouchHandler()).isSameAs(touchHandler);

        textLine.setTouchHandler(null);
        assertThat(textLine.getTouchHandler()).isNull();
    }

    @Test
    void setGetPickupHandler() {
        ItemLine itemLine = hologram.appendItemLine(new ItemStack(Material.ARROW));

        PickupHandler pickupHandler = player -> {};
        itemLine.setPickupHandler(pickupHandler);
        assertThat(itemLine.getPickupHandler()).isSameAs(pickupHandler);

        itemLine.setPickupHandler(null);
        assertThat(itemLine.getPickupHandler()).isNull();
    }

}
