package fr.rouret.scripts;

import java.io.File;

public class Script {

    private String name;

    private File file;

    public Script(String name, File file) {
        this.name = name.toLowerCase();
        this.file = file;
    }

    public Script(File file) {
        this.file = file;
        this.name = file.getName().toLowerCase();
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Script{" +
                "name='" + name + '\'' +
                ", file=" + file +
                '}';
    }
}