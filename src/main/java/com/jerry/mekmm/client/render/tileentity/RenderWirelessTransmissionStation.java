package com.jerry.mekmm.client.render.tileentity;

import com.google.common.collect.Table;
import com.jerry.mekmm.client.render.RenderLineHelper;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import mekanism.common.lib.transmitter.TransmissionType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NothingNullByDefault
public class RenderWirelessTransmissionStation extends MekanismTileEntityRenderer<TileEntityWirelessTransmissionStation> {

    public RenderWirelessTransmissionStation(Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityWirelessTransmissionStation tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        BlockPos from = tile.getBlockPos();
        Vector3f start = new Vector3f(from.getX(), from.getY() + 3, from.getZ());
        RenderLineHelper.renderLine(matrix, renderer, start, new Vector3f(from.getX(), from.getY() + 4, from.getZ()), 48, 255, 249, 1f);
        List<BlockPos> to = new ArrayList<>();
        for (Map.Entry<BlockPos, Table<Direction, TransmissionType, BlockCapabilityCache<IChemicalHandler, @Nullable Direction>>> entry : tile.chemicalCapabilityCache.entrySet()) {
            to.add(entry.getKey());
        }
        Level level = tile.getLevel();
        for (BlockPos toPos : to) {
            if (level != null && level.isClientSide && toPos != null) {
//                for (int i = 0; i <= 30; i++) {
//                    // 计算插值比例
//                    double ratio = (double) i / 30;
//                    // 计算两点间的插值点坐标，+0.5 通常用于瞄准方块中心
//                    double x = from.getX() + 0.5 + (toPos.getX() - from.getX()) * ratio;
//                    double y = from.getY() + 0.5 + (toPos.getY() - from.getY()) * ratio;
//                    double z = from.getZ() + 0.5 + (toPos.getZ() - from.getZ()) * ratio;
//
//                    // 添加一些随机偏移使线条看起来更自然
//                    double offsetX = (level.random.nextDouble() - 0.5) * 0.2;
//                    double offsetY = (level.random.nextDouble() - 0.5) * 0.2;
//                    double offsetZ = (level.random.nextDouble() - 0.5) * 0.2;
//
//                    // 在计算出的位置生成粒子
//                    level.addParticle(ParticleTypes.GLOW, x + offsetX, y + offsetY, z + offsetZ, 0, 0, 0);
//                }
                Vector3f end = new Vector3f(toPos.getX(), toPos.getY(), toPos.getZ());
                RenderLineHelper.renderLine(matrix, renderer, start, end, 48, 255, 249, 1f);
            }
        }
    }

    @Override
    protected String getProfilerSection() {
        return "wirelessTransmissionStation";
    }
}
