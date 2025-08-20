package com.jerry.meklm.client.render.tile;

import com.jerry.meklm.common.base.LMProfilerConstants;
import com.jerry.meklm.common.tile.TileEntityLargeRotaryCondensentrator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.lib.Outlines;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NothingNullByDefault
public class RenderLargeRotaryCondensentrator extends MekanismTileEntityRenderer<TileEntityLargeRotaryCondensentrator> implements IWireFrameRenderer {

    @Nullable
    private static List<Outlines.Line> lines;

    public static void resetCached() {
        lines = null;
    }

    public RenderLargeRotaryCondensentrator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer) {

    }

    @Override
    protected void render(TileEntityLargeRotaryCondensentrator tileEntityLargeRotaryCondensentrator, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {

    }

    @Override
    protected String getProfilerSection() {
        return LMProfilerConstants.LARGE_ROTARY_CONDENSENTRATOR;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityLargeRotaryCondensentrator tile) {
        return true;
    }
}
