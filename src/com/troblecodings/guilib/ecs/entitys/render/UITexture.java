package com.troblecodings.guilib.ecs.entitys.render;

import org.lwjgl.opengl.GL11;

import com.troblecodings.guilib.ecs.entitys.BufferWrapper;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UITexture extends UIComponent {

    private final double u, v, mu, mv;
    private final ResourceLocation texture;

    public UITexture(final TextureAtlasSprite sprite) {
        this(sprite.atlas().location(), sprite.getU0(), sprite.getV0(), sprite.getU1(),
                sprite.getV1());
    }

    public UITexture(final ResourceLocation texture) {
        this(texture, 0, 0, 1, 1);
    }

    public UITexture(final ResourceLocation texture, final double u, final double v,
            final double maxU, final double maxV) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.mu = maxU;
        this.mv = maxV;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.depthOn();
        info.blendOn();
        info.applyTexture(texture);
        final double w = this.parent.getWidth();
        final double h = this.parent.getHeight();

        final BufferWrapper bufferbuilder = info.builder(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0, h, 0).tex((float) u, (float) mv).end();
        bufferbuilder.pos(w, h, 0).tex((float) mu, (float) mv).end();
        bufferbuilder.pos(w, 0, 0).tex((float) mu, (float) v).end();
        bufferbuilder.pos(0, 0, 0).tex((float) u, (float) v).end();
        info.end();
        info.blendOff();
        info.depthOff();
        info.disableTexture();
    }

    @Override
    public void update() {
    }

}
