package fr.rouret.scripts;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
public class ScriptHandler {

    protected Map<String, Script> scripts = new HashMap<>();

    public ScriptHandler(File scriptDir) {
        Objects.requireNonNull(scriptDir, "scriptDir can't be null");
        File[] files = scriptDir.listFiles();
        Objects.requireNonNull(files, "scriptDir can't empty");
        registerScript(files);
    }

    public void registerScript(File... files) {
        for (File file : files) {
            Script script = new Script(file);
            System.out.println("Registering script: " + script.getName());
            scripts.put(script.getName(), script);
        }
    }

    public Script getScript(String scriptName){
        return scripts.get(scriptName);
    }
}
