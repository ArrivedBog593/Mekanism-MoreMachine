package com.jerry.datagen.client.lang;

import com.jerry.mekmm.Mekmm;
import net.minecraft.data.PackOutput;

public class MoreMachineLangProvider extends BaseLanguageProvider {

    public MoreMachineLangProvider(PackOutput output) {
        super(output, Mekmm.MOD_ID, Mekmm.instance);
    }

    @Override
    protected void addTranslations() {
        addConfigs();
        addAliases();
    }

    private void addConfigs() {
//        addConfigs(MoreMachineConfig.getConfigs());
//        addConfigs(MoreMachineConfigTranslations.values());
    }
}