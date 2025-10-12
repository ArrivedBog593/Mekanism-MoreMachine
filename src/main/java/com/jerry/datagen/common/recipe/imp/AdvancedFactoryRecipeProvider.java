package com.jerry.datagen.common.recipe.imp;

import com.jerry.datagen.common.recipe.ISubRecipeProvider;
import com.jerry.datagen.common.recipe.builder.MekMMDataShapedRecipeBuilder;
import com.jerry.datagen.common.recipe.pattern.Pattern;
import com.jerry.mekaf.common.block.attribute.AttributeAdvancedFactoryType;
import com.jerry.mekaf.common.block.prefab.BlockAdvancedFactoryMachine;
import com.jerry.mekaf.common.content.blocktype.AdvancedFactoryType;
import com.jerry.mekaf.common.item.block.machine.AdvancedItemBlockFactory;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.util.MoreMachineEnumUtils;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.registration.impl.BlockRegistryObject;
import com.jerry.mekaf.common.registries.AdvancedFactoryBlocks;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tags.MekanismTags;
import mekanism.common.tier.FactoryTier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

class AdvancedFactoryRecipeProvider implements ISubRecipeProvider {

    @Override
    public void addRecipes(RecipeOutput consumer, HolderLookup.Provider registries) {
        String basePath = "factory/";
        String basicPath = basePath + "basic/";
        String advancedPath = basePath + "advanced/";
        String elitePath = basePath + "elite/";
        String ultimatePath = basePath + "ultimate/";
        TagKey<Item> osmiumIngot = MekanismTags.Items.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.OSMIUM);
        for (AdvancedFactoryType type : MoreMachineEnumUtils.ADVANCED_FACTORY_TYPES) {
            BlockRegistryObject<BlockAdvancedFactoryMachine.BlockAdvancedFactory<?>, AdvancedItemBlockFactory> basicFactory = AdvancedFactoryBlocks.getAdvancedFactory(FactoryTier.BASIC, type);
            BlockRegistryObject<BlockAdvancedFactoryMachine.BlockAdvancedFactory<?>, AdvancedItemBlockFactory> advancedFactory = AdvancedFactoryBlocks.getAdvancedFactory(FactoryTier.ADVANCED, type);
            BlockRegistryObject<BlockAdvancedFactoryMachine.BlockAdvancedFactory<?>, AdvancedItemBlockFactory> eliteFactory = AdvancedFactoryBlocks.getAdvancedFactory(FactoryTier.ELITE, type);
            addFactoryRecipe(consumer, basicPath, basicFactory, type.getBaseBlock().getItemHolder(), Tags.Items.INGOTS_IRON, MekanismTags.Items.ALLOYS_BASIC, MekanismTags.Items.CIRCUITS_BASIC);
            addFactoryRecipe(consumer, advancedPath, advancedFactory, basicFactory.getItemHolder(), osmiumIngot, MekanismTags.Items.ALLOYS_INFUSED, MekanismTags.Items.CIRCUITS_ADVANCED);
            addFactoryRecipe(consumer, elitePath, eliteFactory, advancedFactory.getItemHolder(), Tags.Items.INGOTS_GOLD, MekanismTags.Items.ALLOYS_REINFORCED, MekanismTags.Items.CIRCUITS_ELITE);
            addFactoryRecipe(consumer, ultimatePath, AdvancedFactoryBlocks.getAdvancedFactory(FactoryTier.ULTIMATE, type), eliteFactory.getItemHolder(), Tags.Items.GEMS_DIAMOND, MekanismTags.Items.ALLOYS_ATOMIC, MekanismTags.Items.CIRCUITS_ULTIMATE);
        }
    }

    private void addFactoryRecipe(RecipeOutput consumer, String basePath, BlockRegistryObject<BlockAdvancedFactoryMachine.BlockAdvancedFactory<?>, ?> factory, Holder<Item> toUpgrade,
                                  TagKey<Item> ingotTag, TagKey<Item> alloyTag, TagKey<Item> circuitTag) {
        MekMMDataShapedRecipeBuilder.shapedRecipe(factory)
              .pattern(MoreMachineRecipeProvider.TIER_PATTERN)
              .key(Pattern.PREVIOUS, toUpgrade.value())
              .key(Pattern.CIRCUIT, circuitTag)
              .key(Pattern.INGOT, ingotTag)
              .key(Pattern.ALLOY, alloyTag)
              .build(consumer, Mekmm.rl(basePath + Attribute.getOrThrow(factory, AttributeAdvancedFactoryType.class).getAdvancedFactoryType().getRegistryNameComponent()));
    }
}