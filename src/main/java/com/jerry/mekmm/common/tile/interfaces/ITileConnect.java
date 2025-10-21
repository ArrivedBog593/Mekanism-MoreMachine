package com.jerry.mekmm.common.tile.interfaces;

import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation.ConnectStatus;
import mekanism.common.lib.transmitter.TransmissionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface ITileConnect {

    ConnectStatus connectOrCut(BlockPos pos, Direction side, TransmissionType type);
}
