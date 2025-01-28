package com.troblecodings.core;

import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Quaternion;

public class QuaternionWrapper {

    public static final Quaternion ONE = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);

    public static Quaternion fromXYZ(final float rotateX, final float rotateY,
            final float rotateZ) {
        final Quaternion quaternion = new Quaternion();
        Quaternion.mul(quaternion, new Quaternion((float) Math.sin(rotateX / 2.0F), 0.0F, 0.0F,
                (float) Math.cos(rotateX / 2.0F)), quaternion);
        Quaternion.mul(quaternion, new Quaternion(0.0F, (float) Math.sin(rotateY / 2.0F), 0.0F,
                (float) Math.cos(rotateY / 2.0F)), quaternion);
        Quaternion.mul(quaternion, new Quaternion(0.0F, 0.0F, (float) Math.sin(rotateZ / 2.0F),
                (float) Math.cos(rotateZ / 2.0F)), quaternion);
        return quaternion;
    }

    public static Vector3f toYXZ(final Quaternion q) {
        float f = q.getW() * q.getW();
        float f1 = q.getX() * q.getX();
        float f2 = q.getY() * q.getY();
        float f3 = q.getZ() * q.getZ();
        float f4 = f + f1 + f2 + f3;
        float f5 = 2.0F * q.getW() * q.getX() - 2.0F * q.getY() * q.getZ();
        float f6 = (float) Math.asin((double) (f5 / f4));
        return Math.abs(f5) > 0.999F * f4
                ? new Vector3f(f6, 2.0F * (float) Math.atan2((double) q.getY(), (double) q.getW()),
                        0.0F)
                : new Vector3f(f6,
                        (float) Math.atan2(
                                (double) (2.0F * q.getX() * q.getZ() + 2.0F * q.getY() * q.getW()),
                                (double) (f - f1 - f2 + f3)),
                        (float) Math.atan2(
                                (double) (2.0F * q.getX() * q.getY() + 2.0F * q.getW() * q.getZ()),
                                (double) (f - f1 + f2 - f3)));
    }

}
