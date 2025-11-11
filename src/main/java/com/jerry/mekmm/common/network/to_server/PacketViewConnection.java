package com.jerry.mekmm.common.network.to_server;

import com.jerry.mekmm.common.attachments.ConnectionConfig;
import com.jerry.mekmm.common.attachments.WirelessConnectionManager;
import com.jerry.mekmm.common.network.MoreMachinePacketUtils;
import mekanism.common.Mekanism;
import mekanism.common.network.IMekanismPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

//TODO:这里似乎需要完善
public record PacketViewConnection(BlockPos pos, ConnectionConfig config) implements IMekanismPacket {

    public static final Type<PacketViewConnection> TYPE = new Type<>(Mekanism.rl("view_connection"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketViewConnection> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PacketViewConnection::pos,
            ConnectionConfig.STREAM_CODEC, PacketViewConnection::config,
            PacketViewConnection::new
    );

    @Override
    public void handle(IPayloadContext context) {
        WirelessConnectionManager connectionManager = MoreMachinePacketUtils.connectManager(context, pos);
        if (connectionManager != null) {
            connectionManager.remove(config);
        }
    }

    @Override
    public @NotNull Type<PacketViewConnection> type() {
        return TYPE;
    }
}
