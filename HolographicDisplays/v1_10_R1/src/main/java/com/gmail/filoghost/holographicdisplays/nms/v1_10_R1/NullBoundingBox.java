package com.gmail.filoghost.holographicdisplays.nms.v1_10_R1;

import net.minecraft.server.v1_10_R1.AxisAlignedBB;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.MovingObjectPosition;
import net.minecraft.server.v1_10_R1.Vec3D;

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
	public MovingObjectPosition b(Vec3D arg0, Vec3D arg1) {
		return null;
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
	public boolean c(Vec3D arg0) {
		return false;
	}

	@Override
	public boolean d(Vec3D arg0) {
		return false;
	}

	@Override
	public AxisAlignedBB e(double arg0) {
		return this;
	}
	
	@Override
	public AxisAlignedBB g(double arg0) {
		return this;
	}

}