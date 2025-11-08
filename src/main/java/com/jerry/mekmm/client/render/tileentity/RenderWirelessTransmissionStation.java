package com.jerry.mekmm.client.render.tileentity;

import com.jerry.mekmm.client.render.RenderLineHelper;
import com.jerry.mekmm.common.attachments.ConnectionConfig;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import mekanism.common.lib.transmitter.TransmissionType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Collection;

@NothingNullByDefault
public class RenderWirelessTransmissionStation extends MekanismTileEntityRenderer<TileEntityWirelessTransmissionStation> {

    public RenderWirelessTransmissionStation(Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityWirelessTransmissionStation tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        // 获取传输站的位置 (中心点)
        BlockPos stationPos = tile.getBlockPos();
//        Vec3 stationCenter = new Vec3(stationPos.getX(), stationPos.getY() + 3, stationPos.getZ());
        Vec3 stationCenter = Vec3.atCenterOf(stationPos);
        System.out.println("=== RENDER CALLED ===");
        // 获取所有连接配置
        Collection<ConnectionConfig> connections = tile.connectionManager.getAllConnections();
        System.out.println("Connection count: " + connections.size());

        if (connections.isEmpty()) {
            System.out.println("WARNING: No connections to render!");
            return;
        }
        // 为每个连接绘制光束
        for (ConnectionConfig config : connections) {
            renderConnection(matrix, renderer, stationCenter, config, partialTick);
        }
    }

    /**
     * 渲染单个连接
     */
    private void renderConnection(PoseStack matrix, MultiBufferSource renderer, Vec3 stationCenter, ConnectionConfig config, float partialTick) {
        BlockPos targetPos = config.pos();
        Direction targetFace = config.direction();
        TransmissionType type = config.type();
        // 计算目标方块连接面的中心点
        Vec3 targetFaceCenter = getFaceCenter(targetPos, targetFace);
        // 转换为相对于传输站的坐标 (渲染坐标系)
        Vector3f start = new Vector3f((float) stationCenter.x, (float) stationCenter.y, (float) stationCenter.z);
        Vector3f end = new Vector3f((float) targetFaceCenter.x, (float) targetFaceCenter.y, (float) targetFaceCenter.z);
        // 根据传输类型选择颜色
        ColorRGB color = getColorForType(type);
        // 绘制光束 (厚度可以根据需要调整)
        RenderLineHelper.renderLine(matrix, renderer, start, end, color.r, color.g, color.b, 0.1f);
    }

    /**
     * 获取方块某个面的中心点
     */
    private Vec3 getFaceCenter(BlockPos pos, Direction face) {
        Vec3 center = Vec3.atCenterOf(pos);
        // 根据方向偏移到面的中心
        // 偏移量为 0.5 (半个方块) 加上一个小的间隙 (0.01) 避免 Z-fighting
        double offset = 0.51;
        return switch (face) {
            case UP -> center.add(0, offset, 0);
            case DOWN -> center.add(0, -offset, 0);
            case NORTH -> center.add(0, 0, -offset);
            case SOUTH -> center.add(0, 0, offset);
            case WEST -> center.add(-offset, 0, 0);
            case EAST -> center.add(offset, 0, 0);
        };
    }

    /**
     * 根据传输类型返回颜色
     */
    private ColorRGB getColorForType(TransmissionType type) {
        return switch (type) {
            case ENERGY -> new ColorRGB(1.0f, 0.0f, 0.0f);      // 红色 - 能量
            case FLUID -> new ColorRGB(0.0f, 0.4f, 1.0f);       // 蓝色 - 流体
            case CHEMICAL -> new ColorRGB(0.0f, 1.0f, 0.0f);    // 绿色 - 化学品
            case ITEM -> new ColorRGB(1.0f, 1.0f, 0.0f);        // 黄色 - 物品
            case HEAT -> new ColorRGB(1.0f, 0.5f, 0.0f);        // 橙色 - 热量
        };
    }

    @Override
    protected String getProfilerSection() {
        return "wirelessTransmissionStation";
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityWirelessTransmissionStation blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(TileEntityWirelessTransmissionStation blockEntity, Vec3 cameraPos) {
        return super.shouldRender(blockEntity, cameraPos);
    }

    /**
     * 简单的 RGB 颜色封装
     */
    private record ColorRGB(float r, float g, float b) {
    }
}
