package com.jerry.mekmm.client.jei;

import com.jerry.mekmm.common.registries.MoreMachineGas;
import com.jerry.mekmm.common.tile.machine.TileEntityReplicator;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.common.util.RegistryUtils;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MoreMachineRecipeRegistryHelper {

    private MoreMachineRecipeRegistryHelper() {
    }

    public static void registerItemReplicator(IRecipeRegistration registry) {
        List<ItemStackGasToItemStackRecipe> list = new ArrayList<>();
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (TileEntityReplicator.customRecipeMap.containsKey(RegistryUtils.getName(item).toString())) {
                list.add(TileEntityReplicator.getRecipe(new ItemStack(item), MoreMachineGas.UU_MATTER.getStack(1)));
            }
        }
        RecipeRegistryHelper.register(registry, MoreMachineJEIRecipeType.REPLICATOR, list);
    }
}
