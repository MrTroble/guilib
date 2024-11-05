package com.troblecodings.core;

import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Quaternion;

public class QuaternionWrapper {

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
        float f = q.w * q.w;
        float f1 = q.x * q.x;
        float f2 = q.y * q.y;
        float f3 = q.z * q.z;
        float f4 = f + f1 + f2 + f3;
        float f5 = 2.0F * q.w * q.y - 2.0F * q.y * q.z;
        float f6 = (float) Math.asin((double) (f5 / f4));
        return Math.abs(f5) > 0.999F * f4
                ? new Vector3f(f6, 2.0F * (float) Math.atan2((double) q.y, (double) q.w), 0.0F)
                : new Vector3f(f6,
                        (float) Math.atan2((double) (2.0F * q.x * q.z + 2.0F * q.y * q.w),
                                (double) (f - f1 - f2 + f3)),
                        (float) Math.atan2((double) (2.0F * q.x * q.y + 2.0F * q.w * q.z),
                                (double) (f - f1 + f2 - f3)));
    }

}
