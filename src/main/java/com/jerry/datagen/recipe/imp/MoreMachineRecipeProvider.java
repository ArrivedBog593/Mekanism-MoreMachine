package com.jerry.datagen.recipe.imp;

import com.jerry.datagen.recipe.BaseRecipeProvider;
import com.jerry.datagen.recipe.ISubRecipeProvider;
import com.jerry.datagen.recipe.builder.ExtendedShapedRecipeBuilder;
import com.jerry.datagen.recipe.builder.MekMMDataShapedRecipeBuilder;
import com.jerry.datagen.recipe.compat.IERecipeProvider;
import com.jerry.datagen.recipe.compat.MysticalRecipeProvider;
import com.jerry.datagen.recipe.pattern.Pattern;
import com.jerry.datagen.recipe.pattern.RecipePattern;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.registries.MMItems;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.registries.*;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.tags.MekanismTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@NothingNullByDefault
public class MoreMachineRecipeProvider extends BaseRecipeProvider {

    static final char DIAMOND_CHAR = 'D';
    static final char GLASS_CHAR = 'G';
    static final char PERSONAL_STORAGE_CHAR = 'P';
    static final char MIXING_CHAR = 'M';
    static final char ROBIT_CHAR = 'R';
    static final char SORTER_CHAR = 'S';
    static final char TELEPORTATION_CORE_CHAR = 'T';

    //TODO: Do we want to use same pattern for fluid tank and chemical tank at some point
    static final RecipePattern TIER_PATTERN = RecipePattern.createPattern(
            RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
            RecipePattern.TripleLine.of(Pattern.INGOT, Pattern.PREVIOUS, Pattern.INGOT),
            RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY));
    static final RecipePattern STORAGE_PATTERN = RecipePattern.createPattern(
            RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
            RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
            RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT));
    static final RecipePattern TYPED_STORAGE_PATTERN = RecipePattern.createPattern(
            RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
            RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.PREVIOUS, Pattern.CONSTANT),
            RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT));
    public static final RecipePattern BASIC_MODULE = RecipePattern.createPattern(
            RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CONSTANT, Pattern.ALLOY),
            RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.PREVIOUS, Pattern.ALLOY),
            RecipePattern.TripleLine.of(Pattern.HDPE_CHAR, Pattern.HDPE_CHAR, Pattern.HDPE_CHAR));

    private final List<ISubRecipeProvider> compatProviders = new ArrayList<>();
    private final Set<String> disabledCompats = new HashSet<>();

    public MoreMachineRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, existingFileHelper);

        //Mod Compat Recipe providers
        checkCompat("mysticalagriculture", MysticalRecipeProvider::new);
        checkCompat("immersiveengineering", IERecipeProvider::new);
    }

    private void checkCompat(String modid, Function<String, ISubRecipeProvider> providerCreator) {
        if (ModList.get().isLoaded(modid)) {
            compatProviders.add(providerCreator.apply(modid));
        } else {
            disabledCompats.add(modid);
        }
    }

    public Set<String> getDisabledCompats() {
        return Collections.unmodifiableSet(disabledCompats);
    }

    @Override
    protected void addRecipes(RecipeOutput consumer, HolderLookup.Provider registries) {
        addMiscRecipes(consumer);
        addGearModuleRecipes(consumer);
        addLateGameRecipes(consumer);
        for (ISubRecipeProvider compatProvider : compatProviders) {
            compatProvider.addRecipes(consumer, registries);
        }
    }

    @Override
    protected List<ISubRecipeProvider> getSubRecipeProviders() {
        return List.of(
                new MMFactoryRecipeProvider(),
                new AdvancedFactoryRecipeProvider(),
                new PlantingRecipeProvider()
        );
    }

    private void addMiscRecipes(RecipeOutput consumer) {
//        SpecialRecipeBuilder.special(ClearConfigurationRecipe::new).save(consumer, MekanismRecipeSerializersInternal.CLEAR_CONFIGURATION.getId());
        // 回收机
        MekMMDataShapedRecipeBuilder.shapedRecipe(MMBlocks.RECYCLER)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.OSMIUM, Pattern.CONSTANT, Pattern.OSMIUM),
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_ADVANCED)
                .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ADVANCED)
                .key(Pattern.OSMIUM, osmiumIngot())
                .key(Pattern.CONSTANT, MekanismBlocks.CRUSHER)
                .build(consumer, Mekmm.rl("recycler"));
        // 种植机
        MekMMDataShapedRecipeBuilder.shapedRecipe(MMBlocks.PLANTING_STATION)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.STEEL_CASING, Pattern.CONSTANT),
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_REINFORCED)
                .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ELITE)
                .key(Pattern.CONSTANT, MekanismItems.BIO_FUEL)
                .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
                .build(consumer, Mekmm.rl("planting_station"));
        // 压模机
        MekMMDataShapedRecipeBuilder.shapedRecipe(MMBlocks.CNC_STAMPER)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.STEEL_CASING, Pattern.CONSTANT),
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_BASIC)
                .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_BASIC)
                .key(Pattern.CONSTANT, Ingredient.of(Items.PISTON, Items.STICKY_PISTON))
                .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
                .build(consumer, Mekmm.rl("cnc_stamper"));
        // 车床
        MekMMDataShapedRecipeBuilder.shapedRecipe(MMBlocks.CNC_LATHE)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.STEEL_CASING, Pattern.CONSTANT),
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_BASIC)
                .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_BASIC)
                .key(Pattern.CONSTANT, MekanismItems.ROBIT)
                .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
                .build(consumer, Mekmm.rl("cnc_lathe"));
        // 轧机
        MekMMDataShapedRecipeBuilder.shapedRecipe(MMBlocks.CNC_ROLLING_MILL)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.STEEL, Pattern.STEEL_CASING, Pattern.STEEL),
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_BASIC)
                .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_BASIC)
                .key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
                .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
                .build(consumer, Mekmm.rl("cnc_rolling_mill"));
    }

    private void addGearModuleRecipes(RecipeOutput consumer) {

    }

    private void addLateGameRecipes(RecipeOutput consumer) {
        // 复制机
        MekMMDataShapedRecipeBuilder.shapedRecipe(MMBlocks.REPLICATOR)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.STEEL_CASING, Pattern.CONSTANT),
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_ATOMIC)
                .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ULTIMATE)
                .key(Pattern.CONSTANT, MMItems.UU_MATTER)
                .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
                .build(consumer, Mekmm.rl("replicator"));
        // 环境气体收集器
        ExtendedShapedRecipeBuilder.shapedRecipe(MMBlocks.AMBIENT_GAS_COLLECTOR)
                .pattern(RecipePattern.createPattern(
                        RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CONSTANT, Pattern.ALLOY),
                        RecipePattern.TripleLine.of(Pattern.TANK, Pattern.STEEL_CASING, Pattern.TANK),
                        RecipePattern.TripleLine.of(Pattern.OSMIUM, Pattern.OSMIUM, Pattern.OSMIUM)
                )).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_ATOMIC)
                .key(Pattern.CONSTANT, MekanismBlocks.ULTIMATE_PRESSURIZED_TUBE)
                .key(Pattern.TANK, MekanismBlocks.ULTIMATE_CHEMICAL_TANK)
                .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
                .key(Pattern.OSMIUM, MekanismTags.Items.PROCESSED_RESOURCE_BLOCKS.get(PrimaryResource.OSMIUM))
                .build(consumer, Mekmm.rl("ambient_gas_collector"));
    }
}