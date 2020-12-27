package midnight.gradle.changelog;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class MarkdownChangelogGenerator {
    private final ChangelogInfo info;
    private final File outFile;

    public MarkdownChangelogGenerator(ChangelogInfo info, File out) {
        this.info = info;
        this.outFile = out;
    }

    public void generate() throws IOException {
        outFile.getParentFile().mkdirs();
        PrintStream out = new PrintStream(outFile);
        out.printf("## %s - %s\n", info.getVersionNumber(), info.getVersionName());
        out.println();
        out.printf("**For Minecraft %s**\n", info.getMcversion());
        out.println();
        if (info.getDescription() != null) {
            out.println(info.getDescription());
            out.println();
        }
        out.println("#### Changelog");
        out.println();
        for (String changelog : info.getChangelog()) {
            out.printf("- %s\n", changelog);
        }
        out.close();
    }
}
