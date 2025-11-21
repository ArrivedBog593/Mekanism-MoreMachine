package com.jerry.meklm.client.render.tile;

import com.jerry.meklm.client.model.LargeMachineModelCache;
import com.jerry.meklm.common.base.LMProfilerConstants;
import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.RenderTickHandler;
import mekanism.client.render.lib.Outlines;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NothingNullByDefault
public class RenderLargeElectrolyticSeparator extends MekanismTileEntityRenderer<TileEntityLargeElectrolyticSeparator> implements IWireFrameRenderer {

    @Nullable
    private static List<Outlines.Line> lines;

    protected RenderLargeElectrolyticSeparator(Context context) {
        super(context);
    }

    public static void resetCached() {
        lines = null;
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer) {
        if (tile instanceof TileEntityLargeElectrolyticSeparator generator) {
            if (lines == null) {
                if (generator.getLevel() != null) {
                    lines = Outlines.extract(LargeMachineModelCache.INSTANCE.LARGE_ELECTROLYTIC_SEPARATOR.getBakedModel(), null, generator.getLevel().random, ModelData.EMPTY, null);
                }
            }
            PoseStack.Pose pose = matrix.last();
            RenderTickHandler.renderVertexWireFrame(lines, buffer, pose.pose(), pose.normal());
            matrix.popPose();
        }
    }

    @Override
    protected void render(TileEntityLargeElectrolyticSeparator tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {}

    @Override
    protected String getProfilerSection() {
        return LMProfilerConstants.LARGE_ELECTROLYTIC_SEPARATOR;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityLargeElectrolyticSeparator tile) {
        return true;
    }
}
