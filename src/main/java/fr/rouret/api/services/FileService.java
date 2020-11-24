package fr.rouret.api.services;


import java.io.IOException;
import java.net.URISyntaxException;

import java.util.List;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;


public class FileService {
    public List<Class<?>> getAvailableScripts() throws URISyntaxException, IOException {
        ScanResult scanResult = new ClassGraph().acceptPackages("fr.rouret.generated")
                .enableClassInfo().scan();
        return scanResult.getAllClasses().loadClasses();
    }
    private boolean isScriptAvailable(String scriptName) throws URISyntaxException, IOException {
        Object[] filteredClass = this.getAvailableScripts()
                    .stream()
                    .filter(className -> className.getSimpleName().equalsIgnoreCase(scriptName))
                    .toArray();
        return filteredClass.length != 1;
    }
    public Class getClassOfScript(String scriptName) throws Exception {
        if(this.isScriptAvailable(scriptName))
            throw new Exception("erreur");
        return this.getAvailableScripts()
        .stream()
        .filter(className-> className.getSimpleName().equalsIgnoreCase(scriptName))
        .findFirst()
        .get();
    }


    public List<Class<?>> getAvailableGenerator() throws URISyntaxException, IOException {
        ScanResult scanResult = new ClassGraph().acceptPackages("fr.rouret.generator")
                .enableClassInfo().scan();
        return scanResult.getAllClasses().loadClasses();
    }
    private boolean isGeneratorAvailable(String generatorName) throws URISyntaxException, IOException {
        Object[] filteredClass = this.getAvailableGenerator()
                    .stream()
                    .filter(className -> className.getSimpleName().equalsIgnoreCase(generatorName))
                    .toArray();
        return filteredClass.length != 1;
    }
    public Class getClassOfGenerator(String generatorName) throws Exception {
        if(this.isGeneratorAvailable(generatorName))
            throw new Exception("erreur");
        return this.getAvailableGenerator()
        .stream()
        .filter(className-> className.getSimpleName().equalsIgnoreCase(generatorName))
        .findFirst()
        .get();
    }
}
