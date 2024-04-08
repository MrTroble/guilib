package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

import com.troblecodings.core.QuaternionWrapper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class UIMultiBlockRender extends UIComponent {

    private final List<UIBlockRenderInfo> renderInfos = new ArrayList<>();
    private final BufferBuilder buffer = new BufferBuilder(5000);
    private final float scale;
    private final float height;
    private final Quaternion quaternion = QuaternionWrapper.fromXYZ(0.0f, (float) Math.PI, 0.0f);

    public UIMultiBlockRender(final float scale, final float height) {
        this.scale = scale;
        this.height = height;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.applyTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableRescaleNormal();
        info.depthOn();
        info.alphaOn();
        info.scale(scale, -scale, scale);
        info.translate(1.5, 0, 1.5);
        info.rotate(this.quaternion);
        info.translate(-0.5, this.height, -0.5);
        info.drawBuffer(buffer);
        info.alphaOff();
        info.depthOff();
        GlStateManager.disableRescaleNormal();
    }

    public void updateRotation(final Quaternion quaternion) {
        Quaternion.mul(this.quaternion, quaternion, this.quaternion);
    }

    @Override
    public void update() {
    }

    public void clear() {
        renderInfos.clear();
        updateBuffer();
    }

    public void setBlockState(final UIBlockRenderInfo info) {
        if (!renderInfos.contains(info)) {
            renderInfos.add(info);
            updateBuffer();
        }
    }

    private void updateBuffer() {
        final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        for (final UIBlockRenderInfo info : renderInfos) {
            final IBlockState ebs = info.wrapper.getBlockState();
            assert ebs != null;
            buffer.setTranslation(info.vector.getX(), info.vector.getY(), info.vector.getZ());
            final List<BakedQuad> lst = new ArrayList<>();
            lst.addAll(info.model.getQuads(ebs, null, 0));
            for (final EnumFacing face : EnumFacing.VALUES)
                lst.addAll(info.model.getQuads(ebs, face, 0));

            for (final BakedQuad quad : lst) {
                final int k = quad.hasTintIndex()
                        ? (blockColors.colorMultiplier(info.state, null, null, quad.getTintIndex())
                                + 0xFF000000)
                        : 0xFFFFFFFF;
                LightUtil.renderQuadColor(buffer, quad, k);
            }
        }
        buffer.finishDrawing();
    }
}