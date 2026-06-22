package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TTItemModelProvider extends ItemModelProvider {

    public TTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TacticalTridents.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = new ModelFile.UncheckedModelFile("item/generated");
        ModelFile builtinEntity = new ModelFile.UncheckedModelFile("builtin/entity");

        ItemModelBuilder tridentInventory2D = ((ItemModelBuilder) nested()).parent(itemGenerated)
                .texture("layer0", ResourceLocation.fromNamespaceAndPath("tactical_tridents", "item/netherite_trident"));

        ItemModelBuilder tridentHand3D = ((ItemModelBuilder) nested()).parent(builtinEntity)
                .guiLight(BlockModel.GuiLight.FRONT)
                .texture("particle", ResourceLocation.fromNamespaceAndPath("tactical_tridents", "item/netherite_trident"));

        tridentHand3D.transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0F, 60F, 0F).translation(11F, 17F, -2F).scale(1.0F, 1.0F, 1.0F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(0F, 60F, 0F).translation(3F, 17F, 12F).scale(1.0F, 1.0F, 1.0F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0F, -90F, 25F).translation(-3F, 17F, 1F).scale(1.0F, 1.0F, 1.0F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0F, 90F, -25F).translation(13F, 17F, 1F).scale(1.0F, 1.0F, 1.0F).end()
                .transform(ItemDisplayContext.GUI)
                .rotation(15F, -25F, -5F).translation(2F, 3F, 0F).scale(0.65F, 0.65F, 0.65F).end()
                .transform(ItemDisplayContext.FIXED)
                .rotation(0F, 180F, 0F).translation(-2F, 4F, -5F).scale(0.5F, 0.5F, 0.5F).end()
                .transform(ItemDisplayContext.GROUND)
                .rotation(0F, 0F, 0F).translation(4F, 4F, 2F).scale(0.25F, 0.25F, 0.25F).end();

        withExistingParent("netherite_trident", "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(tridentInventory2D)
                .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, tridentHand3D) // 3D offsets for player perspectives
                .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, tridentHand3D)
                .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, tridentHand3D)
                .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, tridentHand3D)
                .perspective(ItemDisplayContext.GROUND, tridentHand3D);
    }
}