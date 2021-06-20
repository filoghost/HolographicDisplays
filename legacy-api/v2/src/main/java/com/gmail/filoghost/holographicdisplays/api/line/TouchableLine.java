/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface TouchableLine extends HologramLine {

    @Deprecated
    void setTouchHandler(TouchHandler touchHandler);

    @Deprecated
    TouchHandler getTouchHandler();

}
