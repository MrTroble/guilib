package com.troblecodings.guilib.ecs.entitys.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UITexture extends UIComponent {

    private final double u, v, mu, mv;
    private final AbstractTexture texture;

    @SuppressWarnings("deprecation")
	public UITexture(final TextureAtlasSprite sprite) {
        this(TextureAtlas.LOCATION_BLOCKS, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1());
    }

    public UITexture(final ResourceLocation texture) {
        this(texture, 0, 0, 1, 1);
    }

    public UITexture(final ResourceLocation texture, final double u, final double v,
            final double maxU, final double maxV) {
        Minecraft mc = Minecraft.getInstance();
        this.texture = mc.getTextureManager().getTexture(texture);
        this.u = u;
        this.v = v;
        this.mu = maxU;
        this.mv = maxV;
    }

    @Override
    public void draw(final DrawInfo info) {
    	this.texture.bind();
        final double w = this.parent.getWidth();
        final double h = this.parent.getHeight();

        final Tesselator tessellator = Tesselator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0, h, 0).uv((float)u, (float)mv).endVertex();
        bufferbuilder.vertex(w, h, 0).uv((float)mu, (float)mv).endVertex();
        bufferbuilder.vertex(w, 0, 0).uv((float)mu, (float)v).endVertex();
        bufferbuilder.vertex(0, 0, 0).uv((float)u, (float)v).endVertex();
        tessellator.end();
    }

    @Override
    public void update() {
    }

}
