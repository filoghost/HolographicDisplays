/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.legacy.api.v2;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.object.api.APIHologram;
import me.filoghost.holographicdisplays.object.api.APITextLine;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
class V2TouchableLineAdapterTest {

    APIHologram hologram = mock(APIHologram.class);

    @Test
    void setNullV2TouchHandler() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleNewTouchHandler newTouchHandler = new ExampleNewTouchHandler();

        newLine.setTouchHandler(newTouchHandler);
        v2Line.setTouchHandler(null);
        
        assertThat(v2Line.getTouchHandler()).isNull();
        assertThat(newLine.getTouchHandler()).isNull();
    }

    @Test
    void setNullNewTouchHandler() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();
        
        v2Line.setTouchHandler(v2TouchHandler);
        newLine.setTouchHandler(null);

        assertThat(v2Line.getTouchHandler()).isNull();
        assertThat(newLine.getTouchHandler()).isNull();
    }

    @Test
    void setV2GetV2TouchHandler() {
        TextLine v2Line = new APITextLine(hologram, "").getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);
        
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
    }

    @Test
    void setNewGetV2TouchHandler() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleNewTouchHandler newTouchHandler = new ExampleNewTouchHandler();
        
        newLine.setTouchHandler(newTouchHandler);
        
        assertThat(v2Line.getTouchHandler()).isNotNull();
    }

    @Test
    void setV2GetNewTouchHandler() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();

        v2Line.setTouchHandler(v2TouchHandler);

        assertThat(newLine.getTouchHandler()).isNotNull();
    }

    @Test
    void repeatedGetV2TouchHandlerShouldReturnSameInstance() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();

        newLine.setTouchHandler(new ExampleNewTouchHandler());
        assertThat(v2Line.getTouchHandler()).isSameAs(v2Line.getTouchHandler());

        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        assertThat(v2Line.getTouchHandler()).isSameAs(v2Line.getTouchHandler());
    }

    @Test
    void repeatedSetV2TouchHandlerShouldNotChangeNewTouchHandlerInstance() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();
        
        v2Line.setTouchHandler(v2TouchHandler);
        TouchHandler newTouchHandlerA = newLine.getTouchHandler();
        v2Line.setTouchHandler(v2TouchHandler);
        TouchHandler newTouchHandlerB = newLine.getTouchHandler();
        
        assertThat(newTouchHandlerA).isSameAs(newTouchHandlerB);
    }

    @Test
    void repeatedSetNewTouchHandlerShouldNotChangeV2TouchHandlerInstance() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleNewTouchHandler v2TouchHandler = new ExampleNewTouchHandler();

        newLine.setTouchHandler(v2TouchHandler);
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerA = v2Line.getTouchHandler();
        newLine.setTouchHandler(v2TouchHandler);
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerB = v2Line.getTouchHandler();

        assertThat(v2TouchHandlerA).isSameAs(v2TouchHandlerB);
    }

    @Test
    void v2TouchHandlerChangesWhenNewTouchHandlerChanges() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();

        newLine.setTouchHandler(new ExampleNewTouchHandler());
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerA = v2Line.getTouchHandler();
        newLine.setTouchHandler(new ExampleNewTouchHandler());
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandlerB = v2Line.getTouchHandler();
        
        assertThat(v2TouchHandlerA).isNotSameAs(v2TouchHandlerB);
    }

    @Test
    void newTouchHandlerChangesWhenV2TouchHandlerChanges() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();

        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        TouchHandler newTouchHandlerA = newLine.getTouchHandler();
        v2Line.setTouchHandler(new ExampleV2TouchHandler());
        TouchHandler newTouchHandlerB = newLine.getTouchHandler();

        assertThat(newTouchHandlerA).isNotSameAs(newTouchHandlerB);
    }

    @Test
    void preserveTouchHandlerInstancesWhenUsingNewAPI() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleV2TouchHandler v2TouchHandler = new ExampleV2TouchHandler();
        
        v2Line.setTouchHandler(v2TouchHandler);
        
        TouchHandler newTouchHandler = newLine.getTouchHandler();
        v2Line.setTouchHandler(null); // This also clears the adapter
        newLine.setTouchHandler(newTouchHandler); // Return to the initial value

        // Instances should be the same
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
        assertThat(newLine.getTouchHandler()).isSameAs(newTouchHandler);
    }

    @Test
    void preserveTouchHandlerInstancesWhenUsingV2API() {
        APITextLine newLine = new APITextLine(hologram, "");
        TextLine v2Line = newLine.getV2Adapter();
        ExampleNewTouchHandler newTouchHandler = new ExampleNewTouchHandler();
        
        newLine.setTouchHandler(newTouchHandler);
        
        com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler v2TouchHandler = v2Line.getTouchHandler();
        v2Line.setTouchHandler(null); // This also clears the adapter
        v2Line.setTouchHandler(v2TouchHandler); // Return to the initial value

        // Instances should be the same
        assertThat(v2Line.getTouchHandler()).isSameAs(v2TouchHandler);
        assertThat(newLine.getTouchHandler()).isSameAs(newTouchHandler);
    }


    private static class ExampleNewTouchHandler implements TouchHandler {

        @Override
        public void onTouch(@NotNull Player player) {}

    }
    
    
    private static class ExampleV2TouchHandler implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

        @Override
        public void onTouch(Player player) {}

    }

}
