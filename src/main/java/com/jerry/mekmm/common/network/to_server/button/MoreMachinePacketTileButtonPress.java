package com.jerry.mekmm.common.network.to_server.button;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.MoreMachineLang;
import com.jerry.mekmm.common.registries.MoreMachineContainerTypes;
import io.netty.buffer.ByteBuf;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

public record MoreMachinePacketTileButtonPress(MoreMachineClickedTileButton buttonClicked,
                                               BlockPos pos) implements IMekanismPacket {

    public static final CustomPacketPayload.Type<MoreMachinePacketTileButtonPress> TYPE = new CustomPacketPayload.Type<>(Mekmm.rl("tile_button"));
    public static final StreamCodec<ByteBuf, MoreMachinePacketTileButtonPress> STREAM_CODEC = StreamCodec.composite(
            MoreMachineClickedTileButton.STREAM_CODEC, MoreMachinePacketTileButtonPress::buttonClicked,
            BlockPos.STREAM_CODEC, MoreMachinePacketTileButtonPress::pos,
            MoreMachinePacketTileButtonPress::new
    );

    public MoreMachinePacketTileButtonPress(MoreMachineClickedTileButton buttonClicked, BlockEntity tile) {
        this(buttonClicked, tile.getBlockPos());
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(), pos);
        MenuProvider provider = buttonClicked.getProvider(tile);
        if (provider != null) {
            player.openMenu(provider, buf -> {
                buf.writeBlockPos(pos);
                buttonClicked.encodeExtraData(buf, tile);
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum MoreMachineClickedTileButton {
        WIRELESS_TRANSMISSION_STATION_CONFIG(tile -> MoreMachineContainerTypes.WIRELESS_TRANSMISSION_STATION_CONFIG.getProvider(MoreMachineLang.TRANSMITTER_CONFIG, tile));

        public static final IntFunction<MoreMachineClickedTileButton> BY_ID = ByIdMap.continuous(MoreMachineClickedTileButton::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, MoreMachineClickedTileButton> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, MoreMachineClickedTileButton::ordinal);

        private final Function<TileEntityMekanism, @Nullable MenuProvider> providerFromTile;
        @Nullable
        private final BiConsumer<RegistryFriendlyByteBuf, TileEntityMekanism> extraEncodingData;

        MoreMachineClickedTileButton(Function<TileEntityMekanism, @Nullable MenuProvider> providerFromTile) {
            this(providerFromTile, null);
        }

        MoreMachineClickedTileButton(Function<TileEntityMekanism, @Nullable MenuProvider> providerFromTile,
                                     @Nullable BiConsumer<RegistryFriendlyByteBuf, TileEntityMekanism> extraEncodingData) {
            this.providerFromTile = providerFromTile;
            this.extraEncodingData = extraEncodingData;
        }

        @Nullable
        @Contract("null -> null")
        public MenuProvider getProvider(@Nullable TileEntityMekanism tile) {
            return tile == null ? null : providerFromTile.apply(tile);
        }

        private void encodeExtraData(RegistryFriendlyByteBuf buffer, TileEntityMekanism tile) {
            if (extraEncodingData != null) {
                extraEncodingData.accept(buffer, tile);
            }
        }
    }
}
