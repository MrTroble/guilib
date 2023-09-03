package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.property.IExtendedBlockState;

public class UIBlockRender extends UIComponent {

    private final BufferBuilder buffer = new BufferBuilder(500);

    private final Quaternion quaternion = new Quaternion(0.0f, (float) Math.PI, 0.0f, 0);
    private final float scale;
    private final float height;

    public UIBlockRender(final float scale, final float height) {
        this.scale = scale;
        this.height = height;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.scale(scale, -scale, scale);
        info.translate(1.5, 0, 1.5);
        info.rotate(this.quaternion);
        info.translate(-0.5, this.height, -0.5);
        info.drawBuffer(buffer);
    }

    public void updateRotation(final Quaternion quaternion) {
        this.quaternion.set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }

    @Override
    public void update() {
    }

    public void setBlockState(final BlockModelDataWrapper wrapper) {
        this.setBlockState(wrapper, 0, 0, 0);
    }

    public void setBlockState(final BlockModelDataWrapper wrapper, final double x, final double y,
            final double z) {
        applyState(buffer, wrapper);
    }

    private static void applyState(final BufferBuilder buffer,
            final BlockModelDataWrapper wrapper) {
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        final IExtendedBlockState state = wrapper.getIExtendedBlockState();
        final IBakedModel mdl = Minecraft.getMinecraft().getBlockRendererDispatcher()
                .getBlockModelShapes().getModelForState(state);
        final List<BakedQuad> lst = new ArrayList<>();
        lst.addAll(mdl.getQuads(state, null, 0));
        for (final EnumFacing face : EnumFacing.values())
            lst.addAll(mdl.getQuads(state, face, 0));

        final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        for (final BakedQuad quad : lst) {
            final int k = quad.hasTintIndex()
                    ? (blockColors.colorMultiplier(state, null, null, quad.getTintIndex())
                            + 0xFF000000)
                    : 0xFFFFFFFF;
            LightUtil.renderQuadColor(buffer, quad, 0 + k);
        }
        buffer.finishDrawing();
    }

}
