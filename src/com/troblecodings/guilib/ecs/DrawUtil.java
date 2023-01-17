package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.interfaces.IIntegerable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;

public final class DrawUtil {

    private DrawUtil() {
    }

    public static class DisableIntegerable<T> implements IIntegerable<T> {

        private final IIntegerable<T> integerable;
        private final String name;

        public DisableIntegerable(final IIntegerable<T> integerable) {
            this.integerable = integerable;
            this.name = this.integerable.getName();
        }

        public DisableIntegerable(final IIntegerable<T> integerable, final String customName) {
            this.integerable = integerable;
            this.name = customName;
        }

        @Override
        public T getObjFromID(final int obj) {
            if (obj < 0 || count() <= obj)
                return null;
            return integerable.getObjFromID(obj);
        }

        @Override
        public int count() {
            return integerable.count();
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getNamedObj(final int obj) {
            final T current = getObjFromID(obj);
            if (current == null)
                return getLocalizedName() + ": " + I18n.get("property.disabled.name");
            return integerable.getNamedObj(obj);
        }

    }

    public static class EnumIntegerable<T extends Enum<T>> implements IIntegerable<T> {

        private final Class<T> t;

        public EnumIntegerable(final Class<T> t) {
            this.t = t;
        }

        @Override
        public T getObjFromID(final int obj) {
            return t.getEnumConstants()[obj];
        }

        @Override
        public int count() {
            return t.getEnumConstants().length;
        }

        @Override
        public String getName() {
            return t.getSimpleName().toLowerCase();
        }
    }

    public static class NamedEnumIntegerable<T extends Enum<T>> extends EnumIntegerable<T> {

        private final String name;

        public NamedEnumIntegerable(final String name, final Class<T> t) {
            super(t);
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    public interface ObjGetter<D> {

        D getObjFrom(int x);
    }

    public static class SizeIntegerables<T> implements IIntegerable<T> {

        private final int count;
        private final ObjGetter<T> getter;
        private final String name;

        public SizeIntegerables(final String name, final int count, final ObjGetter<T> getter) {
            this.count = count;
            this.getter = getter;
            this.name = name;
        }

        @Override
        public T getObjFromID(final int obj) {
            return this.getter.getObjFrom(obj);
        }

        @Override
        public int count() {
            return count;
        }

        public static <T> IIntegerable<T> of(final String name, final int count,
                final ObjGetter<T> get) {
            return new SizeIntegerables<T>(name, count, get);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getNamedObj(final int obj) {
            return getLocalizedName() + ": " + this.getter.getObjFrom(obj);
        }
    }

    public static class BoolIntegerables implements IIntegerable<String> {

        private final String name;

        public BoolIntegerables(final String name) {
            this.name = name;
        }

        @Override
        public String getObjFromID(final int obj) {
            return obj == 0 ? "true" : "false";
        }

        @Override
        public int count() {
            return 2;
        }

        public static BoolIntegerables of(final String name) {
            return new BoolIntegerables(name);
        }

        @Override
        public String getName() {
            return this.name;
        }

    }

    public static class IntegerHolder {

        private int obj;

        public IntegerHolder(final int i) {
            this.setObj(i);
        }

        public int getObj() {
            return obj;
        }

        public void setObj(final int obj) {
            this.obj = obj;
        }

    }

    public static void draw(final BufferBuilder bufferBuilderIn) {
        BufferUploader.end(bufferBuilderIn); // TODO check if requires rerecording?
    }

    public static void addToBuffer(final BufferBuilder builder, final BlockModelShaper manager,
            final BlockState ebs) {
        addToBuffer(builder, manager, ebs, 0);
    }

    private static final Random random = new Random();

    @SuppressWarnings("deprecation")
    public static void addToBuffer(final BufferBuilder builder, final BlockModelShaper manager,
            final BlockState ebs, final int color) {
        assert ebs != null;
        final BakedModel mdl = manager.getBlockModel(ebs);
        final List<BakedQuad> lst = new ArrayList<>();
        lst.addAll(mdl.getQuads(ebs, null, random));
        for (final Direction face : Direction.values())
            lst.addAll(mdl.getQuads(ebs, face, random));

        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        final IVertexConsumer consumer = new VertexBufferConsumer(builder);
        for (final BakedQuad quad : lst) {
            final int k = quad.isTinted()
                    ? (blockColors.getColor(ebs, null, null, quad.getTintIndex()) + 0xFF000000)
                    : 0xFFFFFFFF;
            // TODO color
            LightUtil.putBakedQuad(consumer, quad);
        }
    }

    public static void drawCenteredString(DrawInfo info, final Font fontRendererIn,
            final String text, final int x, final int y, final int color) {
        fontRendererIn.drawShadow(info.stack, text, x - fontRendererIn.width(text) / 2, y, color);
    }
}
