package com.jerry.meklm.client.model.bake;

import com.jerry.meklm.common.base.holiday.holiday_info.LargeSNAHolidayInfo;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;

import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;

import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class LargeSNABakedModel extends ExtensionBakedModel.TransformedBakedModel<Void> {

    public LargeSNABakedModel(BakedModel original) {
        super(original, QuadTransformation.translate(0, 1, 0));
    }

    @Nullable
    @Override
    protected ExtensionBakedModel.QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
        QuadTransformation holidayTransform = LargeSNAHolidayInfo.getTransform();
        if (holidayTransform != null) {
            return key.transform(holidayTransform.and(QuadTransformation.translate(0, 1, 0)));
        }
        return super.createKey(key, data);
    }

    @Override
    protected LargeChemicalInfuserBakedModel wrapModel(BakedModel model) {
        return new LargeChemicalInfuserBakedModel(model);
    }
}
