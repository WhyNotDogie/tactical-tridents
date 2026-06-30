package dev.dogie.tactical_tridents;

import com.mojang.logging.LogUtils;
import dev.dogie.tactical_tridents.netherite_trident.NetheriteTridentItem;
import dev.dogie.tactical_tridents.netherite_trident.ThrownNetheriteTrident;
import dev.dogie.tactical_tridents.netherite_trident.render.NetheriteTridentBewlr;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.entity.MobCategory;
import org.slf4j.Logger;
// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TacticalTridents.MODID)
public class TacticalTridents {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tactical_tridents";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "tactical_tridents" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "tactical_tridents" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, TacticalTridents.MODID);

    public static final DeferredItem<Item> NETHERITE_TRIDENT = ITEMS.register("netherite_trident",
            () -> new NetheriteTridentItem(new Item.Properties()
                    .durability(500)
                    .fireResistant()
                    .component(DataComponents.TOOL, TridentItem.createToolProperties())
                    .attributes(createTridentAttributes())
            )
    );

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownNetheriteTrident>> THROWN_NETHERITE_TRIDENT = ENTITY_TYPES.register("thrown_netherite_trident",
            () -> EntityType.Builder.<ThrownNetheriteTrident>of(ThrownNetheriteTrident::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F) // i think this is the right hitbox whatever
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("thrown_netherite_trident")
    );

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public TacticalTridents(IEventBus modEventBus, ModContainer modContainer) {
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        ENTITY_TYPES.register(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (TacticalTridents) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
//        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static ItemAttributeModifiers createTridentAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "base_attack_damage"),
                                10.0,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "base_attack_speed"),
                        -2.9,
                        AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
