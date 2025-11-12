package com.jerry.mekmm.client.render;

import com.jerry.mekmm.client.BlockHighlightManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class MoreMachineRenderTickHandler {

    @SubscribeEvent
    public void renderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 cameraPos = event.getCamera().getPosition();
        long gameTime = mc.level.getGameTime();

        poseStack.pushPose();
        for (BlockPos pos : BlockHighlightManager.getInstance().getHighlightedPositions()) {
            BlockHighlightManager.HighlightData data = BlockHighlightManager.getInstance().getHighlightData(pos);
            if (data != null) {
                renderHighlight(poseStack, bufferSource, pos, cameraPos, data, gameTime);
            }
        }
        poseStack.popPose();
    }

    private static void renderHighlight(PoseStack poseStack, MultiBufferSource bufferSource, BlockPos pos, Vec3 cameraPos, BlockHighlightManager.HighlightData data, long gameTime) {
        poseStack.pushPose();
        poseStack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
        float alpha = data.getAlpha(gameTime);
        LevelRenderer.renderLineBox(poseStack, consumer, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, data.getRed(), data.getGreen(), data.getBlue(), alpha);
        poseStack.popPose();
    }
}
