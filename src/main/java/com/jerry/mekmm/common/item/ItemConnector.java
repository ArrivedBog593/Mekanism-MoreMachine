package com.jerry.mekmm.common.item;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.MoreMachineItemAbilities;
import com.jerry.mekmm.common.MoreMachineLang;
import com.jerry.mekmm.common.registries.MoreMachineDataComponents;
import com.jerry.mekmm.common.tile.TileEntityWirelessTransmissionStation;
import com.jerry.mekmm.common.tile.interfaces.ITileConnect;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mekanism.api.IIncrementalEnum;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.radial.IRadialDataHelper;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.IRadialMode;
import mekanism.api.text.EnumColor;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.ILangEntry;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.MekanismLang;
import mekanism.common.lib.radial.IRadialModeItem;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.IntFunction;

public class ItemConnector extends Item implements IRadialModeItem<ItemConnector.ConnectorMode> {
    private static final Lazy<RadialData<ItemConnector.ConnectorMode>> LAZY_RADIAL_DATA = Lazy.of(() ->
            IRadialDataHelper.INSTANCE.dataForEnum(Mekmm.rl("connector_mode"), ItemConnector.ConnectorMode.class));

    public ItemConnector(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).stacksTo(1)
                .component(MoreMachineDataComponents.CONNECTOR_MODE, ConnectorMode.CONNECT_ENERGY)
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(MekanismLang.STATE.translateColored(EnumColor.PINK, getMode(stack)));
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(EnumColor.AQUA, super.getName(stack));
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility action) {
        if (action == MoreMachineItemAbilities.CONNECT_CHEMICALS) {
            return getMode(stack) == ConnectorMode.CONNECT_CHEMICALS;
        } else if (action == MoreMachineItemAbilities.CONNECT_ENERGY) {
            return getMode(stack) == ConnectorMode.CONNECT_ENERGY;
        } else if (action == MoreMachineItemAbilities.CONNECT_FLUIDS) {
            return getMode(stack) == ConnectorMode.CONNECT_FLUIDS;
        } else if (action == MoreMachineItemAbilities.CONNECT_HEAT) {
            return getMode(stack) == ConnectorMode.CONNECT_HEAT;
        } else if (action == MoreMachineItemAbilities.CONNECT_ITEMS) {
            return getMode(stack) == ConnectorMode.CONNECT_ITEMS;
        }
        return super.canPerformAction(stack, action);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (!world.isClientSide && player != null) {
            BlockPos pos = context.getClickedPos();
            Direction side = context.getClickedFace();
            ItemStack stack = context.getItemInHand();
            Block block = Objects.requireNonNull(WorldUtils.getBlockStateIfLoaded(world, pos)).getBlock();
            BlockEntity tile = WorldUtils.getTileEntity(world, pos);
            if (player.isShiftKeyDown()) {
                if (tile instanceof ITileConnect) {
//                    NBTUtils.writeRegistryEntry(data, SerializationConstants.DATA_TYPE, BuiltInRegistries.BLOCK_ENTITY_TYPE, MoreMachineTileEntityTypes.WIRELESS_TRANSMISSION_STATION.get());
                    stack.set(MoreMachineDataComponents.CONNECT_FROM, GlobalPos.of(world.dimension(), pos));
                    player.displayClientMessage(MoreMachineLang.CONNECTOR_FROM.translate(EnumColor.INDIGO, TextComponentUtil.translate(block.getDescriptionId())), true);
                }
            } else {
                GlobalPos globalPos = stack.get(MoreMachineDataComponents.CONNECT_FROM);
                if (globalPos != null && world.dimension() == globalPos.dimension()) {
                    TileEntityWirelessTransmissionStation linkTile = WorldUtils.getTileEntity(TileEntityWirelessTransmissionStation.class, world, globalPos.pos(), true);
                    if (linkTile != null) {
                        switch (linkTile.connectOrCut(pos, side, getMode(stack).transmissionType)) {
                            case CONNECT -> {
                                player.displayClientMessage(MoreMachineLang.CONNECTOR_TO.translate(EnumColor.INDIGO, TextComponentUtil.translate(block.getDescriptionId()), EnumColor.INDIGO, side), true);
                                return InteractionResult.SUCCESS;
                            }
                            case DISCONNECT -> {
                                player.displayClientMessage(MoreMachineLang.CONNECTOR_DISCONNECT.translate(EnumColor.INDIGO, TextComponentUtil.translate(block.getDescriptionId()), EnumColor.INDIGO, side), true);
                                return InteractionResult.SUCCESS;
                            }
                            case CONNECT_FAIL -> {
                                player.displayClientMessage(MoreMachineLang.CONNECTOR_FAIL.translate(EnumColor.DARK_RED, TextComponentUtil.translate(block.getDescriptionId()), EnumColor.DARK_RED, side), true);
                                return InteractionResult.SUCCESS;
                            }
                        }
                        return InteractionResult.PASS;
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (player.isShiftKeyDown()) {
            ItemStack connector = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                connector.remove(MoreMachineDataComponents.CONNECT_FROM);
                player.displayClientMessage(MoreMachineLang.CONNECTOR_CLEARED.translate(), true);
            }
            return InteractionResultHolder.sidedSuccess(connector, level.isClientSide);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public @NotNull RadialData<ConnectorMode> getRadialData(ItemStack stack) {
        return LAZY_RADIAL_DATA.get();
    }

    @Override
    public DataComponentType<ConnectorMode> getModeDataType() {
        return MoreMachineDataComponents.CONNECTOR_MODE.get();
    }

    @Override
    public ConnectorMode getDefaultMode() {
        return ConnectorMode.CONNECT_ENERGY;
    }

    @Override
    public void changeMode(@NotNull Player player, @NotNull ItemStack stack, int shift, DisplayChange displayChange) {
        ConnectorMode mode = getMode(stack);
        ConnectorMode newMode = mode.adjust(shift);
        if (mode != newMode) {
            setMode(stack, player, newMode);
            displayChange.sendMessage(player, newMode, MekanismLang.CONFIGURE_STATE::translate);
        }
    }


    @NothingNullByDefault
    public enum ConnectorMode implements IIncrementalEnum<ConnectorMode>, IHasTextComponent.IHasEnumNameTextComponent, IRadialMode, StringRepresentable {
        CONNECT_ITEMS(MekanismLang.CONFIGURATOR_CONFIGURATE, TransmissionType.ITEM, EnumColor.BRIGHT_GREEN, null),
        CONNECT_FLUIDS(MekanismLang.CONFIGURATOR_CONFIGURATE, TransmissionType.FLUID, EnumColor.BRIGHT_GREEN, null),
        CONNECT_CHEMICALS(MekanismLang.CONFIGURATOR_CONFIGURATE, TransmissionType.CHEMICAL, EnumColor.BRIGHT_GREEN, null),
        CONNECT_ENERGY(MekanismLang.CONFIGURATOR_CONFIGURATE, TransmissionType.ENERGY, EnumColor.BRIGHT_GREEN, null),
        CONNECT_HEAT(MekanismLang.CONFIGURATOR_CONFIGURATE, TransmissionType.HEAT, EnumColor.BRIGHT_GREEN, null);

        public static final Codec<ConnectorMode> CODEC = StringRepresentable.fromEnum(ConnectorMode::values);
        public static final IntFunction<ConnectorMode> BY_ID = ByIdMap.continuous(ConnectorMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, ConnectorMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ConnectorMode::ordinal);

        private final String serializedName;
        private final ILangEntry langEntry;
        @Nullable
        private final TransmissionType transmissionType;
        private final EnumColor color;
        private final ResourceLocation icon;

        ConnectorMode(ILangEntry langEntry, @Nullable TransmissionType transmissionType, EnumColor color, @Nullable ResourceLocation icon) {
            this.serializedName = name().toLowerCase(Locale.ROOT);
            this.langEntry = langEntry;
            this.transmissionType = transmissionType;
            this.color = color;
            if (transmissionType == null) {
                this.icon = Objects.requireNonNull(icon, "Icon should only be null if there is a transmission type present.");
            } else {
                this.icon = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, transmissionType.getTransmission() + ".png");
            }
        }

        @Override
        public ConnectorMode byIndex(int index) {
            return BY_ID.apply(index);
        }

        @Override
        public @NotNull Component sliceName() {
            return transmissionType != null ? transmissionType.getLangEntry().translateColored(color) : getTextComponent();
        }

        @Override
        public @NotNull ResourceLocation icon() {
            return icon;
        }

        @Override
        public Component getTextComponent() {
            if (transmissionType == null) {
                return langEntry.translateColored(color);
            }
            return langEntry.translateColored(color, transmissionType);
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }
    }
}
