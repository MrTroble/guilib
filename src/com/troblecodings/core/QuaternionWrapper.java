package com.troblecodings.core;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class QuaternionWrapper {

    public static final Quaternion ONE = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);

    public static Quaternion fromXYZ(final float p_175229_, final float p_175230_,
            final float p_175231_) {
        Quaternion quaternion = ONE.copy();
        quaternion.mul(new Quaternion((float) Math.sin((double) (p_175229_ / 2.0F)), 0.0F, 0.0F,
                (float) Math.cos((double) (p_175229_ / 2.0F))));
        quaternion.mul(new Quaternion(0.0F, (float) Math.sin((double) (p_175230_ / 2.0F)), 0.0F,
                (float) Math.cos((double) (p_175230_ / 2.0F))));
        quaternion.mul(new Quaternion(0.0F, 0.0F, (float) Math.sin((double) (p_175231_ / 2.0F)),
                (float) Math.cos((double) (p_175231_ / 2.0F))));
        return quaternion;
    }

    public static Vector3f toYXZ(final Quaternion q) {
        float f = q.r() * q.r();
        float f1 = q.i() * q.i();
        float f2 = q.j() * q.j();
        float f3 = q.k() * q.k();
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
