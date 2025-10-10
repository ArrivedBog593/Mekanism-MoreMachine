package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.block.prefab.MMBlockFactoryMachine;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.item.block.machine.MMItemBlockFactory;
import com.jerry.mekmm.common.tile.factory.TileEntityMMFactory;
import com.jerry.mekmm.common.tile.machine.*;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.tier.ITier;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class MMBlocks {

    private MMBlocks() {

    }

    public static final BlockDeferredRegister MM_BLOCKS = new BlockDeferredRegister(Mekmm.MOD_ID);

    private static final Table<FactoryTier, MMFactoryType, BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory>> FACTORIES = HashBasedTable.create();

    static {
        // factories
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MMFactoryType type : MMEnumUtils.MM_FACTORY_TYPES) {
                FACTORIES.put(tier, type, registerFactory(MMBlockTypes.getMMFactory(tier, type)));
            }
        }
    }

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityRecycler, MMMachine.MMFactoryMachine<TileEntityRecycler>>, ItemBlockMachine> RECYCLER = MM_BLOCKS.register("recycler", () -> new MMBlockFactoryMachine<>(MMBlockTypes.RECYCLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityPlantingStation, MMMachine.MMFactoryMachine<TileEntityPlantingStation>>, ItemBlockMachine> PLANTING_STATION = MM_BLOCKS.register("planting_station", () -> new MMBlockFactoryMachine<>(MMBlockTypes.PLANTING_STATION, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityStamper, MMMachine.MMFactoryMachine<TileEntityStamper>>, ItemBlockMachine> CNC_STAMPER = MM_BLOCKS.register("cnc_stamper", () -> new MMBlockFactoryMachine<>(MMBlockTypes.CNC_STAMPER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityLathe, MMMachine.MMFactoryMachine<TileEntityLathe>>, ItemBlockMachine> CNC_LATHE = MM_BLOCKS.register("cnc_lathe", () -> new MMBlockFactoryMachine<>(MMBlockTypes.CNC_LATHE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityRollingMill, MMMachine.MMFactoryMachine<TileEntityRollingMill>>, ItemBlockMachine> CNC_ROLLING_MILL = MM_BLOCKS.register("cnc_rolling_mill", () -> new MMBlockFactoryMachine<>(MMBlockTypes.CNC_ROLLING_MILL, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityReplicator, MMMachine.MMFactoryMachine<TileEntityReplicator>>, ItemBlockMachine> REPLICATOR = MM_BLOCKS.register("replicator", () -> new MMBlockFactoryMachine<>(MMBlockTypes.REPLICATOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    private static <TILE extends TileEntityMMFactory<?>> BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory> registerFactory(MMFactory<TILE> type) {
        return registerTieredBlock(type, "_" + type.getMMFactoryType().getRegistryNameComponent() + "_factory", () -> new MMBlockFactoryMachine.MMBlockFactory<>(type), MMItemBlockFactory::new);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        return registerTieredBlock(type.get(AttributeTier.class).tier(), suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        return MM_BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

    /**
     * Retrieves a Factory with a defined tier and recipe type.
     *
     * @param tier - tier to add to the Factory
     * @param type - recipe type to add to the Factory
     * @return factory with defined tier and recipe type
     */
    public static BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory> getMMFactory(@NotNull FactoryTier tier, @NotNull MMFactoryType type) {
        return FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory>[] getMMFactoryBlocks() {
        return FACTORIES.values().toArray(new BlockRegistryObject[0]);
    }
}
