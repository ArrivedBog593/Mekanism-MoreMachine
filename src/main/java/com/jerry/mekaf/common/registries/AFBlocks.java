package com.jerry.mekaf.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekaf.common.attachments.containers.chemical.AFChemicalTanksBuilder;
import com.jerry.mekaf.common.attachments.containers.item.AFItemSlotsBuilder;
import com.jerry.mekaf.common.block.prefab.AdvancedBlockFactoryMachine;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactory;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekaf.common.item.block.machine.AdvancedItemBlockFactory;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekaf.common.tile.factory.TileEntityAdvancedFactoryBase;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.tier.ITier;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AFBlocks {

    private AFBlocks() {
    }

    public static final BlockDeferredRegister AF_BLOCKS = new BlockDeferredRegister(Mekmm.MOD_ID);

    private static final Table<FactoryTier, AdvancedFactoryType, BlockRegistryObject<AdvancedBlockFactoryMachine.AdvancedBlockFactory<?>, AdvancedItemBlockFactory>> AF_FACTORIES = HashBasedTable.create();

    static {
        // factories
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (AdvancedFactoryType type : MMEnumUtils.ADVANCED_FACTORY_TYPES) {
                AF_FACTORIES.put(tier, type, registerMMFactory(AFBlockTypes.getAdvancedFactory(tier, type)));
            }
        }
    }

    private static <TILE extends TileEntityAdvancedFactoryBase<?>> BlockRegistryObject<AdvancedBlockFactoryMachine.AdvancedBlockFactory<?>, AdvancedItemBlockFactory> registerMMFactory(AdvancedFactory<TILE> type) {
        FactoryTier tier = (FactoryTier) Objects.requireNonNull(type.get(AttributeTier.class)).tier();
        BlockRegistryObject<AdvancedBlockFactoryMachine.AdvancedBlockFactory<?>, AdvancedItemBlockFactory> factory = registerTieredBlock(tier, "_" + type.getAdvancedFactoryType().getRegistryNameComponent() + "_factory", () -> new AdvancedBlockFactoryMachine.AdvancedBlockFactory<>(type), AdvancedItemBlockFactory::new);
        factory.forItemHolder(holder -> {
            int processes = tier.processes;
            Predicate<ItemStack> recipeItemInputPredicate = switch (type.getAdvancedFactoryType()) {
                case OXIDIZING -> s -> MekanismRecipeType.OXIDIZING.getInputCache().containsInput(null, s);
                case DISSOLVING -> s -> MekanismRecipeType.DISSOLUTION.getInputCache().containsInputA(null, s);
                case PRESSURISED_REACTING -> s -> MekanismRecipeType.REACTION.getInputCache().containsInputA(null, s);
                default -> null;
            };
            Predicate<ChemicalStack> recipeChemicalInputPredicate = switch (type.getAdvancedFactoryType()) {
                case CHEMICAL_INFUSING ->
                        s -> MekanismRecipeType.CHEMICAL_INFUSING.getInputCache().containsInput(null, s);
                case WASHING -> s -> MekanismRecipeType.WASHING.getInputCache().containsInputB(null, s);
                case CRYSTALLIZING -> s -> MekanismRecipeType.CRYSTALLIZING.getInputCache().containsInput(null, s);
                case PRESSURISED_REACTING -> s -> MekanismRecipeType.REACTION.getInputCache().containsInputC(null, s);
                case CENTRIFUGING -> s -> MekanismRecipeType.CENTRIFUGING.getInputCache().containsInput(null, s);
                default -> null;
            };
            switch (type.getAdvancedFactoryType()) {
                case OXIDIZING ->
                        holder.addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                //化学品输出（多个）
                                .addOutputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> AFItemSlotsBuilder.builder()
                                //物品输入（多个）
                                .addInputFactorySlots(processes, recipeItemInputPredicate)
                                //物品输出
                                .addOutput()
                                .addEnergy()
                                .build()
                        );
                case DISSOLVING ->
                        holder.addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                //化学品输入
                                .addBasic(TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                //化学品输出（多个）
                                .addOutputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> AFItemSlotsBuilder.builder()
                                .addChemicalFillOrConvertSlot(9)
                                .addInputFactorySlots(processes, recipeItemInputPredicate)
                                .addOutput()
                                .addEnergy()
                                .build()
                        );
                case CHEMICAL_INFUSING ->
                        holder.addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                //Left
                                .addInputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                //Right
                                .addBasic(TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                .addOutputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addOutput()
                                .addEnergy()
                                .build()
                        );
                case WASHING -> holder
                        .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                                .addBasic(TileEntityAdvancedFactoryBase.MAX_FLUID * tier.processes, MekanismRecipeType.WASHING, InputRecipeCache.FluidChemical::containsInputA)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                .addInputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                .addOutputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addFluidFillSlot(0)
                                .addOutput()
                                .addEnergy()
                                .build()
                        );
                case PRESSURISED_REACTING -> holder
                        .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                                .addBasic(TileEntityAdvancedFactoryBase.MAX_FLUID * tier.processes, MekanismRecipeType.REACTION, InputRecipeCache.ItemFluidChemical::containsInputB)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                .addBasic(TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                .addBasic(TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addBasicFactorySlots(processes, recipeItemInputPredicate)
                                .addEnergy()
                                .build()
                        );
                case CRYSTALLIZING ->
                        holder.addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                .addInputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> AFItemSlotsBuilder.builder()
                                .addOutputFactorySlots(tier.processes)
                                .addEnergy()
                                .build()
                        );
                case CENTRIFUGING -> holder
                        .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> AFChemicalTanksBuilder.builder()
                                .addInputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes, recipeChemicalInputPredicate)
                                .addOutputFactoryTank(tier.processes, TileEntityAdvancedFactoryBase.MAX_CHEMICAL * tier.processes)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addOutput()
                                .addEnergy()
                                .build()
                        );
            }
        });
        return factory;
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return AF_BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

    /**
     * Retrieves a Factory with a defined tier and recipe type.
     *
     * @param tier - tier to add to the Factory
     * @param type - recipe type to add to the Factory
     * @return factory with defined tier and recipe type
     */
    public static BlockRegistryObject<AdvancedBlockFactoryMachine.AdvancedBlockFactory<?>, AdvancedItemBlockFactory> getAdvancedFactory(@NotNull FactoryTier tier, @NotNull AdvancedFactoryType type) {
        return AF_FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static BlockRegistryObject<AdvancedBlockFactoryMachine.AdvancedBlockFactory<?>, AdvancedItemBlockFactory>[] getAdvancedFactoryBlocks() {
        return AF_FACTORIES.values().toArray(new BlockRegistryObject[0]);
    }
}
