package com.jerry.mekmm.client.recipe_viewer.jei.machine;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.recipes.basic.BasicFluidChemicalToFluidRecipe;
import com.jerry.mekmm.common.recipe.impl.FluidReplicatorIRecipeSingle;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.SerializationConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.jei.BaseRecipeCategory;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NothingNullByDefault
public class FluidReplicatorRecipeCategory extends BaseRecipeCategory<BasicFluidChemicalToFluidRecipe> {

    //TODO: Re-evaluate
    private static final Codec<BasicFluidChemicalToFluidRecipe> RECIPE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidStackIngredient.CODEC.fieldOf(SerializationConstants.INPUT).forGetter(BasicFluidChemicalToFluidRecipe::getFluidInput),
            IngredientCreatorAccess.chemicalStack().codec().fieldOf(SerializationConstants.CHEMICAL_INPUT).forGetter(BasicFluidChemicalToFluidRecipe::getChemicalInput),
            FluidStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicFluidChemicalToFluidRecipe::getOutputRaw)
    ).apply(instance, FluidReplicatorIRecipeSingle::new));

    private final GuiGauge<?> inputGauge;
    private final GuiGauge<?> outputSlot;
    private final GuiGauge<?> inputSlot;
    private final GuiSlot extra;

    public FluidReplicatorRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<BasicFluidChemicalToFluidRecipe> recipeType) {
        super(helper, recipeType);
        GaugeType type1 = GaugeType.STANDARD.with(DataType.INPUT);
        GaugeType output = GaugeType.STANDARD.with(DataType.OUTPUT);
        inputGauge = addElement(GuiChemicalGauge.getDummy(type1, this, 7, 4));
        inputSlot = addElement(GuiChemicalGauge.getDummy(type1, this, 28, 4));
        outputSlot = addElement(GuiChemicalGauge.getDummy(output, this, 131, 4));
        extra = addSlot(SlotType.EXTRA, 8, 65).with(SlotOverlay.MINUS);
        addSlot(SlotType.INPUT, 29, 65).with(SlotOverlay.PLUS);
        addSlot(SlotType.OUTPUT, 132, 65).with(SlotOverlay.PLUS);
        addSlot(SlotType.POWER, 152, 65).with(SlotOverlay.POWER);
        addSimpleProgress(ProgressType.LARGE_RIGHT, 64, 36);
        addElement(new GuiEnergyGauge(new GuiEnergyGauge.IEnergyInfoHandler() {
            @Override
            public long getEnergy() {
                return 1L;
            }

            @Override
            public long getMaxEnergy() {
                return 1L;
            }
        }, GaugeType.STANDARD, this, 151, 4));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BasicFluidChemicalToFluidRecipe recipe, IFocusGroup focuses) {
        initFluid(builder, RecipeIngredientRole.INPUT, inputSlot, recipe.getFluidInput().getRepresentations());
        initChemical(builder, RecipeIngredientRole.INPUT, inputGauge, recipe.getChemicalInput().getRepresentations());
        initFluid(builder, RecipeIngredientRole.OUTPUT, outputSlot, recipe.getOutputDefinition());
        initItem(builder, RecipeIngredientRole.CATALYST, extra, RecipeViewerUtils.getStacksFor(recipe.getChemicalInput(), true));
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(BasicFluidChemicalToFluidRecipe recipe) {
        List<@NotNull FluidStack> representations = recipe.getFluidInput().getRepresentations();
        if (representations.size() == 1) {
            ResourceLocation fluidId = BuiltInRegistries.FLUID.getKeyOrNull(representations.getFirst().getFluid());
            if (fluidId != null) {
                return RecipeViewerUtils.synthetic(fluidId, "replicator", Mekmm.MOD_ID);
            }
        }
        return null;
    }

    @Override
    public Codec<BasicFluidChemicalToFluidRecipe> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return RECIPE_CODEC;
    }
}
