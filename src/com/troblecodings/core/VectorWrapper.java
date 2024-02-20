package com.troblecodings.core;

import java.util.Objects;

import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

public class VectorWrapper {

    public static final VectorWrapper ZERO = new VectorWrapper(0, 0, 0);
    private static final String VECTOR_X = "vectorX";
    private static final String VECTOR_Y = "vectorY";
    private static final String VECTOR_Z = "vectorZ";

    private final int x, y, z;

    public VectorWrapper(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public VectorWrapper offset(final int offsetX, final int offsetY, final int offsetZ) {
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

    public Vector3i toMC3iVector() {
        return new Vector3i(z, y, z);
    }

    public void writeNetwork(final WriteBuffer buffer) {
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.putInt(z);
    }

    public void writeNBT(final NBTWrapper tag) {
        tag.putInteger(VECTOR_X, x);
        tag.putInteger(VECTOR_Y, y);
        tag.putInteger(VECTOR_Z, z);
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