package com.troblecodings.core;

import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;

public class VectorWrapper {

    public static final VectorWrapper ZERO = new VectorWrapper(0, 0, 0);
    private static final String VECTOR_X = "vectorX";
    private static final String VECTOR_Y = "vectorY";
    private static final String VECTOR_Z = "vectorZ";

    private final float x, y, z;

    public VectorWrapper(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public VectorWrapper offset(final float offsetX, final float offsetY, final float offsetZ) {
        return new VectorWrapper(this.x + offsetX, this.y + offsetY, this.z + offsetZ);
    }

    public BlockPos addToPos(final BlockPos pos) {
        return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
    }

    public VectorWrapper subtract(final VectorWrapper vec) {
        return offset(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    public VectorWrapper relative(final Direction direction, final int steps) {
        return steps == 0 ? this
                : new VectorWrapper(this.x + direction.getStepX() * steps,
                        this.y + direction.getStepY() * steps,
                        this.z + direction.getStepZ() * steps);
    }

    public VectorWrapper above() {
        return this.above(1);
    }

    public VectorWrapper above(final int steps) {
        return this.relative(Direction.UP, steps);
    }

    public VectorWrapper below() {
        return this.below(1);
    }

    public VectorWrapper below(final int steps) {
        return this.relative(Direction.DOWN, steps);
    }

    public VectorWrapper north() {
        return this.north(1);
    }

    public VectorWrapper north(final int steps) {
        return this.relative(Direction.NORTH, steps);
    }

    public VectorWrapper south() {
        return this.south(1);
    }

    public VectorWrapper south(final int steps) {
        return this.relative(Direction.SOUTH, steps);
    }

    public VectorWrapper west() {
        return this.west(1);
    }

    public VectorWrapper west(final int steps) {
        return this.relative(Direction.WEST, steps);
    }

    public VectorWrapper east() {
        return this.east(1);
    }

    public VectorWrapper east(final int steps) {
        return this.relative(Direction.EAST, steps);
    }

    public VectorWrapper relative(final Direction direction) {
        return this.relative(direction, 1);
    }

    public VectorWrapper addOnAxis(final Axis axis, final int steps) {
        switch (axis) {
            case X:
                return new VectorWrapper(this.x + steps, this.y, this.z);
            case Y:
                return new VectorWrapper(this.x, this.y + steps, this.z);
            case Z:
                return new VectorWrapper(this.x, this.y, this.z + steps);
            default:
                return this;
        }
    }

    public Vec3i toMC3iVector() {
        return new Vec3i(x, y, z);
    }

    public void writeNetwork(final WriteBuffer buffer) {
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
    }

    public void writeNBT(final NBTWrapper tag) {
        tag.putFloat(VECTOR_X, x);
        tag.putFloat(VECTOR_Y, y);
        tag.putFloat(VECTOR_Z, z);
    }

    public VectorWrapper copy() {
        return new VectorWrapper(x, y, z);
    }

    public static VectorWrapper of(final ReadBuffer buffer) {
        return new VectorWrapper(buffer.getInt(), buffer.getInt(), buffer.getInt());
    }

    public static VectorWrapper of(final NBTWrapper tag) {
        return new VectorWrapper(tag.getInteger(VECTOR_X), tag.getInteger(VECTOR_Y),
                tag.getInteger(VECTOR_Z));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VectorWrapper other = (VectorWrapper) obj;
        return x == other.x && y == other.y && z == other.z;
    }

}