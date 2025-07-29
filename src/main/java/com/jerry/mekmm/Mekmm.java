package com.jerry.mekmm;

import com.jerry.mekmm.common.registries.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Mekmm.MOD_ID)
public class Mekmm {

    public static final String MOD_ID = "mekmm";
    public static final String MOD_NAME = "";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Mekmm(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MMItems.MM_ITEMS.register(modEventBus);
        MMBlocks.MM_BLOCKS.register(modEventBus);
        MMTileEntityTypes.MM_TILE_ENTITY_TYPES.register(modEventBus);
        MMContainerTypes.MM_CONTAINER_TYPES.register(modEventBus);
        MMRecipeSerializers.MM_RECIPE_SERIALIZERS.register(modEventBus);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
