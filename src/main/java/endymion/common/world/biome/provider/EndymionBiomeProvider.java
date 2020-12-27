package endymion.common.world.biome.provider;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import endymion.common.world.biome.EndBiomes;
import endymion.common.world.biome.climate.ChorusBarrensClimate;
import endymion.common.world.biome.climate.EndClimate;
import endymion.common.world.biome.climate.ShadeFieldsClimate;
import endymion.common.world.gen.chunk.IEndymionNoiseChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;

import net.shadew.ptg.region.*;

public class EndymionBiomeProvider extends BiomeProvider {
    public static final Codec<EndymionBiomeProvider> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            RegistryLookupCodec.of(Registry.BIOME_KEY)
                               .forGetter(provider -> provider.biomeRegistry),
            Codec.LONG.fieldOf("seed")
                      .stable()
                      .forGetter(provider -> provider.seed)
        ).apply(
            instance,
            instance.stable(EndymionBiomeProvider::new)
        )
    );

    private static final int TYPE_CHORUS_DESERTS = 0;
    private static final int TYPE_SHADE_FIELDS = 1;
    private static final int[] TYPES = {
        TYPE_CHORUS_DESERTS,
        TYPE_CHORUS_DESERTS,
        TYPE_SHADE_FIELDS
    };

    private static final int END_MAIN_ISLAND = 0;
    private static final int END_HIGHLANDS = 1;
    private static final int END_MIDLANDS = 2;
    private static final int END_SMALL_ISLANDS = 3;
    private static final int END_BARRENS = 4;
    private static final int SHADE_PLAINS = 5;
    private static final int SHADE_VOID = 6;

    private final ThreadLocal<double[]> noisebuf = ThreadLocal.withInitial(() -> new double[3]);
    private final ThreadLocal<EndClimate[]> climatebuf = ThreadLocal.withInitial(() -> new EndClimate[25]);

    private final SimplexNoiseGenerator generator;
    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private final Biome[] biomes;
    private final EndClimate[] climates;

    private final FractalGenerator<EndClimate> climateGenQuarter;
    private final FractalGenerator<EndClimate> climateGenBase;
    private final FractalGenerator<EndClimate> climateGenBlock;

    private final Map<Biome, EndClimate> climateMap = new HashMap<>();

    public EndymionBiomeProvider(Registry<Biome> biomeReg, long seed) {
        this(
            biomeReg, seed,
            biomeReg.getOrThrow(Biomes.THE_END),
            biomeReg.getOrThrow(Biomes.END_HIGHLANDS),
            biomeReg.getOrThrow(Biomes.END_MIDLANDS),
            biomeReg.getOrThrow(Biomes.SMALL_END_ISLANDS),
            biomeReg.getOrThrow(Biomes.END_BARRENS),
            biomeReg.getOrThrow(EndBiomes.SHADE_PLAINS),
            biomeReg.getOrThrow(EndBiomes.SHADE_VOID)
        );
    }

    private EndymionBiomeProvider(Registry<Biome> biomeReg, long seed, Biome... biomes) {
        super(ImmutableList.copyOf(biomes));
        this.biomeRegistry = biomeReg;
        this.seed = seed;
        this.biomes = biomes;

        this.climates = new EndClimate[] {
            new ChorusBarrensClimate(biomes[END_HIGHLANDS], biomes[END_MIDLANDS], biomes[END_SMALL_ISLANDS], biomes[END_BARRENS]),
            new ShadeFieldsClimate(biomes[SHADE_PLAINS], biomes[SHADE_VOID])
        };

        for (EndClimate c : climates) {
            for (Biome b : c.getBiomes()) {
                climateMap.put(b, c);
            }
        }

        SharedSeedRandom rand = new SharedSeedRandom(seed);
        rand.skip(17292);
        generator = new SimplexNoiseGenerator(rand);

        CachingRegionContext ctx = new CachingRegionContext(25, seed);
        RegionBuilder<CachingRegion, ?> crb = ctx.pick(TYPES, 4919L)
                                                 .zoomFuzzy(1)
                                                 .zoom(3); // TODO Larger zoom

        RegionFactory<CachingRegion> root = crb.getFactory();
        climateGenQuarter = createGenerator(ctx.extend(root, 8821).zoom().buildRegion());
        climateGenBase = createGenerator(ctx.extend(root, 8821).zoom(2).buildRegion());
        climateGenBlock = createGenerator(ctx.extend(root, 8821).zoom(2).zoomVoronoi().buildRegion());
    }

    private FractalGenerator<EndClimate> createGenerator(Region region) {
        return new FractalGenerator<EndClimate>(region) {
            @Override
            protected EndClimate toValue(int id) {
                return climates[id];
            }

            @Override
            protected EndClimate[] createArray(int size) {
                return new EndClimate[size];
            }
        };
    }

    @Override
    protected Codec<? extends BiomeProvider> getCodec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider withSeed(long seed) {
        return new EndymionBiomeProvider(biomeRegistry, seed, biomes);
    }

    private static boolean checkDistance(int x, int z, int limit) {
        return Math.abs(x) <= limit && Math.abs(z) <= limit && x * x + z * z <= limit * limit;
    }

    public boolean isSeedEqual(long seed) {
        return this.seed == seed;
    }

    public void processChunk(WorldGenRegion region, IChunk chunk) {
        ChunkPos pos = chunk.getPos();
        int cx = pos.getXStart();
        int cz = pos.getZStart();
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int h = region.getHeight(Heightmap.Type.WORLD_SURFACE_WG, cx + x, cz + z);
                EndClimate climate = climateMap.get(region.getBiome(mpos.setPos(cx + x, h, cz + z)));
                for (int y = 0; y <= h; y++) {
                    mpos.setPos(cx + x, y, cz + z);
                    if (climate != null) {
                        climate.replaceBlockState(region.getBlockState(mpos), chunk, mpos);
                    }
                }
            }
        }
    }

    public double[] getIslandNoise(SimplexNoiseGenerator noise, int x, int z, double[] buf) {
        // Chunk coordinates
        int cx = x / 2;
        int cz = z / 2;

        // Chunk-local coordinates
        int lx = x % 2;
        int lz = z % 2;

        EndClimate[] climates = climateGenQuarter.generate(climatebuf.get(), x - 2, z - 2, 5, 5);
        climatebuf.set(climates);

        EndClimate center = climates[2 * 5 + 2];

        double dens = 0;
        double wgt = 0;
        for (int rx = -2; rx <= 2; rx++) {
            for (int rz = -2; rz <= 2; rz++) {
                int ix = rx + 2;
                int iz = rz + 2;
                EndClimate climate = climates[iz * 5 + ix];
                double weight = 4 / (4D * (rx * rx + rz * rz) + 1);
                dens += climate.getDensity(x, z) * weight;
                wgt += weight;
            }
        }
        dens /= wgt;

        //System.out.println("Density: " + dens);

        float islandFactor = -100;

        // Generate main island (fix MC-159283)
        if (Math.abs(x) < 200 && Math.abs(z) < 200) {
            islandFactor = 100 - MathHelper.sqrt(x * x + z * z) * 8;
            islandFactor = MathHelper.clamp(islandFactor, -100, 80);
        }

        float totalRemoteIslandFactor = -100;

        if (!checkDistance(cx, cz, 48)) {


            // Generate remote islands
            for (int rx = -12; rx <= 12; rx++) {
                for (int rz = -12; rz <= 12; rz++) {
                    int grx = cx + rx;
                    int grz = cz + rz;

                    // Check if we can generate a remote island nearby
                    // Remote islands are at least 64 chunks (= 1024 blocks) away
                    if (!checkDistance(grx, grz, 64) && noise.getValue(grx, grz) < -1 + dens) {
                        float lrx = lx - rx * 2;
                        float lrz = lz - rz * 2;



                        float randominess = center.getIslandRandomness(grx, grz);
                        float remoteIslandFactor = 100 - MathHelper.sqrt(lrx * lrx + lrz * lrz) * randominess;
                        remoteIslandFactor = MathHelper.clamp(remoteIslandFactor, -100, 80);

                        if (remoteIslandFactor > totalRemoteIslandFactor) {
                            totalRemoteIslandFactor = remoteIslandFactor;
                        }
                    }
                }
            }
        }

        double island = Math.max(islandFactor, totalRemoteIslandFactor);

        double dpt = 0;
        double scl = 0;
        wgt = 0;
        for (int rx = -2; rx <= 2; rx++) {
            for (int rz = -2; rz <= 2; rz++) {
                int ix = rx + 2;
                int iz = rz + 2;
                EndClimate climate = climates[iz * 5 + ix];
                double weight = 4 / (4D * (rx * rx + rz * rz) + 1);
                dpt += climate.getDepth(x, z, island) * weight;
                scl += climate.getScaleFactor(x, z, island) * weight;
                wgt += weight;
            }
        }

        //System.out.println("Depth:   " + dpt);
        //System.out.println("Scale:   " + scl);

        dpt /= wgt;
        scl /= wgt;
        if (buf.length > 0) buf[0] = dpt;
        if (buf.length > 1) buf[1] = scl;
        if (buf.length > 2) buf[2] = island;
        return buf;
    }

    @Override
    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        int cx = x >> 2;
        int cz = z >> 2;

        if (checkDistance(x, z, 64)) {
            return biomes[END_MAIN_ISLAND];
        } else {
            double noise = getIslandNoise(generator, cx * 2 + 1, cz * 2 + 1, noisebuf.get())[2];
            EndClimate climate = climateGenBase.generate(x, z);
            return climate.getBiome(x, y, z, noise);
        }
    }
}
