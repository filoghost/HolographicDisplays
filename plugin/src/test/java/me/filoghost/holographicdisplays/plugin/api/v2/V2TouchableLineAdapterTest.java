/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.api.hologram.ClickListener;
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
        ExampleV3ClickListener v3ClickListener = new ExampleV3ClickListener();

        v3Line.setClickListener(v3ClickListener);
        v2Line.setTouchHandler(null);

        assertThat(v2Line.getTouchHandler()).isNull();
        assertThat(v3Line.getClickListener()).isNull();
    }

    @Test
    void setNullV3ClickListener() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);
        v3Line.setClickListener(null);

        assertThat(v2Line.getTouchHandler()).isNull();
        assertThat(v3Line.getClickListener()).isNull();
    }

    @Test
    void setThenGetV2TouchHandler() {
        TextLine v2Line = new APITextLine(hologram, "").getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
    }

    @Test
    void setV3ClickListenerThenGetV2TouchHandler() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3ClickListener v3ClickListener = new ExampleV3ClickListener();

        v3Line.setClickListener(v3ClickListener);

        assertThat(v2Line.getTouchHandler()).isNotNull();
    }

    @Test
    void setV2TouchHandlerThenGetV3ClickListener() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        assertThat(v3Line.getClickListener()).isNotNull();
    }

    @Test
    void repeatedGetV2TouchHandlerShouldReturnSameInstance() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();

        v3Line.setClickListener(new ExampleV3ClickListener());
        assertThat(v2Line.getTouchHandler()).isSameAs(v2Line.getTouchHandler());

        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        assertThat(v2Line.getTouchHandler()).isSameAs(v2Line.getTouchHandler());
    }

    @Test
    void repeatedSetV2TouchHandlerShouldNotChangeV3ClickListenerInstance() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);
        ClickListener v3ClickListenerA = v3Line.getClickListener();
        v2Line.setTouchHandler(v2TouchHandler);
        ClickListener v3ClickListenerB = v3Line.getClickListener();

        assertThat(v3ClickListenerA).isSameAs(v3ClickListenerB);
    }

    @Test
    void repeatedSetV3ClickListenerShouldNotChangeV2TouchHandlerInstance() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3ClickListener v2TouchHandler = new ExampleV3ClickListener();

        v3Line.setClickListener(v2TouchHandler);
        TouchHandler v2TouchHandlerA = v2Line.getTouchHandler();
        v3Line.setClickListener(v2TouchHandler);
        TouchHandler v2TouchHandlerB = v2Line.getTouchHandler();

        assertThat(v2TouchHandlerA).isSameAs(v2TouchHandlerB);
    }

    @Test
    void v2TouchHandlerChangesWhenV3ClickListenerChanges() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();

        v3Line.setClickListener(new ExampleV3ClickListener());
        TouchHandler v2TouchHandlerA = v2Line.getTouchHandler();
        v3Line.setClickListener(new ExampleV3ClickListener());
        TouchHandler v2TouchHandlerB = v2Line.getTouchHandler();

        assertThat(v2TouchHandlerA).isNotSameAs(v2TouchHandlerB);
    }

    @Test
    void v3ClickListenerChangesWhenV2TouchHandlerChanges() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();

        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        ClickListener v3ClickListenerA = v3Line.getClickListener();
        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        ClickListener v3ClickListenerB = v3Line.getClickListener();

        assertThat(v3ClickListenerA).isNotSameAs(v3ClickListenerB);
    }

    @Test
    void preserveInstancesWhenUsingV3API() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        ClickListener v3ClickListener = v3Line.getClickListener();
        v2Line.setTouchHandler(null); // This also clears the adapter
        v3Line.setClickListener(v3ClickListener); // Return to the initial value

        // Instances should be the same
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
        assertThat(v3Line.getClickListener()).isSameAs(v3ClickListener);
    }

    @Test
    void preserveInstancesWhenUsingV2API() {
        APITextLine v3Line = hologram.appendTextLine("");
        TextLine v2Line = v3Line.getV2Adapter();
        ExampleV3ClickListener v3ClickListener = new ExampleV3ClickListener();

        v3Line.setClickListener(v3ClickListener);

        TouchHandler v2TouchHandler = v2Line.getTouchHandler();
        v2Line.setTouchHandler(null); // This also clears the adapter
        v2Line.setTouchHandler(v2TouchHandler); // Return to the initial value

        // Instances should be the same
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
        assertThat(v3Line.getClickListener()).isSameAs(v3ClickListener);
    }


    private static class ExampleV3ClickListener implements ClickListener {

        @Override
        public void onClick(@NotNull Player player) {}

    }


    private static class ExampleV2TouchHandler implements TouchHandler {

        @Override
        public void onTouch(Player player) {}

    }

}
