/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.api.hologram.TouchHandler;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologram;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.api.APITextLine;
import me.filoghost.holographicdisplays.plugin.test.Mocks;
import me.filoghost.holographicdisplays.plugin.test.TestAPIHologramManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("deprecation")
class V2TouchableLineAdapterTest {

    APIHologramManager apiHologramManager = new TestAPIHologramManager();

    APIHologram hologram = apiHologramManager.createHologram(
            new Location(Mocks.WORLD, 0, 0, 0),
            Mocks.PLUGIN
    );

    @Test
    void setNullV2TouchHandler() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3TouchHandler v3TouchHandler = new ExampleV3TouchHandler();

        v3Line.setTouchHandler(v3TouchHandler);
        v2Line.setTouchHandler(null);

        assertThat(v2Line.getTouchHandler()).isNull();
        assertThat(v3Line.getTouchHandler()).isNull();
    }

    @Test
    void setNullV3TouchHandler() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);
        v3Line.setTouchHandler(null);

        assertThat(v2Line.getTouchHandler()).isNull();
        assertThat(v3Line.getTouchHandler()).isNull();
    }

    @Test
    void setV2GetV2TouchHandler() {
        TextLine v2Line = new APITextLine(hologram, "").getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
    }

    @Test
    void setV3GetV2TouchHandler() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3TouchHandler v3TouchHandler = new ExampleV3TouchHandler();

        v3Line.setTouchHandler(v3TouchHandler);

        assertThat(v2Line.getTouchHandler()).isNotNull();
    }

    @Test
    void setV2GetV3TouchHandler() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        assertThat(v3Line.getTouchHandler()).isNotNull();
    }

    @Test
    void repeatedGetV2TouchHandlerShouldReturnSameInstance() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();

        v3Line.setTouchHandler(new ExampleV3TouchHandler());
        assertThat(v2Line.getTouchHandler()).isSameAs(v2Line.getTouchHandler());

        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        assertThat(v2Line.getTouchHandler()).isSameAs(v2Line.getTouchHandler());
    }

    @Test
    void repeatedSetV2TouchHandlerShouldNotChangeV3TouchHandlerInstance() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);
        TouchHandler v3TouchHandlerA = v3Line.getTouchHandler();
        v2Line.setTouchHandler(v2TouchHandler);
        TouchHandler v3TouchHandlerB = v3Line.getTouchHandler();

        assertThat(v3TouchHandlerA).isSameAs(v3TouchHandlerB);
    }

    @Test
    void repeatedSetV3TouchHandlerShouldNotChangeV2TouchHandlerInstance() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3TouchHandler v2TouchHandler = new ExampleV3TouchHandler();

        v3Line.setTouchHandler(v2TouchHandler);
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerA = v2Line.getTouchHandler();
        v3Line.setTouchHandler(v2TouchHandler);
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerB = v2Line.getTouchHandler();

        assertThat(v2TouchHandlerA).isSameAs(v2TouchHandlerB);
    }

    @Test
    void v2TouchHandlerChangesWhenV3TouchHandlerChanges() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();

        v3Line.setTouchHandler(new ExampleV3TouchHandler());
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerA = v2Line.getTouchHandler();
        v3Line.setTouchHandler(new ExampleV3TouchHandler());
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerB = v2Line.getTouchHandler();

        assertThat(v2TouchHandlerA).isNotSameAs(v2TouchHandlerB);
    }

    @Test
    void v3TouchHandlerChangesWhenV2TouchHandlerChanges() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();

        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        TouchHandler v3TouchHandlerA = v3Line.getTouchHandler();
        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        TouchHandler v3TouchHandlerB = v3Line.getTouchHandler();

        assertThat(v3TouchHandlerA).isNotSameAs(v3TouchHandlerB);
    }

    @Test
    void preserveTouchHandlerInstancesWhenUsingV3API() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        TouchHandler v3TouchHandler = v3Line.getTouchHandler();
        v2Line.setTouchHandler(null); // This also clears the adapter
        v3Line.setTouchHandler(v3TouchHandler); // Return to the initial value

        // Instances should be the same
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
        assertThat(v3Line.getTouchHandler()).isSameAs(v3TouchHandler);
    }

    @Test
    void preserveTouchHandlerInstancesWhenUsingV2API() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3TouchHandler v3TouchHandler = new ExampleV3TouchHandler();

        v3Line.setTouchHandler(v3TouchHandler);

        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandler = v2Line.getTouchHandler();
        v2Line.setTouchHandler(null); // This also clears the adapter
        v2Line.setTouchHandler(v2TouchHandler); // Return to the initial value

        // Instances should be the same
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
        assertThat(v3Line.getTouchHandler()).isSameAs(v3TouchHandler);
    }


    private static class ExampleV3TouchHandler implements TouchHandler {

        @Override
        public void onTouch(@NotNull Player player) {}

    }


    private static class ExampleV2TouchHandler implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

        @Override
        public void onTouch(Player player) {}

    }

}
