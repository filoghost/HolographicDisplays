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
package com.gmail.filoghost.holographicdisplays.nms.v1_8_R2;

import net.minecraft.server.v1_8_R2.AxisAlignedBB;
import net.minecraft.server.v1_8_R2.MovingObjectPosition;
import net.minecraft.server.v1_8_R2.Vec3D;

public class NullBoundingBox extends AxisAlignedBB {

	public NullBoundingBox() {
		super(0, 0, 0, 0, 0, 0);
	}

	@Override
	public double a() {
		return 0.0;
	}

	@Override
	public double a(AxisAlignedBB arg0, double arg1) {
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
	public MovingObjectPosition a(Vec3D arg0, Vec3D arg1) {
		return super.a(arg0, arg1);
	}

	@Override
	public boolean a(Vec3D arg0) {
		return false;
	}

	@Override
	public double b(AxisAlignedBB arg0, double arg1) {
		return 0.0;
	}

	@Override
	public boolean b(AxisAlignedBB arg0) {
		return false;
	}

	@Override
	public double c(AxisAlignedBB arg0, double arg1) {
		return 0.0;
	}

	@Override
	public AxisAlignedBB c(double arg0, double arg1, double arg2) {
		return this;
	}

	@Override
	public AxisAlignedBB grow(double arg0, double arg1, double arg2) {
		return this;
	}

	@Override
	public AxisAlignedBB shrink(double arg0, double arg1, double arg2) {
		return this;
	}

	

}
