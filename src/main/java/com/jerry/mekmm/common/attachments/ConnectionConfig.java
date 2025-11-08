package com.jerry.mekmm.common.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.common.lib.transmitter.TransmissionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public record ConnectionConfig(BlockPos pos, Direction direction, TransmissionType type) {

    public static final Codec<ConnectionConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(BlockPos.CODEC.fieldOf("pos").forGetter(ConnectionConfig::pos),
                    Direction.CODEC.fieldOf("direction").forGetter(ConnectionConfig::direction),
                    TransmissionType.CODEC.fieldOf("type").forGetter(ConnectionConfig::type)
            ).apply(instance, ConnectionConfig::new)
    );

    /**
     * 保存到NBT
     */
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("pos", pos.asLong());
        tag.putByte("direction", (byte) direction.ordinal());
        tag.putByte("type", (byte) type.ordinal());
        return tag;
    }

    /**
     * 从NBT加载
     */
    public static ConnectionConfig fromNBT(CompoundTag tag) {
        BlockPos pos = BlockPos.of(tag.getLong("pos"));
        Direction dir = Direction.values()[(tag.getByte("direction"))];
        TransmissionType type = TransmissionType.values()[tag.getByte("type")];
        return new ConnectionConfig(pos, dir, type);
    }
}
