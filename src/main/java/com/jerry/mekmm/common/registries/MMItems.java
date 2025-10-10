package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;

public class MMItems {

    private MMItems() {

    }

    public static final ItemDeferredRegister MM_ITEMS = new ItemDeferredRegister(Mekmm.MOD_ID);

    public static final ItemRegistryObject<Item> SCRAP = MM_ITEMS.register("scrap");
    public static final ItemRegistryObject<Item> UU_MATTER = MM_ITEMS.register("uu_matter");
}
