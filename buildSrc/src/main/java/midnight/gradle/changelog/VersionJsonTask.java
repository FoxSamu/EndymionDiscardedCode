package midnight.gradle.changelog;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class VersionJsonTask extends DefaultTask {
    private ChangelogInfo info;
    private File jsonFile;

    public void setInfo(ChangelogInfo info) {
        this.info = info;
    }

    public ChangelogInfo getInfo() {
        return info;
    }

    public void setJsonFile(File jsonFile) {
        this.jsonFile = jsonFile;
    }

    public File getJsonFile() {
        return jsonFile;
    }

    public void changelog(ChangelogInfo info) {
        setInfo(info);
    }

    public void updateJson(File json) {
        setJsonFile(json);
    }

    @TaskAction
    private void invoke() {
        try {
            VersionJsonGenerator gen = VersionJsonGenerator.loadFrom(jsonFile, info);
            gen.update();
            gen.saveTo(jsonFile);
        } catch (IOException exc) {
            exc.printStackTrace();
            throw new UncheckedIOException(exc);
        }
    }
}
