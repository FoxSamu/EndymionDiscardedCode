package endymion.common.world.biome;

import endymion.util.IRegistry;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

public final class EndBiomes {
    public static final RegistryKey<Biome> SHADE_PLAINS = key("endymion:shade_plains");
    public static final RegistryKey<Biome> SHADE_VOID = key("endymion:shade_void");

    private EndBiomes() {
    }

    public static void register(IRegistry<Biome> biomes) {
        biomes.register("endymion:shade_plains", EndBiomeMaker.createShadePlains());
        biomes.register("endymion:shade_void", EndBiomeMaker.createShadeVoid());
    }

    private static RegistryKey<Biome> key(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, new ResourceLocation(name));
    }
}
