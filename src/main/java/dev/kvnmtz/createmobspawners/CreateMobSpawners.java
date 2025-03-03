package dev.kvnmtz.createmobspawners;

import com.mojang.logging.LogUtils;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import dev.kvnmtz.createmobspawners.block.registry.ModBlocks;
import dev.kvnmtz.createmobspawners.block.custom.entity.registry.ModBlockEntities;
import dev.kvnmtz.createmobspawners.item.registry.ModCreativeModeTabs;
import dev.kvnmtz.createmobspawners.item.registry.ModItems;
import dev.kvnmtz.createmobspawners.item.tooltip.AddonTooltipModifierFactory;
import dev.kvnmtz.createmobspawners.network.PacketHandler;
import dev.kvnmtz.createmobspawners.ponder.AddonPonderPlugin;
import dev.kvnmtz.createmobspawners.recipe.registry.ModRecipes;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateMobSpawners.MOD_ID)
public class CreateMobSpawners {
    public static final String MOD_ID = "create_mob_spawners";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("DataFlowIssue")
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public static final CreateMobSpawnersServerConfig SERVER_CONFIG;
    public static final ForgeConfigSpec SERVER_CONFIG_SPEC;

    static {
        REGISTRATE.setTooltipModifierFactory(AddonTooltipModifierFactory::factory);

        final var serverPair = new ForgeConfigSpec.Builder().configure(CreateMobSpawnersServerConfig::new);
        SERVER_CONFIG = serverPair.getLeft();
        SERVER_CONFIG_SPEC = serverPair.getRight();
    }

    public CreateMobSpawners() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(modEventBus);

        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModCreativeModeTabs.register(modEventBus);
        ModRecipes.register(modEventBus);

        PacketHandler.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG_SPEC);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new AddonPonderPlugin());

            try {
                Class.forName("mezz.jei.api.JeiPlugin");
                MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(ModItems.EMPTY_SOUL_CATCHER.get().getDefaultInstance(), ModItems.SOUL_CATCHER.get().getDefaultInstance()));
            } catch (ClassNotFoundException ignored) {
            }
        }
    }
}
