/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EnumDirection.EnumAxis;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.Vec3D;

public class NullBoundingBox extends AxisAlignedBB {

    public NullBoundingBox() {
        super(0, 0, 0, 0, 0, 0);
    }

    @Override
    public double a() {
        return 0.0;
    }

    @Override
    public AxisAlignedBB a(AxisAlignedBB arg0) {
        return this;
    }

    @Override
    public AxisAlignedBB a(double arg0, double arg1, double arg2) {
        return this;
    }

    @Override
    public MovingObjectPosition b(Vec3D arg0, Vec3D arg1) {
        return null;
    }

    @Override
    public AxisAlignedBB grow(double arg0, double arg1, double arg2) {
        return this;
    }

    @Override
    public AxisAlignedBB shrink(double arg0) {
        return this;
    }
    
    @Override
    public AxisAlignedBB a(BlockPosition arg0) {
        return this;
    }

    @Override
    public boolean a(double arg0, double arg1, double arg2, double arg3, double arg4, double arg5) {
        return false;
    }

    @Override
    public boolean b(Vec3D arg0) {
        return false;
    }
    
    @Override
    public AxisAlignedBB g(double arg0) {
        return this;
    }

    @Override
    public AxisAlignedBB a(Vec3D arg0) {
        return this;
    }

    @Override
    public AxisAlignedBB b(AxisAlignedBB arg0) {
        return this;
    }

    @Override
    public AxisAlignedBB b(double arg0, double arg1, double arg2) {
        return this;
    }

    @Override
    public boolean c(AxisAlignedBB arg0) {
        return false;
    }

    @Override
    public AxisAlignedBB d(double arg0, double arg1, double arg2) {
        return this;
    }

    @Override
    public double a(EnumAxis arg0) {
        return 0.0;
    }

    @Override
    public MovingObjectPosition a(Vec3D arg0, Vec3D arg1, BlockPosition arg2) {
        return null;
    }

    @Override
    public double b(EnumAxis arg0) {
        return 0.0;
    }

    @Override
    public boolean e(double arg0, double arg1, double arg2) {
        return false;
    }

    @Override
    public AxisAlignedBB f(double arg0, double arg1, double arg2) {
        return this;
    }
    
    
    

}
