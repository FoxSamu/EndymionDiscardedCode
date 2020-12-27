package endymion.common.world.gen.surface;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public final class EndConfiguredSurfaceBuilders {
   public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> SHADE = register("endymion:shade", SurfaceBuilder.DEFAULT.method_30478(EndSurfaceBuilders.SHADE_CONFIG));

   private EndConfiguredSurfaceBuilders() {
   }

   private static <SC extends ISurfaceBuilderConfig> ConfiguredSurfaceBuilder<SC> register(String id, ConfiguredSurfaceBuilder<SC> configuredBuilder) {
      return WorldGenRegistries.add(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, id, configuredBuilder);
   }
}
