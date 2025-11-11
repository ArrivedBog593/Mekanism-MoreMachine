package com.jerry.mekmm.common.network;

import com.jerry.mekmm.common.attachments.WirelessConnectionManager;
import com.jerry.mekmm.common.tile.interfaces.ITileConnectHolder;
import mekanism.common.network.PacketUtils;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MoreMachinePacketUtils {

    private MoreMachinePacketUtils() {
    }

    public static WirelessConnectionManager connectManager(IPayloadContext context, BlockPos pos) {
        if (PacketUtils.blockEntity(context, pos) instanceof ITileConnectHolder connectHolder) {
            return connectHolder.getConnectManager();
        }
        return null;
    }
}
