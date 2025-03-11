package com.troblecodings.core;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class QuaternionWrapper {

    public static Quaternion fromXYZ(final float rotateX, final float rotateY,
            final float rotateZ) {
        final Quaternion quaternion = Quaternion.ONE.copy();
        quaternion.mul(new Quaternion((float) Math.sin(rotateX / 2.0F), 0.0F, 0.0F,
                (float) Math.cos(rotateX / 2.0F)));
        quaternion.mul(new Quaternion(0.0F, (float) Math.sin(rotateY / 2.0F), 0.0F,
                (float) Math.cos(rotateY / 2.0F)));
        quaternion.mul(new Quaternion(0.0F, 0.0F, (float) Math.sin(rotateZ / 2.0F),
                (float) Math.cos(rotateZ / 2.0F)));
        return quaternion;
    }

    public static Vector3f toYXZ(final Quaternion q) {
        float f = q.i() * q.i();
        float f1 = q.j() * q.j();
        float f2 = q.k() * q.k();
        float f3 = q.r() * q.r();
        float f4 = f + f1 + f2 + f3;
        float f5 = 2.0F * q.r() * q.i() - 2.0F * q.j() * q.k();
        float f6 = (float) Math.asin((double) (f5 / f4));
        return Math.abs(f5) > 0.999F * f4
                ? new Vector3f(f6, 2.0F * (float) Math.atan2((double) q.j(), (double) q.r()), 0.0F)
                : new Vector3f(f6,
                        (float) Math.atan2((double) (2.0F * q.i() * q.k() + 2.0F * q.j() * q.r()),
                                (double) (f - f1 - f2 + f3)),
                        (float) Math.atan2((double) (2.0F * q.i() * q.j() + 2.0F * q.r() * q.k()),
                                (double) (f - f1 + f2 - f3)));
    }
}
