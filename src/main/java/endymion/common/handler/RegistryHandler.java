package endymion.common.handler;

import endymion.common.block.EndBlocks;
import endymion.common.item.EndItems;
import endymion.common.world.gen.features.EndFeature;
import endymion.common.world.gen.placer.EndBlockPlacerTypes;
import endymion.util.IRegistry;
import endymion.common.world.biome.EndBiomes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegistryHandler {
    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        EndBlocks.register(IRegistry.forge(event.getRegistry()));
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        EndBlocks.registerItems(IRegistry.forge(event.getRegistry()));
        EndItems.register(IRegistry.forge(event.getRegistry()));
    }

    @SubscribeEvent
    public void onRegisterBiomes(RegistryEvent.Register<Biome> event) {
        EndBiomes.register(IRegistry.forge(event.getRegistry()));
    }

    @SubscribeEvent
    public void onRegisterBlockPlacerTypes(RegistryEvent.Register<BlockPlacerType<?>> event) {
        EndBlockPlacerTypes.register(IRegistry.forge(event.getRegistry()));
    }

    @SubscribeEvent
    public void onRegisterFeatures(RegistryEvent.Register<Feature<?>> event) {
        EndFeature.register(IRegistry.forge(event.getRegistry()));
    }
}
