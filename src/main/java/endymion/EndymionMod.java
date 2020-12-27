package endymion;

import endymion.common.block.EndBlocks;
import endymion.common.handler.RegistryHandler;
import endymion.common.world.biome.provider.EndymionBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("endymion")
public class EndymionMod {
    public EndymionMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().register(new RegistryHandler());
        FMLJavaModLoadingContext.get().getModEventBus().register(new ClientSetup());
    }

    private void setup(FMLCommonSetupEvent event) {
        // We currently register as a new biome source, maybe we can just override minecraft:the_end?
        Registry.register(Registry.BIOME_SOURCE, "endymion:endymion", EndymionBiomeProvider.CODEC);
    }

    public static ResourceLocation resLoc(String path) {
        int colon = path.indexOf(':');
        if (colon >= 0) {
            return new ResourceLocation(path.substring(0, colon), path.substring(colon + 1));
        }
        return new ResourceLocation("endymion", path);
    }

    public static String resStr(String path) {
        if (path.indexOf(':') >= 0) {
            return path;
        }
        return "endymion:" + path;
    }

    private static class ClientSetup {
        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public void setup(FMLCommonSetupEvent event) {
            EndBlocks.setupClient();
        }
    }
}
