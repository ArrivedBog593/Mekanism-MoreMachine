package com.jerry.meklm.client.model.bake;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import net.minecraft.client.resources.model.BakedModel;

@NothingNullByDefault
public class LargeRotaryCondensentratorBakedModel extends ExtensionBakedModel<Void> {

    public LargeRotaryCondensentratorBakedModel(BakedModel original) {
        super(original);
    }

    @Override
    protected LargeRotaryCondensentratorBakedModel wrapModel(BakedModel model) {
        return new LargeRotaryCondensentratorBakedModel(model);
    }
}
