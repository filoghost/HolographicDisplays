/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common;

public final class Position {

    private final double x, y, z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Position addY(double y) {
        if (y == 0) {
            return this;
        }

        return new Position(this.x, this.y + y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Position other = (Position) obj;
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y)
                && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        result = 31 * result + Double.hashCode(z);
        return result;
    }

}
