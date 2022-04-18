package eu.gir.guilib.ecs.entitys.render;

import eu.gir.guilib.ecs.entitys.UIComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UITexture extends UIComponent {

    private final ResourceLocation texture;
    private final double u, v, mu, mv;

    public UITexture(final TextureAtlasSprite sprite) {
        this.texture = TextureMap.LOCATION_BLOCKS_TEXTURE;
        this.u = sprite.getMinU();
        this.v = sprite.getMinV();
        this.mu = sprite.getMaxU();
        this.mv = sprite.getMaxV();
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
    public void draw(final int mouseX, final int mouseY) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(this.texture);

        final double w = this.parent.getWidth();
        final double h = this.parent.getHeight();

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0, h, 0).tex(u, mv).endVertex();
        bufferbuilder.pos(w, h, 0).tex(mu, mv).endVertex();
        bufferbuilder.pos(w, 0, 0).tex(mu, v).endVertex();
        bufferbuilder.pos(0, 0, 0).tex(u, v).endVertex();
        tessellator.draw();
    }

    @Override
    public void update() {
    }

}
