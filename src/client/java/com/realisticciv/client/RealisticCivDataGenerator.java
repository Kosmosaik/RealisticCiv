package com.realisticciv.client;

import com.realisticciv.client.datagen.RealisticCivEnglishLangProvider;
import com.realisticciv.client.datagen.RealisticCivItemTagProvider;
import com.realisticciv.client.datagen.RealisticCivModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class RealisticCivDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(RealisticCivEnglishLangProvider::new);
        pack.addProvider(RealisticCivModelProvider::new);
        pack.addProvider(RealisticCivItemTagProvider::new);
    }
}
