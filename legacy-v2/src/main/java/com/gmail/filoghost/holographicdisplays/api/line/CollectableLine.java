/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface CollectableLine extends HologramLine {

    @Deprecated
    public void setPickupHandler(PickupHandler pickupHandler);
    
    @Deprecated
    public PickupHandler getPickupHandler();
    
}
