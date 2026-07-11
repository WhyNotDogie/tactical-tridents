package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TTItemModelProvider extends ItemModelProvider {

    public TTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TacticalTridents.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("netherite_trident", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath("tactical_tridents", "item/netherite_trident"));
    }
}
