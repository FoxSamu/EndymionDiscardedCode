package endymion.data;

import endymion.common.block.EndBlocks;
import endymion.common.item.EndItems;
import endymion.common.world.biome.EndBiomes;
import endymion.common.world.gen.features.EndFeature;
import endymion.common.world.gen.placer.EndBlockPlacerTypes;
import endymion.data.loottables.EndLootTableProvider;
import endymion.data.models.EndStateModelProvider;
import endymion.data.recipes.EndRecipeProvider;
import endymion.data.recipes.EndStonecuttingRecipeProvider;
import endymion.data.tags.EndBlockTagsProvider;
import endymion.data.tags.EndFluidTagsProvider;
import endymion.data.tags.EndItemTagsProvider;
import endymion.util.IRegistry;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

public final class DataGen {
    private DataGen() {
    }

    public static void main2(String[] args) throws IOException {
        main(new String[] {
            "--all",
            "--output", "src/generated/resources",
            "--gameDir", "run"
        });
    }

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<Void> helpSpec = parser.accepts("help", "Show the help menu")
                                          .forHelp();
        OptionSpec<Void> serverSpec = parser.accepts("server", "Include server generators");
        OptionSpec<Void> clientSpec = parser.accepts("client", "Include client generators");
        OptionSpec<Void> devSpec = parser.accepts("dev", "Include development tools");
        OptionSpec<Void> reportsSpec = parser.accepts("reports", "Include data reports");
        OptionSpec<Void> validateSpec = parser.accepts("validate", "Validate inputs");
        OptionSpec<Void> allSpec = parser.accepts("all", "Include all generators");
        OptionSpec<String> outputSpec = parser.accepts("output", "Output folder")
                                              .withRequiredArg()
                                              .defaultsTo("generated");
        OptionSpec<String> inputSpec = parser.accepts("input", "Input folder")
                                             .withRequiredArg();
        OptionSpec<File> gameDirSpec = parser.accepts("gameDir")
                                             .withRequiredArg()
                                             .ofType(File.class)
                                             .defaultsTo(new File("."))
                                             .required();
        OptionSet opts = parser.parse(args);

        if (!opts.has(helpSpec) && opts.hasOptions() && !(opts.specs().size() == 1 && opts.has(gameDirSpec))) {
            Path output = Paths.get(outputSpec.value(opts));
            boolean all = opts.has(allSpec);
            boolean client = all || opts.has(clientSpec);
            boolean server = all || opts.has(serverSpec);
            boolean dev = all || opts.has(devSpec);
            boolean reports = all || opts.has(reportsSpec);
            boolean validate = all || opts.has(validateSpec);
            Collection<Path> inputs = opts.valuesOf(inputSpec).stream().map(Paths::get).collect(Collectors.toList());
            bootstrap();
            makeGenerator(output, inputs, client, server, dev, reports, validate).run();
        } else {
            parser.printHelpOn(System.out);
        }
    }

    @SuppressWarnings("deprecation")
    private static void bootstrap() {
        Bootstrap.register();

        EndBlocks.register(IRegistry.vanilla(Registry.BLOCK));
        ObjectHolderHacks.hackObjectHolder(EndBlocks.class, Registry.BLOCK, Block.class);

        EndBlocks.registerItems(IRegistry.vanilla(Registry.ITEM));
        EndItems.register(IRegistry.vanilla(Registry.ITEM));
        ObjectHolderHacks.hackObjectHolder(EndItems.class, Registry.ITEM, Item.class);

        EndBiomes.register(IRegistry.vanilla(WorldGenRegistries.BIOME));
        EndBlockPlacerTypes.register(IRegistry.vanilla(Registry.BLOCK_PLACER_TYPE));
        EndFeature.register(IRegistry.vanilla(Registry.FEATURE));
    }

    /**
     * Creates a data generator based on the given options
     */
    public static DataGenerator makeGenerator(Path out, Collection<Path> ins, boolean client, boolean server, boolean dev, boolean reports, boolean validate) {
        DataGenerator gen = new DataGenerator(out, ins);
        if (client || server) {
//            datagen.addProvider((new SNBTToNBTConverter(datagen)).addWriter(new StructureUpdater()));
        }

        if (client) {
            gen.addProvider(new EndStateModelProvider(gen));
        }

        if (server) {
            EndBlockTagsProvider blockTags = new EndBlockTagsProvider(gen);
            gen.addProvider(blockTags);
            gen.addProvider(new EndItemTagsProvider(gen, blockTags));
            gen.addProvider(new EndFluidTagsProvider(gen));

            gen.addProvider(new EndRecipeProvider(gen));
            gen.addProvider(new EndStonecuttingRecipeProvider(gen));
            gen.addProvider(new EndLootTableProvider(gen));
        }

        if (dev) {
//            datagen.addProvider(new NBTToSNBTConverter(datagen));
        }

        if (reports) {
            gen.addProvider(new BiomeProvider(gen));
            gen.addProvider(new BlockListReport(gen));
            gen.addProvider(new RegistryDumpReport(gen));
            gen.addProvider(new CommandsReport(gen));
        }

        return gen;
    }
}
