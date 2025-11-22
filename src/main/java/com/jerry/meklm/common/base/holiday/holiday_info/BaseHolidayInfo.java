package com.jerry.meklm.common.base.holiday.holiday_info;

import mekanism.client.render.lib.QuadTransformation;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public abstract class BaseHolidayInfo {

    protected BaseHolidayInfo() {}

    protected static QuadTransformation suffixTexture(TextureAtlas atlas, ResourceLocation screenNamespace, String screen) {
        return texture(atlas, screenNamespace.withSuffix(screen));
    }

    protected static QuadTransformation texture(TextureAtlas atlas, ResourceLocation location) {
        return QuadTransformation.texture(atlas.getSprite(location));
    }
}
