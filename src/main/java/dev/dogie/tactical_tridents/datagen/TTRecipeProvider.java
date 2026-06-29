package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class TTRecipeProvider extends RecipeProvider {

    public TTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(Items.TRIDENT),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.COMBAT,
                        TacticalTridents.NETHERITE_TRIDENT.get()
                )
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "smithing/netherite_trident"));
    }
}