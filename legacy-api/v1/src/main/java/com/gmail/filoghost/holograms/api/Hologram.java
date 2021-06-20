/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface Hologram {

    @Deprecated
    boolean update();

    @Deprecated
    void hide();

    @Deprecated
    void addLine(String text);

    @Deprecated
    void removeLine(int index);

    @Deprecated
    void setLine(int index, String text);

    @Deprecated
    void insertLine(int index, String text);

    @Deprecated
    String[] getLines();

    @Deprecated
    int getLinesLength();

    @Deprecated
    void clearLines();

    @Deprecated
    Location getLocation();

    @Deprecated
    double getX();

    @Deprecated
    double getY();

    @Deprecated
    double getZ();

    @Deprecated
    World getWorld();

    @Deprecated
    void setLocation(Location location);

    @Deprecated
    void teleport(Location location);

    @Deprecated
    void setTouchHandler(TouchHandler handler);

    @Deprecated
    TouchHandler getTouchHandler();

    @Deprecated
    boolean hasTouchHandler();

    @Deprecated
    long getCreationTimestamp();

    @Deprecated
    void delete();

    @Deprecated
    boolean isDeleted();

}
