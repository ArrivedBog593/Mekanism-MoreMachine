package com.jerry.mekmm.common.util;

import com.jerry.mekmm.api.chemical.chemicals.ChemicalStackLinkedSet;

import mekanism.api.chemical.ChemicalStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

import java.util.Map;

public class ChemicalStackMap {

    public static <V> Map<ChemicalStack, V> createTypeAndComponentsLinkedMap() {
        return new Object2ObjectLinkedOpenCustomHashMap(ChemicalStackLinkedSet.TYPE_AND_COMPONENTS);
    }

    public static <V> Map<ChemicalStack, V> createTypeAndComponentsMap() {
        return new Object2ObjectOpenCustomHashMap(ChemicalStackLinkedSet.TYPE_AND_COMPONENTS);
    }
}
