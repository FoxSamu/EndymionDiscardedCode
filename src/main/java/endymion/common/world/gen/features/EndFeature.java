package endymion.common.world.gen.features;

import endymion.EndymionMod;
import endymion.util.IRegistry;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public final class EndFeature {
    private static final List<Feature<?>> FEATURES = new ArrayList<>();

    public static final Feature<RandomDepositConfig> RANDOM_DEPO = register("random_deposit", new RandomDepositFeature(RandomDepositFeature.CODEC));

    private EndFeature() {
    }

    public static void register(IRegistry<Feature<?>> registry) {
        FEATURES.forEach(registry::register);
    }

    private static <T extends Feature<?>> T register(String id, T feature) {
        feature.setRegistryName(EndymionMod.resLoc(id));
        FEATURES.add(feature);
        return feature;
    }
}
